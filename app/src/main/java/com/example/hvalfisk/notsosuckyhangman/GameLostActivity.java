package com.example.hvalfisk.notsosuckyhangman;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameLostActivity extends AppCompatActivity implements View.OnClickListener {

    Button confirmLetter;
    TextView hiddenWord;
    TextView infoText;
    ImageView gameOverImage;
    String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finished_game);

        hiddenWord = findViewById(R.id.gameWord);
        confirmLetter = findViewById(R.id.playAgain);
        infoText = findViewById(R.id.finishedGameSpeech);
        gameOverImage = findViewById(R.id.gameOverImage);



        hiddenWord.setText(getIntent().getStringExtra("Word"));

        Bundle user = getIntent().getBundleExtra("CurrentUser");
        int currentStreak = user.getInt("CurrentStreak");
        int highestStreak = user.getInt("HighestStreak");
        userName = user.getString("UserName");

        gameOverImage.setImageResource(R.drawable.forkert6);

        infoText.setText(R.string.game_lost_info);

        confirmLetter.setOnClickListener(this);
        confirmLetter.setText(R.string.try_again);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.loser_sound);
        mp.start();

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.playAgain) {
            Intent newGame = new Intent(this, GameActivity.class);
            newGame.putExtra("playerName", userName);

            this.finish();
            startActivity(newGame);
        }
    }
}
