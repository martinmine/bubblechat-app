package imt3662.hig.no.bubbles;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Serves as adapter between the contacts and the GUI contact list
 */
class ChatListAdapter extends ArrayAdapter<ChatMessage> {
    private static List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
    private static final float MSG_CORNER_RADIUS = 20.0F; //radius of messages in the list view
    private static final float[] MSG_RAD = {MSG_CORNER_RADIUS, MSG_CORNER_RADIUS,
            MSG_CORNER_RADIUS, MSG_CORNER_RADIUS, MSG_CORNER_RADIUS,
            MSG_CORNER_RADIUS, MSG_CORNER_RADIUS, MSG_CORNER_RADIUS};

    /**
     * Gest a chat message from the messages being stored in the view
     * @param i Index of the chat message
     * @return The chat message being requested, null if not found
     */
    public static ChatMessage getChatMessage(int i) {
        return chatMessages.get(i);
    }

    public static void addChatMessage(ChatMessage message) {
        chatMessages.add(message);
    }

    private LayoutInflater inflater;
    private View.OnTouchListener touchListener;

    public ChatListAdapter(Context context, LayoutInflater inflater) {
        super(context, R.layout.listitem, chatMessages);
        this.inflater = inflater;
        this.touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.showContextMenu();
                view.performClick();
                return true;
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem, parent, false);
        }

        final ChatMessage currentMessage = chatMessages.get(position);
        TextView msgText = (TextView) convertView.findViewById(R.id.msgText);
        RelativeLayout rv = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
        ShapeDrawable shapeDrawable = new ShapeDrawable();

        shapeDrawable.setShape(new RoundRectShape(MSG_RAD, null, MSG_RAD));
        shapeDrawable.getPaint().setColor(currentMessage.getColor());
        rv.setBackground(shapeDrawable);

        Spinner spinner = (Spinner) rv.findViewById(R.id.msgSpinner);

        if (currentMessage.hasFixedColor() == false) {
            spinner.setVisibility(View.INVISIBLE);
            spinner.setOnTouchListener(null);
            convertView.setClickable(true); // Avoid long key press (haha, tricked u!)
        }
        else {
            spinner.setVisibility(View.VISIBLE);
            spinner.setOnTouchListener(this.touchListener);
            convertView.setClickable(false);
        }

        if (currentMessage.getUsername() != null && !currentMessage.getUsername().isEmpty()){
            msgText.setText(currentMessage.getUsername() + ": " + currentMessage.getMsg());
        }
        else {
            msgText.setText(currentMessage.getMsg());
        }

        return convertView;
    }
}
