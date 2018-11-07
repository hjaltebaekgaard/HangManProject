package com.example.hvalfisk.notsosuckyhangman;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.hvalfisk.notsosuckyhangman.MainActivity.gameLogic;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    Button confirmLetter;
    EditText inputLetter;
    TextView hiddenWord;
    TextView infoText;
    TextView guessCount;
    ImageView nooseImage;
    ArrayList<Integer> nooseImages;
    boolean prepareRerun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        hiddenWord = findViewById(R.id.hiddenWord);
        inputLetter = findViewById(R.id.inputLetter);
        confirmLetter = findViewById(R.id.confirmLetter);
        infoText = findViewById(R.id.infoText);
        guessCount = findViewById(R.id.guessCount);
        nooseImage = findViewById(R.id.nooseImage);
        prepareRerun = false;

        /* TODO
            make better async */
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    gameLogic.getWordsFromDR();
                } catch (Exception e) {
                    System.out.println("boellehat");
                    e.printStackTrace();
                }

                nooseImages = new ArrayList<Integer>();
                nooseImages.add(R.drawable.galge);
                nooseImages.add(R.drawable.forkert1);
                nooseImages.add(R.drawable.forkert2);
                nooseImages.add(R.drawable.forkert3);
                nooseImages.add(R.drawable.forkert4);
                nooseImages.add(R.drawable.forkert5);


                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                setConfirmButtonListener();
                hiddenWord.setText(gameLogic.getVisibleWord());

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
        infoText.setText(gameLogic.getUsedLettersString());

        if(prepareRerun) {
            reRun();

        }


        if (gameLogic.isGameWon()) {
            guessCount.setText("You found the word!");
            nooseImage.setImageResource(R.drawable.medal);
            confirmLetter.setText("Try again");
            prepareRerun = true;
        } else if (gameLogic.isGameLost()) {
            guessCount.setText("No pardons this time!");
            nooseImage.setImageResource(R.drawable.forkert6);
            hiddenWord.setText(gameLogic.getWord());
            confirmLetter.setText("Try again");
            prepareRerun = true;
        } else {
            guessCount.setText("Guesses remaining: " + gameLogic.getRemainingGuesses());
            nooseImage.setImageResource(nooseImages.get(gameLogic.getAmountWrongLetters()));
        }


    }

    private void reRun() {
        gameLogic.reset();
        hiddenWord.setText(gameLogic.getVisibleWord());
        confirmLetter.setText(R.string.game_confirm_button);
        infoText.setText(R.string.game_info_text_rerun);
        guessCount.setText(R.string.clear);
        prepareRerun = false;
    }

}


