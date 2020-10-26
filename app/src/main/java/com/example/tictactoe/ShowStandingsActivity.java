package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class ShowStandingsActivity extends AppCompatActivity
{
    private ArrayAdapter<String> adapter;
    private String PREF_NAME = "PlayerNames";
    private SharedPreferences playerNames;
    private SharedPreferences.Editor editor;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_standings);
        playerNames = getSharedPreferences(PREF_NAME,0);
        editor = playerNames.edit();
        listView = this.findViewById(R.id.listView_standings);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        //This method gets all the entries in the standingsPrefs SharedPreferences
        Map<String, ?> allEntries = playerNames.getAll();

        //Initialize the playerNamesArray to the size of SharedPreferences found
        ArrayList<LeaderBoardEntry> playerNamesArray = new ArrayList<>();

        int count = 0;
        for (Map.Entry<String, ?> entry : allEntries.entrySet())
        {
            LeaderBoardEntry scoreEntry = new LeaderBoardEntry(entry.getKey().toString(),Integer.parseInt(entry.getValue().toString()));
            playerNamesArray.add(scoreEntry);
            count++;
        }

        Collections.sort(playerNamesArray);

        ArrayList<String> tempStringArray = new ArrayList<>();
        for(int i=0;i<playerNamesArray.size();i++)
        {
            tempStringArray.add(playerNamesArray.get(i).playerName + " has " + playerNamesArray.get(i).playerScore + " Win(s)");
        }

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,tempStringArray);
        listView.setAdapter(adapter);
    }
}

//This class holds the player information in one place
//Allows for comparing between values as well to see if identical exists mostly
class LeaderBoardEntry implements Comparable<LeaderBoardEntry>
{
    public String playerName;
    public int playerScore;

    public LeaderBoardEntry(String name, int score)
    {
        playerName = name;
        playerScore = score;
    }

    private int getScore()
    {
        return playerScore;
    }

    @Override
    public int compareTo(LeaderBoardEntry compareEntry)
    {
        int compareScore=(compareEntry).getScore();

        /* For Ascending order*/
        //return this.playerScore-compareScore;

        /* For Descending order do like this */
        return compareScore-this.playerScore;
    }
}