package com.example.hvalfisk.notsosuckyhangman;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import static com.example.hvalfisk.notsosuckyhangman.MainActivity.gameLogic;


public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    static GameActivity game;
    static AsyncTaskLoadPlayers asyncLoadPlayers;
    static AsyncTaskUpdateUser asyncUpdateUser;

    Button confirmLetter;
    EditText inputLetter;
    TextView hiddenWord;
    TextView infoText;
    TextView guessCount;
    ImageView nooseImage;
    ArrayList<Integer> nooseImages;
    User currentUser;
    String userName;
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

        nooseImages = new ArrayList<Integer>();
        nooseImages.add(R.drawable.galge);
        nooseImages.add(R.drawable.forkert1);
        nooseImages.add(R.drawable.forkert2);
        nooseImages.add(R.drawable.forkert3);
        nooseImages.add(R.drawable.forkert4);
        nooseImages.add(R.drawable.forkert5);


        userName = getIntent().getStringExtra("playerName");

        game = this;


        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {

                if (gameLogic.getUsedLetters().size()==0 || gameLogic.isGameFinished()) {
                    try {
                        gameLogic.getWordsFromDR();
                    } catch (Exception e) {
                        System.out.println("boellehat");
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                infoText.setText("Welcome " + userName);
                setConfirmButtonListener();

                hiddenWord.setText(gameLogic.getVisibleWord());
                if(gameLogic.getUsedLetters().size()!=0) {
                    guessCount.setText("Guesses remaining: " + gameLogic.getRemainingGuesses());
                    nooseImage.setImageResource(nooseImages.get(gameLogic.getAmountWrongLetters()));
                    infoText.setText(gameLogic.getUsedLettersString());
                }

            }
        }.execute();

        if(currentUser==null || !(currentUser.getName().equals(userName))) {

           asyncLoadPlayers = new AsyncTaskLoadPlayers();
           asyncLoadPlayers.execute();
        }

        System.out.println("onCreate is done");
    }

    @Override
    protected void onDestroy() {

        game = null;
        super.onDestroy();
    }


    private static User getUser(String userName) {

        for(User user:MainActivity.users) {
            if(user.getName().equals(userName)) {
                return user;
            }
        }
        return null;
    }
    private static int getUserIndex(String userName) {
        int index = 0;
        for(User user: MainActivity.users) {
            if(user.getName().equals(userName)) {
                index = MainActivity.users.indexOf(user);
            }
        }
        return index;

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

        System.out.println("user array has size: "+MainActivity.users.size());

        if(prepareRerun) {
            reRun();

        }


        if (gameLogic.isGameWon()) {
            guessCount.setText("You found the word!");
            nooseImage.setImageResource(R.drawable.medal);
            confirmLetter.setText("Try again");
            currentUser.setCurrentStreak(currentUser.getCurrentStreak()+1);
            if(currentUser.getHighestStreak()<currentUser.getCurrentStreak()) {
                currentUser.setHighestStreak(currentUser.getCurrentStreak());
            }
            prepareRerun = true;
            asyncUpdateUser = new AsyncTaskUpdateUser();
            asyncUpdateUser.execute();

        } else if (gameLogic.isGameLost()) {
            guessCount.setText("No pardons this time!");
            nooseImage.setImageResource(R.drawable.forkert6);
            hiddenWord.setText(gameLogic.getWord());
            confirmLetter.setText("Try again");
            prepareRerun = true;
            currentUser.setCurrentStreak(0);
            asyncUpdateUser = new AsyncTaskUpdateUser();
            asyncUpdateUser.execute();
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

    private static class AsyncTaskLoadPlayers extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            if(MainActivity.knownUsersList.contains(game.userName)) {
                System.out.println("contained username");
                game.currentUser = getUser(game.userName);
            } else {
                System.out.println("did not contain username");
                game.currentUser = new User(game.userName);
                MainActivity.knownUsersList.add(game.userName);
                MainActivity.users.add(game.currentUser);
            }

            return null;
        }
    }

    private static class AsyncTaskUpdateUser extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            MainActivity.users.set(getUserIndex(game.userName),game.currentUser);

            return null;
        }


    }

}


