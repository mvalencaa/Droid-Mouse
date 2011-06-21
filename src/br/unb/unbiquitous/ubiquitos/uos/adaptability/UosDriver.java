package br.unb.unbiquitous.ubiquitos.uos.adaptability;

import br.unb.unbiquitous.ubiquitos.network.model.NetworkDevice;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpDriver;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceCall;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceResponse;

/**
 * Interface representing the UbiquitOSDrivers.
 * 
 * Each driver is responsible for handling its services through the method :
 * 
 * 		public void handleServiceCall(ServiceCall serviceCall, ServiceResponse serviceResponse, UOSMessageContext messageContext);
 * 
 * @author Fabricio Nogueira Buzeto
 *
 */
public interface UosDriver {

	/**
	 * Method responsible for returning the Driver Interface which the implementing class refers to.
	 * 
	 * @return Driver containing the Interface correspondent to the implementation.
	 */
	public UpDriver getDriver();
	
	/**
	 * Method responsible for executing initialization tasks for the Driver
	 */
	public void init(String name, String address);
	
	/**
	 * Method responsible for executing clean-up tasks for the Driver
	 */
	public void tearDown();
	
	/**
	 * In this method the Driver Must handle the services which it supports.
	 * 
	 * @param serviceCall represent the service call request which originated the current call.
	 * @param serviceResponse represent the service call response which will be returned after the current call.
	 * @param networkDevice representation of the network interface used to call this service.
	 * @param messageContext represent the context of this conversation.
	 */
	public void handleServiceCall(ServiceCall serviceCall, ServiceResponse serviceResponse, NetworkDevice networkDevice);
}
