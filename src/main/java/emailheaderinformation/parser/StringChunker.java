package emailheaderinformation.parser;

public class StringChunker {
  private String[] words;
  private int position = 0;

  public StringChunker(String s) {
    words = s.split("\\s+");
  }

  public boolean hasNext() {
    return words.length > position;
  }

  public String peek() {
    return words[position];
  }

  public String next() {
    return words[position++];
  }
}
