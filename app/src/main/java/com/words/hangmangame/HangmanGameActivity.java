package com.words.hangmangame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;


public class HangmanGameActivity extends AppCompatActivity {

  @Override
  public void onBackPressed() {
    System.out.println("GOING BACK!!!");
    System.out.println("Popping: " + HangmanDataHolder.vocabularyStack.pop().size());
    System.out.println("Popping: " + HangmanDataHolder.guessStack.pop());
    System.out.println("Popping: " + HangmanDataHolder.partialWordStack.pop());
    System.out.println("Top Dictionary Size: " + HangmanDataHolder.vocabularyStack.peek().size());
    System.out.println("Total guesses " + HangmanDataHolder.guessStack);
    super.onBackPressed();
  }

  private static final int NUMBER_OF_WORDS = 5;

  private static Thread dataLoader;

  TextView currentWordDisplay;
  TextView guessQuestion;
  private Button yesButton;
  private Button noButton;

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

    isFirstRound = intent.getBooleanExtra("is_first_round", false);

    long startTime = System.currentTimeMillis();

    try {
      if (dataLoader != null && dataLoader.isAlive()) {
        dataLoader.join();
      }
    } catch (InterruptedException e) {
      System.out.println(e);
    }

    if (isFirstRound) {
      guess = mostCommonLetter(HangmanDataHolder.partialWordStack.peek().length());
    } else {
      HangmanDataHolder.vocabularyStack.push(HangmanDataHolder.vocabularyStack.peek());
      guess = choose(HangmanDataHolder.partialWordStack.peek());
      System.out.println("Size of Vocab History: " + HangmanDataHolder.vocabularyStack.size());
    }

//    if (HangmanDataHolder.stlVocab.isEmpty() && !isFirstRound) {
    if (HangmanDataHolder.vocabularyStack.isEmpty() && !isFirstRound) {
      ((TextView) findViewById(getResources().getIdentifier("word_1", "id", getPackageName())))
          .setText("No words match this criteria!!!");
    } else {
      int i = NUMBER_OF_WORDS;
      for (Map.Entry<Integer, String> word : HangmanDataHolder.stlTopWords.entrySet()) {
        if (i > 0 ) {
          ((TextView) findViewById(
              getResources().getIdentifier("word_" + i, "id", getPackageName())))
              .setText("Guess #" + i + ": " + word.getValue());
        }
        i--;
      }
    }

    System.out.println("Guess calculation time: " + (System.currentTimeMillis() - startTime));
    System.out.println("and guessed " + guess);
    System.out.println("All Guesses: " + HangmanDataHolder.guessStack);
    if (HangmanDataHolder.vocabularyStack != null
        && HangmanDataHolder.vocabularyStack.peek() != null) {
      System.out.println("Current vocab size: " + HangmanDataHolder.vocabularyStack.peek().size());
    }

    currentWordDisplay.setText(HangmanDataHolder.partialWordStack.peek().replace(' ', '_'));
    guessQuestion.setText("Does your word contain " + guess);
    HangmanDataHolder.guessStack.push(guess);
    HangmanDataHolder.stlTopWords.clear();

    yesButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
//        yesButton.setVisibility(View.GONE);
//        noButton.setVisibility(View.GONE);
        modifyDictionary(HangmanDataHolder.partialWordStack.peek().length(), true, isFirstRound);
      }
    });

    noButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        HangmanDataHolder.partialWordStack.push(HangmanDataHolder.partialWordStack.peek());
