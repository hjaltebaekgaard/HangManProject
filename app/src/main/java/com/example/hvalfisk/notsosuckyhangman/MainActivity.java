package com.example.hvalfisk.notsosuckyhangman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText playerName;
    private Button newGame;
    private Button instructions;
    private Button highscores;
    public static HangManLogic gameLogic;
    public static ArrayList<String> knownUsersList;
    public static ArrayList<User> users;

    public static final String SHARED_PREFERENCES = "sharedPreferences";
    public static final String KNOWN_USERS = "knownUserNames";
    public static final String USERS = "users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("entered onCreate MainActivity");
        gameLogic = new HangManLogic();
        setContentView(R.layout.activity_main);

        playerName = findViewById(R.id.userName);
        newGame = findViewById(R.id.newGame);
        instructions = findViewById(R.id.instructions);
        highscores = findViewById(R.id.highscore);

        newGame.setOnClickListener(this);
        instructions.setOnClickListener(this);
        highscores.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {

        saveUserData();
        super.onDestroy();
    }

    private void saveUserData() {
        System.out.println("saveUserData was called");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println("Am I called "+buildString(knownUsersList));
        if(knownUsersList!=null) {
            System.out.println("Am I called "+knownUsersList.size());
        }
        editor.putString(KNOWN_USERS,buildString(knownUsersList));

        Gson gson = new Gson();
        String json = gson.toJson(users);
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


    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.newGame:
                i = new Intent(this, GameActivity.class);
                i.putExtra("playerName", playerName.getText().toString());
                startActivity(i);
                break;

            case R.id.instructions:
                i = new Intent(this, InstructionsActivity.class);
                startActivity(i);
                break;

            case R.id.highscore:
                i = new Intent(this, HighscoreActivity.class);
                startActivity(i);
                break;

            default:

        }

    }
}
