package emailheaderinformation.parser;

class StringChunker {
  private String[] words;
  private int position = 0;

  StringChunker(String s) {
    words = s.split("\\s+");
  }

  boolean hasNext() {
    return words.length > position;
  }

  String peek() {
    return words[position];
  }

  String next() {
    return words[position++];
  }
}
