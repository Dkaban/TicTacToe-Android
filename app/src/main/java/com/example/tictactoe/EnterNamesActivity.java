package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EnterNamesActivity extends AppCompatActivity
{
    private String PREF_NAME = "PlayerNames";
    private SharedPreferences playerNames;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_names);

        playerNames = getSharedPreferences(PREF_NAME,0);
        editor = playerNames.edit();
    }

    public void onClickSaveName(View view)
    {
        EditText nameOne = this.findViewById(R.id.editText_playerOneName);
        EditText nameTwo = this.findViewById(R.id.editText_playerTwoName);
        String nameOneString = nameOne.getText().toString();
        String nameTwoString = nameTwo.getText().toString();

        if(nameOneString.equals(""))
        {
            //We can't enter player two's name if we don't have player one's name
            Toast.makeText(this, "You must enter a name for Player One before Player Two", Toast.LENGTH_SHORT).show();
        }
        else
        {
            verifyPlayerName(nameOneString,nameTwoString);
        }
    }

    //This checks to see if the player has entered a name.
    //If only the first name is entered, it's a 1 player game
    //If both names entered, it is a 2 player game.
    private void verifyPlayerName(String nameOne, String nameTwo)
    {
        int numberOfPlayers = 0;
        if(!nameOne.equals(""))
        {
            if(!playerNames.contains(nameOne))
            {
                editor.putInt(nameOne,0);
                editor.commit();
            }

            DataHolder.getInstance().setNameOne(nameOne);
            numberOfPlayers++;
        }

        //Checking for second name entry
        if(!nameTwo.equals(""))
        {
            if(!playerNames.contains(nameTwo))
            {
                editor.putInt(nameTwo,0);
                editor.commit();
            }

            DataHolder.getInstance().setNameTwo(nameTwo);
            numberOfPlayers++;
        }

        DataHolder.getInstance().setNumberOfPlayers(numberOfPlayers);

        //Lets the user know what time of game they will be playing
        if(numberOfPlayers >= 2)
        {
            Toast.makeText(this, "Two Player Game", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "One Player Game", Toast.LENGTH_SHORT).show();
        }
    }
}