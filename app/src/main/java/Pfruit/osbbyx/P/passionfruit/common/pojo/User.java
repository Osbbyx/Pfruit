package Pfruit.osbbyx.P.passionfruit.common.pojo;

import android.net.Uri;

import com.google.firebase.database.Exclude;


public class User {
    public static final String USERNAME = "username";
    public static final String PHOTO_URL = "photoUrl";
    public static final String EMAIL = "email";
    public static final String LAST_CONNECTION_WITH = "lastConnectionWith";
    public static final String MESSAGES_UNREAD = "messagesUnread";
    public static final String UID = "uid";

    private String lastConnectionWith;
    private String username;
    private String email;
    private String photoUrl;
    private int messagesUnread;

    @Exclude
    private String uid;
    @Exclude
    private Uri uri;

    public User() {
    }

    public String getLastConnectionWith() {
        return lastConnectionWith;
    }

    public void setLastConnectionWith(String lastConnectionWith) {
        this.lastConnectionWith = lastConnectionWith;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl != null? photoUrl : uri.toString();
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getMessagesUnread() {
        return messagesUnread;
    }

    public void setMessagesUnread(int messagesUnread) {
        this.messagesUnread = messagesUnread;
    }
    @Exclude
    public String getUid() {
        return uid;
    }
    @Exclude
    public void setUid(String uid) {
        this.uid = uid;
    }
    @Exclude
    public Uri getUri() {
        return uri;
    }
    @Exclude
    public void setUri(Uri uri) {
        this.uri = uri;
    }
    @Exclude
    public String getUsernameValid(){
        return username == null?
                getEmail() : username.isEmpty()?
                getEmail() : username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return uid != null ? uid.equals(user.uid) : user.uid == null;
    }

    @Override
    public int hashCode() {
        return uid != null ? uid.hashCode() : 0;
    }
}
