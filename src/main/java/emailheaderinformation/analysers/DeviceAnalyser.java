package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Device;
import emailheaderinformation.model.Header;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static emailheaderinformation.analysers.GeoIPAnalyser.*;

public class DeviceAnalyser extends HeaderAnalyser {

  public DeviceAnalyser (Header header, MainWindow mainWindow) {
    super(header, mainWindow);
  }

  @Override public Device call () throws Exception {
    Device device = mHeader.getStartDevice();

    while (device.getNext() != null) {
      // Regex to match IP addresses, with some bounds checking
      Pattern pattern = Pattern.compile("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).){3}" +
                                        "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
      Matcher matcher = pattern.matcher(device.getName());
      if (matcher.find()) {
        for (int i = 0;  i < matcher.groupCount(); i++) {
          String ip = matcher.group(i);
          GeoIPAnalyser geoIPAnalyser = new GeoIPAnalyser(mHeader, mMainWindow, ip);
          String result = (String) mMainWindow.submitToExecutorService(geoIPAnalyser).get();
          // Allow for local IP addresses being found
          if (!NO_RESULT_FOUND.equals(result)) {
            device.setLatitude(Float.parseFloat(result.split(",")[0]));
            device.setLongitude(Float.parseFloat(result.split(",")[1]));
          }
        }
      }

      mMainWindow.getVfm().lookupVulnerabilityForKeyword(device.getSoftware());

      device = device.getNext();
    }

    return null;
  }
}
