package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import Game.GameStates.State;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

    public int lenght; //how many pieces of tail
    public boolean justAte; //true when player eats apple
    public boolean justTime; //true when player slows time
    private Handler handler; //x y coordinates of the head

    public int xCoord;
    public int yCoord;

    public int moveCounter; //how many times the player moved
    public long frameCounter; //how many frames have gone by

    //Stores current direction
    public String direction;//is your first name one?
    
    //colors
    //Color grn = new Color(24, 125, 29);
    public Color snakeColor = new Color(24, 125, 29);
    //Color rd = new Color(179, 18, 18);
    public Color appleColor = new Color(179, 18, 18);
    
    Color snakeDefault = new Color(24, 125, 29);
	Color appleDefault = new Color(179, 18, 18);
	Color timePurple = new Color(64, 0, 128);
	Color timeYellow = new Color(196, 98, 0);

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
        if(moveCounter>=5) { //every five frames the snake moves, changes snake speed
            checkCollisionAndMove();
            moveCounter=0; 
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
        
        //pauses game when 'esc' is pressed
        if(handler.getKeyManager().pbutt) {
        	State.setState(handler.getGame().pauseState);
        }
        
        frameCounter++; //counts how many frames have passed
        if(frameCounter > 540) {
        	setSnakeColor(snakeDefault);
        	setAppleColor(appleDefault);
        	//revert speed back to normal
        	//resume theme music
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
        
        //activates slow time power up when player eats it
        if(handler.getWorld().slowTimeLocation[xCoord][yCoord]) {
        	eatSlowTime();
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
    
    //method to slow snake speed when it eats power up
    //place inside Player->checkColisionsAndMove
    public void eatSlowTime() {
    	setFrameCounter(0);
    	long frames = getFrameCounter();
    	
    	//540frames = 9seconds
    	//changes snake and apple color while power up is active
    	if(frames <= 540) {
        	setSnakeColor(timePurple);
        	setAppleColor(timeYellow);
        	//stop music
        	//set speed to slower pace
        	//play za warudo sound
        }

    	handler.getWorld().slowTimeLocation[xCoord][yCoord]=false; //deletes eaten power up, if true spawns new power up
        handler.getWorld().slowTimeOnBoard=false; //tells that a new power up needs to be generated
    }
    
    public void setFrameCounter(long setFrame) {
    	this.frameCounter = setFrame;
    }
    public long getFrameCounter() {
    	return frameCounter;
    }

    public void kill(){
        lenght = 0;
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

                handler.getWorld().playerLocation[i][j]=false;

            }
        }
    }

    public boolean isJustAte() {
        return justAte;
    }

    public void setJustAte(boolean justAte) {
        this.justAte = justAte;
    }
    
    //Method to add tail using "N" key
    public void addTail() {
    	if(handler.getKeyManager().tail) {
    		Eat();
    		handler.getWorld().appleOnBoard=true;
    	}
    }
}
