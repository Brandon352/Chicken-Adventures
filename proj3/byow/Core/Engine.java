package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
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
    StringBuilder movementMemory;
    String filename = "savedWorld.txt";
    Out out;

    Random randx = new Random(seed);
    Random randy = new Random(seed);
    int randomx;
    int randomy;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
//        Displays the main menu and listens for USER input
        menuInput = UserInterface.MainMenu();
        while (!menuInput.equals("Q") && !menuInput.equals("N") && !menuInput.equals("L")) {
            menuInput = UserInterface.MainMenu();
        }

        if (menuInput.equals("Q")) {
            return;
        }

        else if (menuInput.equals("N")) {
//            Displays the seed menu and listens for USER input
            seedInput = UserInterface.seedMenu();
            interactWithInputString(seedInput);

            while (true) {
                randomx = randx.nextInt(0, WIDTH);
                randomy = randy.nextInt(0, HEIGHT);
                if (randomWorld[randomx][randomy].equals(Tileset.FLOOR)) {
                    randomWorld[randomx][randomy] = Tileset.AVATAR;
                    avatar = new Avatar(randomx, randomy, randomWorld);
                    break;
                }
            }

            ter.renderFrame(randomWorld);
        }

        else if (menuInput.equals("L")) {
            TETile[][] cloneWorld2;
            In in = new In(filename);

            if (in.hasNextLine()) {
                String inputLine = in.readString();
                if (in.hasNextLine()) {
                    if (in.hasNextLine()) {
                        int xStart = in.readInt();
                        int yStart = in.readInt();
                        interactWithInputString(inputLine);
                        avatar = new Avatar(xStart, yStart, randomWorld);
//                        ter.renderFrame(randomWorld);
                    } else {
                        interactWithInputString(inputLine);
                        while (true) {
                            randomx = randx.nextInt(0, WIDTH);
                            randomy = randy.nextInt(0, HEIGHT);
                            if (randomWorld[randomx][randomy].equals(Tileset.FLOOR)) {
                                randomWorld[randomx][randomy] = Tileset.AVATAR;
                                avatar = new Avatar(randomx, randomy, randomWorld);
                                break;
                            }
                        }


                    }

                    ter.renderFrame(randomWorld);
                } else {
                    interactWithInputString(inputLine);
                    while (true) {
                        randomx = randx.nextInt(0, WIDTH);
                        randomy = randy.nextInt(0, HEIGHT);
                        if (randomWorld[randomx][randomy].equals(Tileset.FLOOR)) {
                            randomWorld[randomx][randomy] = Tileset.AVATAR;
                            avatar = new Avatar(randomx, randomy, randomWorld);
                            break;
                        }
                    } ter.renderFrame(randomWorld);
                }
            } else {
                return;
            }
        }

        memory = new StringBuilder();
        movementMemory = new StringBuilder();
        TETile[][] cloneWorld;

//        Constantly refreshes so that the cursor is always detecting its location
        while (true) {
            HUD();
            if (StdDraw.hasNextKeyTyped()) {
                String moveKey = String.valueOf(StdDraw.nextKeyTyped()).toUpperCase();

                if (!moveKey.equals("L")) {
                    avatar.move(moveKey);
                    cloneWorld = TETile.copyOf(randomWorld);
                    ambition(Avatar.x, Avatar.y, cloneWorld);
                    ter.renderFrame(cloneWorld);
                    if (Avatar.directions(moveKey)) {
                        movementMemory.append(moveKey);
                    }
                    memory.append(moveKey);

                    if (quitSave(moveKey, String.valueOf(memory))) {
                        out.println(Avatar.x);
                        out.println(Avatar.y);
                        out.println(memory.substring(0, memory.length() - 2));
                        return;
                    }

                } else {
                    avatar.move(moveKey);
                    ter.renderFrame(randomWorld);
                }
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
        }
    }

/** Creates and blacks out the map except for a 3x3 square around the player **/
    public void ambition(int x, int y, TETile[][] world) {
        for (int posx = 0; posx < WIDTH; posx ++) {
            for (int posy = 0; posy < HEIGHT; posy++) {
                if ((posx == (x - 1) || posx == (x) || posx == (x + 1) || posx == (x - 2) || posx == (x + 2)
                        || posx == (x - 3) || posx == (x + 3)) &&
                        (posy == (y - 1) || posy == y || posy == (y + 1) || posy == (y - 2) || posy == (y + 2)
                                || posy == (y - 3) || posy == (y + 3))) {
                        continue;
                } else {
                    world[posx][posy] = Tileset.NOTHING;
                }
            }
        }
    }

    public boolean quitSave(String moveKey, String movementMem) {
        return moveKey.equals("Q") && (movementMem.indexOf(":") == movementMem.length() - 2);
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


        out = new Out(filename);
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

        out.println(input);

        input = input.substring(Nindex + 1, Sindex);
        seed = Integer.parseInt(input);
        randomWorld = new TETile[WIDTH][HEIGHT];
        MakeWorld rw = new MakeWorld(randomWorld, WIDTH, HEIGHT, seed);
        ter.initialize(WIDTH, HEIGHT);
//        randomWorld[5][7] = Tileset.AVATAR;
        ter.renderFrame(randomWorld);

        if (input.length() > Sindex + 1) {
            while (true) {
                randomx = randx.nextInt(0, WIDTH);
                randomy = randy.nextInt(0, HEIGHT);
                if (randomWorld[randomx][randomy].equals(Tileset.FLOOR)) {
                    randomWorld[randomx][randomy] = Tileset.AVATAR;
                    avatar = new Avatar(randomx, randomy, randomWorld);
                    break;
                }

                String movementLine = input.substring(Sindex + 1).toUpperCase();
                String[] inputs = movementLine.split("");
                for (String movement : inputs) {
                    avatar.move(movement);
                    ter.renderFrame(randomWorld);
                }

                out.println(Avatar.x);
                out.println(Avatar.y);
            }
        }
        return randomWorld;
    }

    public char getNextKey(String input, int index) {
        char returnChar = input.charAt(index);
        return returnChar;
    }
}
