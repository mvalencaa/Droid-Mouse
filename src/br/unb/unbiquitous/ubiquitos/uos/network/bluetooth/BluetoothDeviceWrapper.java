package br.unb.unbiquitous.ubiquitos.uos.network.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import br.unb.unbiquitous.ubiquitos.network.model.NetworkDevice;

/**
 * This class implement a generic device discovered by the Bluetooth Radar in
 * the smart-space
 * 
 * @author Bruno Pessanha
 * @author Marcelo Valença
 * 
 */
public class BluetoothDeviceWrapper extends NetworkDevice {

	private static final String NETWORK_DEVICE_TYPE = "Bluetooth";

	// The bluetooth local device
	protected BluetoothAdapter localDevice;

	// The bluetooth device discovered
	protected BluetoothDevice remoteDevice;

	// the name of the device
	protected String deviceName = "";

	// the serviceUUID
	protected String serviceUUID;

	/**
	 * Constructor
	 * 
	 * @param remoteDevice
	 */
	public BluetoothDeviceWrapper(BluetoothAdapter localDevice, String serviceUUID) {
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
	public BluetoothDeviceWrapper(BluetoothDevice remoteDevice) {
		this.remoteDevice = remoteDevice;
		this.deviceName = remoteDevice.getAddress();
	}

	public BluetoothDeviceWrapper(String networkDeviceName) {
		this.deviceName = networkDeviceName;
	}

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
