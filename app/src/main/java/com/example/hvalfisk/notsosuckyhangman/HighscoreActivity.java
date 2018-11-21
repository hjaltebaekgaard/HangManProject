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
        if(!(MainActivity.users==null)) {
            dummyList = new ArrayList<>();
            for(User user: MainActivity.users) {
                dummyList.add(user);
            }
//        System.out.println("dummylist has size: "+dummyList.size());
            highScoreList = new ArrayList<User>();

            while (highScoreList.size() < 10 && 0 < dummyList.size()) {
                //          System.out.println("dummylist has size: "+dummyList.size());
                highScoreList.add(getHighestScorer(dummyList));
                //System.out.println("dummylist has size: "+dummyList.size());
                //System.out.println("highscorelist has size: "+highScoreList.size());
            }

            readHighscoreData(highScoreList);
            if(MainActivity.users.size()==0) {
                System.out.println("DEBUG: somethings wrong with highscore");
            }
        }
    }

    private User getHighestScorer(ArrayList<User> dummyList) {
        User highestScoringUser = new User("dummyUser");
        int index = -1;
        for(User user:dummyList) {
            if(user.getHighestStreak()>=highestScoringUser.getHighestStreak()) {
                highestScoringUser = user;
                ++index;
            }
        }
        System.out.println("dummylist indexof: "+dummyList.indexOf(highestScoringUser));
        return dummyList.remove(index);
    }

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
