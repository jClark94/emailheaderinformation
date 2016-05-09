package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

import java.util.Set;

public class ExchangeHeaderAnalyser extends HeaderAnalyser {

  public ExchangeHeaderAnalyser (Header header, MainWindow mainWindow) {
    super(header, mainWindow);
  }

  @Override public Object call () throws Exception {
    Set<String> keySet = mHeader.getFields().keySet();
    for (String key : keySet) {
      if (key.startsWith("X-MS-Exchange")) {
        Object[] arr = { "Exchange", key, "Yes", mHeader.getFields().get(key) };
        mMainWindow.addToTable(arr);
      }
    }
    return null;
  }
}
