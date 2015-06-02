package com.words.hangmangame;

public class Word implements Comparable<Word> {

    private int count;
    private double probability;
    private String word;

    Word() {
        this.count = 0;
        this.word = "";
    }

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

    public void setWord(String new_word) {
        this.word = new_word;
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
        if ( this.getPosterior() > other.getPosterior() ) return -1;
        if ( this.getPosterior() == other.getPosterior() ) return 0;
        return 1;
    }
}