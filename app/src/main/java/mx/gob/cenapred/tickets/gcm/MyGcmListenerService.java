package mx.gob.cenapred.tickets.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.activity.MainActivity;

public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";

    // Llamado cuando un mensaje es recibido
    // @param from SenderID of the sender
    // @param data Data bundle containing message data as key/value pairs.
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Integer idNotification = Integer.parseInt(data.getString("id"));
        String message = data.getString("message");
        String title = data.getString("title");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + data);

        if (from.startsWith("/topics/")) {
            // Si el mensaje fue enviado a una lista
        } else {
            // Si el mensaje fue enviado individualmente
        }

        sendNotification(idNotification, title, message);
    }

    // Crea una notificacion simple del mensaje recibido
    private void sendNotification(Integer idNotification, String title, String message) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("idNotification",idNotification);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrattion = {0,100,200,300};
        Integer ledARGB = 0xff00ff00, ledOnMS = 300, ledOffMS = 1000;

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_cenapred)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setLights(ledARGB, ledOnMS, ledOffMS)
                .setVibrate(vibrattion);

        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(idNotification, notification);
    }
}
