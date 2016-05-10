package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;
import uk.bl.wa.whois.JRubyWhois;

public class WhoIsAnalyser extends HeaderAnalyser {

  private final String lookup;

  public WhoIsAnalyser (Header header, MainWindow main, String lookup) {
    super(header, main);
    this.lookup = lookup;
  }

  @Override public String call () {
    JRubyWhois jRubyWhoIs = new JRubyWhois();
    return jRubyWhoIs.lookup(lookup).getAdminContacts().get(0).getOrganization();
  }
}
