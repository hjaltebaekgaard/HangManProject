package com.example.hvalfisk.notsosuckyhangman;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.hvalfisk.notsosuckyhangman.MainActivity.KNOWN_USERS;
import static com.example.hvalfisk.notsosuckyhangman.MainActivity.SHARED_PREFERENCES;
import static com.example.hvalfisk.notsosuckyhangman.MainActivity.USERS;
import static com.example.hvalfisk.notsosuckyhangman.MainActivity.gameLogic;
import static com.example.hvalfisk.notsosuckyhangman.MainActivity.knownUsersList;
import static com.example.hvalfisk.notsosuckyhangman.MainActivity.users;


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
        if(users==null || users.size()!=knownUsersList.size() || currentUser==null) {
            loadUserData();
        }


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
        saveUserData();
        game = null;
        super.onDestroy();
    }


    private static User getUser(String userName) {

        System.out.println("getUser visit");
        for(User user:users) {
            System.out.println("getUser: "+user.getName());
        }
        for(User user:users) {
            if(user.getName().equals(userName)) {
                return user;
            }
        }
        return null;
    }
    private static int getUserIndex(String userName) {
        int index = 0;
        for(User user: users) {
            if(user.getName().equals(userName)) {
                index = users.indexOf(user);
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

        System.out.println("user array has size: "+users.size());

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

    void loadUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        if(MainActivity.knownUsersList==null) {
            MainActivity.knownUsersList = new ArrayList<String>();
        }
        String names = sharedPreferences.getString(KNOWN_USERS, null);
        System.out.println(names);
        if(names!=null) {
            String[] knownUsersTemp = names.split(",");
            for (String userName : knownUsersTemp) {
                if(!(MainActivity.knownUsersList.contains(userName)&&userName.length()>0)) {
                    MainActivity.knownUsersList.add(userName);
                }
            }
        }
        Gson gson = new Gson();
        String json = sharedPreferences.getString(USERS,null);
        System.out.println("loadData json "+json);
        Type type = new TypeToken<ArrayList<User>>() {}.getType();
        users = gson.fromJson(json,type);

        if(users==null) {
            users = new ArrayList<>();
        }
    }
    private void saveUserData() {
        System.out.println("saveUserData was called in GameActivity");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println("Am I called "+buildString(knownUsersList));
        if(knownUsersList!=null) {
            System.out.println("Am I called "+knownUsersList.size());
        }
        editor.putString(KNOWN_USERS,buildString(knownUsersList));
        System.out.println();
        Gson gson = new Gson();
        String json = gson.toJson(users);
        System.out.println("saveData json: "+json);
        editor.putString(USERS,json);

        editor.apply();
    }
    private String buildString(ArrayList<String> knownUsersList) {

        if(knownUsersList!=null) {
            StringBuilder stringBuilder = new StringBuilder();
            System.out.println("stringbuilder length: "+stringBuilder.length());
            for (String userName : knownUsersList) {
                stringBuilder.append(userName);
                stringBuilder.append(",");
            }
            return stringBuilder.toString();
        }
        return null;
    }

    private static class AsyncTaskLoadPlayers extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            if(MainActivity.knownUsersList.contains(game.userName)) {
                System.out.println("contained username");
                game.currentUser = getUser(game.userName);
            } else {
                System.out.println("did not contain username: " + game.userName);
                System.out.println("did not, but did: "+knownUsersList.size()+" "+users.size());
                game.currentUser = new User(game.userName);
                System.out.println("did not, but did: "+game.currentUser.getName());
                MainActivity.knownUsersList.add(game.userName);
                users.add(game.currentUser);
                System.out.println("did not, but did: "+knownUsersList.size()+" "+users.size());
            }

            return null;
        }
    }

    private static class AsyncTaskUpdateUser extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            users.set(getUserIndex(game.userName),game.currentUser);

            return null;
        }


    }

}


