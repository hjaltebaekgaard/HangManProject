package com.example.hvalfisk.notsosuckyhangman;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HangManApplication extends Application {

    public static HangManLogic gameLogic;
    public static ArrayList<String> knownUsersList;
    public static ArrayList<User> users;

    public static final String SHARED_PREFERENCES = "sharedPreferences";
    public static final String KNOWN_USERS = "knownUserNames";
    public static final String USERS = "users";

    @Override
    public void onCreate() {
        super.onCreate();

        gameLogic = new HangManLogic();

        System.out.println("DEBUG: loadUserData() begin");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        if(knownUsersList==null) {
            knownUsersList = new ArrayList<String>();
        }
        String names = sharedPreferences.getString(KNOWN_USERS, null);
        System.out.println("DEBUG:" + names);
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

    }
}
