package com.actiknow.clearsale.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;


import com.actiknow.clearsale.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Ravi on 31/03/15.
 */
public class NotificationUtils {
    private static String TAG = NotificationUtils.class.getSimpleName ();
    int notification_id = 1;
    int AcceptID = 5;
    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground (Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService (Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses ();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals (context.getPackageName ())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks (1);
            ComponentName componentInfo = taskInfo.get (0).topActivity;
            if (componentInfo.getPackageName ().equals (context.getPackageName ())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications (Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService (Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll ();
    }

    public static long getTimeMilliSec (String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse (timeStamp);
            return date.getTime ();
        } catch (ParseException e) {
            e.printStackTrace ();
        }
        return 0;
    }

    public void showNotificationMessage (Intent intent, com.actiknow.clearsale.model.Notification notification) {
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder (mContext);
        final Uri alarmSound = Uri.parse (ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName () + "/raw/notification");

        //Check for empty push message
        if (TextUtils.isEmpty (notification.getMessage ()))
            return;


        switch (notification.getNotification_type ()) {
            case 1:
                switch (notification.getNotification_style ()) {
                    case 1: // Simple Notification
                        mBuilder
//                                .setSmallIcon (R.drawable.ic_notification_icon_2)
                                .setContentTitle (notification.getTitle ())
                                .setPriority (notification.getNotification_priority ())
                                .setContentText (notification.getMessage ());
                        break;
                    case 2: // Inbox Style Notification

                        mBuilder.setContentTitle (notification.getMessage ())
                                .setSmallIcon (R.mipmap.ic_launcher)
                                .setContentText ("A request for blood has been generated")
                                .setContentTitle ("Title")//   \u293f")
                                .setSubText ("Let's donate some blood. Are you free?")
                                .setUsesChronometer (true)
                                .setShowWhen (true)
                                .setStyle (new NotificationCompat.InboxStyle ()
                                        .addLine ("Hi"))
                                .setPriority (notification.getNotification_priority ())
                                .setDefaults (Notification.DEFAULT_VIBRATE)
                                .setColor (mContext.getResources ().getColor (R.color.text_color_black))
//                .setSmallIcon (R.drawable.ic_donate)
                                .build ();
                        break;
                    case 3: // Big Text Style Notification
                        Bitmap icon1 = BitmapFactory.decodeResource (mContext.getResources (), R.mipmap.ic_launcher);
                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle ();
                        bigText.bigText ("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
                        bigText.setBigContentTitle ("Big Text Notification");
                        bigText.setSummaryText ("By: Author of Lorem ipsum");

                        mBuilder.setSmallIcon (R.mipmap.ic_launcher)
                                .setContentTitle ("Big Text notification")
                                .setContentText ("This is test of big text style notification.")
                                .setLargeIcon (icon1)
                                .setStyle (bigText);
                        break;
                    case 4: // Big Picture Notification
                        Bitmap bitmap = null;
                        if (notification.getImage_url () != null && notification.getImage_url ().length () > 4 && Patterns.WEB_URL.matcher (notification.getImage_url ()).matches ()) {
                            bitmap = getBitmapFromURL (notification.getImage_url ());
                        } else {
                            bitmap = getBitmapFromURL ("http://bloodondemand.com/img/02.jpg");
                        }
                        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle ();

//                        bigPictureStyle.setSummaryText ("");
//                        bigPictureStyle.setBigContentTitle ("");
//                        bigPictureStyle.bigLargeIcon ();
                        bigPictureStyle.bigPicture (bitmap);

                        mBuilder
                                .setSmallIcon (R.mipmap.ic_launcher)
                                .setContentTitle ("Big picture notification")
                                .setContentText ("This is test of big picture notification.")
                                .setStyle (bigPictureStyle);

                        break;
                    default:
                        break;
                }
                break;
            case 2:
                switch (notification.getNotification_style ()) {
                    case 2:
                        break;
                }
                break;
            case 3:
                Log.e ("karman", "in notification type 3 in notification utils");
                switch (notification.getNotification_style ()) {
                    case 3: // Big Text Style Notification
                        Bitmap icon1 = BitmapFactory.decodeResource (mContext.getResources (), R.mipmap.ic_launcher);
                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle ();
                        bigText.bigText ("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
                        bigText.setBigContentTitle ("Big Text Notification");
                        bigText.setSummaryText ("By: Author of Lorem ipsum");

                        mBuilder.setSmallIcon (R.mipmap.ic_launcher)
                                .setContentTitle ("Big Text notification")
                                .setContentText ("This is test of big text style notification.")
                                .setLargeIcon (icon1)
                                .setStyle (bigText);
                        break;
                    case 4: // Big Picture Notification
                        Log.e ("karman", "in big picture style");

                        Bitmap bitmap = null;
                        if (notification.getImage_url () != null && notification.getImage_url ().length () > 4 && Patterns.WEB_URL.matcher (notification.getImage_url ()).matches ()) {
                            bitmap = getBitmapFromURL (notification.getImage_url ());
                        } else {
                            bitmap = getBitmapFromURL ("http://bloodondemand.com/img/02.jpg");
                        }
                        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle ();

//                        bigPictureStyle.setSummaryText ("");
                        bigPictureStyle.setBigContentTitle (notification.getMessage ());
//                        bigPictureStyle.bigLargeIcon ();
                        bigPictureStyle.bigPicture (bitmap);

                        mBuilder.setSmallIcon (R.mipmap.ic_launcher)
                                .setContentTitle (notification.getTitle ())
                                .setContentText (notification.getMessage ())
                                .setStyle (bigPictureStyle);

                        break;
                    default:
                        break;
                }
                break;
        }

        // now show notification..
        NotificationManager mNotifyManager = (NotificationManager) mContext.getSystemService (Context.NOTIFICATION_SERVICE);
        mNotifyManager.notify (notification_id, mBuilder.build ());
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL (String strURL) {
        try {
            URL url = new URL (strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection ();
            connection.setDoInput (true);
            connection.connect ();
            InputStream input = connection.getInputStream ();
            Bitmap myBitmap = BitmapFactory.decodeStream (input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace ();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound () {
        try {
            Uri alarmSound = Uri.parse (ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName () + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone (mContext, alarmSound);
            r.play ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
}
