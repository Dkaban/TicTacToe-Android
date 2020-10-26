package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

enum PlacingMode
{
    Empty,
    X,
    O
}

public class PlayGameActivity extends AppCompatActivity
{
    //SharedPreference variables for saving data
    private String PREF_NAME = "PlayerNames";
    private SharedPreferences playerNames;
    private SharedPreferences.Editor editor;

    //Variables for keeping track of the game states / Tile states
    private PlacingMode placingState = PlacingMode.X;
    private Tile[] tileArray = new Tile[9];

    //Variables for manipulating the UI
    private TextView textViewTurnInfo;
    private Button b00,b01,b02,b10,b11,b12,b20,b21,b22;
    private Button[] buttons = new Button[9];
    private PlacingMode whoWins = PlacingMode.Empty;
    private boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        textViewTurnInfo = this.findViewById(R.id.text_turnInformation);
        b00 = this.findViewById(R.id.button_00);
        b01 = this.findViewById(R.id.button_01);
        b02 = this.findViewById(R.id.button_02);
        b10 = this.findViewById(R.id.button_10);
        b11 = this.findViewById(R.id.button_11);
        b12 = this.findViewById(R.id.button_12);
        b20 = this.findViewById(R.id.button_20);
        b21 = this.findViewById(R.id.button_21);
        b22 = this.findViewById(R.id.button_22);

        //Tile Array holds all the information about a tile
        tileArray[0] = new Tile(b00);
        tileArray[1] = new Tile(b01);
        tileArray[2] = new Tile(b02);
        tileArray[3] = new Tile(b10);
        tileArray[4] = new Tile(b11);
        tileArray[5] = new Tile(b12);
        tileArray[6] = new Tile(b20);
        tileArray[7] = new Tile(b21);
        tileArray[8] = new Tile(b22);

