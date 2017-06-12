package com.actiknow.clearsale.service;

import android.util.Log;

import com.actiknow.clearsale.model.Notification;
import com.actiknow.clearsale.utils.NotificationUtils;
import com.actiknow.clearsale.utils.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("karman", "message received");
//        Utils.showLog (Log.DEBUG, TAG, "from " + remoteMessage.getFrom (), true);
//        Utils.showLog (Log.DEBUG, TAG, "To " + remoteMessage.getTo (), true);
//        Utils.showLog (Log.DEBUG, TAG, "Collapse Key " + remoteMessage.getCollapseKey (), true);
//        Utils.showLog (Log.DEBUG, TAG, "Message ID " + remoteMessage.getMessageId (), true);
//        Utils.showLog (Log.DEBUG, TAG, "Message Type" + remoteMessage.getMessageType (), true);
//        Utils.showLog (Log.DEBUG, TAG, "Sent Time " + remoteMessage.getSentTime (), true);
//        Utils.showLog (Log.DEBUG, TAG, "TTL " + remoteMessage.getTtl (), true);
    
        if (remoteMessage == null)
            return;
        if (remoteMessage.getData ().size () > 0) {
//            Utils.showLog (Log.DEBUG, TAG, "Data Payload: " + remoteMessage.getData ().toString (), true);
            try {
                handleDataMessage (new JSONObject (remoteMessage.getData ().toString ()));
            } catch (Exception e) {
                Utils.showLog (Log.ERROR, TAG, "Exception: " + e.getMessage (), true);
            }
        }
    }
    
    private void handleDataMessage (JSONObject notificationData) {
        Notification notification = new Notification();
        try {
            JSONObject data = notificationData.getJSONObject ("data");
            notification.setBackground (data.getBoolean ("is_background"));
            notification.setTitle (data.getString ("title"));
            notification.setMessage (data.getString ("message"));
            notification.setImage_url (data.getString ("image"));
            notification.setTimestamp (data.getString ("timestamp"));
            notification.setPayload (data.getJSONObject ("payload"));
    
            JSONObject payload = data.getJSONObject ("payload");

            notification.setNotification_style (payload.getInt ("notification_style"));
            notification.setNotification_type (payload.getInt ("notification_type"));
            notification.setNotification_priority (payload.getInt ("notification_priority"));
        } catch (JSONException e) {
            Utils.showLog (Log.ERROR, TAG, "JSON Exception: " + e.getMessage (), true);
        } catch (Exception e) {
            Utils.showLog (Log.ERROR, TAG, "Exception: " + e.getMessage (), true);
        }
        new NotificationUtils (getApplicationContext ()).showNotificationMessage (notification);
    }
}
