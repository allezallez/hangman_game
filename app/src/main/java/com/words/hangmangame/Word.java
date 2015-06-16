package com.words.hangmangame;

import android.os.Parcel;
import android.os.Parcelable;

public class Word implements Comparable<Word> {

  private int count;
  private double probability;
  private String word;

  Word(String word, int count) {
    this.count = count;
    this.word = word;
  }

  Word(String word, int count, double probability) {
    this.count = count;
    this.word = word;
    this.probability = probability;
  }

  public char letterAt(int index) {
    return word.charAt(index);
  }

  public String getWord() {
    return word;
  }

  public int getCount() {
    return count;
  }

  public double getPosterior() {
    return probability;
  }

  public void setPosterior(double probability) {
    this.probability = probability;
  }

  public int compareTo(Word other) {
    if (this.getPosterior() > other.getPosterior()) return -1;
    if (this.getPosterior() == other.getPosterior()) return 0;
    return 1;
  }
}