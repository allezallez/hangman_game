package com.words.hangmangame;

import android.os.Parcel;
import android.os.Parcelable;

public class Word implements Comparable<Word>, Parcelable {

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

  public int getLength() {
    return word.length();
  }

  public char letterAt(int index) {
    return word.charAt(index);
  }

  public void setWord(String word) {
    this.word = word;
  }

  public String getWord() {
    return word;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public double getPosterior() {
    return probability;
  }

  public void setPosterior(double probability) {
    this.probability = probability;
  }

  public void setLetterAt(char letter, int index) {
    this.word = word.substring(0, index) + letter + word.substring(index+1);
  }

  public int compareTo(Word other) {
    if (this.getPosterior() > other.getPosterior()) return -1;
    if (this.getPosterior() == other.getPosterior()) return 0;
    return 1;
  }


  protected Word(Parcel in) {
    count = in.readInt();
    probability = in.readDouble();
    word = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(count);
    dest.writeDouble(probability);
    dest.writeString(word);
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>() {
    @Override
    public Word createFromParcel(Parcel in) {
      return new Word(in);
    }

    @Override
    public Word[] newArray(int size) {
      return new Word[size];
    }
  };
}