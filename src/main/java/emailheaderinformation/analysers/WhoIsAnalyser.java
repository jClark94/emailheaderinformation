package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;
import uk.bl.wa.whois.JRubyWhois;

import java.io.IOException;

/**
 * Created by jaclark on 03/05/16.
 */
public class WhoIsAnalyser extends HeaderAnalyser {


    public WhoIsAnalyser (Header header, MainWindow main) {
        super(header, main);
    }

    public static void main(String[] args) {
        WhoIsAnalyser wia = new WhoIsAnalyser(null, null);
        wia.run();
    }

    public void addAndRun(String domain) {

    }

    @Override public void run () {
        JRubyWhois jRubyWhoIs = new JRubyWhois();
        System.out.println(jRubyWhoIs.lookup("google.com").getAdminContacts().get(0).getOrganization().toString());


    }
}
