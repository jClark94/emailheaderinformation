package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Uses results found in http://isyou.info/jisis/vol5/no1/jisis-2015-vol5-no1-04.pdf
 */
public class ClientInferrer extends HeaderAnalyser {
  private final Header mHeader;
  private final MainWindow mMainWindow;

  private final HashSet<String> outlookAppleKeywords;

  public ClientInferrer (Header header, MainWindow mainWindow) {
    super(header, mainWindow);
    mHeader = header;
    mMainWindow = mainWindow;

    String[] kws = { "Accept-Language", "Content-Language", "Threat-Index", "Threat-Topic" };
    outlookAppleKeywords = new HashSet<>();

    Collections.addAll(outlookAppleKeywords, kws);
  }

  @Override public Object call () throws Exception {
    run();
    return null;
  }

  private void run () {
    String product = null;
    String hint = null;
    String lookup = null;
    // Presence of User-Agent string implies Thunderbird
    if (mHeader.getFields().containsKey("User-Agent")) {
      product = "Thunderbird";
      hint = "User-Agent";
      lookup = "thunderbird";
    } else if (!mHeader.getFields()
                    .keySet()
                    .stream()
                    .filter(outlookAppleKeywords:: contains)
                    .collect(Collectors.toList())
                    .isEmpty()) {
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
      Object[] arr = { "Application", product, "Inferred", hint };
      mMainWindow.addToTable(arr);
      mMainWindow.getVfm().lookupVulnerabilityForKeyword(lookup);
      mMainWindow.getFoundInformation().setSoftware(product);
    }
  }
}
