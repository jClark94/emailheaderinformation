package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Uses results found in http://isyou.info/jisis/vol5/no1/jisis-2015-vol5-no1-04.pdf
 */
public class ClientInferrer extends HeaderAnalyser {
    final Header mHeader;
    final MainWindow mMainWindow;

    private final HashSet<String> outlookAppleKeywords;

    public ClientInferrer (Header header, MainWindow mainWindow) {
        super(header, mainWindow);
        mHeader = header;
        mMainWindow = mainWindow;

        String[] kws = { "Accept-Language", "Content-Language", "Threat-Index", "Threat-Topic" };
        outlookAppleKeywords = new HashSet<String>();

        for (String keyword : kws) {
            outlookAppleKeywords.add(keyword);
        }
    }

    public void run () {
        String product = null;
        String hint = null;
        String lookup = null;
        // Presence of User-Agent string implies Thunderbird
        if (mHeader.getFields().containsKey("User-Agent")) {
            product = "Thunderbird";
            hint = "User-Agent";
            lookup = "thunderbird";
        } else if (!mHeader.getFields().keySet().stream().filter(
                x -> outlookAppleKeywords.contains(x)).collect(Collectors.toList()).isEmpty()) {
            // Presence of keys from outlookAppleKeywords implies Apple Mail
            // or Outlook
            if (mHeader.getFields().containsKey("X-Mailer")) {
                // If X-Mailer is seen, then more likely to have been sent by
                // Apple Mail
                product = "Apple Mail";
                hint = "X-Mailer";
                lookup = "apple:mail";
            } else {
                // Otherwise, more likely to be Outlook
                product = "Microsoft Outlook";
                hint = "Accept-Language";
                lookup = "outlook";
            }
        }

        if (product != null) {
            Object[] arr = { "Application", "Inferred", product, hint };
            mMainWindow.getVfm().lookupVulnerabilityForKeyword(lookup);
            mMainWindow.addToTable(arr);
            mMainWindow.getFoundInformation().setSoftware(product);
        }
    }
}
