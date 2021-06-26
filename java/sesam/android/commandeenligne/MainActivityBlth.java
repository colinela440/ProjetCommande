package sesam.android.commandeenligne;          //Gestion Bluetooth

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivityBlth extends AppCompatActivity implements AdapterView.OnItemClickListener {
    //déclarations
    private static final String TAG = "Main Activity Blth";

    BluetoothAdapter mBluetoothAdapteur;
    Button btEnDisableDiscoverable;

    BlthConnectionService mBluetoothConnection;
    Button btStartConnection;
    Button btSend;

    EditText etSend;

    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    BluetoothDevice mBTDevice;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();

    public DeviceListAdapter mDeviceListAdapter;

    ListView lvNewDevices;

    String deviceSelected;

    //broadcast receiver for changes made to BT states (btEnableDisableBT)
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            TextView tvOnOff = findViewById(R.id.txtOnOff);

            // When discovery finds a device
            if (action.equals(mBluetoothAdapteur.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapteur.ERROR);
                switch(state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE OFF");
                        tvOnOff.setText(R.string.blth_state_off);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d (TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d (TAG, "mBroadcastReceiver1: STATE ON");
                        tvOnOff.setText(R.string.blth_state_on);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d (TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    //broadcast receiver for changes made to BT scan modes (btDiscoverability)
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            TextView tvDiscoverable = findViewById(R.id.txtDiscoverable);

            if (action.equals(mBluetoothAdapteur.ACTION_SCAN_MODE_CHANGED)) {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, mBluetoothAdapteur.ERROR);
                switch(mode) {
                    //device is in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: discoverability enabled.");
                        tvDiscoverable.setText(R.string.blth_scan_discoverable);
                        break;
                    //device is not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d (TAG, "mBroadcastReceiver2: discoverability disabled. Able to receive connections.");
                        tvDiscoverable.setText(R.string.blth_scan_connectable);
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d (TAG, "mBroadcastReceiver2: discoverability disabled. Not able to receive connections.");
                        tvDiscoverable.setText(R.string.blth_scan_not_connectable);
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d (TAG, "mBroadcastReceiver2: connecting...");
                        tvDiscoverable.setText(R.string.blth_state_connecting);
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d (TAG, "mBroadcastReceiver2: connected.");
                        tvDiscoverable.setText(R.string.blth_state_connected);
                        break;
                }
            }
        }
    };

    //broadcast receiver for listing devices that are not yet paired (btDiscover)
    private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + " : " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };

    //broadcast receiver that detects bond state changes (pairing status changes)
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            TextView tvDeviceSelected = findViewById(R.id.txtBlthChosen);
            String connectedTo = "Connecté à " + deviceSelected;
            String notConnected = "Non connecté";

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //case 1 : already bonded
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "mBroadcastReceiver4: BOND_BONDED.");
                    tvDeviceSelected.setText(connectedTo);
                    //inside BroadcastReceiver4
                    mBTDevice = mDevice;

                    //rendre bouton connexion accessible
                    btStartConnection.setEnabled(true);
                }
                //case 2 : creating a bond
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING){
                    Log.d(TAG, "mBroadcastReceiver4: BOND_BONDING.");
                }
                //case 3 : breaking bond
                if(mDevice.getBondState() == BluetoothDevice.BOND_NONE){
                    Log.d(TAG, "mBroadcastReceiver4: BOND_NONE.");
                    tvDeviceSelected.setText(notConnected);

                    //rendre boutons connexion et envoi msg, et zone saisir texte msg inaccessibles
                    btStartConnection.setEnabled(false);
                    btSend.setEnabled(false);
                    etSend.setEnabled(true);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        //broadcast receiver gets closed when app is paused or destroyed
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_blth);

        //instanciations
        Button btOnOff = findViewById(R.id.btBlthOnOff);
        btEnDisableDiscoverable = findViewById(R.id.btBlthDiscoverable);
        lvNewDevices = findViewById(R.id.lvDiscover);
        mBTDevices = new ArrayList<>();

        btStartConnection = findViewById(R.id.btBlthStartConnection);
        btSend = findViewById(R.id.btBlthSendText);
        etSend = findViewById(R.id.etxtBlthSendText);

        //broadcasts when bond state changes (i.e. pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        mBluetoothAdapteur = BluetoothAdapter.getDefaultAdapter();

        lvNewDevices.setOnItemClickListener(MainActivityBlth.this);

        //pour click sur bt Activer/Désactiver Bluetooth
        btOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: enabling/disabling BT.");
                btEnableDisableBT();
            }
        });

        btStartConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConnection();

                //rendre zone saisir msg et bouton envoi msg accessibles
                etSend.setEnabled(true);
                btSend.setEnabled(true);
            }
        });

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);
                //appel de la méthode write dans la classe BlthConnectionService
            }
        });

        //bt retour homepage
        ImageButton btToHome = findViewById(R.id.logo);
        Intent monIntentToHome = new Intent(this, MainActivityHome.class);

        btToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(monIntentToHome);
            }
        });
    }

    //method for starting connection
    // !! : if not paired first, will fail and app will crash
    public void startConnection(){
        startBTConnection(mBTDevice,MY_UUID_INSECURE);
    }

    //method for starting chat service
    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        mBluetoothConnection.startClient(device, uuid);
        //appel de la méthode startClient dans la classe BlthConnectionService
    }

    public void btEnableDisableBT(){
        TextView tvOnOff = findViewById(R.id.txtOnOff);

        if(mBluetoothAdapteur == null){
            Log.d(TAG, "enableDisableBT: device does not support BT.");
            tvOnOff.setText(R.string.blth_not_supported);
        }
        if(!mBluetoothAdapteur.isEnabled()){
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Toast.makeText(this, "Activation BT...", Toast.LENGTH_SHORT).show();

            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if (mBluetoothAdapteur.isEnabled()){
            Log.d(TAG, "enableDisableBT: disabling BT.");

            mBluetoothAdapteur.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
    }

    public void btDiscoverable(View view) {
        Log.d(TAG,"onClick: enabling discoverability.");
        Log.d(TAG, "btDiscoverability: making device discoverable for 2min.");
        Toast.makeText(this, "Appareil détectable 2 minutes", Toast.LENGTH_SHORT).show();

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapteur.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, intentFilter);
    }

    public void btDiscover(View view) {
        Log.d(TAG,"onClick: enabling discovery.");
        Log.d(TAG, "btDiscover: looking for unpaired devices.");
        Toast.makeText(this, "Recherche d'appareils...", Toast.LENGTH_SHORT).show();

        if(mBluetoothAdapteur.isDiscovering()){
            mBluetoothAdapteur.cancelDiscovery();
            Log.d(TAG, "btDiscover: canceling discovery.");

            //explicit runtime blth authorizations request (in case running device has a version after LOLLIPOP)
            blthPermissions();

            mBluetoothAdapteur.startDiscovery();
            Log.d(TAG, "btDiscover: starting discovery.");
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if(!mBluetoothAdapteur.isDiscovering()){
            //explicit runtime blth authorizations request (in case running device has a version after LOLLIPOP)
            blthPermissions();

            mBluetoothAdapteur.startDiscovery();
            Log.d(TAG, "btDiscover: starting discovery.");
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }

    public void blthPermissions(){
        int permissions = ContextCompat.checkSelfPermission (this,Manifest.permission.ACCESS_FINE_LOCATION);
        permissions += ContextCompat.checkSelfPermission (this,Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permissions != 0) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);//any number
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        //first cancel discovery because it is very memory intensive.
        mBluetoothAdapteur.cancelDiscovery();

        Log.d(TAG, "onItemClick: you clicked on a device");

        String deviceName = mBTDevices.get(position).getName();
        String deviceAddress = mBTDevices.get(position).getAddress();

        Log.d(TAG, "onItemClick: device name = " + deviceName);
        Log.d(TAG, "onItemClick: device address = " + deviceAddress);

        Toast.makeText(this, "Connexion à " + deviceName + "...", Toast.LENGTH_SHORT).show();
        deviceSelected = deviceName;

        //create the bond
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Log.d(TAG, "onItemClick: Trying to pair with " + deviceName);
            mBTDevices.get(position).createBond();

            //make sure we assign the BT device
            mBTDevice = mBTDevices.get(position);
            //so that we can start the connection service
            mBluetoothConnection = new BlthConnectionService(MainActivityBlth.this);
        }
    }
}