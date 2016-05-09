package emailheaderinformation.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaclark on 03/05/16.
 */
public class FoundInformation {
  private String name;
  private String software;
  private List<Fact> facts = new ArrayList<>();
  private List<Username> usernameList = new ArrayList<Username>();

  public List<Username> getUsernameList () {
    return usernameList;
  }

  public void addUsername (String context, String username) {
    usernameList.add(new Username(context, username));
  }

  public String getName () {
    return name == null ? "Not found" : name;
  }

  public void setName (String name) {
    System.out.printf("Name: %s%n", name);
    this.name = name;
  }

  public void addFact (String factClass, String factType, String factDetails) {
    facts.add(new Fact(factClass, factType, factDetails));
  }

  public List<Fact> getFacts () {
    return facts;
  }

  public String getSoftware () {
    return software;
  }

  public void setSoftware (String software) {
    this.software = software;
  }
}
