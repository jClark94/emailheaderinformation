package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

public class UsernameHeaderAnalyser extends HeaderAnalyser {

  public UsernameHeaderAnalyser (Header header, MainWindow mainWindow) {
    super(header, mainWindow);
  }

  @Override public Object call () {
    String[] kws = {"X-Oxford-Username", "X-Username", "X-Authenticated-User"};
    for (String kw : kws) {
      if (mHeader.getFields().containsKey(kw)) {
        Object[] arr = {
            "Username", "Username", "Yes", mHeader.getFields().get(kw) };
        mMainWindow.addToTable(arr);
        mMainWindow.getFoundInformation().addUsername("Oxford", mHeader.getFields().get(kw));
      }
    }
    return null;
  }
}
