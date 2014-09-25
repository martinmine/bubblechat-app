package imt3662.hig.no.bubbles;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by Pål S on 25.09.2014.
 */
public class ChatMessage {
    private int userID;
    private String msg;
    private boolean hasLocation;
    private double latitude;
    private double longitude;
    private String username;
    private int color;

    public ChatMessage(int userID, String msg, boolean hasLocation, double latitude, double longitude, String username) {
        this.userID = userID;
        this.msg = msg;
        this.hasLocation = hasLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.username = username;

        Color c = new Color();
        color = c.argb(255, new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));

    }

    public ChatMessage(int userID, String msg, String username) {
        this.userID = userID;
        this.msg = msg;
        this.username = username;

        Color c = new Color();
        color = c.argb(255, new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
    }

    public int getUserID() {
        return userID;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isHasLocation() {
        return hasLocation;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getUsername() {
        return username;
    }

    public int getColor() {
        return color;
    }
}
