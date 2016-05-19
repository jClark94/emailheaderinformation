package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;
import uk.bl.wa.whois.JRubyWhois;
import uk.bl.wa.whois.record.WhoisResult;

public class WhoIsAnalyser extends HeaderAnalyser {

  private final String lookup;

  public WhoIsAnalyser (Header header, MainWindow main, String lookup) {
    super(header, main);
    this.lookup = lookup;
  }

  @Override public String call () {
    JRubyWhois jRubyWhoIs = new JRubyWhois();
    String search = lookup;
    while (search.contains(".")) {
      System.out.printf("WhoIs: %s%n", search);
      WhoisResult wir = jRubyWhoIs.lookup(lookup);
      String[] lines = wir.getParts().get(0).getBody().split("\\n");
      for (String line : lines) {
        int index = line.indexOf("-name");
        // if found, and we've not just found the domain name, which is a bit pointless
        if (index > 0 && !line.startsWith("Domain")) {
          System.out.printf("Org: %s%n", line.substring(index + 5).trim());
          return line.substring(index + 5).trim();
        }
      }
      int nextDot = search.indexOf(".");
      search = search.substring(nextDot+1);
    }
    return "NOT FOUND";
  }
}
