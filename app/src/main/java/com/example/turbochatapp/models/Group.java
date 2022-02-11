package com.example.turbochatapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Objects;
import java.util.UUID;

@IgnoreExtraProperties
public class Group implements Parcelable {

    @Exclude
    private String id;
    @Exclude
    private String documentId;
    private String name;
    private int size;
    private String displayPicture;
    private Timestamp dateCreated;

    public Group(){
        this.id = UUID.randomUUID().toString();
    }

    public Group(String id, String documentId, String name, int size, String displayPicture, Timestamp dateCreated) {
        this.id = id;
        this.documentId = documentId;
        this.name = name;
        this.size = size;
        this.displayPicture = displayPicture;
        this.dateCreated = dateCreated;
    }

    protected Group(Parcel in) {
        id = in.readString();
        documentId = in.readString();
        name = in.readString();
        size = in.readInt();
        displayPicture = in.readString();
        dateCreated = in.readParcelable(Timestamp.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(documentId);
        dest.writeString(name);
        dest.writeInt(size);
        dest.writeString(displayPicture);
        dest.writeParcelable(dateCreated, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDisplayPicture() {
        return displayPicture;
    }

    public void setDisplayPicture(String displayPicture) {
        this.displayPicture = displayPicture;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return getSize() == group.getSize() && Objects.equals(getName(), group.getName()) && Objects.equals(getDisplayPicture(), group.getDisplayPicture()) && Objects.equals(getDateCreated(), group.getDateCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSize(), getDisplayPicture(), getDateCreated());
    }

    public boolean isShimmer(){
        return getName() == null && getDateCreated() == null && getSize() == 0 && getDisplayPicture() == null;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    @Exclude
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
