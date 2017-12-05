package com.masterdevskills.downlaoder;

public class Main {

    public static void main(String[] args) {
        if (args.length > 2 || args.length < 2) {
            System.err.println("Invalid argument");
        } else {
            final String downloadDir = args[0];
            final String url = args[1];

            DownloadManager downloadManager = new DownloadManager();
            downloadManager.download(url, downloadDir);
        }
    }
}
