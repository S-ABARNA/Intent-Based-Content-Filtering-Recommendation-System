package com.youtubefilter;

import com.youtubefilter.gui.MainFrame;

public class MainGUI {
    public static void main(String[] args) {
        // Start the GUI application
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}