package byow.Core;

import byow.TileEngine.TERenderer;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class UserInterface {
    private Engine engine;
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    TERenderer ter = new TERenderer();
    static StringBuilder input;

    /** Master UI for all USER interactivity **/
    public UserInterface (Engine eng) {
        this.engine = eng;
        ter.initialize(WIDTH, HEIGHT, 4, 5);
    }

/** Main Menu **/
    public static String MainMenu() {

//        Creates the Main Menu screen
        StdDraw.setPenColor(new Color(255, 255, 255));
        Font font = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) * 1.5, "A Chicken's Adventure");

        Font font2 = new Font("Monaco", Font.BOLD, 25);
        StdDraw.setFont(font2);
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 2, "Load Game (L)");
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 4, "Quit (Q)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New Game (N)");
        StdDraw.show();

//        Listens to USER for menu input and RETURNS user input as STRING
        input = new StringBuilder();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                input.append(StdDraw.nextKeyTyped());
                return input.toString().toUpperCase();
            }
        }
    }

    /** Asks USER for seed input in the form N####S **/
    public static String seedMenu() {
        input = new StringBuilder();
        seedHelper("");

//TODO make backspace button work
        while (true) {
//            if (StdDraw.isKeyPressed(VK_BACK_SPACE)) {
//                input.delete(input.length(), input.length());
//                seedHelper(input.toString().toUpperCase());
//                System.out.println("PRESSED");
//            }

//            Listens to USER INPUT and return once it reaches the letter "S"
            if (StdDraw.hasNextKeyTyped()) {
                String letter = String.valueOf(StdDraw.nextKeyTyped()).toUpperCase();
                input.append(letter.toUpperCase());
                seedHelper(input.toString().toUpperCase());

                if (letter.equals("S")) {
//                    this.seed = input.toString();
                    return input.toString();
                }
            }
        }
    }

    /** Helper function to continuously update the seed menu page while USER types **/
    private static void seedHelper(String letter) {
        StdDraw.clear(Color.BLACK);
        StdDraw.text(WIDTH/2, (HEIGHT/2) * 1.5, "Input Seed:");
        StdDraw.text(WIDTH/2, HEIGHT/2, letter);
        StdDraw.show();
    }
}

