package helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.project.uichack.notifyme.MainActivity;
import com.project.uichack.notifyme.R;

public class NotificationHelper {

    NotificationManager notificationManager;
    Bitmap bitmap;
    Notification.Builder builder;
    PendingIntent pendingIntent;
    Intent intent;


    //Generic notification method
    public void notifyAndroid(Context ctx, String notification, String title) {


        notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new Notification.Builder(ctx);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(notification); // pass the notification
        intent = new Intent(ctx, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("10001", "NOTIFICATION_CHANNEL_NAME", importance);
            assert notificationManager != null;
            builder.setChannelId("10001");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        notificationManager.notify(0, builder.build());



    }


}
