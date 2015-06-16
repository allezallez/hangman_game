package com.words.hangmangame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class HangmanGameActivity extends AppCompatActivity {

  private static final int NUMBER_OF_WORDS = 5;

  TextView currentWordDisplay;
  TextView guessQuestion;
  private Button yesButton;
  private Button noButton;

  private String partialWord;
  private char guess;
  private boolean isFirstRound;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hangman_game);
    Intent intent = getIntent();

    currentWordDisplay = (TextView) findViewById(R.id.current_word);
    guessQuestion = (TextView) findViewById(R.id.first_guess);
    yesButton = (Button) findViewById(R.id.yes_button);
    noButton = (Button) findViewById(R.id.no_button);

    partialWord = intent.getStringExtra("partial_word");
    isFirstRound = intent.getBooleanExtra("is_first_round", false);

    guess = isFirstRound
        ? mostCommonLetter(partialWord.length())
        : choose(partialWord);

    currentWordDisplay.setText(partialWord);
    guessQuestion.setText("Does your word contain " + guess);
    HangmanDataHolder.guesses.add(guess);
    HangmanDataHolder.topWords.clear();

    yesButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        modifyDictionary(partialWord.length(), true, isFirstRound);
      }
    });

    noButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        modifyDictionary(partialWord.length(), false, isFirstRound);
      }
    });

    int i = 0;
    for (Word word : HangmanDataHolder.topWords) {
      if (i < NUMBER_OF_WORDS) {
        ((TextView) findViewById(getResources().getIdentifier("word_" + i, "id", getPackageName())))
            .setText(word.getWord());
      }
      i++;
    }
  }

  private void modifyDictionary(int wordLength, boolean containsGuess, boolean firstRound) {
    System.out.println("containsGuess: " + containsGuess);
    if (firstRound) {
      createVocab(Boolean.toString(containsGuess) + Integer.toString(wordLength));
    }

    startActivity(new Intent(this, containsGuess
            ? AtWhatLettersActivity.class
            : HangmanGameActivity.class)
        .putExtra("partial_word", partialWord)
        .putExtra("last_guess", guess));
  }

  private void createVocab(String filename) {
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(
          getResources().openRawResource(getResources().getIdentifier(
              filename,
              "raw",
              getPackageName())),
          "UTF-8"));
      while (br.ready()) {
        String[] singleWordData = br.readLine().split("\\s+");
        HangmanDataHolder.vocab.add(new Word(singleWordData[0], Integer.parseInt(singleWordData[1])));
      }
    } catch (IOException e) {
      System.out.println(e);
    }
    System.out.println(HangmanDataHolder.vocab.size());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_hangman_game, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public static char choose(String partial) {

    Pair[] compset = new Pair[26];

    //temp variables
    double prob;
    Pair temp_pair;
    char letter;
    boolean run_vocab = true;

    //init top_words with very unlikely blanks
    for (int i = 0; i < 5; i++)
      HangmanDataHolder.topWords.add(new Word(" ", 0, ((double) i / (double) 999999999)));

    for (int i = 0; i < 26; i++) {    //for each letter in alphabet
      letter = (char) (i + 65);

      //predictive calculates probability of letter
      prob = predictive(partial, letter, run_vocab);
      temp_pair = new Pair(prob, letter);   //pairs prob' with letter
      compset[i] = temp_pair;                 //stores in compset

      //output probability of each letter
      System.out.println(letter + ": " + prob);
      run_vocab = false;                      //saves time
    }

    //output most likely words
    for (Word it : HangmanDataHolder.topWords)
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
  private static double predictive(
      String partial,
      char letter,
      boolean run_vocab) {
    System.out.println("In predictive: " + HangmanDataHolder.vocab.size());
    //temp variables
    boolean checker;
    double sum_prob = 0;
    long sum_count = 0;
    Word temp_word;
    double post = 0;
    int j = 0;

    ArrayList<Word> new_vocab = new ArrayList<>();

    //current most likely words (used to prevent duplicates)
    HashSet<String> tops = new HashSet<String>();

    if (run_vocab)                //if we are running calcs
      for (Word tw : HangmanDataHolder.topWords)    //then store all current top words
        tops.add(tw.getWord());

    for (Word cw : HangmanDataHolder.vocab) {        //for every word in the vocab
      if (compatible(cw, HangmanDataHolder.guesses, partial)) { //check compatible
        sum_count += cw.getCount();//add its count to the sum
        new_vocab.add(cw);        //add to new vocab
      }
    }

    HangmanDataHolder.vocab = new ArrayList<Word>(new_vocab.size());   //clear vocab
    for (Word w : new_vocab) {            //update vocab
      HangmanDataHolder.vocab.add(w);
    }
    new_vocab.clear();                      //clear temp array

    if (run_vocab) {              //if we are running calcs
      for (Word cw : HangmanDataHolder.vocab) {     //recalc all posterior prob's
        post = ((double) cw.getCount()) / (double) sum_count;
        cw.setPosterior(post);

        //check if current word needs to be in top words
        if (!tops.contains(cw.getWord())) {
          temp_word = HangmanDataHolder.topWords.last();
          if (post >= temp_word.getPosterior()) {
            HangmanDataHolder.topWords.remove(temp_word);
            HangmanDataHolder.topWords.add(cw);
          }
        }
      }
    }

    for (Word cw : HangmanDataHolder.vocab) {
      checker = false;
      for (int i = 0; i < partial.length(); i++) {
        if ((partial.charAt(i) == ' ') &&
            (cw.letterAt(i) == letter)) {
          checker = true;
        }
      }

      if (checker)
        sum_prob += cw.getPosterior();
    }
    return sum_prob;
  }

  private static boolean compatible(Word w, ArrayList<Character> guesses, String partial) {
    for (int i = 0; i < partial.length(); i++) {
      if ((partial.charAt(i) != ' ') &&
          (partial.charAt(i) != w.letterAt(i))) {
        return false;
      }
    }
    for (int i = 0; i < partial.length(); i++) {
      if (partial.charAt(i) == ' ') {
        for (Character guess : guesses) {
          if (guess.charValue() == w.letterAt(i))
            return false;
        }
      }
    }
    return true;
  }

  private static char mostCommonLetter(int length) {
    return (length < 11) ? 'E' : 'I';
  }
}
