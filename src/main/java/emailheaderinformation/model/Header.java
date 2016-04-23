package emailheaderinformation.model;

import java.util.HashMap;
import java.util.Map;

public class Header {
	public Header() {
		super();
	}

	public Device getStartDevice() {
		return startDevice;
	}

	public void setStartDevice(Device startDevice) {
		this.startDevice = startDevice;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	private Device startDevice;
	private Map<String, String> fields = new HashMap<String, String>();

}
