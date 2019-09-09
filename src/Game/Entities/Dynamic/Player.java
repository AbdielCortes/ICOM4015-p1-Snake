package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.math.*;

import Game.GameStates.State;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

    public int lenght; //how many pieces of tail
    public boolean justAte; //true when player eats apple
    private Handler handler; //x y coordinates of the head

    public int xCoord;
    public int yCoord;

    public int moveCounter; //how many times the player moved

    //Stores current direction
    public String direction;//is your first name one?
    public int velocity = 5;

    public Player(Handler handler){
        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        direction= "Right";
        justAte = false;
        lenght= 1; //how long is player at start

    }

    public void tick(){
        moveCounter++; 
        if(moveCounter>=velocity) { //every five frames the snake moves, changes snake speed
            checkCollisionAndMove();
            moveCounter=0; 
        }
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) && direction != "Down"){ 
            direction="Up";
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) && direction != "Up"){
            direction="Down";
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) && direction != "Right"){
            direction="Left";
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT) && direction != "Left"){
            direction="Right";
        }
        
        addTail(); //adds tail piece when n key is pressed
        //Method to increase velocity
        
        velocity();
        
        //pauses game when 'esc' is pressed
        if(handler.getKeyManager().pbutt) {
        	State.setState(handler.getGame().pauseState);
        }
    }

    public void checkCollisionAndMove(){
        handler.getWorld().playerLocation[xCoord][yCoord]=false;
        int x = xCoord;
        int y = yCoord;
        switch (direction){
            case "Left":
                if(xCoord==0){ //checks if it not hitting left wall
                	//if snake at left wall, teleport to right wall
                	xCoord = handler.getWorld().GridWidthHeightPixelCount-1;
                }else{
                    xCoord--;
                }
                break;
            case "Right":
                if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){ //checks if it not hitting right wall
                    xCoord = 0; //if snake at right wall, teleport to left
                }else{
                    xCoord++;
                }
                break;
            case "Up":
                if(yCoord==0){ //checks if it not hitting top wall
                	//if snake at top wall, teleport to bottom
                	yCoord = handler.getWorld().GridWidthHeightPixelCount-1;
                }else{
                    yCoord--;
                }
                break;
            case "Down":
                if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){ //checks if it not hitting bottom wall
                    yCoord = 0; //if snake at bottom wall, teleport to top
                }else{
                    yCoord++; 
                }
                break;
        }
        handler.getWorld().playerLocation[xCoord][yCoord]=true;


        if(handler.getWorld().appleLocation[xCoord][yCoord]){ //eats apple
            Eat();
        }

        if(!handler.getWorld().body.isEmpty()) {
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().body.removeLast(); //removes last piece of the tail from body
            handler.getWorld().body.addFirst(new Tail(x, y,handler)); //adds new tail at the front
            //this creates missing tail segment glitch
        }

    }

    public void render(Graphics g,Boolean[][] playeLocation){
        Random r = new Random();
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
                
            	//snake color
                if(playeLocation[i][j]){
                	Color grn = new Color(24, 125, 29);
                    g.setColor(grn);
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }
            	//apple color
                if(handler.getWorld().appleLocation[i][j]){
                	Color rd = new Color(179, 18, 18);
                    g.setColor(rd);
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }

            }
        }


    }

    public void Eat(){ //used to add tail piece
        lenght++; //increases tail length
        Tail tail= null;
        handler.getWorld().appleLocation[xCoord][yCoord]=false; //deletes eaten apple, if true spawns new apple
        handler.getWorld().appleOnBoard=false; //tells that a new apple needs to be generated
        switch (direction){
            case "Left":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail = new Tail(this.xCoord+1,this.yCoord,handler);
                    }
                    else{
                        if(this.yCoord!=0){
                            tail = new Tail(this.xCoord,this.yCoord-1,handler);
                        }
                        else{
                            tail =new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }
                else{
                    if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
                    }
                    else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
                        }
                        else{
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

                        }
                    }

                }
                break;
            case "Right":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=0){
                        tail=new Tail(this.xCoord-1,this.yCoord,handler);
                    }
                    else{
                        if(this.yCoord!=0){
                            tail=new Tail(this.xCoord,this.yCoord-1,handler);
                        }
                        else{
                            tail=new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }
                else{
                    if(handler.getWorld().body.getLast().x!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                    }
                    else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                        }
                        else{
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                        }
                    }

                }
                break;
            case "Up":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(this.xCoord,this.yCoord+1,handler));
                    }
                    else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }
                        else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        }
                    }
                }
                else{
                    if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                    }
                    else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }
                        else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
            case "Down":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=0){
                        tail=(new Tail(this.xCoord,this.yCoord-1,handler));
                    }
                    else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }
                        else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        } System.out.println("Tu biscochito");
                    }
                }
                else{
                    if(handler.getWorld().body.getLast().y!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                    }
                    else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }
                        else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
        }
        handler.getWorld().body.addLast(tail);
        handler.getWorld().playerLocation[tail.x][tail.y] = true;
        
        
    }

    public void kill(){
        lenght = 0;
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

                handler.getWorld().playerLocation[i][j]=false;
            }
        }
        //import java.awt.Graphics2D;

        //if(kill()){
        	//g2.setColor(Color.BLACK);
        	//g2.drawString("Game Over");
    }

    public boolean isJustAte() {
        return justAte;
    }

    public void setJustAte(boolean justAte) {
        this.justAte = justAte;
        //Add Score. Quizas en display.java
        //int currScore = 0;
        //if(justAte) {
        	//currScore=currScore + (sqrt(2*currScore+1));
        	//public void paintComponent (Graphics g){
        		//g2.setColor(Color.BLACK);
        		//g2.drawint(currScore, 5 , 50);
        //}
    }
    
    //Method to add tail using "N" key
    public void addTail() {
    	if(handler.getKeyManager().tail) {
    		Eat();
    		handler.getWorld().appleOnBoard=true;
    	}
    }
    public void velocity() {
    //Method to increase velocity
        if(handler.getKeyManager().increase) { //O increase velocity
        	velocity--;}
    	//Method to decrease velocity
        if(handler.getKeyManager().decrease) { //I decrease velocity
        	velocity++;
        }

    }

}
