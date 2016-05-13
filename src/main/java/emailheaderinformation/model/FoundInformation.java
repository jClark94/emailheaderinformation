package emailheaderinformation.model;

import java.util.ArrayList;
import java.util.List;

public class FoundInformation {
  private String name;
  private String software;
  private List<Fact> facts = new ArrayList<>();
  private List<Username> usernameList = new ArrayList<Username>();

  public List<DeviceInformation> getDevices () {
    return devices;
  }

  private List<DeviceInformation> devices = new ArrayList<>();
  private int deviceCount = 0;

  public List<Username> getUsernameList () {
    return usernameList;
  }

  public void addUsername (String context, String username) {
    usernameList.add(new Username(context, username));
  }

  public String getName () {
    return name == null ? "Not found" : name;
  }

  public void setName (String name) {
    this.name = name;
  }

  public void addFact (String factClass, String factType, String factDetails) {
    facts.add(new Fact(factClass, factType, factDetails));
  }

  public List<Fact> getFacts () {
    return facts;
  }

  public String getSoftware () {
    return software == null ? "Not found, or using web client" : software;
  }

  public void setSoftware (String software) {
    this.software = software;
  }

  public void addDevice(String ip, float lat, float lon) {
    DeviceInformation di = new DeviceInformation(deviceCount++, ip, lat, lon);
    devices.add(di);
  }
}
