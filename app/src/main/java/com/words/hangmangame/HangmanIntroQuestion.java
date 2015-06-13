package com.words.hangmangame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class HangmanIntroQuestion extends AppCompatActivity {

  private EditText wordLength;
  private Button goButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hangman_intro_question);
    wordLength = (EditText) findViewById(R.id.number_of_letters);
    goButton = (Button) findViewById(R.id.go_button);

    HangmanDataHolder.vocab = new ArrayList<>();

    // Setup event handlers
    goButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        switchToGame(wordLength);
      }
    });
    wordLength.setOnKeyListener(new View.OnKeyListener() {
      public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
          switchToGame(wordLength);
          return true;
        }
        return false;
      }
    });
  }

  public void switchToGame(EditText numberOfLettersEditable) {
    int numberOfLetters = Integer.parseInt(numberOfLettersEditable.getText().toString());
    startActivity(new Intent(this, HangmanGameActivity.class)
        .putExtra("last_guess", mostCommonLetter(numberOfLetters))
        .putExtra("the_word", getBlankWord(numberOfLetters))
        .putExtra("guesses", "")
        .putParcelableArrayListExtra("vocab", new ArrayList<Word>()));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_hangman, menu);
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
