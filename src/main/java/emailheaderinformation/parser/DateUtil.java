package emailheaderinformation.parser;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by joshua on 14/11/15.
 */
public class DateUtil {

  // non-standard date formats to take into account spacing introduced in parser
  // module
  private static final SimpleDateFormat rfc822DateFormats[] = new SimpleDateFormat[] {
      new SimpleDateFormat(" EEE , d MMM yy HH : mm : ss z"),
      new SimpleDateFormat(" EEE , d MMM yy HH : mm z"),
      new SimpleDateFormat(" EEE , d MMM yyyy HH : mm : ss z"),
      new SimpleDateFormat(" EEE , d MMM yyyy HH : mm z"),
      new SimpleDateFormat(" d MMM yy HH : mm z"),
      new SimpleDateFormat(" d MMM yy HH : mm : ss z"),
      new SimpleDateFormat(" d MMM yyyy HH : mm z"),
      new SimpleDateFormat(" d MMM yyyy HH : mm : ss z") };

  /**
   * Parse an RFC 822 date string.
   *
   * @param dateString
   *     The date string to parse
   *
   * @return The date, or null if it could not be parsed.
   */
  public static Date parseRfc822DateString (String dateString) {
    Date date = null;
    for (SimpleDateFormat sdf : rfc822DateFormats) {
      try {
        date = sdf.parse(dateString);
      } catch (java.text.ParseException e) {
        // Don't care, we'll just run through all
      }
      if (date != null) {
        return date;
      }
    }
    return null;
  }

}
