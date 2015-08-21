package com.words.hangmangame;

import java.util.Deque;
import java.util.Map;
import java.util.TreeMap;

public class HangmanDataHolder {

  public static TreeMap<Integer, String> stlTopWords;

//  public static Deque<Map<String, Integer>> vocabularyStack;
//  public static Deque<Character> guessStack;
//  public static Deque<String> partialWordStack;
  public static Deque<HangmanGameState> stateStack;
}
