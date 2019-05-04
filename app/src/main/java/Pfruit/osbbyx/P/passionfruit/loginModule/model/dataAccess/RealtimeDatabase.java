package Pfruit.osbbyx.P.passionfruit.loginModule.model.dataAccess;

import android.support.annotation.NonNull;

import Pfruit.osbbyx.P.passionfruit.R;
import Pfruit.osbbyx.P.passionfruit.common.model.EventErrorTypeListener;
import Pfruit.osbbyx.P.passionfruit.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import Pfruit.osbbyx.P.passionfruit.common.pojo.User;
import Pfruit.osbbyx.P.passionfruit.loginModule.events.LoginEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class RealtimeDatabase {
    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;

    public RealtimeDatabase() {
        mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public void registerUser(User user){
        Map<String, Object> values = new HashMap<>();
        values.put(User.USERNAME, user.getUsername());
        values.put(User.EMAIL, user.getEmail());
        values.put(User.PHOTO_URL, user.getPhotoUrl());

        mDatabaseAPI.getUserReferenceByUid(user.getUid()).updateChildren(values);
    }

    public void checkUserExist(String uid, final EventErrorTypeListener listener){
        mDatabaseAPI.getUserReferenceByUid(uid).child(User.EMAIL)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            listener.onError(LoginEvent.USER_NOT_EXIST, R.string.login_error_user_exist);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onError(LoginEvent.ERROR_SERVER, R.string.login_message_error);
                    }
                });
    }
}
