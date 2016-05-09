package emailheaderinformation.analysers;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;

/**
 * Created by jaclark on 09/05/16.
 */
public class GeoIPAnalyser extends HeaderAnalyser<String> {
  public GeoIPAnalyser (Header header, MainWindow mainWindow) {
    super(header, mainWindow);
  }

  public static void main(String[] args) {
    GeoIPAnalyser geoIPAnalyser = new GeoIPAnalyser(null, null);
    geoIPAnalyser.call();

  }

  @Override public String call () {
    DatabaseReader.Builder builder = null;
    builder = new DatabaseReader.Builder(new File(GeoIPAnalyser.class.getResource
                                                                          ("/GeoLite2-City" +
                                                                           ".mmdb").getFile()));
    try (DatabaseReader dbr = builder.build()) {
      CityResponse city = dbr.city(InetAddress.getByName("129.67.89.5"));
      System.out.println(city.getCity());
    } catch (Exception ignored) {
      ignored.printStackTrace();
    }

    return null;
  }
}
