package me.noci.labyaddon;

public class ServerHandler {

    private static String currentIP = "";

    public static boolean isCurrentlyPlayingOn(String ip) {
        return ip.toLowerCase().contains(currentIP);
    }

    public static void setCurrentIP(String currentIP) {
        ServerHandler.currentIP = currentIP;
    }
}
