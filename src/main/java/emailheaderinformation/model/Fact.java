package emailheaderinformation.model;

public class Fact {
  private final String factClass;
  private final String factType;
  private final String factDetails;

  public Fact (String factClass, String factType, String factDetails) {
    this.factClass = factClass;
    this.factType = factType;
    this.factDetails = factDetails;
  }

  public String getFactClass () {
    return factClass;
  }

  public String getFactType () {
    return factType;
  }

  public String getFactDetails () {
    return factDetails;
  }
}
