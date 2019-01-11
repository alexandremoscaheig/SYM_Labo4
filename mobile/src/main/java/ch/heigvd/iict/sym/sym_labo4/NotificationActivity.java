package ch.heigvd.iict.sym.sym_labo4;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ch.heigvd.iict.sym.wearcommon.Constants;

public class NotificationActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 1; //code to use for the notification id
    private static final String CHANNEL_ID = "channel_01";

    private Button btnSimpleNotification;
    private Button btnActionButtons;
    private Button btnWearableOnly;

    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        btnSimpleNotification = findViewById(R.id.notification_btn_display_notification_1);
        btnActionButtons = findViewById(R.id.notification_btn_display_notification_2);
        btnWearableOnly = findViewById(R.id.notification_btn_display_notification_3);

        createNotificationChannel(CHANNEL_ID, "SYM", "Channel SYM");

        // Get an instance of the NotificationManager service
        this.notificationManager = NotificationManagerCompat.from(this);

        PendingIntent pi = createPendingIntent(200, "Welcome back");
        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(NotificationActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_lightbulb_on_black_18dp)
                        .setContentTitle("My super notif")
                        .setContentText("Hello this is Lenny")
                        .setContentIntent(pi);

        btnSimpleNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Issue the notification with notification manager.
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
            }
        });


        final NotificationCompat.Builder notificationAction =
                new NotificationCompat.Builder(NotificationActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_message_bulleted_grey600_18dp)
                        .setContentTitle("My super notif with action button")
                        .setContentText("You can do something")
                        .setContentIntent(pi)
                        .addAction(R.drawable.ic_message_bulleted_black_18dp,
                                "MySuperAction", pi);

        btnActionButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationManager.notify(NOTIFICATION_ID, notificationAction.build());
            }
        });

        // Create the action
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_message_bulleted_black_18dp,
                        "MySuperAction", pi).build();


        final NotificationCompat.Builder notificationActionWearableOnly =
                new NotificationCompat.Builder(NotificationActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_alert_white_18dp)
                        .setContentTitle("My super notif with action button on watch")
                        .setContentText("You can do something from the watch")
                        .setContentIntent(pi)
                        .extend(new NotificationCompat.WearableExtender().addAction(action));


        btnWearableOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationManager.notify(NOTIFICATION_ID, notificationActionWearableOnly.build());
            }
        });


        if(getIntent() != null)
            onNewIntent(getIntent());

    }

    /*
     *  Code fourni pour les PendingIntent
     */

    /*
     *  Method called by system when a new Intent is received
     *  Display a toast with a message if the Intent is generated by
     *  createPendingIntent method.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent == null) return;
        if(Constants.MY_PENDING_INTENT_ACTION.equals(intent.getAction())) {
            Toast.makeText(this, "" + intent.getStringExtra("msg"), Toast.LENGTH_SHORT).show();
            NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID); //we close the notification
        }
    }

    /**
     * Method used to create a PendingIntent with the specified message
     * The intent will start a new activity Instance or bring to front an existing one.
     * See parentActivityName and launchMode options in Manifest
     * See https://developer.android.com/training/notify-user/navigation.html for TaskStackBuilder
     * @param requestCode The request code
     * @param message The message
     * @return The pending Intent
     */
    private PendingIntent createPendingIntent(int requestCode, String message) {
        Intent myIntent = new Intent(NotificationActivity.this, NotificationActivity.class);
        myIntent.setAction(Constants.MY_PENDING_INTENT_ACTION);
        myIntent.putExtra("msg", message);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(myIntent);

        return stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void createNotificationChannel(String channelId, String channelName, String channelDescription) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = channelName;
            String description = channelDescription;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
