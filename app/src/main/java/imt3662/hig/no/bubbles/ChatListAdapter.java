package imt3662.hig.no.bubbles;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Serves as adapter between the contacts and the GUI contact list
 */
class ChatListAdapter extends ArrayAdapter<ChatMessage> {
    private static List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
    private static final float chatMsgRadius = 20.0F; //radius of messages in the list view

    /**
     * Gest a chat message from the messages being stored in the view
     * @param i
     * @return
     */
    public static ChatMessage getChatMessage(int i) {
        return chatMessages.get(i);
    }

    public static void addChatMessage(ChatMessage message) {
        chatMessages.add(message);
    }

    private LayoutInflater inflater;

    public ChatListAdapter(Context context, LayoutInflater inflater) {
        super(context, R.layout.listitem, chatMessages);
        this.inflater = inflater;
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

        //creating the graphics for the chat message
        float[] rad = {chatMsgRadius, chatMsgRadius, chatMsgRadius, chatMsgRadius,
                chatMsgRadius, chatMsgRadius, chatMsgRadius, chatMsgRadius};
        shapeDrawable.setShape(new RoundRectShape(rad, null, rad));
        shapeDrawable.getPaint().setColor(currentMessage.getColor());
        rv.setBackground(shapeDrawable);

        if(currentMessage.getUsername() != null && !currentMessage.getUsername().isEmpty()){
            msgText.setText(currentMessage.getUsername() + ": " + currentMessage.getMsg());
        }
        else {
            msgText.setText(currentMessage.getMsg());
        }

        return convertView;
    }
}
