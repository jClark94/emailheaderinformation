package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Device;
import emailheaderinformation.model.Header;

/**
 * Created by jaclark on 09/05/16.
 */
public class DeviceAnalyser extends HeaderAnalyser {

  public DeviceAnalyser (Header header, MainWindow mainWindow) {
    super(header, mainWindow);
  }

  @Override public Device call () throws Exception {
    return null;
  }
}
