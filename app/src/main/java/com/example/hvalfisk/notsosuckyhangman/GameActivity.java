package com.example.hvalfisk.notsosuckyhangman;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.hvalfisk.notsosuckyhangman.HangManApplication.KNOWN_USERS;
import static com.example.hvalfisk.notsosuckyhangman.HangManApplication.SHARED_PREFERENCES;
import static com.example.hvalfisk.notsosuckyhangman.HangManApplication.USERS;
import static com.example.hvalfisk.notsosuckyhangman.HangManApplication.gameLogic;
import static com.example.hvalfisk.notsosuckyhangman.HangManApplication.knownUsersList;
import static com.example.hvalfisk.notsosuckyhangman.HangManApplication.users;



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
    Toast toast;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        System.out.println("DEBUG: entered onCreate in GameActivity");

        hiddenWord = findViewById(R.id.hiddenWord);
        inputLetter = findViewById(R.id.inputLetter);
        confirmLetter = findViewById(R.id.confirmLetter);
        infoText = findViewById(R.id.infoText);
        guessCount = findViewById(R.id.guessCount);
        nooseImage = findViewById(R.id.nooseImage);


        nooseImages = new ArrayList<Integer>();
        nooseImages.add(R.drawable.galge);
        nooseImages.add(R.drawable.forkert1);
        nooseImages.add(R.drawable.forkert2);
        nooseImages.add(R.drawable.forkert3);
        nooseImages.add(R.drawable.forkert4);
        nooseImages.add(R.drawable.forkert5);


        userName = getIntent().getStringExtra("playerName");

        game = this;

        if(savedInstanceState==null) {
            gameLogic.reset();
        }

        if(users==null || users.size()!=knownUsersList.size()) {
            if(!(users==null && knownUsersList==null)) {
                System.out.println("DEBUG: " + users.size() + " " + knownUsersList.size());
            }
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
                setOnClickListeners();
                toast = Toast.makeText(GameActivity.this, null, Toast.LENGTH_SHORT);

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

    private void setOnClickListeners() {
        confirmLetter.setOnClickListener(this);
        nooseImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.confirmLetter) {
            Boolean changeNooseImage = gameLogic.guessLetter(inputLetter.getText().toString());
            inputLetter.setText("");
            hiddenWord.setText(gameLogic.getVisibleWord());
            infoText.setText(gameLogic.getUsedLettersString());


            if (gameLogic.isGameWon()) {

                currentUser.setCurrentStreak(currentUser.getCurrentStreak() + 1);
                if (currentUser.getHighestStreak() < currentUser.getCurrentStreak()) {
                    currentUser.setHighestStreak(currentUser.getCurrentStreak());
                }

                asyncUpdateUser = new AsyncTaskUpdateUser();
                asyncUpdateUser.execute();

                Intent gameWon = new Intent(this, GameWonActivity.class);
                Bundle winner = new Bundle();
                winner.putString("UserName", currentUser.getName());
                winner.putInt("CurrentStreak", currentUser.getCurrentStreak());
                winner.putInt("HighestStreak", currentUser.getHighestStreak());

                gameWon.putExtra("CurrentUser", winner);
                gameWon.putExtra("Word", gameLogic.getWord());

                this.finish();
                startActivity(gameWon);

            } else if (gameLogic.isGameLost()) {

                currentUser.setCurrentStreak(0);

                asyncUpdateUser = new AsyncTaskUpdateUser();
                asyncUpdateUser.execute();

                Intent gameLost = new Intent(this, GameLostActivity.class);
                Bundle loser = new Bundle();
                loser.putString("UserName", currentUser.getName());
                loser.putInt("CurrentStreak", currentUser.getCurrentStreak());
                loser.putInt("HighestStreak", currentUser.getHighestStreak());
                gameLost.putExtra("CurrentUser", loser);
                gameLost.putExtra("Word", gameLogic.getWord());

                this.finish();
                startActivity(gameLost);

            } else {

                if (changeNooseImage) {
                    Animation fadeOut = AnimationUtils.loadAnimation(GameActivity.this, R.anim.fade_out);
                    nooseImage.startAnimation(fadeOut);

                    fadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation fadeOut = AnimationUtils.loadAnimation(GameActivity.this, R.anim.fade_in);
                            nooseImage.setImageResource(nooseImages.get(gameLogic.getAmountWrongLetters()));
                            nooseImage.startAnimation(fadeOut);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    guessCount.setText("Guesses remaining: " + gameLogic.getRemainingGuesses());

                }
            }

        }

        if(v.getId()==R.id.nooseImage) {

            toast.setText("Stop it!!!");
            toast.setGravity(Gravity.TOP|Gravity.LEFT,nooseImage.getLeft(),nooseImage.getTop());
            toast.show();

            nooseImage.startAnimation(AnimationUtils.loadAnimation(GameActivity.this, R.anim.tickle));
        }
    }

    void loadUserData() {
        System.out.println("DEBUG: loadUserData() begin");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        System.out.println("DEBUG: whaat");
        if(knownUsersList==null) {
            knownUsersList = new ArrayList<String>();
        }
        String names = sharedPreferences.getString(KNOWN_USERS, null);
        System.out.println(names);
        if(names!=null) {
            String[] knownUsersTemp = names.split(",");
            for (String userName : knownUsersTemp) {
                if(!(knownUsersList.contains(userName)&&userName.length()>0)) {
                    knownUsersList.add(userName);
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
        System.out.println("DEBUG: whaat igen");
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

            if(knownUsersList.contains(game.userName)) {
                System.out.println("contained username");
                game.currentUser = getUser(game.userName);
            } else {
                System.out.println("did not contain username: " + game.userName);
                System.out.println("did not, but did: "+knownUsersList.size()+" "+users.size());
                game.currentUser = new User(game.userName);
                System.out.println("did not, but did: "+game.currentUser.getName());
                knownUsersList.add(game.userName);
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


