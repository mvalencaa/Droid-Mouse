package br.unb.unbiquitous.ubiquitos.uos.adaptability;

import br.unb.unbiquitous.ubiquitos.uos.context.UOSMessageContext;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceCall;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceResponse;

public interface UosEventDriver extends UosDriver {

	/**
	 * Service responsible for registering the caller device as a listener for the event under the key informed 
	 * in the 'eventKey' parameter for the implementing driver.
	 */
	public void registerListener(ServiceCall serviceCall, ServiceResponse serviceResponse);
	
	/**
	 * Service responsible for removing the caller device as a listener for the event under the key informed 
	 * in the 'eventKey' parameter for the implementing driver. If no key is informed the current device will be
	 * removed as listener from all event queues. 
	 */
	public void unregisterListener(ServiceCall serviceCall, ServiceResponse serviceResponse);
}
