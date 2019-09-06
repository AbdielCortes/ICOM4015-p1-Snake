package Worlds;

import Game.Entities.Dynamic.Player;
import Game.Entities.Dynamic.Tail;
import Game.Entities.Static.Apple;
import Main.Handler;

import java.awt.*;
import java.util.LinkedList;


/**
 * Created by AlexVR on 7/2/2018.
 */
public abstract class WorldBase {

    //How many pixels are from left to right
    //How many pixels are from top to bottom
    //Must be equal
    public int GridWidthHeightPixelCount;

    //automatically calculated, depends on previous input.
    //The size of each box, the size of each box will be GridPixelsize x GridPixelsize.
    public int GridPixelsize;
    
    public static int windowPixelSize = 800; //sets window pixel dimensions
    
    public Player player;

    protected Handler handler;


    public Boolean appleOnBoard;
    public Boolean appleTwoOnBoard; //boolean variable to spawn second apple
    protected Apple apple;
    public Boolean[][] appleLocation;


    public Boolean[][] playerLocation;

    public LinkedList<Tail> body = new LinkedList<>();


    public WorldBase(Handler handler){
        this.handler = handler;

        appleOnBoard = false;
        appleTwoOnBoard = false; //sets second apple to false so that it spawns one at start


    }
    public void tick(){



    }

    public void render(Graphics g){

        for (int i = 0; i <= windowPixelSize; i = i + GridPixelsize) { //draws grid lines
        	
        	//---------------------------grid color------------------------------------------------
        	Color purp = new Color(243, 182, 252);
            g.setColor(purp);
            g.drawLine(0, i, handler.getWidth() , i);
            g.drawLine(i,0,i,handler.getHeight());

        }



    }

}
