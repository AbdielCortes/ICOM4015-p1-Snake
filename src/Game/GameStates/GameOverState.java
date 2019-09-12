package Game.GameStates;

import Main.Handler;
import Resources.Images;
import UI.ClickListlener;
import UI.UIImageButton;
import UI.UIManager;

import java.awt.*;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class GameOverState extends State {

    private UIManager uiManager;

    public GameOverState(Handler handler) {
    	super(handler);
    	uiManager = new UIManager(handler);
    	handler.getMouseManager().setUimanager(uiManager);

    	uiManager.addObjects(new UIImageButton(80, 230, 186, 66, Images.Restart, new ClickListlener() {
    		@Override
            public void onClick() {
                handler.getMouseManager().setUimanager(null);
                handler.getGame().reStart(); //restarts game
                State.setState(handler.getGame().gameState);
            }
    	}));
    	
    	uiManager.addObjects(new UIImageButton(80, 230+(66+30), 186, 66, Images.BTitle, () -> {
    		handler.getMouseManager().setUimanager(null);
    		State.setState(handler.getGame().menuState);
    	}));

    }

    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
       
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.GameOver,0,0,800,800,null);
        uiManager.Render(g);

    }
}
