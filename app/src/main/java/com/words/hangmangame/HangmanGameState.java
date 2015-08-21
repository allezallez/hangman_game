package com.words.hangmangame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A state of the game.
 */
public class HangmanGameState {

  public HangmanGameState() {
    vocabulary = new HashMap<>();
    cumulativeGuesses = new ArrayList<>();
    partialWord = "";
    atWhatLetters = false;
  }

  public Map<String, Integer> vocabulary;
  public List<Character> cumulativeGuesses;
  public String partialWord;
  public Boolean atWhatLetters;

  @Override
  public String toString() {
    return "Vocabulary size: " + vocabulary.size() + "\n"
        + "Guess: " + cumulativeGuesses  + "\n"
        + "partial word " + partialWord + "\n"
        + "at what letters " + atWhatLetters + "\n";
  }
}
