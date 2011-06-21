//package br.unb.unbiquitous.ubiquitos.uos.network.bluetooth;
//
//import java.util.ResourceBundle;
//import java.util.Vector;
//
//import bluetooth.BtUtil;
//import bluetooth.BtUtilClientListener;
//import bluetooth.BtUtilException;
//import br.unb.unbiquitous.ubiquitos.network.bluetooth.BluetoothDevice;
//import br.unb.unbiquitous.ubiquitos.network.bluetooth.channelManager.BluetoothChannelManager;
//import br.unb.unbiquitous.ubiquitos.network.bluetooth.connection.BluetoothClientConnection;
//import br.unb.unbiquitous.ubiquitos.network.connectionManager.ChannelManager;
//import br.unb.unbiquitous.ubiquitos.network.connectionManager.ConnectionManager;
//import br.unb.unbiquitous.ubiquitos.network.connectionManager.ConnectionManagerListener;
//import br.unb.unbiquitous.ubiquitos.network.exceptions.NetworkException;
//import br.unb.unbiquitous.ubiquitos.network.model.NetworkDevice;
//
//public class BluetoothConnectionManager implements ConnectionManager, BtUtilClientListener {
//	
//	/* *****************************
//	 *   	ATRUBUTES
//	 * *****************************/
//	
//	/** The ResourceBundle to get some properties. */
//	private ResourceBundle resource;
//	
//	//TODO REVIEW THESE ATTRIBUTE NAMES!
//	/** Specify the client and the provider for the bluetooth connection */
//	private static final String UBIQUITOS_BTH_PROVIDER_KEY = "ubiquitos.bth.provider";
//	public static String UBIQUITOS_BTH_PROVIDER;
//	private static final String UBIQUITOS_BTH_CLIENT_KEY = "ubiquitos.bth.client";
//	private String UBIQUITOS_BTH_CLIENT;
//	
//    /** Object for logging registration.*/
////	private static final Logger logger = Logger.getLogger(BluetoothConnectionManager.class.getName());
//
//    /** A simple way to handle the bluetooth stuff. */
//    private BtUtil btUtil = null;
//    
//    /** A Connection Manager Listener (ConnectionManagerControlCenter) */
//    private ConnectionManagerListener connectionManagerListener = null;
//    
//    /** Server Connection */
//    private BluetoothDevice clientDevice;
//    private BluetoothClientConnection client;
//    
//    /** The ChannelManager for new channels */
//    private BluetoothChannelManager channelManager;
//    
//    /* *****************************
//	 *   	CONSTRUCTOR
//	 * *****************************/
//	
//    /**
//	 * Constructor
//	 * @throws UbiquitOSException
//	 */
//    public BluetoothConnectionManager() throws NetworkException {
//        //Start the bluetooth API
//        try {
//            btUtil = new BtUtil();
//        } catch (BtUtilException ex) {
//            throw new NetworkException("Error creating Bluetooth Connection Manager: " + ex.toString());
//        }
//    }
//
//	@Override
//	public ChannelManager getChannelManager() {
//		if(channelManager == null){
//			channelManager = new BluetoothChannelManager(btUtil,UBIQUITOS_BTH_CLIENT);
//		}
//		return channelManager;
//	}
//
//	@Override
//	public NetworkDevice getNetworkDevice() {
//		if(clientDevice == null){
//			try {
//				clientDevice = new BluetoothDevice("");
//			} catch (BluetoothStateException e) {
//				throw new RuntimeException("Error creating Bluetooth Connection Manager: " + e.toString());
//			}
//		}
//		return clientDevice;
//	}
//
//	@Override
//	public void setConnectionManagerListener(ConnectionManagerListener connectionManagerListener) {
//		this.connectionManagerListener = connectionManagerListener;
//	}
//
//	@Override
//	public void setResourceBundle(ResourceBundle resourceBundle) {
//		this.resource = resourceBundle;
//	}
//
//	@Override
//	public void tearDown() {
//		try {
////			logger.debug("Closing Bluetooth Connection Manager...");
//			client.closeConnection();
//			if(channelManager != null){
//				channelManager.tearDown();
//			}
////			logger.debug("Bluetooth Connection Manager is closed.");
//		} catch (Exception e) {
//			String msg = "Error stoping Bluetooth Connection Manager. ";
////            logger.fatal(msg, e);
//		}
//	}
//
//	@Override
//	// TODO: [BRUNO E MARCELO] REVIEW THIS METHOD! UBIQUITOS_BTH_PROVIDER_KEY and UBIQUITOS_BTH_CLIENT_KEY
//	public void run() {
////		logger.debug("Starting UbiquitOS Smart-Space Bluetooth Connection Manager.");
////        logger.info("Starting Bluetooth Connection Manager...");
//        
//        if(resource == null){
//        	String msg = "ResourceBundle is null";
////        	logger.fatal(msg);
//            throw new RuntimeException(msg);
//        }else{
//        	UBIQUITOS_BTH_PROVIDER = resource.getString(UBIQUITOS_BTH_PROVIDER_KEY);
//        	UBIQUITOS_BTH_CLIENT = resource.getString(UBIQUITOS_BTH_CLIENT_KEY);
//        }
//        
//    	try {
////    		server = new BluetoothServerConnection(this,btUtil,(BluetoothDevice)getNetworkDevice(),UBIQUITOS_BTH_CLIENT);
//    		client = new BluetoothClientConnection(???,(BluetoothDevice)getNetworkDevice(),UBIQUITOS_BTH_CLIENT);
//    		server.start();
////            logger.info("Bluetooth Connection Manager is started.");
//        } catch (Exception ex) {
//            String msg = "Error starting Bluetooth Connection Manager. ";
////            logger.fatal(msg, ex);
//        }
//	}
//
//	@Override
//	public void deviceDiscovered(RemoteDevice arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void deviceDiscoveryFinished(Vector arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void serviceDiscoveryFinished(Vector arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void tryingAgain(int arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//}
