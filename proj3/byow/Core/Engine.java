package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;


import java.awt.*;
import java.util.Random;


public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    UserInterface UI = new UserInterface(this);
    String menuInput;
    String seedInput;
    public static TETile[][] randomWorld;
    int seed;
    Avatar avatar;
    StringBuilder memory;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
//        Displays the main menu and listens for USER input
        menuInput = UserInterface.MainMenu();
        if (menuInput.equals("N")) {
//            Displays the seed menu and listens for USER input
            seedInput = UserInterface.seedMenu();

            interactWithInputString(seedInput);

            Random randx = new Random(seed);
            Random randy = new Random(seed);

            while (true) {
                int randomx = randx.nextInt(0, WIDTH);
                int randomy = randy.nextInt(0, HEIGHT);
                if (randomWorld[randomx][randomy].equals(Tileset.FLOOR)) {
                    randomWorld[randomx][randomy] = Tileset.AVATAR;
                    avatar = new Avatar(randomx, randomy, randomWorld);
                    break;
                }
            }

            ter.renderFrame(randomWorld);
        }

        memory = new StringBuilder();

//        Constantly refreshes so that the cursor is always detecting its location
        while (true) {
            HUD();
            if (StdDraw.hasNextKeyTyped()) {
                String moveKey = String.valueOf(StdDraw.nextKeyTyped()).toUpperCase();
                avatar.move(moveKey);
                ter.renderFrame(randomWorld);
                memory.append(moveKey);
//                System.out.println(moveKey);
        }
            }
        }

/** Heads Up Display function **/
    public void HUD() {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();

        if (x < WIDTH && y < HEIGHT) {
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.filledRectangle(5, HEIGHT - 2, 10, 1);
            StdDraw.setPenColor(Color.ORANGE);
            StdDraw.textLeft(2, HEIGHT - 2, randomWorld[x][y].description());
            StdDraw.show();
//            StdDraw.pause(10);
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        input = input.toUpperCase();
        int Nindex = input.indexOf("N");
        int Sindex = input.indexOf("S");

        if (Nindex < 0) {
            throw new IllegalArgumentException("Input string must start with 'N'");
        } else if (Sindex < 0) {
            throw new IllegalArgumentException("Input string must include 'S'");
        } else if (Sindex < Nindex) {
            throw new IllegalArgumentException("'S' must precede 'N' in the input string");
        }
        input = input.substring(Nindex + 1, Sindex);
        seed = Integer.parseInt(input);
        randomWorld = new TETile[WIDTH][HEIGHT];
        MakeWorld rw = new MakeWorld(randomWorld, WIDTH, HEIGHT, seed);
        ter.initialize(WIDTH, HEIGHT);
//        randomWorld[5][7] = Tileset.AVATAR;
//        ter.renderFrame(randomWorld);
        return randomWorld;
    }

    public char getNextKey(String input, int index) {
        char returnChar = input.charAt(index);
        return returnChar;
    }
}
