package emailheaderinformation.parser;

import com.devdaily.system.SystemCommandExecutor;
import emailheaderinformation.model.Device;
import emailheaderinformation.model.DeviceBuilder;
import emailheaderinformation.model.Header;

import java.io.IOException;
import java.util.*;

public class EmailParser {
  private static Set<String> keywords = new HashSet<String>();

  public EmailParser () {
    setupParser();
  }

  private static void setupParser() {
    String[] kws = {
        "Received", "from", "by", "via", "with", "id", "for", ";" };
    Collections.addAll(keywords, kws);
  }

/*  public static Header parseHeader (String headerText) {
    Header header = new Header();
    headerText = headerText.replaceAll("\\t", " ");
    headerText = headerText.replaceAll("\\n ", " ");
    StringBuilder stringBuilder = new StringBuilder(headerText);
    for (int i = 0; i < headerText.length() - 1; i++) {
      if (headerText.charAt(i) == '\n' && headerText.charAt(i + 1) == ' ') {
        stringBuilder.setCharAt(i, ' ');
      }
    }

    HeaderTokenizer headerTokenizer = new HeaderTokenizer(stringBuilder.toString(),
                                                          HeaderTokenizer.MIME);

    return createDeviceChain(header, headerTokenizer);
  }*/

  /**
   * @param header - the header model object to populate
   * @param output - the header entry string
   *
   * @return
   *
   */
  private static Header createDeviceChain (Header header, String output) {
    try {
      Device lastDevice = null;
      Device firstDevice = null;
      for (String line : output.split("\\n")) {
        Device device = constructDevice(new StringChunker(line));
        if (firstDevice == null) {
          firstDevice = device;
        }
        if (lastDevice != null) {
          lastDevice.setNext(device);
        }
        lastDevice = device;
      }
      header.setStartDevice(firstDevice);
      return header;
    } catch (NullPointerException e) {
      // we've reached the end of the text to parse
      e.printStackTrace();
      return header;
    }
  }

