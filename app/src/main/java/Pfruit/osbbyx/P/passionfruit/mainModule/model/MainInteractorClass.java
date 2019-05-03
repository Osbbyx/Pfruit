package Pfruit.osbbyx.P.passionfruit.mainModule.model;

import Pfruit.osbbyx.P.passionfruit.common.Constants;
import Pfruit.osbbyx.P.passionfruit.common.model.BasicEventsCallback;
import Pfruit.osbbyx.P.passionfruit.common.model.dataAccess.FirebaseCloudMessagingAPI;
import Pfruit.osbbyx.P.passionfruit.common.pojo.User;
import Pfruit.osbbyx.P.passionfruit.mainModule.events.MainEvent;
import Pfruit.osbbyx.P.passionfruit.mainModule.model.dataAccess.Authentication;
import Pfruit.osbbyx.P.passionfruit.mainModule.model.dataAccess.RealtimeDatabase;
import Pfruit.osbbyx.P.passionfruit.mainModule.model.dataAccess.UserEventListener;

import org.greenrobot.eventbus.EventBus;


public class MainInteractorClass implements MainInteractor {
    private RealtimeDatabase mDatabase;
    private Authentication mAuthentication;
    //notify
    private FirebaseCloudMessagingAPI mCloudMessagingAPI;

    private User mMyUser = null;

    public MainInteractorClass() {
        mDatabase = new RealtimeDatabase();
        mAuthentication = new Authentication();
        //notify
        mCloudMessagingAPI = FirebaseCloudMessagingAPI.getInstance();
    }

    @Override
    public void subscribeToUserList() {
        mDatabase.subscribeToUserList(getCurrentUser().getUid(), new UserEventListener() {
            @Override
            public void onUserAdded(User user) {
                post(MainEvent.USER_ADDED, user);
            }

            @Override
            public void onUserUpdated(User user) {
                post(MainEvent.USER_UPDATED, user);
            }

            @Override
            public void onUserRemoved(User user) {
                post(MainEvent.USER_REMOVED, user);
            }

            @Override
            public void onError(int resMsg) {
                postError(resMsg);
            }
        });

        mDatabase.subscribeToRequests(getCurrentUser().getEmail(), new UserEventListener() {
            @Override
            public void onUserAdded(User user) {
                post(MainEvent.REQUEST_ADDED, user);
            }

            @Override
            public void onUserUpdated(User user) {
                post(MainEvent.REQUEST_UPDATED, user);
            }

            @Override
            public void onUserRemoved(User user) {
                post(MainEvent.REQUEST_REMOVED, user);
            }

            @Override
            public void onError(int resMsg) {
                post(MainEvent.ERROR_SERVER);
            }
        });

        changeConnectionStatus(Constants.ONLINE);
    }

    private void changeConnectionStatus(boolean online) {
        mDatabase.getmDatabaseAPI().updateMyLastConnection(online, getCurrentUser().getUid());
    }

    @Override
    public void unsubscribeToUserList() {
        mDatabase.unsubscribeToUsers(getCurrentUser().getUid());
        mDatabase.unsbuscribeToRequests(getCurrentUser().getEmail());

        changeConnectionStatus(Constants.OFFLINE);
    }

    @Override
    public void signOff() {
        mCloudMessagingAPI.unsubscribeToMyTopic(getCurrentUser().getEmail());

        mAuthentication.signOff();
    }

    @Override
    public User getCurrentUser() {
        return mMyUser == null? mAuthentication.getmAuthenticationAPI().getAuthUser() : mMyUser;
    }

    @Override
    public void removeFriend(String friendUid) {
        mDatabase.removeUser(friendUid, getCurrentUser().getUid(), new BasicEventsCallback() {
            @Override
            public void onSuccess() {
                post(MainEvent.USER_REMOVED);
            }

            @Override
            public void onError() {
                post(MainEvent.ERROR_SERVER);
            }
        });
    }

    @Override
    public void acceptRequest(final User user) {
        mDatabase.acceptRequest(user, getCurrentUser(), new BasicEventsCallback() {
            @Override
            public void onSuccess() {
                post(MainEvent.REQUEST_ACCEPTED, user);
            }

            @Override
            public void onError() {
                post(MainEvent.ERROR_SERVER);
            }
        });
    }

    @Override
    public void denyRequest(User user) {
        mDatabase.denyRequest(user, getCurrentUser().getEmail(), new BasicEventsCallback() {
            @Override
            public void onSuccess() {
                post(MainEvent.REQUEST_DENIED);
            }

            @Override
            public void onError() {
                post(MainEvent.ERROR_SERVER);
            }
        });
    }

    private void postError(int resMsg) {
        post(MainEvent.ERROR_SERVER, null, resMsg);
    }

    private void post(int typeEvent) {
        post(typeEvent, null, 0);
    }

    private void post(int typeEvent, User user){
        post(typeEvent, user, 0);
    }

    private void post(int typeEvent, User user, int resMsg) {
        MainEvent event = new MainEvent();
        event.setTypeEvent(typeEvent);
        event.setUser(user);
        event.setResMsg(resMsg);
        EventBus.getDefault().post(event);
    }
}
