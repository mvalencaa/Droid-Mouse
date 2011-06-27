package br.unb.unbiquitous.ubiquitos.uos.adaptability;

import java.util.Hashtable;
import java.util.Vector;

import android.util.Log;
import br.unb.unbiquitous.ubiquitos.network.model.NetworkDevice;
import br.unb.unbiquitous.ubiquitos.uos.UosDeviceManager;
import br.unb.unbiquitous.ubiquitos.uos.exception.UosException;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceCall;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceResponse;

/**
 * 
 * Class responsible for dealing with service requests, event handling and drivers.
 * 
 * @author Fabricio Nogueira Buzeto
 *
 */
public class AdaptabilityEngine {
	
	private static final String TAG = "DroidMouse - [AdaptabilityEngine]";
	
    private UosDeviceManager deviceManager;
    
    //<String,Vector>
    //<DriverName, Drivers Implementing it>
    private Hashtable driverMap = new Hashtable();

    //<String,UosDriver>
    //<instanceID, DriverInstance>
    private Hashtable instanceDriverMap = new Hashtable();
    
    private static long deployedDriverCount = 0;
    
    
    /**
     * This class needs to know which device manager to use to access the current device information. 
     * @param deviceManager Current device Manager.
     */
    public AdaptabilityEngine(UosDeviceManager deviceManager) {
            super();
            this.deviceManager = deviceManager;
    }

    /**
     * Method responsible for adding a Driver to the current device available resources.
     * 
     * @param driver Driver to be deployed.
     * @param instanceId Instance id of the driver, if not informed one will be assigned for it.
     * @throws UosException
     */
    public synchronized void addDriver(UosDriver driver, String instanceId) throws UosException{
            if (instanceDriverMap.get(instanceId) != null){
                    throw new UosException("Another Driver with the same instance Id already deployed.");
            }
            if (instanceId == null){
                    instanceId = driver.getDriver().getName()+deployedDriverCount;
            }
            
            Vector driverSet = (Vector)driverMap.get(driver.getDriver().getName());
            
            if (driverSet == null){
                    driverSet = new Vector();
                    driverMap.put(driver.getDriver().getName(), driverSet);
            }
            
            driverSet.addElement(driver);
            instanceDriverMap.put(instanceId, driver);
    }
    
    /**
     * Methods responsible for delegating a service call for the responsible driver.
     * 
     * @param serviceCall Service Call and its parameters.
     * @param networkDevice representation of the network interface used to call this service.
     * @return ServiceResponse with the data about the service execution.
     * @throws UosException
     */
    public ServiceResponse handleServiceCall(ServiceCall serviceCall, NetworkDevice networkDevice) throws UosException{
            ServiceResponse serviceResponse = new ServiceResponse();
            Log.d(TAG, "ServiceCall received.");
            if (serviceCall != null){
                    String instanceId = serviceCall.getInstanceId();
                    if (instanceId != null &&
                                    !instanceId.trim().equals("")){
                            // Call the proper instance
                    	Log.d(TAG, "Searching for a driver with instanceID : " + instanceId);
                            UosDriver driver = (UosDriver)instanceDriverMap.get(instanceId);
                            if (driver != null){
                            	Log.d(TAG, "Delegating service Call to proper driver instance.");    	
                                    driver.handleServiceCall(serviceCall, serviceResponse, networkDevice);
                            }else{
                                    String message = "No Instance Driver was found with the instanceID :"+instanceId;
                                    serviceResponse.setError(message);
                                	Log.i(TAG, message);
                            }
                    } else {
                            String driverName = serviceCall.getDriver();
                            if (driverName != null &&
                                            !driverName.trim().equals("")){
                                    // Choose a proper driver to call
                            	Log.d(TAG, "Searching for a driver instance fro driver : " + driverName);
                                    Vector driverPool = (Vector)driverMap.get(driverName);
                                    if (driverPool != null && !driverPool.isEmpty()){
                                    	Log.d(TAG, "Getting first driver instance available.");
                                            UosDriver driver = (UosDriver)driverPool.firstElement();
                                            Log.d(TAG, "Delegating service Call to proper driver instance chosen.");
                                            driver.handleServiceCall(serviceCall, serviceResponse, networkDevice);
                                    }else{
                                            String message = "No Instance Driver was found for driver :"+driverName;
                                            serviceResponse.setError(message);
                                            Log.i(TAG, message);
                                    }
                            }else{
                                    // Inform error
                                    String message = "Malformed Service Call";
                                    serviceResponse.setError(message);
                                    Log.w(TAG, message);
                            }
                    }
            }else{
            	Log.e(TAG, "No service call received");
            }
            return serviceResponse;
    }
    
    /**
     * @return Returns the current dataTable containing the InstanceIds and DriversObjects Available in the 
     *                      current device.
     */
    public Hashtable getDriversMap(){
            return instanceDriverMap;
    }
    
}