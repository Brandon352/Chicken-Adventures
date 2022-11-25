package byow.InputDemo;

/**
 * Created by hug.
 */
import byow.TileEngine.TERenderer;
import edu.princeton.cs.algs4.StdDraw;

public class KeyboardInputSource implements InputSource {
    private static final boolean PRINT_TYPED_KEYS = false;
    TERenderer ter = new TERenderer();
    public KeyboardInputSource() {
//        ter.initialize(40, 50);
        StdDraw.text(0.3, 0.3, "press m to moo, q to quit");
    }

    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (PRINT_TYPED_KEYS) {
                    System.out.print(c);
                }
                return c;
            }
        }
    }

    public boolean possibleNextInput() {
        return true;
    }
}
