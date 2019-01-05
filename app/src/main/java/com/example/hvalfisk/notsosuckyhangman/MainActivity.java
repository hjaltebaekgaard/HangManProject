package com.example.hvalfisk.notsosuckyhangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText playerName;
    private Button newGame;
    private Button instructions;
    private Button highscores;
   /* public static HangManLogic gameLogic;
    public static ArrayList<String> knownUsersList;
    public static ArrayList<User> users;
*/

   /*
    public static final String SHARED_PREFERENCES = "sharedPreferences";
    public static final String KNOWN_USERS = "knownUserNames";
    public static final String USERS = "users";
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("entered onCreate MainActivity");
        //gameLogic = new HangManLogic();
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

        super.onDestroy();
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
