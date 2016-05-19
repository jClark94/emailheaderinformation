package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.concurrent.ExecutionException;

public class SenderInformationExtractor extends HeaderAnalyser {

  public SenderInformationExtractor (Header header, MainWindow mainWindow) {
    super(header, mainWindow);
  }

  @Override public Object call () {
    String fromField = mHeader.getFields().get("From");
    int emailStart = fromField.indexOf("<");
    int emailEnd = fromField.indexOf(">");
    mMainWindow.getFoundInformation().setName(fromField.substring(0, emailStart - 1));
    Object[] arr = {
        "Personal", "Sender Information", "Yes", fromField.substring(0, emailStart - 1) };
    mMainWindow.addToTable(arr);
    String email = fromField.substring(emailStart+1, emailEnd);
    System.out.println(email);
    if (isValidEmailAddress(email)) {
      String domain = email.split("@")[1];
      WhoIsAnalyser wia = new WhoIsAnalyser(mHeader, mMainWindow, domain);
      String result = "";
      try {
        System.out.println("WIA lookup started");
        result = (String) mMainWindow.submitToExecutorService(wia).get();
        System.out.println("WIA lookup ended");
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
      if (result.isEmpty()) {
        mMainWindow.getFoundInformation().setOrganisation("Not found");
      } else {
        mMainWindow.getFoundInformation().setOrganisation(result);
      }
    }

    return null;
  }

  private static boolean isValidEmailAddress (String email) {
    boolean result = true;
    try {
      InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
    } catch (AddressException ex) {
      result = false;
    }
    return result;
  }

}
