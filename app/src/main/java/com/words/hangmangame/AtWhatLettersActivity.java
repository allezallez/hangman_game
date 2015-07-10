package com.words.hangmangame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class AtWhatLettersActivity extends AppCompatActivity {

  private static final int MAX_WORD_LENGTH = 34;

  private RadioButton[] letter_position_buttons = new RadioButton[MAX_WORD_LENGTH];
  private Button doneButton;
  private boolean[] positions;
  private char lastGuess;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    System.out.println("AtWhatLetters onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_at_what_letters);
    Intent intent = getIntent();

    lastGuess = intent.getCharExtra("last_guess", '?');

    positions = new boolean[HangmanDataHolder.partialWordStack.peek().length()];

    for (int i = 0; i < HangmanDataHolder.partialWordStack.peek().length(); i++) {
      final int j = i + 1;
      int id = getResources().getIdentifier("radio_" + j, "id", getPackageName());
      letter_position_buttons[i] = (RadioButton) findViewById(id);
      final RadioButton button = (RadioButton) findViewById(id);
      letter_position_buttons[i].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          button.setChecked(!positions[j - 1]);
          positions[j - 1] = !positions[j - 1];
        }
      });
    }

    for (int i = 0; i < MAX_WORD_LENGTH; i++) {
      final int j = i + 1;
      int id = getResources().getIdentifier("radio_" + j, "id", getPackageName());
      letter_position_buttons[i] = (RadioButton) findViewById(id);
      if (i >= HangmanDataHolder.partialWordStack.peek().length()
          || HangmanDataHolder.partialWordStack.peek().charAt(i) != ' ') {
        letter_position_buttons[i].setVisibility(View.GONE);
      }
    }
    doneButton = (Button) findViewById(R.id.done_button);
    final AtWhatLettersActivity finalThis = this;
    doneButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        doneButton.setVisibility(View.GONE);
        String newWord = HangmanDataHolder.partialWordStack.peek();
        int i = 0;
        for (boolean position : positions) {
          if (position) {
            newWord = newWord.substring(0,i) + lastGuess + newWord.substring(i+1);
          }
          i++;
        }
        HangmanDataHolder.partialWordStack.push(newWord);
        startActivity(new Intent(finalThis, HangmanGameActivity.class));
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
