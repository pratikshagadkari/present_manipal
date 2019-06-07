package manipal.com.present_manipal;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class teacher_hotspot extends AppCompatActivity {
    private static final String TAG ="dasd" ;
    WifiManager wifiManager;
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BluetoothAdapter bluetoothAdapter;
    BroadcastReceiver receiver;
    Button button;
    IntentFilter filter;
    private WifiManager.LocalOnlyHotspotReservation mReservation;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_hotspot);

        wifiManager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        manager= (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel=manager.initialize(this,getMainLooper(),null);

        receiver=new wifidirectbroadcastreceiver(manager,channel,this);
        filter=new IntentFilter();
        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        button=findViewById(R.id.toggle);
        button.setText("OFF");

        WifiConfiguration wifiConfiguration=new WifiConfiguration();
        wifiConfiguration.SSID="Srijit";
        wifiConfiguration.preSharedKey="sad";
        wifiConfiguration.allowedKeyManagement.set(4);
        wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        mReservation.getWifiConfiguration();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void turnOnHotspot() {
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        manager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {

            @Override
            public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                super.onStarted(reservation);
                Toast.makeText(teacher_hotspot.this,"Switching Hotspot On",Toast.LENGTH_SHORT).show();
                mReservation = reservation;
                button.setText("ON");
            }

            @Override
            public void onStopped() {
                super.onStopped();
                Log.d(TAG, "onStopped: ");
            }

            @Override
            public void onFailed(int reason) {
                super.onFailed(reason);
                Log.d(TAG, "onFailed: ");
            }
        }, new Handler());
    }

    private void turnOffHotspot() {
        if (mReservation != null) {
            mReservation.close();
            Toast.makeText(teacher_hotspot.this,"Switching Hotspot Off",Toast.LENGTH_SHORT).show();
            button.setText("OFF");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver,filter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void search(View view) {
        if(wifiManager.isWifiEnabled()){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(teacher_hotspot.this);
            alertDialogBuilder.setTitle("Disable Wifi");
            alertDialogBuilder
                    .setMessage("Disable Wifi To Switch On Wifi Hotspot")
                    .setCancelable(false)
                    .setPositiveButton("Disable",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            wifiManager.setWifiEnabled(false);
                            yolo();
                        }
                    })
                    .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
        else {
            yolo();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void yolo(){
        if(button.getText().equals("OFF"))
            turnOnHotspot();
        else
            turnOffHotspot();
    }

}
