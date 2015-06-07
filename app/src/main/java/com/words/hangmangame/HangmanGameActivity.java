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


public class HangmanGameActivity extends AppCompatActivity {

  TextView currentWord;
  TextView firstGuess;
  private Button yesButton;
  private Button noButton;

  private int wordLength;
  private String partialWord;
  private final ArrayList<Character> guesses = new ArrayList<>();
  private final ArrayList<Word> vocabulary = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hangman_game);
    Intent intent = getIntent();

    currentWord = (TextView) findViewById(R.id.current_word);
    firstGuess = (TextView) findViewById(R.id.first_guess);

    yesButton = (Button) findViewById(R.id.yes_button);
    noButton = (Button) findViewById(R.id.no_button);

    partialWord = getBlankWord(intent.getIntExtra("number", 0));
    currentWord.setText(partialWord);
    wordLength = partialWord.length();
    
    System.out.println(wordLength);
    System.out.println(mostCommonLetter(wordLength));

    firstGuess.setText("Does your word contain " + mostCommonLetter(wordLength));

    yesButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        createDictionary(wordLength, true);
      }
    });

    noButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        createDictionary(wordLength, false);
      }
    });
  }

  private void createDictionary(int wordLength, boolean containsMostCommonLetter) {
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(
          getResources().openRawResource(getResources().getIdentifier(
              Boolean.toString(containsMostCommonLetter) + Integer.toString(wordLength),
              "raw",
               getPackageName())),
          "UTF-8"));
      while (br.ready()) {
        String[] singleWordData = br.readLine().split("\\s+");
        vocabulary.add(new Word(singleWordData[0], Integer.parseInt(singleWordData[1])));
      }
    } catch (IOException e) {
      System.out.println(e);
    }
    System.out.println(vocabulary.size());
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

  private final int getWordLength() {
    return partialWord.length();
  }

  private static String getBlankWord(int length) {
    String blank = "";
    for (int i = 0; i < length; i++) {
      blank += ' ';
    }
    return blank;
  }

  private static char mostCommonLetter(int length) {
    return (length < 11) ? 'E' : 'I';
  }
}
