package emailheaderinformation.database;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.mongodb.client.model.Filters.*;

public class DbManager {
  private MongoCollection<Document> coll;

  public DbManager () {
    init();
  }

  private void init () {
    MongoClient mc = new MongoClient();
    MongoDatabase db = mc.getDatabase("cvedb");
    coll = db.getCollection("cves");
  }

  public Stream<String> findVulnerabilitiesForProduct (String product) {
    List<String> vulns = new ArrayList<>();
    findVulnerabilities(product).forEach((Block<? super Document>) document -> {
      vulns.add(document.toJson());
    });
    return vulns.parallelStream();
  }

  /**
   * Finds all the available vulnerabilities for a given product that do not require local access in
   * order to execute
   *
   * @param product
   *     the name of the product to lookup
   *
   * @return an iterable collection of vulnerabilities
   */
  private FindIterable<Document> findVulnerabilities (String product) {
    return coll.find(and(ne("access.network", "LOCAL"), regex("vulnerable_configuration", product)))
               .projection(Projections.excludeId());
  }
}
