package examples.aaronhoskins.com.mvvmexample.model.datasource.remote.notificationservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import examples.aaronhoskins.com.mvvmexample.R;
import examples.aaronhoskins.com.mvvmexample.view.activities.MainActivity;

public class PushNotificationService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        pref.edit().putString("push_key", s).apply();
        Log.d("TAG", "onNewToken: " + s);
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        final String title = remoteMessage.getNotification().getTitle();
        final String message = remoteMessage.getNotification().getBody();

        NotificationCompat.Builder notificationBuilder
                = new NotificationCompat.Builder(this, "channel");

        //Setup the notification
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setSmallIcon(R.drawable.common_google_signin_btn_icon_light_normal_background);
        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, notificationBuilder.build());
        super.onMessageReceived(remoteMessage);
    }
}
