package Pfruit.osbbyx.P.passionfruit.mainModule.model.dataAccess;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import Pfruit.osbbyx.P.passionfruit.R;
import Pfruit.osbbyx.P.passionfruit.common.model.BasicEventsCallback;
import Pfruit.osbbyx.P.passionfruit.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import Pfruit.osbbyx.P.passionfruit.common.pojo.User;
import Pfruit.osbbyx.P.passionfruit.common.utils.UtilsCommon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;


public class RealtimeDatabase {
    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;

    private ChildEventListener mUserEventListener;
    private ChildEventListener mRequestEventListener;

    public RealtimeDatabase() {
        mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    /*
    *   references
    * */

    public FirebaseRealtimeDatabaseAPI getmDatabaseAPI() {
        return mDatabaseAPI;
    }

    private DatabaseReference getUsersReference(){
        return mDatabaseAPI.getRootReference().child(FirebaseRealtimeDatabaseAPI.PATH_USERS);
    }

    /*
    *   public methods
    * */

    public void subscribeToUserList(String myUid, final UserEventListener listener){
        if (mUserEventListener == null){
            mUserEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    listener.onUserAdded(getUser(dataSnapshot));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    listener.onUserUpdated(getUser(dataSnapshot));
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    listener.onUserRemoved(getUser(dataSnapshot));
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    switch (databaseError.getCode()){
                        case DatabaseError.PERMISSION_DENIED:
                            listener.onError(R.string.main_error_permission_denied);
                            break;
                        default:
                            listener.onError(R.string.common_error_server);
                            break;
                    }
                }
            };
        }
        mDatabaseAPI.getContactsReference(myUid).addChildEventListener(mUserEventListener);
    }

    private User getUser(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        if (user != null){
            user.setUid(dataSnapshot.getKey());
        }
        return user;
    }

    public void subscribeToRequests(String email, final UserEventListener listener){
        if (mRequestEventListener == null){
            mRequestEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    listener.onUserAdded(getUser(dataSnapshot));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    listener.onUserUpdated(getUser(dataSnapshot));
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    listener.onUserRemoved(getUser(dataSnapshot));
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    listener.onError(R.string.common_error_server);
                }
            };
        }

        final String emailEncoded = UtilsCommon.getEmailEncoded(email);
        mDatabaseAPI.getRequestReference(emailEncoded).addChildEventListener(mRequestEventListener);
    }

    public void unsubscribeToUsers(String uid){
        if (mUserEventListener != null){
            mDatabaseAPI.getContactsReference(uid).removeEventListener(mUserEventListener);
        }
    }

    public void unsbuscribeToRequests(String email){
        if (mRequestEventListener != null){
            final String emailEncoded = UtilsCommon.getEmailEncoded(email);
            mDatabaseAPI.getRequestReference(emailEncoded).removeEventListener(mRequestEventListener);
        }
    }

    public void removeUser(String friendUid, String myUid, final BasicEventsCallback callback){
        Map<String, Object> removeUserMap = new HashMap<>();
        removeUserMap.put(myUid+"/"+FirebaseRealtimeDatabaseAPI.PATH_CONTACTS+"/"+ friendUid, null);
        removeUserMap.put(friendUid+"/"+FirebaseRealtimeDatabaseAPI.PATH_CONTACTS+"/"+myUid, null);
        getUsersReference().updateChildren(removeUserMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null){
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }
        });
    }

    public void acceptRequest(User user, User myUser, final BasicEventsCallback callback){
        Map<String, String> userRequestMap = new HashMap<>();
        userRequestMap.put(User.USERNAME, user.getUsername());
        userRequestMap.put(User.EMAIL, user.getEmail());
        userRequestMap.put(User.PHOTO_URL, user.getPhotoUrl());

        Map<String, String> myUserMap = new HashMap<>();
        myUserMap.put(User.USERNAME, myUser.getUsername());
        myUserMap.put(User.EMAIL, myUser.getEmail());
        myUserMap.put(User.PHOTO_URL, myUser.getPhotoUrl());

        final String emailEncoded = UtilsCommon.getEmailEncoded(myUser.getEmail());

        Map<String, Object> acceptRequest = new HashMap<>();
        acceptRequest.put(FirebaseRealtimeDatabaseAPI.PATH_USERS+"/"+user.getUid()+"/"+
                FirebaseRealtimeDatabaseAPI.PATH_CONTACTS+"/"+myUser.getUid(), myUserMap);
        acceptRequest.put(FirebaseRealtimeDatabaseAPI.PATH_USERS+"/"+myUser.getUid()+"/"+
                FirebaseRealtimeDatabaseAPI.PATH_CONTACTS+"/"+user.getUid(), userRequestMap);
        acceptRequest.put(FirebaseRealtimeDatabaseAPI.PATH_REQUESTS+"/"+emailEncoded+"/"+
                user.getUid(), null);

        mDatabaseAPI.getRootReference().updateChildren(acceptRequest, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null){
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }
        });
    }

    public void denyRequest(User user, String myEmail, final BasicEventsCallback callback){
        final String emailEncoded = UtilsCommon.getEmailEncoded(myEmail);
        mDatabaseAPI.getRequestReference(emailEncoded).child(user.getUid())
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null){
                            callback.onSuccess();
                        } else {
                            callback.onError();
                        }
                    }
                });
    }
}
