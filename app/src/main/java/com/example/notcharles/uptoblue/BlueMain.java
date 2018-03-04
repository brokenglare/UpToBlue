package com.example.notcharles.uptoblue;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BlueMain extends AppCompatActivity {
    private BluetoothAdapter adapter;
    public static int requestBluetooth = 1;   // Bluetooth assumed enabled by default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_main);

        adapter = BluetoothAdapter.getDefaultAdapter();

        // Check if device is Bluetooth compatible
        if (adapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Compatibility Issue")
                    .setMessage("This device does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        // Ask to enable Bluetooth if not yet enabled
        if (!adapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, requestBluetooth);
        }
    }
}