//        yesButton.setVisibility(View.GONE);
//        noButton.setVisibility(View.GONE);
        modifyDictionary(HangmanDataHolder.partialWordStack.peek().length(), false, isFirstRound);
      }
    });
  }

  private void modifyDictionary(
      final int wordLength,
      final boolean containsGuess,
      boolean firstRound) {
    if (firstRound) {
      dataLoader = new Thread(new Runnable() {
        public void run() {
          createVocab(Boolean.toString(containsGuess) + Integer.toString(wordLength));
        }
      });
      dataLoader.start();
    }

    startActivity(
        new Intent(this, containsGuess ? AtWhatLettersActivity.class : HangmanGameActivity.class)
            .putExtra("last_guess", guess));
  }

  private void createVocab(String filename) {
    long startTime = System.currentTimeMillis();

    Kryo kryo = new Kryo();

    Input input = new Input(getResources().openRawResource(getResources().getIdentifier(
        filename,
        "raw",
        getPackageName())));
//    HangmanDataHolder.stlVocab = kryo.readObject(input, HashMap.class);
    HangmanDataHolder.vocabularyStack.push(kryo.readObject(input, HashMap.class));
    input.close();

    System.out.println("File read time: " + (System.currentTimeMillis() - startTime));
    System.out.println(HangmanDataHolder.vocabularyStack.peek().size());
  }

  public static char choose(String partial) {
    HashMap<Character, Long> lettersToProbabilities = predictive(partial);
    TreeMap<Long, Character> sortingTree = new TreeMap<>();

    for (Character letter : lettersToProbabilities.keySet()) {
      sortingTree.put(lettersToProbabilities.get(letter), letter);
//      System.out.println(letter + ": " + lettersToProbabilities.get(letter));
    }

    for (Map.Entry<Integer, String> it : HangmanDataHolder.stlTopWords.entrySet()) {
      System.out.println(it.getValue() + ": " + HangmanDataHolder.vocabularyStack.peek().get(it.getValue()));
    }

    return (sortingTree.get(sortingTree.lastKey()));
  }

  private static HashMap<Character, Long> predictive(String partial) {
    HashMap<Character, Long> toReturn = new HashMap<>();
    for (char letter = 'A'; letter <= 'Z'; letter++) {
      toReturn.put(letter, 0L);
    }

    for (Iterator<Map.Entry<String,Integer>> it = HangmanDataHolder.vocabularyStack.peek().entrySet().iterator();
         it.hasNext();) {
      Map.Entry<String, Integer> word = it.next();
      if (!partialCompatibleWithWord(word.getKey(), new ArrayList<>(HangmanDataHolder.guessStack), partial)) {
        it.remove();
      } else {
        Integer probability = HangmanDataHolder.vocabularyStack.peek().get(word.getKey());

        if (HangmanDataHolder.stlTopWords.size() >= NUMBER_OF_WORDS
            && probability >= HangmanDataHolder.stlTopWords.firstKey()) {
          HangmanDataHolder.stlTopWords.remove(HangmanDataHolder.stlTopWords.firstKey());
          HangmanDataHolder.stlTopWords.put(word.getValue(), word.getKey());
        } else if (HangmanDataHolder.stlTopWords.size() < NUMBER_OF_WORDS) {
          HangmanDataHolder.stlTopWords.put(word.getValue(), word.getKey());
        }

        for (char letter = 'A'; letter <= 'Z'; letter++) {
          if (blanksCompatibleWithLetter(partial, word.getKey(), letter)) {
            toReturn.put(letter, toReturn.get(letter) + probability);
          }
        }
      }
    }
    return toReturn;
  }

  private static boolean blanksCompatibleWithLetter(String partial, String word, char letter) {
    for (int i = 0; i < partial.length(); i++) {
      if ((partial.charAt(i) == ' ') && (word.charAt(i) == letter)) {
        return true;
      }
    }
    return false;
  }

  private static boolean partialCompatibleWithWord(
      String fullWord,
      List<Character> guesses,
      String partial) {
    for (int i = 0; i < partial.length(); i++) {
      if (partial.charAt(i) == ' ') {
        for (Character guess : guesses) {
          if (guess.charValue() == fullWord.charAt(i)) {
            return false;
          }
        }
      } else if (partial.charAt(i) != fullWord.charAt(i)) {
        return false;
      }
    }
    return true;
  }

  private static char mostCommonLetter(int length) {
    return (length < 11) ? 'E' : 'I';
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_hangman_game, menu);
    return true;
  }
}
