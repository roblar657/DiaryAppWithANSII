package edu.ntnu.iir.bidata;


import java.io.IOException;

import static java.lang.System.out;

public class MainApp {
    public static void main(String[] args){
        DiaryUi ui = new DiaryUi();

        ui.init();
        try {
            ui.start();
        } catch (IOException e) {
            out.println("Program crashed during initialization. Try restarting the application.");
        }
    }
}
