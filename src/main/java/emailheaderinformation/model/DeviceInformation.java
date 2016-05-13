package emailheaderinformation.model;

public class DeviceInformation {
  private int id;
  private String ipAddress;
  private float lat;

  public float getLon () {
    return lon;
  }

  public void setLon (float lon) {
    this.lon = lon;
  }

  public float getLat () {
    return lat;
  }

  public void setLat (float lat) {
    this.lat = lat;
  }

  private float lon;

  public DeviceInformation (int id, String ip, float lat, float lon) {
    this.id = id;
    this.ipAddress = ip;
    this.lat = lat;
    this.lon = lon;
  }

  public int getId () {
    return id;
  }

  public void setId (int id) {
    this.id = id;
  }

  public String getIpAddress () {
    return ipAddress;
  }

  public void setIpAddress (String ipAddress) {
    this.ipAddress = ipAddress;
  }
}
