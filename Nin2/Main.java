package Nin2;

import engine.support.FXApplication;
import engine.support.FXFrontEnd;

/**
 * Here is your main class. You should not have to edit this
 * class at all unless you want to change your window size
 * or turn off the debugging information.
 */

public class Main {

    public static void main(String[] args) {
        FXFrontEnd app = new App("nin");
        FXApplication application = new FXApplication();
        application.begin(app);
    }
}