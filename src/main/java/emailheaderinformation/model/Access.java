package emailheaderinformation.model;

public class Access {
  private Vector vector;
  private Complexity complexity;
  private Authentication authentication;

  Access (Vector vector, Complexity complexity, Authentication authentication) {
    this.vector = vector;
    this.complexity = complexity;
    this.authentication = authentication;
  }

  public Vector getVector () {
    return vector;
  }

  public Complexity getComplexity () {
    return complexity;
  }

  public Authentication getAuthentication () {
    return authentication;
  }

  public enum Vector {LOCAL, ADJACENT_NETWORK, NETWORK;}

  public enum Complexity {HIGH, MEDIUM, LOW;}

  public enum Authentication {MULTIPLE, SINGLE_INSTANCE, SINGLE, NONE;}
}
