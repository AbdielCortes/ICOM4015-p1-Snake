package Worlds;

import Game.Entities.Dynamic.Player;
import Game.Entities.Static.Apple;
import Game.Entities.Static.SlowTime;

import Main.Handler;

import java.awt.*;
import java.util.Random;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class WorldOne extends WorldBase{

    public WorldOne (Handler handler) {
        super(handler);

        //has to be a number bigger than 20 and even
        GridWidthHeightPixelCount = 60; //size of grid
        GridPixelsize = (windowPixelSize/GridWidthHeightPixelCount);
        playerLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];
        appleLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];
        slowTimeLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];

    }

    @Override
    public void tick() {
        super.tick();
        player.tick();
        
        //spawns first apple if its been eaten
        if(!appleOnBoard){
            appleOnBoard = true; //sets state of the apple as present
            spawnApple(); //spawns apple in a random x and y
        }
        
        //spawns second apple if it was eaten
//        if(!appleTwoOnBoard) {
//        	appleTwoOnBoard = true; //sets state of the apple as present
//        	spawnApple(); //spawns apple in a random x and y
//        }
        
        //spawn power up if there isn't one on the board and if time isn't already slowed
        if(!slowTimeOnBoard && Player.getSlowedTime() == false) {
        	slowTimeOnBoard = true; //sets state of power up as present
        	spawnSlowTime(); //spawns power up in a random x and y
        }
        
    }
    
    //method that spawns a new apple
    //code was originally inside WorldOne->tick
    public void spawnApple() {
    	int appleX = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1); //generates random x coordinates
    	int appleY = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1); //generates random y coordinates

    	//change coordinates till one is selected in which the player isn't standing
    	boolean goodCoordinates=false;
    	do{
    		if(!handler.getWorld().playerLocation[appleX][appleY]){ //checks if apple is not in the position of the player
    			goodCoordinates=true;
    		}
    	}while(!goodCoordinates);

    	apple = new Apple(handler,appleX,appleY); 
    	appleLocation[appleX][appleY]=true; //sets apple location to random x and random y
    }
    
    //method that spawns slowTime power up
    public void spawnSlowTime() {
    	int slowTimeX = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1); //generates random x coordinates
    	int slowTimeY = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1); //generates random y coordinates

    	//change coordinates till one is selected in which the player isn't standing
    	boolean goodCoordinates=false;
    	do{
    		if(!handler.getWorld().playerLocation[slowTimeX][slowTimeY]){ //checks if power up is not in the position of the player
    			goodCoordinates=true;
    		}
    	}while(!goodCoordinates);
    	
    	slowTime = new SlowTime(handler, slowTimeX, slowTimeY);
    	slowTimeLocation[slowTimeX][slowTimeY] = true;
    }

    @Override
    public void render(Graphics g){
        super.render(g);
        player.render(g,playerLocation); 
    }

}
