package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Device;
import emailheaderinformation.model.Header;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static emailheaderinformation.analysers.GeoIPAnalyser.NO_RESULT_FOUND;
import static java.lang.Float.parseFloat;

public class DeviceAnalyser extends HeaderAnalyser {

  private Pattern pattern;
  private Pattern hostname;

  public DeviceAnalyser (Header header, MainWindow mainWindow) {
    super(header, mainWindow);
  }

  @Override public Device call () throws ExecutionException {
    Device device = mHeader.getStartDevice();

    pattern = Pattern.compile("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})");
    String validHostnameRegex = "(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*" +
                                "([A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])";
    hostname = Pattern.compile(validHostnameRegex);

    while (device.getNext() != null) {
      try {
        String devName = device.getName();
        extractInformation(devName, device);
      } catch (InterruptedException | ExecutionException | UnknownHostException e) {
        e.printStackTrace();
      }

      device = device.getNext();
    }

    // Special case for the last device
    try {
      extractInformation(device.getOrigin(), device);
    } catch (InterruptedException | UnknownHostException e) {
      e.printStackTrace();
    }

    return null;
  }

  private void extractInformation (String devName, Device device)
      throws InterruptedException, java.util.concurrent.ExecutionException, UnknownHostException {
    // Regex to match IP addresses, with some bounds checking
    String ip = "";
    float lat = 0;
    float lon = 0;
    String owner = "";
    Matcher matcher = pattern.matcher(devName);
    while (matcher.find() && lat == 0 && lon == 0) {
      String tempIp = matcher.group();
      GeoIPAnalyser geoIPAnalyser = new GeoIPAnalyser(mHeader, mMainWindow, tempIp);
      String result = (String) mMainWindow.submitToExecutorService(geoIPAnalyser).get();
      // Allow for local IP addresses being found
      if (!NO_RESULT_FOUND.equals(result)) {
        ip = matcher.group();
        lat = parseFloat(result.split(",")[0]);
        device.setLatitude(lat);
        lon = parseFloat(result.split(",")[1]);
        device.setLongitude(lon);
      }
    }

    // Coincidentally, just above the middle of the Atlantic.  No servers here
    try {
      Matcher hostMatcher = hostname.matcher(devName);
      if (hostMatcher.find()) {
        String host = hostMatcher.group(0);
        if (lat == 0 && lon == 0) {
          InetAddress inetAddress = InetAddress.getByName(host);
          GeoIPAnalyser geoIPAnalyser = new GeoIPAnalyser(mHeader,
                                                          mMainWindow,
                                                          inetAddress.getHostAddress());
          String result = (String) mMainWindow.submitToExecutorService(geoIPAnalyser).get();
          // Allow for local IP addresses being found
          if (!NO_RESULT_FOUND.equals(result)) {
            ip = inetAddress.getHostAddress();
            lat = parseFloat(result.split(",")[0]);
            device.setLatitude(lat);
            lon = parseFloat(result.split(",")[1]);
            device.setLongitude(lon);
          }
        }
        WhoIsAnalyser wia = new WhoIsAnalyser(mHeader, mMainWindow, host);
        owner = (String) mMainWindow.submitToExecutorService(wia).get();
      }
    } catch (UnknownHostException ignored) {
    }

    mMainWindow.getVfm().lookupVulnerabilityForKeyword(device.getSoftware());
    mMainWindow.getFoundInformation().addDevice(ip, lat, lon, device.getSoftware(), owner);
  }
}
