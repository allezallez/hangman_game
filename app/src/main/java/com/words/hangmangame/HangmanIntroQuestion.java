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


public class HangmanIntroQuestion extends AppCompatActivity {

  private static final int MAX_WORD_LENGTH = 34;

  private EditText numberOfLetters;
  private Button goButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hangman_intro_question);
    numberOfLetters = (EditText) findViewById(R.id.number_of_letters);
    goButton = (Button) findViewById(R.id.go_button);

    // Setup event handlers
    goButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        // "Play!" button:
        System.out.println("there");
        switchToGame(Integer.parseInt(numberOfLetters.getText().toString()));
      }
    });
    numberOfLetters.setOnKeyListener(new View.OnKeyListener() {
      public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
          // keyboard enter button:
          System.out.println("here");
          switchToGame(Integer.parseInt(numberOfLetters.getText().toString()));
          return true;
        }
        return false;
      }
    });
  }

  public void switchToGame(int numberOfLetters) {
    Intent intent = new Intent(this, HangmanGameActivity.class);
    intent.putExtra("number", numberOfLetters);
    startActivity(intent);
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

  @Override
  protected void onPause () {
    super.onPause();
    System.out.println("wtf");
  }
}
