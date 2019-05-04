package Pfruit.osbbyx.P.passionfruit.chatModule.model.dataAccess;

import android.app.Activity;
import android.net.Uri;

import Pfruit.osbbyx.P.passionfruit.R;
import Pfruit.osbbyx.P.passionfruit.common.model.StorageUploadImageCallback;
import Pfruit.osbbyx.P.passionfruit.common.model.dataAccess.FirebaseStorageAPI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Storage {
    private static final String PATH_CHATS = "chats";

    private FirebaseStorageAPI mStorageAPI;

    public Storage() {
        mStorageAPI = FirebaseStorageAPI.getInstance();
    }

    /*
    *   Image chat
    * */
    public void uploadImageChat(Activity activity, final Uri imageUri, String myEmail,
                                final StorageUploadImageCallback callback){
        if (imageUri.getLastPathSegment() != null){
            StorageReference photoRef = mStorageAPI.getPhotosReferenceByEmail(myEmail).child(PATH_CHATS)
                    .child(imageUri.getLastPathSegment());
            photoRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (uri != null){
                                        callback.onSuccess(uri);
                                    } else {
                                        callback.onError(R.string.chat_error_imageUpload);
                                    }
                                }
                            });
                }
            });
        }
    }
}
