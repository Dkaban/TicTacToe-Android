package com.example.tictactoe;

//This class holds the information for us during a play session
//Main used for holding player names and 1v1 or 1vCPU checking
class DataHolder
{
    public String nameOne = "";
    public String nameTwo = "";
    public int numberOfPlayers = 0;

    public void setNameOne(String name)
    {
        nameOne = name;
    }

    public void setNameTwo(String name)
    {
        nameTwo = name;
    }

    public void setNumberOfPlayers(int i)
    {
        numberOfPlayers = i;
    }

    //Allows for use-ability across activity changes
    static DataHolder getInstance()
    {
        if( instance == null )
        {
            instance = new DataHolder();
        }
        return instance;
    }

    private static DataHolder instance;
}