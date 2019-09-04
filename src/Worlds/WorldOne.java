package Worlds;

import Game.Entities.Static.Apple;
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
        GridWidthHeightPixelCount = 30; //size of grid
        GridPixelsize = (900/GridWidthHeightPixelCount);
        playerLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];
        appleLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];

    }

    @Override
    public void tick() {
        super.tick();
        player.tick();
        if(!appleOnBoard){
            appleOnBoard=true;
            int appleX = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1); //generates random x coordinates
            int appleY = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1); //generates random y coordinates

            //change coordinates till one is selected in which the player isn't standing
            boolean goodCoordinates=false;
            do{
                if(!handler.getWorld().playerLocation[appleX][appleY]){ //checks if player is not in the position of the player
                    goodCoordinates=true;
                }
            }while(!goodCoordinates);

            apple = new Apple(handler,appleX,appleY); 
            appleLocation[appleX][appleY]=true; //sets apple location to random x and random y

        }
    }

    @Override
    public void render(Graphics g){
        super.render(g);
        player.render(g,playerLocation); 
    }

}
