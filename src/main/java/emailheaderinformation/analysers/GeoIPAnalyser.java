package emailheaderinformation.analysers;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.DatabaseReader.Builder;
import com.maxmind.geoip2.model.CityResponse;
import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

import java.io.File;
import java.net.InetAddress;

public class GeoIPAnalyser extends HeaderAnalyser<String> {

  private final String mAddress;

  public GeoIPAnalyser (Header header, MainWindow mainWindow, String ipAddress) {
    super(header, mainWindow);
    mAddress = ipAddress;
  }

  @Override public String call () {
    Builder builder = new Builder(new File(GeoIPAnalyser.class.getResource("/GeoLite2-City.mmdb")
                                               .getFile()));
    try (DatabaseReader dbr = builder.build()) {
      CityResponse city = dbr.city(InetAddress.getByName(mAddress));
      return String.format("%s,%s",
                           city.getLocation().getLongitude(),
                           city.getLocation().getLatitude());
    } catch (Exception ignored) {
      ignored.printStackTrace();
    }
    return null;
  }
}