  /**
   * RFC 822 defines the structure of the Received section as follows:
   * <p>
   * Example Received field:
   * <p>
   * Received: from relay12.mail.ox.ac.uk (129.67.1.163) by HUB05.ad.oak.ox.ac.uk (163.1.154.231)
   * with Microsoft SMTP Server id 14.3.169.1; Sat, 14 Nov 2015 10:55:35 +0000
   * <p>
   * received = "Received" ":" ; one per relay ["from" domain] ; sending host ["by" domain] ;
   * receiving host ["via" atom] ; physical path *("with" atom) ; link/mail protocol ["id"
   * msg-id] ;
   * receiver msg id ["for" addr-spec] ; initial form ";" date-time ; time received
   * <p>
   * addr-spec = local-part "@" domain ; global address
   * <p>
   * local-part = word *("." word) ; uninterpreted ; case-preserved
   * <p>
   * domain = sub-domain *("." sub-domain) sub-domain = domain-ref / domain-literal domain-ref =
   * atom domain-literal = "[" *(dtext / quoted-pair) "]" dtext = <any CHAR excluding "[", ; => may
   * be folded "]", "\" & CR, & including linear-white-space>
   * <p>
   * msg-id = "<" addr-spec ">" ; Unique message id
   * <p>
   * date-time = [ day "," ] date time ; dd mm yy hh:mm:ss zzz
   * <p>
   * day = "Mon" / "Tue" / "Wed" / "Thu" / "Fri" / "Sat" / "Sun"
   * <p>
   * date = 1*2DIGIT month 2DIGIT ; day month year ; e.g. 20 Jun 82
   * <p>
   * month = "Jan" / "Feb" / "Mar" / "Apr" / "May" / "Jun" / "Jul" / "Aug" / "Sep" / "Oct" /
   * "Nov" /
   * "Dec"
   * <p>
   * time = hour zone ; ANSI and Military
   * <p>
   * hour = 2DIGIT ":" 2DIGIT [":" 2DIGIT] ; 00:00:00 - 23:59:59
   * <p>
   * zone = "UT" / "GMT" ; Universal Time ; North American : UT / "EST" / "EDT" ; Eastern: - 5/ - 4
   * / "CST" / "CDT" ; Central: - 6/ - 5 / "MST" / "MDT" ; Mountain: - 7/ - 6 / "PST" / "PDT" ;
   * Pacific: - 8/ - 7 / 1ALPHA ; Military: Z = UT; ; A:-1; (J not used) ; M:-12; N:+1; Y:+12 / (
   * ("+" / "-") 4DIGIT ) ; Local differential ; hours+min. (HHMM)
   * <p>
   * Atoms may not contain SPACE
   *
   * @param headerTokenizer
   *     - the existing object to separate each field in the header
   *
   * @return a @Device object containing all the necessary information
   */
  private static Device constructDevice (StringChunker headerTokenizer) {
    DeviceBuilder builder = new DeviceBuilder();

    // Received:
    String peeked = headerTokenizer.next();
    boolean finished = false;
    while (!finished) {
      switch (peeked) {
        case "from":
          headerTokenizer.next();
          String[] origin = extractUntilKeyword(headerTokenizer);
          builder.setOrigin(origin[0]);
          peeked = origin[1];
          break;
        case "by":
          headerTokenizer.next();
          String[] name = extractUntilKeyword(headerTokenizer);
          builder.setName(name[0]);
          peeked = name[1];
          break;
        case "id":
        case "for":
        case "via":
          headerTokenizer.next();
          peeked = extractUntilKeyword(headerTokenizer)[1];
          break;
        case "with":
          headerTokenizer.next();
          String[] software = extractUntilKeyword(headerTokenizer);
          builder.setSoftware(software[0]);
          peeked = software[1];
          break;
        default:
          if (peeked.endsWith(";")) {
            finished = true;
            headerTokenizer.next();
          } else {
            peeked = headerTokenizer.peek();
          }
          break;
      }
    }

    Date date = null;
    StringBuilder dateStrB = new StringBuilder("");
    while (date == null) {
      String token = headerTokenizer.next();
      dateStrB.append(token);
      if (dateStrB.charAt(0) == ';') {
        dateStrB.deleteCharAt(0);
        if (!dateStrB.toString().isEmpty()) {
          while (Character.isWhitespace(dateStrB.charAt(0))) {
            dateStrB.deleteCharAt(0);
          }
        }
      }
      dateStrB.append(" ");
      date = DateUtil.parseRfc822DateString(dateStrB.toString());
    }

    builder.setReceivedTime(date);

    return builder.createDevice();
  }

  private static String[] extractUntilKeyword (StringChunker headerTokenizer) {
    String token = "";
    String lastSeen;
    lastSeen = headerTokenizer.peek();
    while (headerTokenizer.hasNext() && !(keywords.contains(lastSeen) || lastSeen.endsWith(";"))) {
      token += " " + headerTokenizer.next();
      lastSeen = headerTokenizer.peek();
    }
    String[] ret = {
        token, lastSeen };
    return ret;
  }

  public Header parse (String inputEmail) {
    try {
      ArrayList<String> command = new ArrayList<>();
      command.add("python2");
      command.add("HeaderParserTrace.py");
      command.add(inputEmail);
      SystemCommandExecutor commandExecutor = new SystemCommandExecutor(command);
      int result = commandExecutor.executeCommand();
      if (result == 0) {
        StringBuilder output = commandExecutor.getStandardOutputFromCommand();
        Header header = new Header();
        header = createDeviceChain(header, output.toString());
        command.set(1, "HeaderParserFields.py");
        SystemCommandExecutor commandExecutor1 = new SystemCommandExecutor(command);
        if (commandExecutor1.executeCommand() == 0) {
          StringBuilder fields = commandExecutor1.getStandardOutputFromCommand();
          String[] keyval = fields.toString().split("\n");
          for (int i = 0; i < keyval.length - 1; i += 2) {
            if (header != null) {
              header.getFields().put(keyval[i], keyval[i + 1]);
            }
          }
          return header;
        } else {
          System.err.println(commandExecutor.getStandardErrorFromCommand().toString());
          return null;
        }
      } else {
        System.err.println(commandExecutor.getStandardErrorFromCommand().toString());
        return null;
      }
    } catch (IOException | InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
}
