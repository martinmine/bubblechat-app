package imt3662.hig.no.bubbles;

import android.graphics.Color;


import com.google.android.gms.maps.model.LatLng;


/**
 * This message contains data about a chat message.
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

    /**
     * Creates a new chat message.
     * @param userID ID of the user sending the message.
     * @param msg The actual content of the chat message.
     * @param hasLocation If the user sending wants to share their location.
     * @param location Location of the user if hasLocation is set to true.
     * @param username Username of the sender if set (otherwise empty).
     * @param color Color of the chat bubble.
     */
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

    /**
     * Creates a new chat message that is not related to any users,
     * normally used for system messages or the user's own chat messages.
     * @param msg
     * @param color
     */
    public ChatMessage(String msg, int color) {
        this.msg = msg;
        this.color = color;
        this.username = this.latitude = this.longitude = "";
    }

    /**
     * Id of the user posting the message
     * @return the user-ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Gets the content of the message
     * @return User id
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Is true if the chat message has any location data associated with it.
     * @return True if has location data, otherwise false.
     */
    public boolean isHasLocation() {
        return hasLocation;
    }

    /**
     * Gets the latitude from where the message is posted.
     * @return Latitude.
     */
    public String getLatitude() { return latitude; }

    /**
     * Gets the longitude of where the message is posted.
     * @return Longitude.
     */
    public String getLongitude() { return longitude; }

    /**
     * Gets the username of the user posting the message.
     * @return The username of op.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the color of the chat message being displayed in the main activity.
     * @return The color code.
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the color code of the message.
     * @param color Color code.
     */
    public void setColor(int color) {
        this.color = color;
    }
}
