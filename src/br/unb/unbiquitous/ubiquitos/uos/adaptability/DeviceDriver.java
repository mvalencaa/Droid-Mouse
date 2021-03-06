package br.unb.unbiquitous.ubiquitos.uos.adaptability;

import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceCall;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceResponse;

public interface DeviceDriver extends UosDriver {

	/**
	 * Service responsible for retrieving the list of Driver Instances present in the underlying device.
	 * This listing service can have its result filtered with the use of the parameters 'serviceName' or 'driverName'.
	 * It responds in a single responseMap within the parameter 'driverList'
	 */
	public void listDrivers(ServiceCall serviceCall, ServiceResponse serviceResponse);
	
	/**
	 * Service responsible for authenticating a device. This Service can be called multiple times for a authentication process
	 * with multiple steps. 
	 * The authentication algorithm is determined by the parameter 'securityType'.
	 */
	public void authenticate(ServiceCall serviceCall, ServiceResponse serviceResponse);
	
	/**
	 * This method is responsible for creting a mutual knowledge of two device about it's basic informations.
	 * This information must be informed in the parameter 'device'(<code>UpDevice</code>) by the caller device 
	 * and will be returned in the same parameter with the information of the called device.
	 */
	public void handshake(ServiceCall serviceCall, ServiceResponse serviceResponse);
	
}
