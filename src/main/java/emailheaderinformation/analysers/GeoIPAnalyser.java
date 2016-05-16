package emailheaderinformation.analysers;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.DatabaseReader.Builder;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

class GeoIPAnalyser extends HeaderAnalyser<String> {

  static final String NO_RESULT_FOUND = "NO_RESULT_FOUND";
  private final String mAddress;

  GeoIPAnalyser (Header header, MainWindow mainWindow, String ipAddress) {
    super(header, mainWindow);
    mAddress = ipAddress;
  }

  @Override public String call () {
    Builder builder = new Builder(new File(GeoIPAnalyser.class.getResource("/GeoLite2-City.mmdb")
                                               .getFile()));
    try (DatabaseReader dbr = builder.build()) {
      CityResponse city = dbr.city(InetAddress.getByName(mAddress));
      return String.format("%s,%s",
                           city.getLocation().getLatitude(),
                           city.getLocation().getLongitude());
    } catch (AddressNotFoundException anfe) {
      return NO_RESULT_FOUND;
    } catch (IOException | GeoIp2Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
