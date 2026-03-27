package com.youtubefilter;

import com.youtubefilter.controller.OfflineFilterController;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting YouTube Content Filter System...");
        OfflineFilterController controller = new OfflineFilterController();
        controller.start();
    }
}