package com.scada.sess;
public class AdminSessionTracker {

    private static String currentAdmin = null;

    public static void setCurrentAdmin(String username) {
        currentAdmin = username;
    }

    public static String getCurrentAdmin() {
        return currentAdmin;
    }

    public static void clearAdmin() {
        currentAdmin = null;
    }
}
