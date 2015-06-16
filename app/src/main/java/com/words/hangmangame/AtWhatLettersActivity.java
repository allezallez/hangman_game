package com.words.hangmangame;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.ArrayList;

public class AtWhatLettersActivity extends AppCompatActivity {

  private RadioButton[] letter_position_buttons = new RadioButton[15];
  private Button doneButton;

  private ArrayList<Word> vocabulary = new ArrayList<>();
  private boolean[] positions;
  private String partialWord;
  private char lastGuess;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    System.out.println("AtWhatLetters onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_at_what_letters);
    Intent intent = getIntent();

    partialWord = intent.getStringExtra("partial_word");
    lastGuess = intent.getCharExtra("last_guess", '?');

    positions = new boolean[partialWord.length()];

    for (int i = 0; i < partialWord.length(); i++) {
      final int j = i + 1;
      int id = getResources().getIdentifier("radio_" + j, "id", getPackageName());
      letter_position_buttons[i] = (RadioButton) findViewById(id);
      letter_position_buttons[i].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          positions[j - 1] = true;
        }
      });
    }

    for (int i = 0; i < 15; i++) {
      final int j = i + 1;
      int id = getResources().getIdentifier("radio_" + j, "id", getPackageName());
      letter_position_buttons[i] = (RadioButton) findViewById(id);
      if (i >= partialWord.length() || partialWord.charAt(i) != ' ') {
        letter_position_buttons[i].setVisibility(View.GONE);
      }
    }
    doneButton = (Button) findViewById(R.id.done_button);
    final AtWhatLettersActivity finalThis = this;
    doneButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int i = 0;
        for (boolean position : positions) {
          if (position) {
            partialWord = partialWord.substring(0,i) + lastGuess + partialWord.substring(i+1);
          }
          i++;
        }
        startActivity(new Intent(finalThis, HangmanGameActivity.class)
            .putExtra("partial_word", partialWord));
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_at_what_letters, menu);
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
}
