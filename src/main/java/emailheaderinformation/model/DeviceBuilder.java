package emailheaderinformation.model;

import java.util.Date;

public class DeviceBuilder {
  private Device next = null;
  private String name;
  private String software;
  private Date receivedTime;
  private String origin;

  public DeviceBuilder setNext (Device next) {
    this.next = next;
    return this;
  }

  public DeviceBuilder setName (String name) {
    this.name = name;
    return this;
  }

  public DeviceBuilder setSoftware (String software) {
    this.software = software;
    return this;
  }

  public DeviceBuilder setReceivedTime (Date receivedTime) {
    this.receivedTime = receivedTime;
    return this;
  }

  public Device createDevice () {
    return new Device(next, name, software, receivedTime, origin);
  }

  public DeviceBuilder setOrigin (String origin) {
    this.origin = origin;
    return this;
  }
}