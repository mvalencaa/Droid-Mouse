package br.unb.unbiquitous.ubiquitos.app;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;
import br.unb.unbiquitous.ubiquitos.app.bluetooth.BluetoothService;
import br.unb.unbiquitous.ubiquitos.driver.DeviceDriverImpl;
import br.unb.unbiquitous.ubiquitos.json.JSONException;
import br.unb.unbiquitous.ubiquitos.json.JSONObject;
import br.unb.unbiquitous.ubiquitos.json.messages.JSONServiceCall;
import br.unb.unbiquitous.ubiquitos.json.messages.JSONServiceResponse;
import br.unb.unbiquitous.ubiquitos.uos.UosDeviceManager;
import br.unb.unbiquitous.ubiquitos.uos.adaptability.UosDriver;
import br.unb.unbiquitous.ubiquitos.uos.exception.UosException;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceCall;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceResponse;
import br.unb.unbiquitous.ubiquitos.uos.network.bluetooth.BluetoothDeviceWrapper;

/**
 * This is the main activity that displays the current mouse application.
 * 
 * @author Bruno Pessanha
 * @author Marcelo Valença
 */
public class MainActivity extends Activity {

	// Debugging
	private static final String TAG = "\n\nDroid Mouse\n\n";
	private static final boolean DEBUG = true;

	// Message types sent from the BluetoothService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	public static final String MESSAGE_SEPARATOR = "\n";

	// Key names received from the BluetoothService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	// Name of the connected device
	private String mConnectedDeviceName = null;

	// Local Bluetooth adapter
	private BluetoothAdapter mAdapter = null;

	// Member object for the chat services
	private BluetoothService mService = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (DEBUG)
			Log.e(TAG, "+++ ON CREATE +++");

		// Set up the window layout
		setContentView(R.layout.main);

		// Get local Bluetooth adapter
		mAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		// Mostra posições
		View view = (View) findViewById(R.id.topLayout);
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				TextView text = (TextView) findViewById(R.id.texto);
				text.setText("Posição X: " + event.getX() + ", Posição Y: "
						+ event.getY());
				// message = "{X:'" + event.getX() + "', Y: '" + event.getY()
				// + "'}";
				return true;
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();

		if (DEBUG)
			Log.e(TAG, "++ ON START ++");

		// If Bluetooth is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mService == null)
				setupService();
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();

		if (DEBUG)
			Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which
		// Bluetooth was not enabled during onStart(), so we were paused
		// to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		if (mService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mService.getState() == BluetoothService.STATE_NONE) {
				// Start the Bluetooth service
				mService.start();
			}
		}
	}

	private void setupService() {
		Log.d(TAG, "setupService()");

		// Initialize the BluetoothService to perform bluetooth connections
		mService = new BluetoothService(this, mHandler);
	}

	@Override
	public synchronized void onPause() {
		super.onPause();

		if (DEBUG)
			Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop() {
		super.onStop();

		if (DEBUG)
			Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Stop the Bluetooth service
		if (mService != null)
			mService.stop();

		if (DEBUG)
			Log.e(TAG, "--- ON DESTROY ---");
	}

	private void ensureDiscoverable() {
		if (DEBUG)
			Log.d(TAG, "ensure discoverable");

		if (mAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            A string of text to send.
	 */
	public void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mService.getState() != BluetoothService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothService to write
			byte[] send = message.getBytes();
			mService.write(send);
		}
	}

	// The Handler that gets information back from the BluetoothService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (DEBUG)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);

				Log.e(TAG, "NOSSO DEBUG - WRITE:" + writeMessage + "!!!");
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);

				Log.e(TAG, "NOSSO DEBUG - READ:" + readMessage + "!!!");

				int index = readMessage.indexOf(MESSAGE_SEPARATOR);
				readMessage = readMessage.substring(0, index);

				BluetoothDeviceWrapper clientDevice = new BluetoothDeviceWrapper(
						mService.getRemoteDevice());

				try {
					JSONObject json = new JSONObject(readMessage);

					// TODO Testar tipo da mensagem!

					UosDriver deviceDriver = new DeviceDriverImpl();
					UosDeviceManager deviceManager = new UosDeviceManager(
							mAdapter.getName(), mAdapter.getAddress().replace(
									":", "").trim());
					deviceManager.addDriver(new DeviceDriverImpl(),
							"defaultDeviceDriver");

					JSONServiceCall jsonUtil = new JSONServiceCall(readMessage);
					ServiceCall serviceCall = jsonUtil.getAsObject();
					ServiceResponse serviceResponse = deviceManager.getAdaptabilityEngine().handleServiceCall(
							serviceCall, clientDevice);

					JSONServiceResponse jsonResponse = new JSONServiceResponse(
							serviceResponse);

					MainActivity.this.sendMessage(jsonResponse.toString()
							+ MESSAGE_SEPARATOR);
				} catch (JSONException e) {
					Log.e(TAG, "Failed to handle Droid Mouse message.", e);
				} catch (UosException e) {
					Log.e(TAG, "Failed to add defaultDeviceDriver.", e);
				}

				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Conectado em " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (DEBUG)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE_SECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data);
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a session
				setupService();
			} else {
				// User did not enable Bluetooth or an error occurred
				Log.d(TAG, "Bluetooth not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void connectDevice(Intent data) {
		// Get the device MAC address
		String address = data.getExtras().getString(
				DeviceListActivity.EXTRA_DEVICE_ADDRESS);

		// Get the BluetoothDevice object
		BluetoothDevice device = mAdapter.getRemoteDevice(address);

		// Attempt to connect to the device
		mService.connect(device);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent serverIntent = null;
		switch (item.getItemId()) {
		case R.id.secure_connect_scan:
			// Launch the DeviceListActivity to see devices and do scan
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
			return true;
		case R.id.discoverable:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		}
		return false;
	}

}