package com.example.hvalfisk.notsosuckyhangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button newGame;
    private Button instructions;
    private Button highscores;
    public static HangManLogic gameLogic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameLogic = new HangManLogic();
        setContentView(R.layout.activity_main);

        newGame = findViewById(R.id.newGame);
        instructions = findViewById(R.id.instructions);
        highscores = findViewById(R.id.highscore);

        newGame.setOnClickListener(this);
        instructions.setOnClickListener(this);
        highscores.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.newGame:
                i = new Intent(this, GameActivity.class);
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
