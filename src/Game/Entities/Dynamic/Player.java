package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.util.Random;

import Display.DisplayScreen;

import java.math.*;

//import Display.DisplayScreen;
import Game.GameStates.State;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

    public int lenght; //how many pieces of tail
    public boolean justAte; //true when player eats apple
    public static boolean slowedTime; //true when player slows time
    private Handler handler; //x y coordinates of the head

    public int xCoord;
    public int yCoord;

    public int moveCounter; //how many times the player moved
    public long frameCounter; //how many frames have gone by
    public long stepCounter; //how many steps has the snake taken

    //Stores current direction
    public String direction;//is your first name one?

    //bigger velocity makes it slower, smaller velocity makes it faster
    public int velocity;

    
    //colors
    //Color grn = new Color(24, 125, 29);
    public Color snakeColor = new Color(24, 125, 29);
    //Color rd = new Color(179, 18, 18);
    public Color appleColor = new Color(179, 18, 18);
    
    Color snakeDefault = new Color(24, 125, 29);
	Color appleDefault = new Color(179, 18, 18);
	
	Color timePurple = new Color(64, 0, 128);
	Color timeYellow = new Color(255, 217, 83);
	
	Color rottenBrown = new Color(91, 46, 0);


    public Player(Handler handler){
        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        frameCounter = 0;
        stepCounter = 0;
        direction= "Right";
        justAte = false;
        slowedTime = false;
        lenght = 1; //how long is player at start
        velocity = 3;

    }

    public void tick(){
        moveCounter++; 
        if(moveCounter>=velocity) { //every five frames the snake moves, changes snake speed
            checkCollisionAndMove();
            moveCounter=0; 
            stepCounter++;
            //System.out.println(stepCounter);
        }
       
        if(handler.getKeyManager().up && direction != "Down"){ 
            direction="Up";
        }if(handler.getKeyManager().down && direction != "Up"){
            direction="Down";
        }if(handler.getKeyManager().left && direction != "Right"){
            direction="Left";
        }if(handler.getKeyManager().right && direction != "Left"){
            direction="Right";
        }
        
        addTail(); //adds tail piece when n key is pressed
        //Method to increase velocity
        
        velocity();
        
        //pauses game when 'esc' is pressed
        if(handler.getKeyManager().pbutt) {
        	State.setState(handler.getGame().pauseState);
        	//abre game over
        	//State.setState(handler.getGame().gameOverState);
        }
        
        if(handler.getKeyManager().debug) {
        	//State.setState(handler.getGame().gameOverState);
        }
        
        frameCounter++; //counts how many frames have passed
        //sets things back to normal after 9s of eating power up
        if(frameCounter == 540 && getSlowedTime() == true) {
        	setSlowedTime(false);
        	velocity -= 5;
        	//revert speed back to normal
        	//resume theme music
        }
        
        timeState(); //sets color of snake and apple depending on whether time is slowed or not
        
        isGood(); //checks if apple is rotten or not
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
            setJustAte(true);
        }
        
        //activates slow time power up when player eats it
        if(handler.getWorld().slowTimeLocation[xCoord][yCoord]) {
        	eatSlowTime();
        }

        if(!handler.getWorld().body.isEmpty()) { //chequear
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().body.removeLast(); //removes last piece of the tail from body
            handler.getWorld().body.addFirst(new Tail(x, y,handler)); //adds new tail at the front
            //this creates missing tail segment glitch
            //kill();
        }

    }

    public void render(Graphics g,Boolean[][] playeLocation){
        Random r = new Random();
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
                
            	//snake color
                if(playeLocation[i][j]){
                    g.setColor(snakeColor);
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }
                
            	//apple color
                if(handler.getWorld().appleLocation[i][j]){
                    g.setColor(appleColor);
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }
                
                //slow time color
                if(handler.getWorld().slowTimeLocation[i][j]){
                    g.setColor(Color.black);
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }

            }
        }


    }
    
    //method that changes snake color
    public void setSnakeColor(Color newColor) {
    	this.snakeColor = newColor;
    }
    
    //method that changes apple color
    public void setAppleColor(Color newColor) {
    	this.appleColor = newColor;
    }
    
  //method to change apple  and snake color depending if time is slowed or not
    public void timeState() {
    	if(slowedTime) {
    		//changes snake and apple color while power up is active
        	setSnakeColor(timePurple);
        	setAppleColor(timeYellow);
    	}
    	else {
    		setSnakeColor(snakeDefault);
        	setAppleColor(appleDefault);
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
    
    public void isGood() {
    	if(stepCounter >= 200) { //apple is bad if player walks 200steps
    		//System.out.println("bad");
    		setAppleColor(rottenBrown); //changes apple color to brown
    		//decrease score
    		//lose tail
    		//lenght--;
    		
    	}
    	if(getJustAte()) { //if player eats apple reset stepCounter
    		//System.out.println("yummy");
    		stepCounter = 0;
    		setJustAte(false);
    	}
    }
    
    //method to slow snake speed when it eats power up
    //place inside Player->checkColisionsAndMove
    public void eatSlowTime() {
    	
    	setFrameCounter(0); //starts timer at 0
    	setSlowedTime(true); //changes snake and apple color
    	
    	//stop music
    	velocity += 5; //set speed to slower pace
    	//play za warudo sound

    	handler.getWorld().slowTimeLocation[xCoord][yCoord]=false; //deletes eaten power up, if true spawns new power up
        handler.getWorld().slowTimeOnBoard=false; //tells that a new power up needs to be generated
    }
    

    public void kill(){
        lenght = 0;
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

                handler.getWorld().playerLocation[i][j]=false;
                State.setState(handler.getGame().gameOverState);
            }
        }

    }
    
    
    public void setFrameCounter(long setFrame) {
    	this.frameCounter = setFrame;
    }
    public long getFrameCounter() {
    	return frameCounter;
    }

    public boolean getJustAte() {
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
    
    public static boolean getSlowedTime() {
    	return slowedTime;
    }
    
    public void setSlowedTime(boolean time) {
    	slowedTime = time;
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
        	velocity--;
        }
    	//Method to decrease velocity
        if(handler.getKeyManager().decrease) { //I decrease velocity
        	velocity++;
        }

    }

}
