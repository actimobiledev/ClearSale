package com.actiknow.clearsale.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.RemoteViews;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.activity.PropertyDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationUtils {
// Notification Type => 1=> new property add, 2=>, 3=>
// Notification Style => 1=> simple_notification, 2=>inbox_style, 3=>big_text_style, 4=>big_picture_style, 5=> custom layout
// Notification Priority => -2=>PRIORITY_MIN, -1=>PRIORITY_LOW, 0=>PRIORITY_DEFAULT, 1=>PRIORITY_HIGH, 2=>PRIORITY_MAX
    
    private static String TAG = NotificationUtils.class.getSimpleName ();
    private Context mContext;
    
    public NotificationUtils (Context mContext) {
        this.mContext = mContext;
    }
    
    public void showNotificationMessage (com.actiknow.clearsale.model.Notification notification) {
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder (mContext);
        if (TextUtils.isEmpty (notification.getMessage ()))
            return;
        
        PendingIntent pendingIntent = null;
        Notification notification1;
        NotificationManager mNotifyManager = (NotificationManager) mContext.getSystemService (Context.NOTIFICATION_SERVICE);
        switch (notification.getNotification_type ()) {
            case 1:
                Bitmap bitmap = null;
                if (notification.getImage_url () != null && notification.getImage_url ().length () > 4 && Patterns.WEB_URL.matcher (notification.getImage_url ()).matches ()) {
                    bitmap = Utils.getBitmapFromURL (notification.getImage_url ());
                } else {
                    bitmap = Utils.getBitmapFromURL ("http://clearsale.com/theme/theme1/seller_files/exterior/property_822/45f866b7729ccacc6869ad8cf5906172IMG_7738.jpg");
                }
    
                RemoteViews new_property_expanded = new RemoteViews (mContext.getPackageName (), R.layout.notification_item_property_expanded);
                RemoteViews new_property_small = new RemoteViews (mContext.getPackageName (), R.layout.notification_item_property_small);
    
                try {
                    JSONObject jsonObject = notification.getPayload ();
        
                    Intent notificationIntent = new Intent (mContext, PropertyDetailActivity.class);
                    notificationIntent.putExtra (AppConfigTags.PROPERTY_ID, jsonObject.getInt (AppConfigTags.PROPERTY_ID));
                    notificationIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    pendingIntent = PendingIntent.getActivity (mContext, 0, notificationIntent, 0);
        
                    new_property_small.setImageViewBitmap (R.id.ivNotificationTitle, Utils.textAsBitmap (mContext, notification.getTitle (), 18, Color.WHITE));
        
                    new_property_expanded.setImageViewBitmap (R.id.ivNotificationTitle, Utils.textAsBitmap (mContext, notification.getTitle (), 18, Color.WHITE));
                    new_property_expanded.setImageViewBitmap (R.id.ivAddress1, Utils.textAsBitmap (mContext, jsonObject.getString (AppConfigTags.PROPERTY_ADDRESS), 10, Color.WHITE));
                    new_property_expanded.setImageViewBitmap (R.id.ivAddress2, Utils.textAsBitmap (mContext, jsonObject.getString (AppConfigTags.PROPERTY_ADDRESS2), 10, Color.WHITE));
                    new_property_expanded.setImageViewBitmap (R.id.iv1, Utils.textAsBitmap (mContext, "Beds", 10, Color.WHITE));
                    new_property_expanded.setImageViewBitmap (R.id.ivBedroom, Utils.textAsBitmap (mContext, jsonObject.getString (AppConfigTags.PROPERTY_BEDROOMS), 16, Color.WHITE));
                    new_property_expanded.setImageViewBitmap (R.id.iv2, Utils.textAsBitmap (mContext, "Baths", 10, Color.WHITE));
                    new_property_expanded.setImageViewBitmap (R.id.ivBathroom, Utils.textAsBitmap (mContext, jsonObject.getString (AppConfigTags.PROPERTY_BATHROOMS), 16, Color.WHITE));
                    new_property_expanded.setImageViewBitmap (R.id.iv3, Utils.textAsBitmap (mContext, "SF", 10, Color.WHITE));
                    new_property_expanded.setImageViewBitmap (R.id.ivSqFeet, Utils.textAsBitmap (mContext, jsonObject.getString (AppConfigTags.PROPERTY_AREA), 16, Color.WHITE));
                    new_property_expanded.setImageViewBitmap (R.id.ivBuildYear, Utils.textAsBitmap (mContext, jsonObject.getString (AppConfigTags.PROPERTY_BUILT_YEAR), 16, Color.WHITE));
                    new_property_expanded.setImageViewBitmap (R.id.ivPropertyPrice, Utils.textAsBitmap (mContext, jsonObject.getString (AppConfigTags.PROPERTY_PRICE), 16, Color.WHITE));
        
                    new_property_expanded.setImageViewBitmap (R.id.ivImage, bitmap);
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
    
                mBuilder.setCustomBigContentView (new_property_expanded)
                        .setCustomContentView (new_property_small)
                        .setAutoCancel (true)
                        .setSmallIcon (R.drawable.hometrust_notification_icon)
                        .setStyle (new NotificationCompat.BigPictureStyle ());
    
                notification1 = mBuilder.build ();
                notification1.contentIntent = pendingIntent;
    
                notification1.flags |= Notification.FLAG_AUTO_CANCEL; //Do not clear  the notification
                notification1.defaults |= Notification.DEFAULT_LIGHTS; // LED
                notification1.defaults |= Notification.DEFAULT_VIBRATE;//Vibration
                notification1.defaults |= Notification.DEFAULT_SOUND; // Sound
    
                mNotifyManager.notify (notification.getNotification_type (), notification1);
                break;
        }
    }
}