package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Device;
import emailheaderinformation.model.Header;

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static emailheaderinformation.analysers.GeoIPAnalyser.NO_RESULT_FOUND;
import static java.lang.Float.parseFloat;

public class DeviceAnalyser extends HeaderAnalyser {

  public DeviceAnalyser (Header header, MainWindow mainWindow) {
    super(header, mainWindow);
  }

  @Override public Device call () throws Exception {
    Device device = mHeader.getStartDevice();

    Pattern pattern = Pattern.compile("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})");
    String
        validHostnameRegex
        = "(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*" +
          "([A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])";
    Pattern hostname = Pattern.compile(validHostnameRegex);

    while (device.getNext() != null) {
      // Regex to match IP addresses, with some bounds checking
      String ip = "";
      float lat = 0;
      float lon = 0;
      System.out.printf("Device Analyser: %s\n", device.getName());
      Matcher matcher = pattern.matcher(device.getName());
      while (matcher.find() && lat == 0 && lon == 0) {
        String tempIp = matcher.group();
        System.out.printf("IP Address: %s\n", tempIp);
        GeoIPAnalyser geoIPAnalyser = new GeoIPAnalyser(mHeader, mMainWindow, tempIp);
        String result = (String) mMainWindow.submitToExecutorService(geoIPAnalyser).get();
        System.out.printf("Result: %s\n", result);
        // Allow for local IP addresses being found
        if (!NO_RESULT_FOUND.equals(result)) {
          ip = matcher.group();
          lat = parseFloat(result.split(",")[0]);
          device.setLatitude(lat);
          lon = parseFloat(result.split(",")[1]);
          device.setLongitude(lon);
          System.out.printf("%s: (%s, %s, %s)%n", device.getName(), ip, lat, lon);
        }
      }

      // Coincidentally, just above the middle of the Atlantic.  No servers here
      if (lat == 0 && lon == 0) {
        Matcher hostMatcher = hostname.matcher(device.getName());
        if (hostMatcher.find()) {
          String host = hostMatcher.group(0);
          InetAddress inetAddress = InetAddress.getByName(host);
          GeoIPAnalyser geoIPAnalyser = new GeoIPAnalyser(mHeader, mMainWindow,
                                                          inetAddress.getHostAddress());
          String result = (String) mMainWindow.submitToExecutorService(geoIPAnalyser).get();
          System.out.printf("Result: %s\n", result);
          // Allow for local IP addresses being found
          if (!NO_RESULT_FOUND.equals(result)) {
            ip = matcher.group();
            lat = parseFloat(result.split(",")[0]);
            device.setLatitude(lat);
            lon = parseFloat(result.split(",")[1]);
            device.setLongitude(lon);
            System.out.printf("%s: (%s, %s, %s)%n", device.getName(), ip, lat, lon);
          }
        }
      }

      mMainWindow.getVfm().lookupVulnerabilityForKeyword(device.getSoftware());
      mMainWindow.getFoundInformation().addDevice(ip, lat, lon);

      device = device.getNext();
    }

    return null;
  }
}
