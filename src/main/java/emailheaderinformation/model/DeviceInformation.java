package emailheaderinformation.model;

public class DeviceInformation {
  private int id;
  private String ipAddress;
  private float lat;
  private float lon;
  private final String software;
  private final String owner;

  public DeviceInformation (int id, String ip, float lat, float lon, String software, String owner) {
    this.id = id;
    this.ipAddress = ip;
    this.lat = lat;
    this.lon = lon;
    this.software = software;
    this.owner = owner;
  }
}
