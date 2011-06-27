package br.unb.unbiquitous.ubiquitos.app.mouse;

import java.util.Vector;

import android.view.Display;
import br.unb.unbiquitous.ubiquitos.network.model.NetworkDevice;
import br.unb.unbiquitous.ubiquitos.uos.UosDeviceManager;
import br.unb.unbiquitous.ubiquitos.uos.adaptability.UosDriver;
import br.unb.unbiquitous.ubiquitos.uos.adaptability.UosEventDriver;
import br.unb.unbiquitous.ubiquitos.uos.adaptabitilyEngine.SmartSpaceGateway;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpDriver;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpService;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpService.ParameterType;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceCall;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceResponse;

public class MouseDriver implements UosDriver, UosEventDriver {

	private static final String MOUSE_DRIVER = "br.unb.unbiquitous.ubiquitos.driver.mouse.MouseDriver";

	private static final String MOUSE_EVENT = "mousevent";

	private static final String MOUSE_EVENT_PARAM_MOVE_FACTOR = "moveFactor";

	private static final String MOUSE_EVENT_PARAM_MOUSE_COMMAND = "mouseCommand";

	// Stores the NetworkInterface of the device that registered for events in
	// this driver
	private Vector listennerDevices = new Vector();

	// Display of the middlet in execution
	private Display display;

	// Mouse factor used to move the mouse on the screen
	private int mouseMoveFactor = 1;

	// Gateway to access the smart space
	private SmartSpaceGateway gateway;

	// Current Id of this instance of a driver
	private String instanceId;

	private UosDeviceManager deviceManager;

	@Override
	public UpDriver getDriver() {
		UpDriver driver = new UpDriver(MOUSE_DRIVER);

		driver.addService("registerListener").addParameter("eventKey",
				ParameterType.MANDATORY);

		driver.addService("unregisterListener").addParameter("eventKey",
				ParameterType.OPTIONAL);

		UpService upService = new UpService(MOUSE_EVENT);
		upService.addParameter(MOUSE_EVENT_PARAM_MOUSE_COMMAND,
				ParameterType.MANDATORY).addParameter(
				MOUSE_EVENT_PARAM_MOVE_FACTOR, ParameterType.MANDATORY);

		driver.addEvent(upService);

		return driver;
	}

	@Override
	public void handleServiceCall(ServiceCall serviceCall,
			ServiceResponse serviceResponse, NetworkDevice networkDevice) {
		if (serviceCall.getService().equals("registerListener")) {
			registerListener(serviceCall, serviceResponse);
		} else if (serviceCall.getService().equals("unregisterListener")) {
			unregisterListener(serviceCall, serviceResponse);
		}
	}

	@Override
	public void init(UosDeviceManager deviceManager, String instanceId) {
		this.deviceManager = deviceManager;
		this.instanceId = instanceId;
	}

	@Override
	public void tearDown() {
	}

	@Override
	public void registerListener(ServiceCall serviceCall,
			ServiceResponse serviceResponse) {
//		UpNetworkInterface uni = new UpNetworkInterface(networkDevice
//				.getNetworkDeviceType(), networkDevice.getDeviceName());
//		if (!listennerDevices.contains(uni)) {
//			listennerDevices.addElement(uni);
//		}
//		displayMouseDriver(display);
	}

	@Override
	public void unregisterListener(ServiceCall serviceCall,
			ServiceResponse serviceResponse) {
//		UpNetworkInterface uni = new UpNetworkInterface(networkDevice.getNetworkDeviceType(), networkDevice.getDeviceName());
//        listennerDevices.removeElement(uni);
//        
//        display.setCurrent(olderDisplayable);
	}
}
