package Pfruit.osbbyx.P.passionfruit.profileModule.model.dataAccess;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.alain.cursos.texting.R;
import Pfruit.osbbyx.P.passionfruit.common.model.StorageUploadImageCallback;
import Pfruit.osbbyx.P.passionfruit.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import Pfruit.osbbyx.P.passionfruit.common.pojo.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class RealtimeDatabase {
    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;

    public RealtimeDatabase() {
        mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public void changeUsername(final User myUser, final UpdateUserListener listener){
        if (mDatabaseAPI.getUserReferenceByUid(myUser.getUid()) != null){
            Map<String, Object> updates = new HashMap<>();
            updates.put(User.USERNAME, myUser.getUsername());
            mDatabaseAPI.getUserReferenceByUid(myUser.getUid()).updateChildren(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            listener.onSuccess();
                            notifyContactsUsername(myUser, listener);
                        }
                    });
        }
    }

    private void notifyContactsUsername(final User myUser, final UpdateUserListener listener) {
        mDatabaseAPI.getContactsReference(myUser.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            String friendUid = child.getKey();
                            DatabaseReference reference = getContactsReference(friendUid, myUser.getUid());
                            Map<String, Object> updates = new HashMap<>();
                            updates.put(User.USERNAME, myUser.getUsername());
                            reference.updateChildren(updates);
                        }
                        listener.onNotifyContacts();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onError(R.string.profile_error_userUpdated);
                    }
                });
    }

    private DatabaseReference getContactsReference(String mainUid, String childUid) {
        return mDatabaseAPI.getUserReferenceByUid(mainUid)
                .child(FirebaseRealtimeDatabaseAPI.PATH_CONTACTS).child(childUid);
    }

    public void updatePhotoUrl(final Uri downloadUri, final String myUid, final StorageUploadImageCallback callback){
        if (mDatabaseAPI.getUserReferenceByUid(myUid) != null){
            Map<String, Object> updates = new HashMap<>();
            updates.put(User.PHOTO_URL, downloadUri.toString());
            mDatabaseAPI.getUserReferenceByUid(myUid).updateChildren(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            callback.onSuccess(downloadUri);
                            notifyContactsPhoto(downloadUri.toString(), myUid, callback);
                        }
                    });
        }
    }

    private void notifyContactsPhoto(final String photoUrl, final String myUid, final StorageUploadImageCallback callback) {
        mDatabaseAPI.getContactsReference(myUid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            String friendUid = child.getKey();
                            DatabaseReference reference = getContactsReference(friendUid, myUid);
                            Map<String, Object> updates = new HashMap<>();
                            updates.put(User.PHOTO_URL, photoUrl);
                            reference.updateChildren(updates);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onError(R.string.profile_error_imageUpdated);
                    }
                });
    }
}
