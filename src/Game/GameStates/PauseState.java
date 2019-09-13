package Game.GameStates;

import Main.Handler;
import Resources.Images;
import UI.UIImageButton;
import UI.UIManager;

import java.awt.*;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class PauseState extends State {

    private int count = 0;
    private UIManager uiManager;

    public PauseState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        //resume button
        uiManager.addObjects(new UIImageButton(80, 223, 186, 66, Images.Resume, () -> {
            handler.getMouseManager().setUimanager(null);
            State.setState(handler.getGame().gameState);
        }));

        //restart button
        uiManager.addObjects(new UIImageButton(80, 223+(66+30), 186, 66, Images.Restart, () -> {
        	handler.getMouseManager().setUimanager(null);
			handler.getGame().reStart(); //restarts game
			State.setState(handler.getGame().gameState);
        }));

        //return to title screen button
        uiManager.addObjects(new UIImageButton(80, (223+(66+30))+(66+30), 186, 66, Images.BTitle, () -> {
            handler.getMouseManager().setUimanager(null);
            State.setState(handler.getGame().menuState);
        }));

    }

    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
        count++;
        if(count>=30){
            count=30;
        }
        if(handler.getKeyManager().pbutt && count>=30){
            count=0;

            State.setState(handler.getGame().gameState);
        }
       
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.Pause,0,0,800,800,null);
        uiManager.Render(g);

    }
}
