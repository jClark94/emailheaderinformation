package emailheaderinformation.model;

public class Impact {
  private Value availability;
  private Value confidentiality;
  private Value integrity;

  Impact(Value availability, Value confidentiality, Value integrity) {
    this.availability = availability;
    this.confidentiality = confidentiality;
    this.integrity = integrity;
  }

  @Override
  public String toString() {
    return String.format(
        "%s,%s,%s",
        availability.toString(),
        confidentiality.toString(),
        integrity.toString());
  }

  public Value getConfidentiality() {
    return confidentiality;
  }

  public Value getIntegrity() {
    return integrity;
  }

  public Value getAvailability() {
    return availability;
  }

  public enum Value {NONE, PARTIAL, COMPLETE;}
}
