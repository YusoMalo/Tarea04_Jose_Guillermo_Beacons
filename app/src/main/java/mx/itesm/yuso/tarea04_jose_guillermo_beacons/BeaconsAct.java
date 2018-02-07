package mx.itesm.yuso.tarea04_jose_guillermo_beacons;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;

public class BeaconsAct extends AppCompatActivity {

    private BeaconManager managerB;
    private BeaconRegion region, regionB;
    private TextView text;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacons);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        text = findViewById(R.id.et_beacon1);
        image = findViewById(R.id.iv_beacon2);
        image.setVisibility(View.INVISIBLE);
        configurarBeacon();
    }

    private void configurarBeacon() {
        managerB = new BeaconManager(this);
        region = new BeaconRegion("azulA", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),  30296, 46626);
        regionB = new BeaconRegion("azulB", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 26891, 46664);

        //Ranging detecta todos los beacons y da info
        //Monitoring cuando entra y sale de la region
        managerB.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion beaconRegion, List<Beacon> beacons) {
                for (Beacon beacon: beacons){
                    if (beacon.getMinor() == 46626 && beacon.getMajor() == 30296){
                        text.setText("Guillermo Pérez Trueba\t\t A01377162\nJosé Antonio Malo de la Peña\t\t A01371454");
                    }
                    else if (beacon.getMinor() == 46664 && beacon.getMajor() == 26891){
                        image.setVisibility(View.VISIBLE);
                    }
                }
            }



            @Override
            public void onExitedRegion(BeaconRegion beaconRegion) {
                if(beaconRegion.equals(region))
                    text.setText("");
                else if(beaconRegion.equals(regionB))
                    image.setVisibility(View.INVISIBLE);




            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beacons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        managerB.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                managerB.startMonitoring(region);
                managerB.startMonitoring(regionB);
            }
        });
    }

    @Override
    protected void onPause() {
        managerB.stopMonitoring("azulA");
        managerB.stopMonitoring("azulB");
        super.onPause();

    }
}
