package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

public class SenderInformationExtractor extends HeaderAnalyser {
  public SenderInformationExtractor (Header header, MainWindow mainWindow) {
    super(header, mainWindow);
  }

  @Override public Object call () {
    String fromField = mHeader.getFields().get("From");
    int emailStart = fromField.indexOf("<");
    mMainWindow.getFoundInformation().setName(fromField.substring(0, emailStart - 1));
    Object[] arr = {
        "Personal", "Sender Information", "Yes", fromField.substring(0, emailStart - 1)
    };
    mMainWindow.addToTable(arr);

    return null;
  }
}
