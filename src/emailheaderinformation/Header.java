package emailheaderinformation;

import java.util.HashMap;
import java.util.Map;

public class Header {
	public Header(Device startDevice, Map<String, String> fields) {
		super();
		this.startDevice = startDevice;
		this.fields = fields;
	}
	Device startDevice;
	Map<String, String> fields = new HashMap<String,String>();

}
