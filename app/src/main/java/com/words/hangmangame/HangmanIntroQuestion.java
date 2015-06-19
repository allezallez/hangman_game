package com.words.hangmangame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


public class HangmanIntroQuestion extends AppCompatActivity {

  private EditText wordLengthExitText;
  private Button goButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hangman_intro_question);

    HangmanDataHolder.stlVocab = new HashMap<>();
    HangmanDataHolder.guesses = new ArrayList<>();
    HangmanDataHolder.stlTopWords = new TreeMap<>();

    wordLengthExitText = (EditText) findViewById(R.id.number_of_letters);
    wordLengthExitText.setOnKeyListener(new View.OnKeyListener() {
      public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
          switchToGame(wordLengthExitText.getText().toString());
          return true;
        }
        return false;
      }
    });

    goButton = (Button) findViewById(R.id.go_button);
    goButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        switchToGame(wordLengthExitText.getText().toString());
      }
    });
  }

  public void switchToGame(String numberOfLettersString) {
    startActivity(new Intent(this, HangmanGameActivity.class)
        .putExtra("partial_word", getBlankWord(Integer.parseInt(numberOfLettersString)))
        .putExtra("is_first_round", true));
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

  public static String getBlankWord(int length) {
    String blank = "";
    for (int i = 0; i < length; i++) {
      blank += ' ';
    }
    return blank;
  }
}
