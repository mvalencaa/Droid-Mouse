package br.unb.unbiquitous.ubiquitos.uos;

import java.util.ArrayList;
import java.util.List;

import br.unb.unbiquitous.ubiquitos.uos.adaptability.AdaptabilityEngine;
import br.unb.unbiquitous.ubiquitos.uos.adaptability.UosDriver;
import br.unb.unbiquitous.ubiquitos.uos.exception.UosException;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpDevice;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpNetworkInterface;

public class UosDeviceManager {

	private AdaptabilityEngine adaptabilityEngine;
	private UpDevice currentDevice;
	private String deviceName;
	private String deviceAddress;

	public UosDeviceManager(String deviceName, String deviceAddress) {
		adaptabilityEngine = new AdaptabilityEngine(this);
		this.deviceName = deviceName;
		this.deviceAddress = deviceAddress;
	}

	/**
	 * Method responsible to build the UpDevice object with the information
	 * provided by the Network Layer.
	 * 
	 * @param networkManager
	 *            Provides the network information about the device.
	 * @param deviceName
	 *            Name of the current device.
	 * @return UpDevice with the information about the current device.
	 */
	private UpDevice composeUpDevice() {
		UpDevice upDevice = new UpDevice(deviceName);
		List<UpNetworkInterface> interfaces = new ArrayList<UpNetworkInterface>();
		interfaces.add(new UpNetworkInterface("Bluetooth", deviceAddress));
		upDevice.setNetworks(interfaces);

		return upDevice;
	}

	/**
	 * @see AdaptabilityEngine#addDriver(UosDriver, String);
	 */
	public void addDriver(UosDriver driver) throws UosException {
		addDriver(driver, null);
	}

	/**
	 * Method responsible for adding a Driver to the current device available
	 * resources.
	 * 
	 * @param driver
	 *            Driver to be deployed.
	 * @param instanceId
	 *            Instance id of the driver, if not informed one will be
	 *            assigned for it.
	 * @throws UosException
	 */
	public void addDriver(UosDriver driver, String instanceId)
			throws UosException {
		driver.init(this, instanceId);
		adaptabilityEngine.addDriver(driver, instanceId);
	}

	/**
	 * Make a network type available through its adapter.
	 * 
	 * @param adapter
	 *            Adapter of the network to be made available in the device.
	 */
	// public void addNetworkAdapter(NetworkAdapter adapter){
	// networkManager.addNetworkAdapter(adapter);
	// adapter.setNetworkManager(networkManager);
	// }

	public AdaptabilityEngine getAdaptabilityEngine() {
		return adaptabilityEngine;
	}

	/**
	 * @return Information about the current device.
	 */
	public UpDevice getCurrentDevice() {
		if (currentDevice == null) {
			currentDevice = composeUpDevice();
		}
		return currentDevice;
	}

}
