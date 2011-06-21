package br.unb.unbiquitous.ubiquitos.app;

import com.example.android.BluetoothChat.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;
import br.unb.unbiquitous.ubiquitos.uos.context.ContextException;
import br.unb.unbiquitous.ubiquitos.uos.context.UOSApplicationContext;

public class ClientLauncher extends Activity {

	private BluetoothAdapter mBluetoothAdapter;
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;
	private static final int REQUEST_ENABLE_BT = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initUos();

		// ************************************************************************************************************************

		// Mostra posições
		View view = (View) findViewById(R.id.topLayout);
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String message;
				TextView text = (TextView) findViewById(R.id.texto);
				text.setText("Posição X: " + event.getX() + ", Posição Y: "
						+ event.getY());
				message = "{X:'" + event.getX() + "', Y: '" + event.getY()
						+ "'}";
				// sendMessage(message);
				return true;
			}
		});

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			// if (mChatService == null) setupChat();
		}
	}

	// ************************************************************************************************************************

	private void initUos() {
		UOSApplicationContext applicationContext = new UOSApplicationContext();

		try {
			applicationContext.init();
		} catch (ContextException e) {
			// Faz alguma coisa
		}
	}
}
