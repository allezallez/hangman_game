package com.words.hangmangame;

public class Pair implements Comparable<Pair> {

    private double prob;
    private char letter;

    public Pair() {
        this.prob = 0;
        this.letter = ' ';
    }

    public Pair(double prob, char letter) {
        this.prob = prob;
        this.letter = letter;
    }

    public char get_letter() {
        return this.letter;
    }

    public double get_prob() {
        return this.prob;
    }

    public void set_prob(double prob) {
        this.prob = prob;
    }

    //NOTE: will result in decreasing sort!!!
    public int compareTo(Pair other) {
        if (this.prob < other.prob) return 1;
        if (this.prob == other.prob) return 0;
        return -1;
    }
}