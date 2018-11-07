package com.example.hvalfisk.notsosuckyhangman;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.hvalfisk.notsosuckyhangman.MainActivity.gameLogic;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    Button confirmLetter;
    EditText inputLetter;
    TextView hiddenWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        hiddenWord = findViewById(R.id.hiddenWord);
        inputLetter = findViewById(R.id.inputLetter);
        confirmLetter = findViewById(R.id.confirmLetter);

        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    gameLogic.getWordsFromDR();
                } catch (Exception e) {
                    System.out.println("boellehat");
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                setConfirmButtonListener();
            }
        }.execute();


    }

    private void setConfirmButtonListener() {
        confirmLetter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        gameLogic.guessLetter(inputLetter.getText().toString());
        inputLetter.setText("");
        hiddenWord.setText(gameLogic.getVisibleWord());
    }
}
