package br.unb.unbiquitous.ubiquitos.app.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * This thread runs while attempting to make an outgoing connection
 * with a device. It runs straight through; the connection either
 * succeeds or fails.
 * 
 * @author Bruno Pessanha
 * @author Marcelo Valença
 */
public class ConnectThread extends Thread {
	
	// Member fields
	private final BluetoothService mService;
    private final BluetoothSocket mSocket;
    private final BluetoothDevice mDevice;

	public ConnectThread(BluetoothService service, BluetoothDevice device) {
    	mService = service;
        mDevice = device;
        BluetoothSocket tmp = null;

        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice
        try {
            tmp = device.createRfcommSocketToServiceRecord(
                    BluetoothService.MY_UUID_SECURE);
        } catch (IOException e) {
            Log.e(BluetoothService.TAG, "create() failed", e);
        }
        mSocket = tmp;
    }

    public void run() {
        Log.i(BluetoothService.TAG, "BEGIN mConnectThread.");
        setName("ConnectThread");

        // Always cancel discovery because it will slow down a connection
        mService.getAdapter().cancelDiscovery();

        // Make a connection to the BluetoothSocket
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            mSocket.connect();
        } catch (IOException e) {
            // Close the socket
        	Log.e(BluetoothService.TAG, "NOSSO DEBUG:" + e.getMessage() + "!!!");
            try {
                mSocket.close();
            } catch (IOException e2) {
                Log.e(BluetoothService.TAG, "unable to close() socket during connection failure", e2);
            }
            mService.connectionFailed();
            return;
        }

        // Reset the ConnectThread because we're done
        synchronized (mService) {
            mService.setConnectThread(null);
        }

        // Start the connected thread
        mService.connected(mSocket, mDevice);
    }

    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
            Log.e(BluetoothService.TAG, "close() of connect socket failed", e);
        }
    }
}
