package Pfruit.osbbyx.P.passionfruit.profileModule.events;


public class ProfileEvent {
    public static final int UPLOAD_IMAGE = 0;
    public static final int SAVE_USERNAME = 1;
    public static final int ERROR_USERNAME = 100;
    public static final int ERROR_IMAGE = 101;
    public static final int ERROR_PROFILE = 102;
    public static final int ERROR_SERVER = 103;

    private int typeEvent;
    private int resMsg;
    private String photoUrl;

    public ProfileEvent() {
    }

    public int getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(int typeEvent) {
        this.typeEvent = typeEvent;
    }

    public int getResMsg() {
        return resMsg;
    }

    public void setResMsg(int resMsg) {
        this.resMsg = resMsg;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
