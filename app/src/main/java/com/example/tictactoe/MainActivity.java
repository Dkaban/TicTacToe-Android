package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity
{
    private ArrayAdapter<String> adapter;
    private String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Resources res = getResources();
        data = res.getStringArray(R.array.mainMenuOptions);

        //Allows the listview to be accessible by code
        ListView listView = findViewById(R.id.listView_mainMenuOptions);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_2,android.R.id.text1,data);
        listView.setAdapter(adapter);

        //Listener for detecting clicks on the listview from the user
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                String value = (String)adapter.getItemAtPosition(position);
                openActivity(value);
            }
        });
    }

    private void openActivity(String activityName)
    {
        Intent intent;

        //Switch statement to handle loading a new activity
        switch(activityName)
        {
            case "Enter Names":
                intent = new Intent(this,EnterNamesActivity.class);
                startActivity(intent);
                break;

            case "Play Game":
                intent = new Intent(this,PlayGameActivity.class);
                startActivity(intent);
                break;

            case "Standings":
                intent = new Intent(this,ShowStandingsActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}