package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;
import uk.bl.wa.whois.JRubyWhois;

/**
 * Created by jaclark on 03/05/16.
 */
public class WhoIsAnalyser extends HeaderAnalyser {

  public WhoIsAnalyser (Header header, MainWindow main) {
    super(header, main);
  }

  public void addAndRun (String domain) {

  }

  @Override public String call () {
    JRubyWhois jRubyWhoIs = new JRubyWhois();
    System.out.println(jRubyWhoIs.lookup("google.com").getAdminContacts().get(0).getOrganization());

    return "thing to return with whois information";
  }
}
