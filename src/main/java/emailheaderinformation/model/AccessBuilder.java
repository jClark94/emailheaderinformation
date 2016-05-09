package emailheaderinformation.model;

import emailheaderinformation.model.Access.Authentication;
import emailheaderinformation.model.Access.Complexity;
import emailheaderinformation.model.Access.Vector;

public class AccessBuilder {
  private Authentication authentication;
  private Complexity complexity;
  private Vector vector;

  public AccessBuilder setVector (Vector vector) {
    this.vector = vector;
    return this;
  }

  public AccessBuilder setComplexity (Complexity complexity) {
    this.complexity = complexity;
    return this;
  }

  public AccessBuilder setAuthentication (Authentication authentication) {
    this.authentication = authentication;
    return this;
  }

  public Access createAccess () {
    return new Access(vector, complexity, authentication);
  }
}