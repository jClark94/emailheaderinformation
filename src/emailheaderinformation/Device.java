package emailheaderinformation;

import java.util.Date;

public class Device {
	public Device(Device next, String name, String software, Date receivedTime) {
		this.next = next;
		this.name = name;
		this.software = software;
		this.receivedTime = receivedTime;
	}

	private final Device next;
	private final String name;
	private final String software;
	private final Date receivedTime;

}
