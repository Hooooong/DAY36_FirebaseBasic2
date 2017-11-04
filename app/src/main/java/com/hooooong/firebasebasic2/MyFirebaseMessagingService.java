package com.hooooong.firebasebasic2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hooooong.firebasebasic2.view.main.MainActivity;

/**
 * Created by Android Hong on 2017-10-31.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MsgService";

    /**
     * 내 앱이 화면에 현재 떠있으면 Notification이 전송되었을 때 이 함수가 호출된다.
     *
     * @param remoteMessage remoteMessage 에는 server 의 data 값이 들어오게 된다.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            // 여기서 notification 메세지를 받아 처리
            // getData() 는 Map 형식이기 때문에 key 값을 알아야 사용할 수 있다.
            sendNotification(remoteMessage.getData().get("type"));
        }

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
    // [END receive_message]


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "DEFAULT CHANNEL";
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Uri sound;
        switch (messageBody){
            case "one":
                sound =  Uri.parse("android.resource://com.hooooong.firebasebasic2/" + R.raw.kick2);
                break;
            default:
                sound =  Uri.parse("android.resource://com.hooooong.firebasebasic2/" + R.raw.laser);
                break;
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("FCM Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(sound)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}