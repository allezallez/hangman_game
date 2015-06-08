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


public class AtWhatLettersActivity extends AppCompatActivity {

  private RadioButton[] letter_position_buttons = new RadioButton[15];
  private Button doneButton;

  private boolean[] positions;
  private int wordLength;
  private String partialWord;
  private char lastGuess;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_at_what_letters);
    Intent intent = getIntent();

    wordLength = intent.getIntExtra("word_length", 34);
    partialWord = intent.getStringExtra("the_word");
    lastGuess = intent.getCharExtra("last_guess", '?');
    positions = new boolean[partialWord.length()];

    for (int i = 0; i < wordLength; i++) {
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

    for (int i = wordLength; i < 15; i++) {
      final int j = i + 1;
      int id = getResources().getIdentifier("radio_" + j, "id", getPackageName());
      letter_position_buttons[i] = (RadioButton) findViewById(id);
      letter_position_buttons[i].setVisibility(View.GONE);
    }
    doneButton = (Button) findViewById(R.id.done_button);
    final AtWhatLettersActivity finalThis = this;
    doneButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(finalThis, HangmanGameActivity.class);
        intent.putExtra("positions", positions)
            .putExtra("word_length", wordLength)
            .putExtra("the_word", partialWord)
            .putExtra("last_guess", lastGuess);
        startActivity(intent);
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
