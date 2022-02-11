package com.example.turbochatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turbochatapp.R;
import com.example.turbochatapp.models.Message;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class MessageListAdapter extends ListAdapter<Message, MessageListAdapter.MessageHolder> {

    private static final DiffUtil.ItemCallback<Message> CALLBACK = new DiffUtil.ItemCallback<Message>() {
        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.equals(newItem);
        }
    };
    private String username;

    public MessageListAdapter(@NonNull String username) {
        super(CALLBACK);
        this.username = username;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = getItem(position);
        return message.getSender().equals(username) ? 1 : 0;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(
                viewType == 1 ? R.layout.layout_message_item_sender :
                        R.layout.layout_message_item_receiver,
                parent,
                false
        );
        MessageHolder messageHolder = new MessageHolder(view);
        return messageHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.bindWith(getItem(position));
    }

    static class MessageHolder extends RecyclerView.ViewHolder{

        private MaterialTextView txtName, txtTime, txtBody;
        private SimpleDateFormat simpleDateFormat;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_message_name);
            txtTime = itemView.findViewById(R.id.txt_message_time);
            txtBody = itemView.findViewById(R.id.txt_message_body);
            simpleDateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
        }

        public void bindWith(Message message){
            txtName.setText(message.getSender());
            txtBody.setText(message.getContent());
            txtTime.setText(simpleDateFormat.format(
                    message.getTimestamp().toDate()
            ));
        }
    }
}
