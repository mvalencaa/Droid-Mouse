package br.unb.unbiquitous.ubiquitos.json.dataType;

import br.unb.unbiquitous.ubiquitos.json.JSONException;
import br.unb.unbiquitous.ubiquitos.json.JSONObject;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.NetworkProtocol;

public class JSONNetType extends JSONObject{

	private static final String PROP_ID = "id";

	public JSONNetType(String source) throws JSONException {
		super(source);
	}

	public JSONNetType(NetworkProtocol bean) throws JSONException {
		this.put(PROP_ID,bean.getId());
	}
	
	public NetworkProtocol getAsObject() throws JSONException{
		NetworkProtocol netType = new NetworkProtocol();
		
		netType.setId( this.getInt(PROP_ID));
		
		return netType;
	}
}
