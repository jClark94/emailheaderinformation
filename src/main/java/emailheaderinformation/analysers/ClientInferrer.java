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
        // Presence of User-Agent string implies Thunderbird
        if (mHeader.getFields().containsKey("User-Agent")) {
				Object[] arr = {"Application", "Inferred", "Thunderbird", "User-Agent" };
                mMainWindow.getVfm().lookupVulnerabilityForKeyword("thunderbird");
				mMainWindow.addToTable(arr);
        } else if (!mHeader.getFields().keySet().stream()
                       .filter(x -> outlookAppleKeywords.contains(x))
                       .collect(Collectors.toList()).isEmpty()) {
            // Presence of keys from outlookAppleKeywords implies Apple Mail
            // or Outlook
            if (mHeader.getFields().containsKey("X-Mailer")) {
                // If X-Mailer is seen, then more likely to have been sent by
                // Apple Mail
                Object[] arr = {"Application", "Inferred", "Apple Mail", "X-Mailer" };
                mMainWindow.addToTable(arr);
                mMainWindow.getVfm().lookupVulnerabilityForKeyword("apple:mail");
            } else {
                // Otherwise, more likely to be Outlook
                Object[] arr = {"Application", "Inferred", "Microsoft Outlook", "Accept-Language" };
                mMainWindow.addToTable(arr);
                mMainWindow.getVfm().lookupVulnerabilityForKeyword("outlook");

            }
        }
    }
}
