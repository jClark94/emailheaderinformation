package emailheaderinformation.model;

import static emailheaderinformation.model.Impact.Value;

public class ImpactBuilder {
  private Value availability;
  private Value confidentiality;
  private Value integrity;

  public ImpactBuilder setAvailability (Value availability) {
    this.availability = availability;
    return this;
  }

  public ImpactBuilder setConfidentiality (Value confidentiality) {
    this.confidentiality = confidentiality;
    return this;
  }

  public ImpactBuilder setIntegrity (Value integrity) {
    this.integrity = integrity;
    return this;
  }

  public Impact createImpact () {
    return new Impact(availability, confidentiality, integrity);
  }
}