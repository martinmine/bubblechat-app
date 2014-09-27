package imt3662.hig.no.bubbles;

import android.graphics.Color;


import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Pål S on 25.09.2014.
 */
public class ChatMessage {
    public static final int USER_MESSAGE = new Color().argb(255, 173, 173, 173);
    public static final int SYSTEM_MESSAGE = new Color().argb(255, 255, 255, 255);

    private int userID;
    private String msg;
    private boolean hasLocation;
    private String latitude;
    private String longitude;
    private String username;
    private int color;


    public ChatMessage(int userID, String msg, boolean hasLocation,
                       LatLng location, String username, int color) {

        this.userID = userID;
        this.msg = msg;
        this.hasLocation = hasLocation;
        this.latitude =  String.valueOf(location.latitude);
        this.longitude = String.valueOf(location.longitude);
        this.username = username;
        this.color = color;
    }

    public ChatMessage(String msg, int color) {
        this.msg = msg;
        this.color = color;
        this.username = this.latitude = this.longitude = "";
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

    public String getLatitude() { return latitude; }

    public String getLongitude() { return longitude; }

    public String getUsername() {
        return username;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
