package com.masterdevskills.downlaoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author Bazlur Rahman Rokon
 * @since 12/5/17.
 */
public class DownloadTask implements Runnable {
    private URL downloadUrl;
    private long startByte;
    private long partSize;
    private String partName;
    private String downloadDir;
    private CountDownLatch countDownLatch;

    public DownloadTask(URL downloadUrl, long startByte, long partSize, String partName, String downloadDir, CountDownLatch countDownLatch) {
        this.downloadUrl = downloadUrl;
        this.startByte = startByte;
        this.partSize = partSize;
        this.partName = partName;
        this.downloadDir = downloadDir;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            final HttpURLConnection connection = getConnection();
            try (ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream())) {
                FileOutputStream stream = new FileOutputStream(getPartFileName());
                stream.getChannel().transferFrom(rbc, 0, connection.getContentLength());

                countDownLatch.countDown();
            }
        } catch (IOException e) {
            System.err.println("Couldn't download the file because: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private String getPartFileName() {

        return downloadDir + File.separator + partName;
    }

    private HttpURLConnection getConnection() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
        String downloadRange = "bytes=" + startByte + "-" + (startByte + partSize);
        connection.setRequestProperty("Range", downloadRange);
        connection.connect();

        return connection;
    }
}