        playerNames = getSharedPreferences(PREF_NAME,0);
        editor = playerNames.edit();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        String textToDisplay = "Your Turn " + DataHolder.getInstance().nameOne
                + " Place your " + placingState.toString();
        textViewTurnInfo.setText(textToDisplay);
    }

    public void onClickTile(View view)
    {
        if(gameOver)
        {
            Toast.makeText(this, "The Game is Over", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check which button was pressed
        switch(view.getId())
        {
            case R.id.button_00:
                if(tileArray[0].used) return;

                //We want to set the default values of a tile and set it to X or O depending
                tileArray[0].setTileUsed(placingState,placingState.toString());
                break;

            case R.id.button_01:
                if(tileArray[1].used) return;

                tileArray[1].setTileUsed(placingState,placingState.toString());
                break;

            case R.id.button_02:
                if(tileArray[2].used) return;

                tileArray[2].setTileUsed(placingState,placingState.toString());
                break;

            case R.id.button_10:
                if(tileArray[3].used) return;

                tileArray[3].setTileUsed(placingState,placingState.toString());
                break;

            case R.id.button_11:
                if(tileArray[4].used) return;

                tileArray[4].setTileUsed(placingState,placingState.toString());
                break;

            case R.id.button_12:
                if(tileArray[5].used) return;

                tileArray[5].setTileUsed(placingState,placingState.toString());
                break;

            case R.id.button_20:
                if(tileArray[6].used) return;

                tileArray[6].setTileUsed(placingState,placingState.toString());
                break;

            case R.id.button_21:
                if(tileArray[7].used) return;

                tileArray[7].setTileUsed(placingState,placingState.toString());
                break;

            case R.id.button_22:
                if(tileArray[8].used) return;

                tileArray[8].setTileUsed(placingState,placingState.toString());
                break;
        }

        placeMove();

        checkForWin();
    }

    //Places either an O or an X and then runs the Androids move if 1 player game
    private void placeMove()
    {
        //If we're placing an O, we want to change it back to an X
        if(placingState == PlacingMode.O)
        {
            placingState = PlacingMode.X;

            //Set the TextView string to the proper player name
            String textToDisplay = "Your Turn " + DataHolder.getInstance().nameOne
                    + " Place your " + placingState.toString();
            textViewTurnInfo.setText(textToDisplay);
        }
        else
        {
            if(!DataHolder.getInstance().nameTwo.equals("")
                    && DataHolder.getInstance().numberOfPlayers >= 2)
            {
                placingState = PlacingMode.O;
                String textToDisplay = "Your Turn " + DataHolder.getInstance().nameTwo
                        + " Place your " + placingState.toString();
                textViewTurnInfo.setText(textToDisplay);
            }
            else if(!gameOver)
            {
                placingState = PlacingMode.O;

                if(!checkForEmptySlot()) return;

                //Runs the android move
                runAndroidASync();
            }
        }
    }

    //Runs the background thread for the CPU's moves
    private void runAndroidASync()
    {
        new AndroidGamePlayTask(this).execute("","");
    }

    //This handles the CPU's move
    public void androidMakeMove()
    {
        //Cycle through random numbers from 0 to 8 to find a free array index
        Random random = new Random();
        int randomIndex = random.nextInt(9);

        //Makes sure we exit the loop
        boolean foundEmptySlot = false;

        //Not the smartest CPU AI but it finds a random available spot and places an O there.
        while(!foundEmptySlot)
        {
            if(!tileArray[randomIndex].used)
            {
                foundEmptySlot = true;
            }
            else
            {
                randomIndex = random.nextInt(9);
            }
        }

        tileArray[randomIndex].used = true;
        tileArray[randomIndex].valuePlaced = PlacingMode.O;
        tileArray[randomIndex].button.setText(placingState.toString());

        //Change the state back to X so the player can go.
        placingState = PlacingMode.X;

        //Set the TextView string to the proper player name
        String textToDisplay = "Your Turn " + DataHolder.getInstance().nameOne
                + " Place your " + placingState.toString();
        textViewTurnInfo.setText(textToDisplay);

        checkForWin();
    }

    private boolean checkForEmptySlot()
    {
        //Check to see if the game is over with no win cases because board is full
        for(int i=0;i<tileArray.length;i++)
        {
            if(!tileArray[i].used)
            {
                return true;
            }
        }
        return false;
    }

    //Has to check all win conditions, crosses, diagonals etc.
    //Allows for reverse diagonals because it checks the indexes for matches not direction.
    private void checkForWin()
    {
        if(checkPlacedArrayForWin(0,1,2))
        {
            gameOver = true;
        }
        else if(checkPlacedArrayForWin(3,4,5))
        {
            gameOver = true;
        }
        else if(checkPlacedArrayForWin(6,7,8))
        {
            gameOver = true;
        }
        else if(checkPlacedArrayForWin(0,3,6))
        {
            gameOver = true;
        }
        else if(checkPlacedArrayForWin(1,4,7))
        {
            gameOver = true;
        }
        else if(checkPlacedArrayForWin(2,5,8))
        {
            gameOver = true;
        }
        else if(checkPlacedArrayForWin(0,4,8))
        {
            gameOver = true;
        }
        else if(checkPlacedArrayForWin(2,4,6))
        {
            gameOver = true;
        }

        if(!gameOver)
        {
            //If there is no more empty slots, the game is over anyway.
            if(!checkForEmptySlot())
            {
                gameOver = true;
                String endCaseMessage = "Nobody Wins this Match.";
                textViewTurnInfo.setText(endCaseMessage);
            }
        }
        else if(gameOver)
        {
            //This is if X wins, we do the corresponding user output.
            if(whoWins == PlacingMode.X)
            {
                if(!DataHolder.getInstance().nameOne.equals(""))
                {
                    String winMessage = DataHolder.getInstance().nameOne + ", placer of "
                            + whoWins.toString() + " WINS!";
                    textViewTurnInfo.setText(winMessage);

                    //Player One Wins
                    int winCount = playerNames.getInt(DataHolder.getInstance().nameOne,0);
                    winCount++;
                    editor.putInt(DataHolder.getInstance().nameOne,winCount);
                    editor.commit();
                }
                else
                {
                    //This is if the player has not entered a name.
                    String winMessage = whoWins.toString() + " WINS!";
                    textViewTurnInfo.setText(winMessage);
                }
            }
            else
            {
                if(!DataHolder.getInstance().nameTwo.equals("")
                        && DataHolder.getInstance().numberOfPlayers >= 2)
                {
                    String winMessage = DataHolder.getInstance().nameTwo + ", placer of "
                            + whoWins.toString() + " WINS!";
                    textViewTurnInfo.setText(winMessage);

                    //Player Two Wins
                    int winCount = playerNames.getInt(DataHolder.getInstance().nameTwo,0);
                    winCount++;
                    editor.putInt(DataHolder.getInstance().nameTwo,winCount);
                    editor.commit();
                }
                else
                {
                    //If no name entered for the 2nd player, it's the CPU!
                    String winMessage = "Android, placer of " + whoWins.toString() + " WINS!";
                    textViewTurnInfo.setText(winMessage);
                }
            }
        }
    }

    private boolean checkPlacedArrayForWin(int index1, int index2, int index3)
    {
        if(tileArray[index1].used && tileArray[index2].used && tileArray[index3].used)
        {
            return checkAlignmentForWin(index1,index2,index3);
        }
        return false;
    }

    private boolean checkAlignmentForWin(int index1, int index2, int index3)
    {
        if(tileArray[index1].valuePlaced == PlacingMode.O)
        {
            if(tileArray[index2].valuePlaced == PlacingMode.O && tileArray[index3].valuePlaced == PlacingMode.O)
            {
                whoWins = PlacingMode.O;
                return true;
            }
        }
        else if(tileArray[index1].valuePlaced == PlacingMode.X)
        {
            if(tileArray[index2].valuePlaced == PlacingMode.X && tileArray[index3].valuePlaced == PlacingMode.X)
            {
                whoWins = PlacingMode.X;
                return true;
            }
        }

        return false;
    }
}

//Background Async, calls the activity to run the logic.
class AndroidGamePlayTask extends AsyncTask<String, String, String>
{
    private Context context;

    public AndroidGamePlayTask(Context context)
    {
        this.context = context;
        PlayGameActivity cont = (PlayGameActivity) context;
        cont.androidMakeMove();
    }

    @Override
    protected String doInBackground(String... strings)
    {
        return null;
    }
}

//This class holds the tile information for determining X or O, and setting button UI
class Tile
{
    public Button button;
    public boolean used = false;
    public PlacingMode valuePlaced = PlacingMode.Empty;

    public Tile(Button btn)
    {
        button = btn;
        used = false;
        valuePlaced = PlacingMode.Empty;
    }

    public void setTileUsed(PlacingMode mode, String textToSet)
    {
        valuePlaced = mode;
        used = true;
        button.setText(textToSet);
    }
}