package nl.tudelft.oopp.demo;

import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.views.LoginView;

public class MainApp {
    public static void main(String[] args) {
        GeneralMethods.loggerSetup();
        LoginView.main(new String[0]);
    }
}
