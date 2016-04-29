package de.tassenmann.ibeacons_poc;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;

public class IBeaconApplication extends Application
{
    private BeaconManager beaconManager;

    @Override
    public void onCreate()
    {
        super.onCreate();

        //  App ID & App Token can be taken from App section of Estimote Cloud.
        EstimoteSDK.initialize(getApplicationContext(), "ibeacons-poc-np6", "ccbc169230d64ee6921e20732e93b6b9");
        // Optional, debug logging.
        EstimoteSDK.enableDebugLogging(true);

        initBeaconManager();
    }

    private void initBeaconManager()
    {
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener()
        {
            @Override
            public void onEnteredRegion(final Region region, List<Beacon> list)
            {
                showNotification("iBeacon", "Hello, " + region.getIdentifier() + "!");
            }

            @Override
            public void onExitedRegion(final Region region)
            {
                showNotification("iBeacon", "Hello, " + region.getIdentifier() + "!");
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback()
        {
            @Override
            public void onServiceReady()
            {
                beaconManager.startMonitoring(IBeacons.MINT_REGION);
                beaconManager.startMonitoring(IBeacons.ICE_REGION);
                beaconManager.startMonitoring(IBeacons.BLUEBERRY_REGION);
            }
        });
    }

    private void showNotification(String title, String message)
    {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_ibeacon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
