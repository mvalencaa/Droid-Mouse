package br.unb.unbiquitous.ubiquitos.uos.network.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import bluetooth.BtUtil;
import br.unb.unbiquitous.ubiquitos.network.model.NetworkDevice;

/**
 * This class implement a generic device discovered by the Bluetooth Radar in
 * the smart-space
 * 
 * @author Bruno Pessanha
 * @author Marcelo Valença
 * 
 */
public class BluetoothDevice2 extends NetworkDevice {

	private static final String NETWORK_DEVICE_TYPE = "Bluetooth";

	/* *****************************
	 * ATRUBUTES ****************************
	 */

	// The bluetooth local device
	protected BluetoothAdapter localDevice;

	// The bluetooth device discovered
	protected BluetoothDevice remoteDevice;

	// the name of the device
	protected String deviceName = "";

	// the serviceUUID
	protected String serviceUUID;

	/* *****************************
	 * CONSTRUCTOR ****************************
	 */

	/**
	 * Constructor
	 * 
	 * @param remoteDevice
	 */
	public BluetoothDevice2(BluetoothAdapter localDevice, String serviceUUID) {
		this.localDevice = localDevice;
		this.serviceUUID = serviceUUID;
		// Load the generic name of the device
		this.deviceName = localDevice.getAddress();
	}

	/**
	 * Constructor
	 * 
	 * @param remoteDevice
	 * @param btUtil
	 */
	public BluetoothDevice2(BluetoothDevice remoteDevice) {
		this.remoteDevice = remoteDevice;
		this.deviceName = remoteDevice.getAddress();
	}

	public BluetoothDevice2(String networkDeviceName) {
		this.deviceName = networkDeviceName;
	}

	/* *****************************
	 * PUBLIC METHODS 
	 * *****************************/

	public String getNetworkDeviceName() {
		return deviceName;
	}

	public String getNetworkDeviceType() {
		return NETWORK_DEVICE_TYPE;
	}

	public String getServiceUUID() {
		return serviceUUID;
	}

	public BluetoothDevice getRemoteDevice() {
		return remoteDevice;
	}
}
