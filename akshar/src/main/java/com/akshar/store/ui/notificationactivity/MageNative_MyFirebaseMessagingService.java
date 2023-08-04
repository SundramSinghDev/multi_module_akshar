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
/*
 *
    Copyright/**
             * CedCommerce
              *
              * NOTICE OF LICENSE
              *
              * This source file is subject to the End User License Agreement (EULA)
              * that is bundled with this package in the file LICENSE.txt.
              * It is also available through the world-wide-web at this URL:
              * http://cedcommerce.com/license-agreement.txt
              *
              * @category  Ced
              * @package   MageNative
              * @author    CedCommerce Core Team <connect@cedcommerce.com >
              * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
              * @license      http://cedcommerce.com/license-agreement.txt
   
 *
 */
package com.akshar.store.ui.notificationactivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement;
import com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login;
import com.akshar.store.R;
import com.akshar.store.ui.websection.Ced_Weblink;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.productsection.activity.Ced_New_Product_Listing;
import com.akshar.store.utils.Urls;

import org.json.JSONObject;

import java.util.Objects;
public class MageNative_MyFirebaseMessagingService extends FirebaseMessagingService {
    private MageNative_NotificationUtils notificationUtils;
    Ced_SessionManagement_login session;
    Ced_SessionManagement management;
    @Override
    public void onNewToken(String s) {
          super.onNewToken(s);
        Log.i("token", "token " + s);
        /*session = new Ced_SessionManagement_login(getApplicationContext());
        management = new Ced_SessionManagement(getApplicationContext());*/
        management =  Ced_SessionManagement.getCed_sessionManagement(getApplicationContext());
        session =  Ced_SessionManagement_login.getShredPrefs(getApplicationContext());
        session.savetoken(s);
        session.save_iffreshinstall_ondevice(true);
        //----------
       // sendRegistrationToServer(s);
      /*  FirebaseMessaging.getInstance().setAutoInitEnabled(true);*/
        FirebaseMessaging.getInstance().subscribeToTopic("com.akshar.store.topic").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "Subscribed";
                if (!task.isSuccessful()) {
                    msg = "Not Subscribed";
                }
                Log.d(Urls.TAG,"SUNDRAM "+ msg);
                Log.d("SUNDRAM"," "+ msg);
            }
        });
    }

    private void sendRegistrationToServer(String token) {
        JSONObject notification_json = new JSONObject();
        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        session.savedeviceid(deviceId);
        String notification_url = Urls.BASE_URL + "rest/V1/mobinotifications/setdevice";
        try
        {
            notification_json.put("Token", session.gettoken());
            notification_json.put("unique_id", session.getdeviceid());
            notification_json.put("email", "GuestUser");
            notification_json.put("type", "2");

           /* Ced_ClientRequestResponseRest ced_clientRequestResponseRest = new Ced_ClientRequestResponseRest(new Ced_AsyncResponse() {
                @Override
                public void processFinish(Object output) throws JSONException {
                    String result = output.toString();
                    Log.i("Response", "result " + result);
                }
            }, getApplicationContext(), "POST", notification_json.toString(), "fcm");
            ced_clientRequestResponseRest.execute(notification_url);*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(Urls.TAG, "onMessageReceived: ");
        Log.e(Urls.TAG, "REpo From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.e(Urls.TAG, "REpoMessage data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.e(Urls.TAG, "REpo Message Notification Body: " + remoteMessage.getNotification().getBody());
            String msgBody = remoteMessage.getNotification().getBody();
            Log.e(Urls.TAG, "REpo Array" + msgBody);
        }
        String title = remoteMessage.getData().get("title");
        String mesg = remoteMessage.getData().get("message");
        String link_type = remoteMessage.getData().get("link_type");
        String link_id = remoteMessage.getData().get("link_id");
        String imageUri = remoteMessage.getData().get("image");
        Intent resultIntent = null;
        if (link_type != null) {
            switch (link_type) {
                case "1":
                    resultIntent = new Intent(getApplicationContext(), Ced_NewProductView.class);
                    resultIntent.putExtra("product_id", link_id);
                    resultIntent.putExtra("fromnotification", "true");
                    break;

                case "2":
                    resultIntent = new Intent(getApplicationContext(), Ced_New_Product_Listing.class);
                    resultIntent.putExtra("ID", link_id);
                    resultIntent.putExtra("fromnotification", "true");
                    break;

                case "3":
                    resultIntent = new Intent(getApplicationContext(), Ced_Weblink.class);
                    resultIntent.putExtra("link", link_id);
                    resultIntent.putExtra("fromnotification", "true");
                    break;

                default:
                    resultIntent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                    Log.d(Urls.TAG, "Null data onMessageReceived: " + link_type);
            }
            if (TextUtils.isEmpty(imageUri)) {
                showNotificationMessage(getApplicationContext(), title, mesg, Objects.requireNonNull(resultIntent));
            } else {
                if (resultIntent == null) {
                    showNotificationMessageWithBigImage(getApplicationContext(), title, mesg, Objects.requireNonNull(new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class)), imageUri);
                } else {
                    showNotificationMessageWithBigImage(getApplicationContext(), title, mesg, Objects.requireNonNull(resultIntent), imageUri);
                }
            }
        } else {
            showNotificationMessage(getApplicationContext(), title, mesg, Objects.requireNonNull(new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class)));
        }
    }
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new MageNative_NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent,null);
    }

    private void showNotificationMessageWithBigImage(Context context, String title, String message, Intent intent, @NonNull String imageUrl) {
        notificationUtils = new MageNative_NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent, imageUrl);
    }
   /* private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.remindUserBecauseCharging(getApplicationContext(), title, message, intent);
        // notificationUtils.showNotificationMessage(title, message, intent);
    }

    private void showNotificationMessageWithBigImage(Context context, String title, String message, Intent intent, @NonNull String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //notificationUtils.showNotificationMessage(title, message, intent, imageUrl);
        notificationUtils.showNotificationMessage(title, message, intent, imageUrl);
    }*/
}