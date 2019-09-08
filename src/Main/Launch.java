package Main;

import Worlds.WorldOne;

/**
 * Created by AlexVR on 7/1/2018.
 */

public class Launch {

    public static void main(String[] args) {
        GameSetUp game = new GameSetUp("The World Snake", WorldOne.windowPixelSize, WorldOne.windowPixelSize);
        game.start();
    }
}
