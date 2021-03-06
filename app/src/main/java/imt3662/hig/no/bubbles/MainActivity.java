package imt3662.hig.no.bubbles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import imt3662.hig.no.bubbles.MessageHandling.MessageDelegater;
import imt3662.hig.no.bubbles.MessageHandling.MessageEventHandler;
import imt3662.hig.no.bubbles.MessageSerializing.DestroyNode;
import imt3662.hig.no.bubbles.MessageSerializing.PostChatMessage;
import imt3662.hig.no.bubbles.MessageSerializing.ServerStatusRequest;

/**
 * Main activity of the application.
 */
public class MainActivity extends Activity implements MessageEventHandler, MessageErrorListener {
    private static List<Integer> ignoredUsers = new LinkedList<Integer>();
    private static int currentUserID = -1;
    private static int userCount;
    private static int radius;

    private GcmHelper gcm;
    private int longPressedMsgPosition = -1;
    private Menu menu;

    private LocationProvider locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            this.locationProvider = LocationProvider.get(this, null);
        }
        catch(Exception e) {
            Log.w("PROVIDER", "FAILED TO GET PROVIDER: " + e.getMessage());
        }

        ListView lv = (ListView) findViewById(R.id.listview);
        registerForContextMenu(lv);

        this.gcm = GcmHelper.get(this, this);
        this.gcm.startPinging();

        if (currentUserID == -1)
            showStatusMessage(R.string.chatmessage_welcome);

        Intent intent = getIntent();
        if (intent.hasExtra("user_id")) {
            currentUserID = intent.getIntExtra("user_id", currentUserID);
            userCount = intent.getIntExtra("user_count", userCount);
            radius = intent.getIntExtra("radius", radius);
        }

        populateListView();
        MessageDelegater.getInstance().setReceiver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LatLng loc = this.locationProvider.getLastKnownLocation();
        gcm.sendMessage(new ServerStatusRequest(loc.latitude, loc.longitude));
    }

    /**
     * Called once a message was failed to be sent to gcm.
     * In that case, we should display an error message to the user.
     * @param ex The exception.
     */
    @Override
    public void failedToSend(IOException ex) {
        Log.w("gcm", "Failed to send message: " + ex.getMessage());
        showStatusMessage(R.string.chatmessage_unable_to_send);
    }

    /**
     * Add a chat message to the user output.
     * @param message The chat message posted.
     */
    @Override
    public void messagePosted(ChatMessage message) {
        Log.i("gcm", "posted a message: " + message.getMsg());
        if (message.getUserID() != this.currentUserID
                && !ignoredUsers.contains(message.getUserID())) {
            addChatMessage(message);
        }
    }

    /**
     * Notifies that a new user entered the chat.
     * @param userId
     */
    @Override
    public void nodeEntered(int userId) {
        if (userId != this.currentUserID && this.currentUserID > 0) {
            showStatusMessage(R.string.chatmessage_someone_joined);
        }
        Log.i("gcm", "node entered: " + userId);
    }

    /**
     * Notify the user that he got kicked out of the server if it is the user's user-id,
     * otherwise post that someone left the chat.
     * @param userId Id of the user leaving the chat.
     */
    @Override
    public void nodeLeft(int userId) {
        if (userId == this.currentUserID) {
            showStatusMessage(R.string.chatmessage_kicked_reconnect);
            currentUserID = 0;
            LatLng loc = this.locationProvider.getLastKnownLocation();
            gcm.sendMessage(new ServerStatusRequest(loc.latitude, loc.longitude));
        }
        else {
            showStatusMessage(R.string.chatmessage_someone_left);
        }

        Log.i("gcm", "node left: " + userId);
    }

    /**
     * Called once we receive an update from the server regarding our user-id and the user count.
     * @param userCount Amount of connected nodes/users within the area.
     * @param userId The assigned user/node id that has been given to us.
     */
    @Override
    public void gotServerInfo(final int userCount, final int userId, final int radius) {
        Log.i("gcm", "Got server info count: " + userCount + ", your user ID: " + userId);
        if (currentUserID != userId) {
            currentUserID = userId;
            showStatusMessage(R.string.chatmessage_reconnect);
        }

        MainActivity.userCount = userCount;
        MainActivity.radius = radius;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (menu != null) {
                    MenuItem numberOfUsers = menu.findItem(R.id.number_users);
                    //numberOfUsers.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
                    numberOfUsers.setTitle(String.valueOf(userCount));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;

        MenuItem numberOfUsers = menu.findItem(R.id.number_users_icon);
        numberOfUsers.setTitle(String.valueOf(userCount));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {//when settings button is pressed, setting act. starts
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateListView() {
        ArrayAdapter<ChatMessage> adapter = new ChatListAdapter(this, getLayoutInflater());
        ListView listView = (ListView)findViewById(R.id.listview);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);
        listView.setAdapter(adapter);
    }

    /**
     * Creates a new message and post it to the server.
     */
    public void newMessage(View view) {
        EditText editText = (EditText)findViewById(R.id.editText);
        if (editText.getText().length() > 0 && this.currentUserID > 0) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            boolean track = prefs.getBoolean(getString(R.string.preference_key_trackme), true);
            String username = prefs.getString(getString(R.string.preference_key_username), "");

            ChatMessage newMsg = new ChatMessage(this.currentUserID,
                    String.valueOf(editText.getText()), track,
                    locationProvider.getLastKnownLocation(),
                    username, ChatMessage.USER_MESSAGE);
            Log.w("TRACK",newMsg.getLatitude());

            gcm.sendMessage(new PostChatMessage(newMsg));
            addChatMessage(newMsg);
            editText.setText("");

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    private void showStatusMessage(int message) {
        ChatMessage statusMessage = new ChatMessage(getString(message), ChatMessage.SYSTEM_MESSAGE);
        addChatMessage(statusMessage);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.msg_long_click, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        longPressedMsgPosition = info.position;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ChatMessage msg = ChatListAdapter.getChatMessage(longPressedMsgPosition);

        switch (item.getItemId()) {
            case R.id.findOnMap:
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

                intent.putExtra("TRACED_LATITUDE", msg.getLatitude());
                intent.putExtra("TRACED_LONGITUDE", msg.getLongitude());
                intent.putExtra("TRACED_USERNAME", msg.getUsername());
                intent.putExtra("LATITUDE", locationProvider.getLastKnownLatitude());
                intent.putExtra("LONGITUDE", locationProvider.getLastKnownLongitude());
                intent.putExtra("RADIUS", String.valueOf(this.radius));
                startActivity(intent);

                return true;
            case R.id.ignore:
                ignoredUsers.add(msg.getUserID());

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public boolean showMyLocation(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("LATITUDE", locationProvider.getLastKnownLatitude());
        intent.putExtra("LONGITUDE", locationProvider.getLastKnownLongitude());
        intent.putExtra("RADIUS", String.valueOf(this.radius));
        startActivity(intent);
        return true;
    }

    private void addChatMessage(final ChatMessage message) {
        Handler handler = new Handler(this.getMainLooper());
        Runnable action = new Runnable() {
            @Override
            public void run() {
                ChatListAdapter.addChatMessage(message);
                ListView listView = (ListView) findViewById(R.id.listview);
                ChatListAdapter adapter = (ChatListAdapter) listView.getAdapter();
                adapter.notifyDataSetChanged();
            }
        };
        handler.post(action);
    }

    @Override
    protected void onStop() {
        super.onStop();
        gcm.stopPining();
        gcm.sendMessage(new DestroyNode());
    }
}


