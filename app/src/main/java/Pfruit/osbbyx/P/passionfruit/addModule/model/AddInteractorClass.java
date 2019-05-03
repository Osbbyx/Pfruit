package Pfruit.osbbyx.P.passionfruit.addModule.model;

import Pfruit.osbbyx.P.passionfruit.addModule.events.AddEvent;
import Pfruit.osbbyx.P.passionfruit.addModule.model.dataAccess.RealtimeDatabase;
import Pfruit.osbbyx.P.passionfruit.common.model.BasicEventsCallback;
import Pfruit.osbbyx.P.passionfruit.common.model.dataAccess.FirebaseAuthenticationAPI;

import org.greenrobot.eventbus.EventBus;


public class AddInteractorClass implements AddInteractor {
    private RealtimeDatabase mDatabase;
    private FirebaseAuthenticationAPI mAuthenticationAPI;

    public AddInteractorClass() {
        this.mDatabase = new RealtimeDatabase();
        this.mAuthenticationAPI = FirebaseAuthenticationAPI.getInstance();
    }

    @Override
    public void addFriend(String email) {
        mDatabase.addFriend(email, mAuthenticationAPI.getAuthUser(), new BasicEventsCallback() {
            @Override
            public void onSuccess() {
                post(AddEvent.SEND_REQUEST_SUCCESS);
            }

            @Override
            public void onError() {
                post(AddEvent.ERROR_SERVER);
            }
        });
    }

    private void post(int typeEvent) {
        AddEvent event = new AddEvent();
        event.setTypeEvent(typeEvent);
        EventBus.getDefault().post(event);
    }
}
