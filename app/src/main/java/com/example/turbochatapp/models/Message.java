package com.example.turbochatapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Objects;

@IgnoreExtraProperties
public class Message implements Parcelable {

    @Exclude
    private String id;
    private String sender;
    private String content;
    private Timestamp timestamp;

    public Message(){}

    public Message(String id, String sender, String content, Timestamp timestamp) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    protected Message(Parcel in) {
        id = in.readString();
        sender = in.readString();
        content = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(sender);
        dest.writeString(content);
        dest.writeParcelable(timestamp, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(getSender(), message.getSender()) && Objects.equals(getContent(), message.getContent()) && Objects.equals(getTimestamp(), message.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSender(), getContent(), getTimestamp());
    }
}
