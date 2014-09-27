package imt3662.hig.no.bubbles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import imt3662.hig.no.bubbles.MessageHandling.MessageDelegater;
import imt3662.hig.no.bubbles.MessageHandling.MessageEventHandler;
import imt3662.hig.no.bubbles.MessageSerializing.PostChatMessage;
import imt3662.hig.no.bubbles.MessageSerializing.ServerStatusRequest;


public class MainActivity extends Activity implements MessageEventHandler, MessageErrorListener, LocationReceiver {
    private List<ChatMessage> chatMessages;
    private static final float chatMsgRadius = 20.0F; //radius of chat messages
    private GcmHelper gcm;
    private int currentUserID;
    int longPressedMsgPosition = -1;

    private LocationProvider locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.locationProvider = new LocationProvider(this, this);
        ListView lv = (ListView) findViewById(R.id.listview);
        registerForContextMenu(lv);

        chatMessages = new ArrayList<ChatMessage>();

        //test chat messages
        ChatMessage cm = new ChatMessage(1,"Hello world!", true, 60.0, 9.0,"Pels");
        ChatMessage cm1 = new ChatMessage(1,"Hello world!2", true, 60.0, 60.0,"Anon");
        ChatMessage cm2 = new ChatMessage(1,"Hello world!3",true, 60.0, 60.0 ,"Anon");
        chatMessages.add(cm);
        chatMessages.add(cm1);
        chatMessages.add(cm2);


        populateListView();

        if (!GcmHelper.checkPlayServices(this)) {
            // TODO show error message that the user needs to upgrade google play services
        }

        SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        this.gcm = new GcmHelper(this, this);
        this.currentUserID = -1;
        this.gcm.beginRegistering(prefs, getAppVersion(this), new DeviceRegisteredListener() {
            @Override
            public void registered(String gcmId) {
                Log.i("gcm", "Registered on gcm with id " + gcmId);
            }
        });

        MessageDelegater.getInstance().setReceiver(this);
    }

    /**
     * @return Current application version
     * Code from google tutorial
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @Override
    public void failedToSend(IOException ex) {
        Log.w("gcm", "Failed to send message: " + ex.getMessage());
    }

    @Override
    public void messagePosted(int userId, String message, boolean hasLocation, double lat, double lng, String username) {
        Log.i("gcm", "posted a message: " + message);
    }

    @Override
    public void nodeEntered(int userId) {
        Log.i("gcm", "node entered: " + userId);
        // TODO add message bubble
    }

    @Override
    public void nodeLeft(int userId) {
        Log.i("gcm", "node left: " + userId);
        // TODO add message bubble
    }

    @Override
    public void gotServerInfo(int userCount, int userId) {
        Log.i("gcm", "Got server info count: " + userCount + ", your user ID: " + userId);
        this.currentUserID = userId;
        // TODO update user count thingy
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void locationChanged(Location loc) {
        if (currentUserID == -1 && !gcm.getRegistrationId().isEmpty()) {
            currentUserID = 0;
            gcm.sendMessage(new ServerStatusRequest(loc.getLatitude(), loc.getLongitude()));
        }
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

    // Inner class
    // Serves as adapter between the contacts and the GUI contact list
    private class ChatListAdapter extends ArrayAdapter<ChatMessage> {

        public ChatListAdapter() {

            super(MainActivity.this, R.layout.listitem, chatMessages);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {

                convertView = getLayoutInflater().inflate(R.layout.listitem, parent, false);
            }

            final ChatMessage currentMessage = chatMessages.get(position);


            TextView msgText = (TextView) convertView.findViewById(R.id.msgText);

            RelativeLayout rv = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);


            ShapeDrawable shapeDrawable = new ShapeDrawable();

            //creating the graphics for the chat message
            float[] rad = {chatMsgRadius, chatMsgRadius, chatMsgRadius, chatMsgRadius,
                    chatMsgRadius, chatMsgRadius, chatMsgRadius, chatMsgRadius};
            shapeDrawable.setShape(new RoundRectShape(rad, null, rad));
            shapeDrawable.getPaint().setColor(currentMessage.getColor());
            rv.setBackground(shapeDrawable);

            if(currentMessage.getUsername() != null){
                msgText.setText(currentMessage.getUsername() + ": ");
            }
            msgText.append(currentMessage.getMsg());

            /*
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("TRACED_LATITUDE", currentMessage.getLatitude());
                    intent.putExtra("TRACED_LONGITUDE", currentMessage.getLongitude());
                    intent.putExtra("TRACED_USERNAME", currentMessage.getUsername());
                    intent.putExtra("LATITUDE", "60.0");
                    intent.putExtra("LONGITUDE", "60.0");
                    startActivity(intent);

                    return true;
                }
            });*/

            return convertView;
        }

    }

    private void populateListView() {

        ArrayAdapter<ChatMessage> adapter = new ChatListAdapter();
        ListView listView = (ListView)findViewById(R.id.listview);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);
        listView.setAdapter(adapter);

    }

    //used for testing the chat locally (AND NOW TOWARDS THE SERVER!)
    public void newMessage(View view) {

        EditText editText = (EditText)findViewById(R.id.editText);
        if (editText.getText().length() > 0 && this.currentUserID > 0) {
            //TODO: GET PREFERENCES FROM SETTING
            ChatMessage newMsg = new ChatMessage(0,String.valueOf(editText.getText()), true, 60.1, 60.1, "Meg");
            gcm.sendMessage(new PostChatMessage(newMsg));
            chatMessages.add(newMsg);
            editText.setText("");

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            //populateListView();
        }

    }

    //this is here just because.
    public void toast(View view) {
        Toast.makeText(getApplicationContext(), "Valg",
                Toast.LENGTH_SHORT).show();

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
        switch (item.getItemId()) {
            case R.id.findOnMap:
                ChatMessage msg = chatMessages.get(longPressedMsgPosition);



                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("TRACED_LATITUDE", msg.getLatitude());
                intent.putExtra("TRACED_LONGITUDE", msg.getLongitude());
                intent.putExtra("TRACED_USERNAME", msg.getUsername());
                intent.putExtra("LATITUDE", "60.0");
                intent.putExtra("LONGITUDE", "60.0");
                startActivity(intent);



                return true;
            case R.id.ignore:

                return true;
            default:
                return super.onContextItemSelected(item);
        }



    }
}
