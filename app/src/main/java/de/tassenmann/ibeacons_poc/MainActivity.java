package de.tassenmann.ibeacons_poc;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.List;
import java.util.UUID;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    public static final String INTENT_KEY_EMAIL = "de.tassenmann.ibeacons_poc.MainActivity.intent.email";

    private BeaconManager beaconManager;
    private Region region;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(createEmailIntent());
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        //noinspection deprecation
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        imageView = (ImageView) findViewById(R.id.imageView);

        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener()
        {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list)
            {
                if (!list.isEmpty())
                {
                    final Beacon nearestBeacon = list.get(0);
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            final String nearestBeaconId = nearestBeacon.getMajor() + ":" + nearestBeacon.getMinor();

                            Log.wtf("wtf", "nearest beacon: " + nearestBeaconId); // wtf
                            Log.wtf("wtf", "nearest beacon: " + nearestBeacon.getProximityUUID()); // wtf

                            int iBeaconDrawableResId = 0;
                            if (nearestBeaconId.equals(IBeacons.BLUEBERRY_REGION.getMajor() + ":" + IBeacons.BLUEBERRY_REGION.getMinor()))
                            {
                                iBeaconDrawableResId = R.drawable.blueberry;
                                Log.wtf("wtf", "nearest beacon: " + IBeacons.BLUEBERRY_REGION.getIdentifier()); // wtf
                            }
                            else if (nearestBeaconId.equals(IBeacons.ICE_REGION.getMajor() + ":" + IBeacons.ICE_REGION.getMinor()))
                            {
                                iBeaconDrawableResId = R.drawable.ice;
                                Log.wtf("wtf", "nearest beacon: " + IBeacons.ICE_REGION.getIdentifier()); // wtf
                            }
                            else if (nearestBeaconId.equals(IBeacons.MINT_REGION.getMajor() + ":" + IBeacons.MINT_REGION.getMinor()))
                            {
                                iBeaconDrawableResId = R.drawable.mint;
                                Log.wtf("wtf", "nearest beacon: " + IBeacons.MINT_REGION.getIdentifier()); // wtf
                            }

                            imageView.setImageResource(iBeaconDrawableResId);
                            imageView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
        region = new Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback()
        {
            @Override
            public void onServiceReady()
            {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPause()
    {
        beaconManager.stopRanging(region);

        super.onPause();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about)
        {
            Intent intent = new Intent(this, AboutActivity.class);
            intent.putExtra(INTENT_KEY_EMAIL, createEmailIntent());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        /*int id = item.getItemId();
        if (id == R.id.nav_blueberry)
        {
        }
        else if (id == R.id.nav_ice)
        {
        }
        else if (id == R.id.nav_mint)
        {
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @NonNull
    private Intent createEmailIntent()
    {
        String appVersion = "0.1";
        String androidVersion = Build.VERSION.RELEASE;
        String device = Build.MODEL;

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "sotra.ouy@burda-forward.de", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "iBeacon PoC v" + appVersion);
        intent.putExtra(Intent.EXTRA_TEXT, device + "\n" + androidVersion);
        return intent;
    }
}
