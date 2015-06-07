package com.words.hangmangame;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class HangmanGameFlow {

    private Word[] vocab;
    private ArrayList<Word> new_vocab;
    int totalpop;

    public char choose(ArrayList<Character> guesses, Word partial) {
        TreeSet<Word> top_words = new TreeSet<Word>();
        //compset holds letters with their probabilites
        Pair[] compset = new Pair[26];

        //temp variables
        double prob;
        Pair temp_pair;
        char letter;
        boolean run_vocab = true;

        //init top_words with very unlikely blanks
        for (int i = 0; i < 5; i++)
            top_words.add(new Word(" ", 0, ((double) i / (double) 999999999)));

        for (int i = 0; i < 26; i++) {    //for each letter in alphabet
            letter = (char) (i + 65);

            //predictive calculates probability of letter
            prob = predictive (guesses, partial, letter, top_words, run_vocab);
            temp_pair = new Pair(prob, letter);   //pairs prob' with letter
            compset[i] = temp_pair;                 //stores in compset

            //output probability of each letter
            System.out.println(letter + ": " + prob);
            run_vocab = false;                      //saves time
        }

        //output most likely words
        for (Word it : top_words)
            System.out.println(it.getWord() + ": " + it.getPosterior());

        //while sorting is over-kill, compset is only 26 letters long
        Arrays.sort(compset);
        return (compset[0].get_letter());
    }

    /*
     * predictive method
     * INPUT:     guesses: letters that have already been guessed
     *            partial: current partial word guess
     *            letter:  current letter whose probability is being calc'ed
     *            top_words:  most likely words in dataset
     *            run_vocab:  whether or not to run calc's on all words
     * */
    public double predictive(ArrayList<Character> guesses, Word partial
            , char letter, TreeSet<Word> top_words, boolean run_vocab) {
        //temp variables
        boolean checker;
        double sum_prob = 0;
        long sum_count = 0;
        Word temp_word;
        double post = 0;
        int j = 0;

        //current most likely words (used to prevent duplicates)
        HashSet<String> tops = new HashSet<String>();

        if (run_vocab)                //if we are running calcs
            for (Word tw: top_words)    //then store all current top words
                tops.add(tw.getWord());

        for (Word cw: vocab) {        //for every word in the vocab
            if (compatible(cw, guesses, partial)) { //check compatible
                sum_count += cw.getCount();//add its count to the sum
                new_vocab.add(cw);        //add to new vocab
            }
        }

        vocab = new Word[ new_vocab.size() ];   //clear vocab
        int k = 0;
        for (Word w : new_vocab) {            //update vocab
            vocab[k] = w;
            k++;
        }
        new_vocab.clear();                      //clear temp array

        if (run_vocab) {              //if we are runniny calcs
            for (Word cw : vocab) {     //recalc all posterior prob's
                post = ((double) cw.getCount()) / (double) sum_count;
                cw.setPosterior(post);

                //check if current word needs to be in top words
                if (!tops.contains(cw.getWord())) {
                    temp_word = top_words.last();
                    if (post >= temp_word.getPosterior()) {
                        top_words.remove(temp_word);
                        top_words.add(cw);
                    }
                }
            }
        }

        for (Word cw : vocab) {
            checker = false;
            for (int i = 0; i < partial.getLength(); i++) {
                if ((partial.letterAt(i) == ' ') &&
                        (cw.letterAt(i) == letter)) {
                    checker = true;
                }
            }

            if (checker)
                sum_prob += cw.getPosterior();
        }
        return sum_prob;
    }

  public boolean compatible(Word w, ArrayList<Character> guesses, Word partial) {
    for (int i = 0; i < partial.getLength(); i++) {
      if ((partial.letterAt(i) != ' ') &&
          (partial.letterAt(i) != w.letterAt(i))) {
        return false;
      }
    }
    for (int i = 0; i < partial.getLength(); i++) {
      if (partial.letterAt(i) == ' ') {
        for (Character guess : guesses) {
          if (((char) guess) == w.letterAt(i))
            return false;
        }
      }
    }
    return true;
  }
}