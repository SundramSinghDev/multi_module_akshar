/*
 *
 *  * Copyright/**
 *  *          * CedCommerce
 *  *           *
 *  *           * NOTICE OF LICENSE
 *  *           *
 *  *           * This source file is subject to the End User License Agreement (EULA)
 *  *           * that is bundled with this package in the file LICENSE.txt.
 *  *           * It is also available through the world-wide-web at this URL:
 *  *           * http://cedcommerce.com/license-agreement.txt
 *  *           *
 *  *           * @category  Ced
 *  *           * @package   MageNative
 *  *           * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *  *           * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *  *           * @license      http://cedcommerce.com/license-agreement.txt
 *  *
 *
 */

package com.akshar.store.ui.notificationactivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


import com.akshar.store.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Random;


class MageNative_NotificationUtils {
    //private static String TAG = NotificationUtils.class.getSimpleName();
    private final Context mContext;
    NotificationManager notificationManager;
    int notificationId = 1;
    String channelId = "channel-01";
    String channelName = "Channel Name";
    int importance = NotificationManager.IMPORTANCE_HIGH;

    public MageNative_NotificationUtils(Context mContext) {
        this.mContext = mContext;
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.e("FirebaseMessageService", "");
    }

    public void showNotificationMessage(String title, String message, @NonNull Intent intent) {
        Log.e("FirebaseMessageService", "message=== " + message);
        showNotificationMessage(title, message, intent, null);
    }

    public void showNotificationMessage(final String title, final String message, @NonNull Intent intent, @NonNull String imageUrl) {
        if (TextUtils.isEmpty(message))
            return;

        Log.e("FirebaseMessageService", "message= " + message);
        final int icon = R.mipmap.ic_launcher;
/*
        final int icon = R.mipmap.ic_launcher;
*/
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext, channelId);
        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + mContext.getPackageName() + "/raw/notification");
        if (!TextUtils.isEmpty(imageUrl)) {
            if (imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                Bitmap bitmap = getBitmapFromURL(imageUrl);
                if (bitmap != null) {
                    showBigNotification(bitmap, mBuilder, icon, title, message, resultPendingIntent, alarmSound);
                } else {

                    showSmallNotification(mBuilder, icon, title, message, resultPendingIntent, alarmSound);
                }
            }
        } else {
            showSmallNotification(mBuilder, icon, title, message, resultPendingIntent, alarmSound);
//            playNotificationSound();
        }
    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
        inboxStyle.bigText(message);

        Log.e("FirebaseMessageService", "showSmallNotification message= " + message);

        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notification = mBuilder
                .setSmallIcon(R.drawable.notification_icon)
                .setColor(mContext.getResources().getColor(R.color.status_bar_color))
                .setTicker(title)
                .setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                // .setColor(getNotificationIcon())
                //  .setWhen(getTimeMilliSec(timeStamp))
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        Objects.requireNonNull(notificationManager).notify(m, notification);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.notification_icon : R.drawable.notification_icon;
    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        Log.e("FirebaseMessageService", "showBigNotification message= " + message);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notification = mBuilder
                .setSmallIcon(R.drawable.notification_icon)
                .setColor(mContext.getResources().getColor(R.color.status_bar_color))
                .setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                //.setWhen(getTimeMilliSec(timeStamp))
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();
        //    notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int m = random.nextInt(9998 - 1000) + 1000;
        Objects.requireNonNull(notificationManager).notify(m, notification);
    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    private void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + MyApplication.getInstance().getApplicationContext().getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(MyApplication.getInstance().getApplicationContext(), alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
@SuppressWarnings("ALL")
class MageNative_NotificationUtils {
    //private static String TAG = NotificationUtils.class.getSimpleName();
    private final Context mContext;
    NotificationManager notificationManager;
    int notificationId = 1;
    String channelId = "channel-01";
    String channelName = "Channel Name";
    int importance = NotificationManager.IMPORTANCE_HIGH;

    private static final int WATER_REMINDER_NOTIFICATION_ID = 1138;

    private static final int WATER_REMINDER_PENDING_INTENT_ID = 3417;

    public MageNative_NotificationUtils(Context mContext) {
        this.mContext = mContext;
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    */
/*public void showNotificationMessage(String title, String message, @NonNull Intent intent) {
        showNotificationMessage(title, message, intent, null);
    }*//*


    public static void remindUserBecauseCharging(Context context, String title, String message, Intent intent) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.status_bar_color))
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(""))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }


        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

    public void showNotificationMessage(final String title, final String message, @NonNull Intent intent, @NonNull String imageUrl) {
        if (TextUtils.isEmpty(message))
            return;

        final int icon = R.drawable.notification_icon;
        Random r=new Random();
        int i=r.nextInt();

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        i,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext, channelId);
        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + mContext.getPackageName() + "/raw/notification");
        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                Bitmap bitmap = getBitmapFromURL(imageUrl);

                if (bitmap != null) {

                    showBigNotification(bitmap, mBuilder, icon, title, message, resultPendingIntent, alarmSound);
                } else {

                    // showSmallNotification(mBuilder, icon, title, message, resultPendingIntent, alarmSound);
                    remindUserBecauseCharging(mContext, title, message, intent);
                }
            }
        } else {

            //showSmallNotification(mBuilder, icon, title, message, resultPendingIntent, alarmSound);
            remindUserBecauseCharging(mContext, title, message, intent);
            playNotificationSound();
        }

    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
        inboxStyle.bigText(message);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            Notification notification;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);

            notification = mBuilder
                    .setSmallIcon(R.drawable.notification_icon)
                    .setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setSound(alarmSound)
                    .setStyle(inboxStyle)
                    .setSmallIcon(icon)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(message)
                    .build();

            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            Objects.requireNonNull(notificationManager).notify(m, notification);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setColor(ContextCompat.getColor(mContext, R.color.status_bar_color))
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(largeIcon(mContext))
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(""))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(mContext))
                .setAutoCancel(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }


        NotificationManager notificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID, notificationBuilder.build());

        */
/*----old----
        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
        inboxStyle.bigText(message);

        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notification = mBuilder
                .setSmallIcon(R.drawable.notification_icon)
                .setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                //  .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        Objects.requireNonNull(notificationManager).notify(m, notification);*//*


    }


    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, Ced_New_home_page.class);
        return PendingIntent.getActivity(
                context,
                WATER_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
       */
/* notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                //.setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();
        //    notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int m = random.nextInt(9998 - 1000) + 1000;
        Objects.requireNonNull(notificationManager).notify(m, notification);*//*


        //------new------
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setColor(ContextCompat.getColor(mContext, R.color.status_bar_color))
                .setSmallIcon(icon)
                .setTicker(title)
                .setWhen(0)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.notification_icon))
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(bigPictureStyle)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        Random random = new Random();
        int m = random.nextInt(9998 - 1000) + 1000;
        notificationManager.notify(m, notificationBuilder.build());
        //---------
    }


    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.notification_icon);
        return largeIcon;
    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    private void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + MyApplication.getInstance().getApplicationContext().getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(MyApplication.getInstance().getApplicationContext(), alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}*/
