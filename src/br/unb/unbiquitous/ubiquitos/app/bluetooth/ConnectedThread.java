package br.unb.unbiquitous.ubiquitos.app.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import br.unb.unbiquitous.ubiquitos.app.MainActivity;

/**
 * This thread runs during a connection with a remote device.
 * It handles all incoming and outgoing transmissions.
 */
/**
 * This thread runs during a connection with a remote device. It handles all
 * incoming and outgoing transmissions.
 */
public class ConnectedThread extends Thread {

	// Member fields
	private final BluetoothService mService;
	private final BluetoothSocket mSocket;
	private final InputStream mInStream;
	private final OutputStream mOutStream;
	private BluetoothDevice mRemoteDevice;

	public ConnectedThread(BluetoothService service, BluetoothSocket socket) {
		Log.d(BluetoothService.TAG, "create ConnectedThread.");
		mService = service;
		mSocket = socket;
		InputStream tmpIn = null;
		OutputStream tmpOut = null;
		mRemoteDevice = mSocket.getRemoteDevice();

		// Get the BluetoothSocket input and output streams
		try {
			tmpIn = socket.getInputStream();
			tmpOut = socket.getOutputStream();
		} catch (IOException e) {
			Log.e(BluetoothService.TAG, "temp sockets not created", e);
		}

		mInStream = tmpIn;
		mOutStream = tmpOut;
	}

	public void run() {
		Log.i(BluetoothService.TAG, "BEGIN mConnectedThread");
		byte[] buffer = new byte[1024];
		int bytes;

		// Keep listening to the InputStream while connected
		while (true) {
			try {
				// Read from the InputStream
				bytes = mInStream.read(buffer);

				// Send the obtained bytes to the UI Activity
				mService.getHandler().obtainMessage(MainActivity.MESSAGE_READ, bytes, -1,
						buffer).sendToTarget();
			} catch (IOException e) {
				Log.e(BluetoothService.TAG, "disconnected", e);
				mService.connectionLost();
				// Start the service over to restart listening mode
				mService.start();
				break;
			}
		}
	}

	/**
	 * Write to the connected OutStream.
	 * 
	 * @param buffer
	 *            The bytes to write
	 */
	public void write(byte[] buffer) {
		try {
			mOutStream.write(buffer);

			// Share the sent message back to the UI Activity
			mService.getHandler().obtainMessage(MainActivity.MESSAGE_WRITE, -1, -1, buffer)
					.sendToTarget();
		} catch (IOException e) {
			Log.e(BluetoothService.TAG, "Exception during write", e);
		}
	}

	public void cancel() {
		try {
			mSocket.close();
		} catch (IOException e) {
			Log.e(BluetoothService.TAG, "close() of connect socket failed", e);
		}
	}

	public BluetoothDevice getRemoteDevice() {
		return mRemoteDevice;
	}
}