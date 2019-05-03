package Pfruit.osbbyx.P.passionfruit.common.model.dataAccess;

import Pfruit.osbbyx.P.passionfruit.common.utils.UtilsCommon;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class FirebaseStorageAPI {
    private FirebaseStorage mFirebaseStorage;

    private static class Singleton{
        private static final FirebaseStorageAPI INSTANCE = new FirebaseStorageAPI();
    }

    public static FirebaseStorageAPI getInstance(){
        return  Singleton.INSTANCE;
    }

    private FirebaseStorageAPI(){
        this.mFirebaseStorage = FirebaseStorage.getInstance();
    }

    public FirebaseStorage getmFirebaseStorage() {
        return mFirebaseStorage;
    }

    public StorageReference getPhotosReferenceByEmail(String email){
        return  mFirebaseStorage.getReference().child(UtilsCommon.getEmailEncoded(email));
    }
}
