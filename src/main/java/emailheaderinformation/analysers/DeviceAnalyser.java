package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Device;
import emailheaderinformation.model.Header;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceAnalyser extends HeaderAnalyser {

  public DeviceAnalyser (Header header, MainWindow mainWindow) {
    super(header, mainWindow);
  }

  @Override public Device call () throws Exception {
    Device device = mHeader.getStartDevice();

    while (device.getNext() != null) {
      String ip = "";
      Pattern pattern = Pattern.compile("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).){3}" +
                                        "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
      Matcher matcher = pattern.matcher(device.getName());
      if (matcher.find()) {
        ip = matcher.group(0);
      }

      GeoIPAnalyser geoIPAnalyser = new GeoIPAnalyser(mHeader, mMainWindow, ip);
      String result = (String) mMainWindow.submitToExecutorService(geoIPAnalyser).get();
      device.setLatitude(Float.parseFloat(result.split(",")[0]));
      device.setLongitude(Float.parseFloat(result.split(",")[1]));

      device = device.getNext();
    }

    return null;
  }
}
