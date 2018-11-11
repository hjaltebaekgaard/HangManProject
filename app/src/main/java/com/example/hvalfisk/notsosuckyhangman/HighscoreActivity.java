package com.example.hvalfisk.notsosuckyhangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class HighscoreActivity extends AppCompatActivity {

    ArrayList<String> highscoresNameList;
    ArrayList<Integer> highscoresStreakCount;
    ArrayList<User> dummyList;
    ArrayList<User> highScoreList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        dummyList = new ArrayList<User>();
        highScoreList = new ArrayList<User>();

        /*User TheGoat = new User("TheGoat");
        TheGoat.setHighestStreak(33);
        User TheFloat = new User("TheFloat");
        TheFloat.setHighestStreak(1);
        User TheFloatingGoat = new User("TheFloatingGoat");
        TheFloatingGoat.setHighestStreak(10);


        dummyList.add(TheGoat);
        dummyList.add(TheFloat);
        dummyList.add(TheFloatingGoat);
        */

        /*
        dummyList.add(new User("TheGoat"));
        dummyList.add(new User("TheFloat"));
        dummyList.add(new User("TheFloatingGoat"));
        dummyList.add(new User("TheGoatingFloat"));
        dummyList.add(new User("TheLoafingGoat"));
        dummyList.add(new User("QueenElizabeth"));
        dummyList.add(new User("RogerFederer"));
        dummyList.add(new User("DarkWingDuck"));
        dummyList.add(new User("Billy"));
        dummyList.add(new User("Foal"));
        */
        while(highScoreList.size()<10 && 0<dummyList.size()) {
            highScoreList.add(getHighestScorer(dummyList));
        }

        readHighscoreData(highScoreList);
    }

    private User getHighestScorer(ArrayList<User> dummyList) {
        return dummyList.remove(0);
    }


    /* TODO
        make sorting
    */
    private void readHighscoreData(ArrayList<User> users) {

        highscoresNameList = new ArrayList<String>();
        highscoresStreakCount = new ArrayList<Integer>();

        for(User user: users) {
            highscoresNameList.add(user.getName());
            highscoresStreakCount.add(user.getHighestStreak());
        }

        activateHighscoreRecyclerView();
    }

    private void activateHighscoreRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.highscoreRecyclerView);
        HighscoreActivityRecyclerViewAdapter adapter = new HighscoreActivityRecyclerViewAdapter(highscoresNameList, highscoresStreakCount,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}
