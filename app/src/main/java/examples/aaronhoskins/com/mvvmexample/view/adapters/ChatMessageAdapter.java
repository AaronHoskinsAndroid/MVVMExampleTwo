package examples.aaronhoskins.com.mvvmexample.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import examples.aaronhoskins.com.mvvmexample.R;
import examples.aaronhoskins.com.mvvmexample.model.chatmessage.ChatMessage;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {
    ArrayList<ChatMessage> messageArrayList;

    public ChatMessageAdapter(ArrayList<ChatMessage> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.message_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = messageArrayList.get(position);
        holder.bindValuesToViews(message);
    }

    public void onMessageListUpdate(ArrayList<ChatMessage> messageArrayList) {
        this.messageArrayList = messageArrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMessageUserId;
        TextView tvMessage;
        TextView tvTime;
        public ViewHolder(View itemView) {
            super(itemView);
            tvMessageUserId = itemView.findViewById(R.id.tvUserId);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }

        public void bindValuesToViews(ChatMessage message) {
            tvMessageUserId.setText(message.getEmailFromID());
            tvMessage.setText(message.getMessage());
            tvTime.setText(message.getTimeOfMessage());
        }
    }
}
