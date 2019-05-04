package Pfruit.osbbyx.P.passionfruit.chatModule.model.dataAccess;

import android.net.Uri;
import android.util.Log;

import Pfruit.osbbyx.P.passionfruit.R;
import Pfruit.osbbyx.P.passionfruit.TextingApplication;
import Pfruit.osbbyx.P.passionfruit.chatModule.events.ChatEvent;
import Pfruit.osbbyx.P.passionfruit.common.Constants;
import Pfruit.osbbyx.P.passionfruit.common.model.EventErrorTypeListener;
import Pfruit.osbbyx.P.passionfruit.common.pojo.User;
import Pfruit.osbbyx.P.passionfruit.common.utils.UtilsCommon;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationRS {
    private static final String TEXTING_RS = "https://pfruit.000webhostapp.com/Pfruit/dataAccess/TextingRS.php";
    private static final String SEND_NOTIFICATION = "sendNotification";

    public void sendNotification(String title, String message, String email, String uid,
                                 String myEmail, Uri photoUrl, final EventErrorTypeListener listener){
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.METHOD, SEND_NOTIFICATION);
            params.put(Constants.TITLE, title);
            params.put(Constants.MESSAGE, message);
            params.put(Constants.TOPIC, UtilsCommon.getEmailToTopic(email));
            params.put(User.UID, uid);
            params.put(User.EMAIL, myEmail);
            params.put(User.PHOTO_URL, photoUrl);
            params.put(User.USERNAME, title);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(ChatEvent.ERROR_PROCESS_DATA, R.string.common_error_process_data);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, TEXTING_RS,
                params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt(Constants.SUCCESS);
                    switch (success){
                        case ChatEvent.SEND_NOTIFICATION_SUCCESS:

                            break;
                        case ChatEvent.ERROR_METHOD_NOT_EXIST:
                            listener.onError(ChatEvent.ERROR_METHOD_NOT_EXIST, R.string.chat_error_method_not_exist);
                            break;
                        default:
                            listener.onError(ChatEvent.ERROR_SERVER, R.string.common_error_server);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError(ChatEvent.ERROR_PROCESS_DATA, R.string.common_error_process_data);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley error", error.getLocalizedMessage());
                listener.onError(ChatEvent.ERROR_VOLLEY, R.string.common_error_volley);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };

        TextingApplication.getInstance().addToReqQueue(jsonObjectRequest);
    }
}
