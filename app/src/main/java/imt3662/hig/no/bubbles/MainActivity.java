package imt3662.hig.no.bubbles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    List<ChatMessage> chatMessages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatMessages = new ArrayList<ChatMessage>();

        //just for the prototype
        ChatMessage cm = new ChatMessage(1,"Hello world!" ,"Anon");
        ChatMessage cm1 = new ChatMessage(1,"Hello world!2" ,"Anon");
        ChatMessage cm2 = new ChatMessage(1,"Hello world!3" ,"Anon");
        chatMessages.add(cm);
        chatMessages.add(cm1);
        chatMessages.add(cm2);


        populateListView();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
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

            if(convertView == null) {

                convertView = getLayoutInflater().inflate(R.layout.listitem, parent, false);
            }

            final ChatMessage currentMessage = chatMessages.get(position);


            TextView txtView = (TextView) convertView.findViewById(R.id.text1);

            RelativeLayout rv = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);


            ShapeDrawable shapeDrawable = new ShapeDrawable();
            float[] radius = new float[8];
            radius[0] = 20.0F;
            radius[1] = 20.0F;
            radius[2] = 20.0F;
            radius[3] = 20.0F;
            radius[4] = 20.0F;
            radius[5] = 20.0F;
            radius[6] = 20.0F;
            radius[7] = 20.0F;


            //Color c = new Color();
            //int colorint = c.argb(255, new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
            shapeDrawable.setShape(new RoundRectShape(radius, null, radius));

            shapeDrawable.getPaint().setColor(currentMessage.getColor());

            rv.setBackground(shapeDrawable);


            txtView.setText(currentMessage.getUsername() + ": " + currentMessage.getMsg());

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Toast.makeText(getApplicationContext(), "Long Clicked ",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

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

    //prototype for testing the chat
    public void newMessage(View view) {

        EditText editText = (EditText)findViewById(R.id.editText);
        if(editText.getText().length() > 0) {
            ChatMessage newmsg = new ChatMessage(1, String.valueOf(editText.getText()), "Anon");
            chatMessages.add(newmsg);
            editText.setText("");

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            //populateListView();
        }

    }

    //prototype. this is here just because.
    public void toast(View view) {
        Toast.makeText(getApplicationContext(), "Valg",
                Toast.LENGTH_SHORT).show();

    }
}
