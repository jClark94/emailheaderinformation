package emailheaderinformation.model;

import java.util.Date;

public class Device {
	private Device next;
	private final String name;
	private final String software;
	private final Date receivedTime;
	private final String origin;
	private boolean setNext = false;

	public Device(Device next, String name, String software, Date receivedTime, String origin) {
		this.name = name;
		this.software = software;
		this.receivedTime = receivedTime;
		this.origin = origin;
	}

	public Device getNext() {
		return next;
	}

	public String getName() {
		return name;
	}

	public String getSoftware() {
		return software;
	}

	public Date getReceivedTime() {
		return receivedTime;
	}

	public String getOrigin() {
		return origin;
	}

	public void setNext(Device next) {
		if (!setNext) {
			this.next = next;
			setNext = true;
		}
	}
}
