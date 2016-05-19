package emailheaderinformation.parser;

import java.util.Arrays;

class StringChunker {
  private String[] words;
  private int position = 0;

  StringChunker(String s) {
    words = s.replace(";", " ;").split("\\s+");
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
