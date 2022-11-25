package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

public class Avatar {
    static int x;
    static int y;
    static TETile[][] randomWorld;

    public Avatar(int posx, int posy, TETile[][] rw) {
        x = posx;
        y = posy;
        randomWorld = rw;
    }

    public static void placeTheAvatar() {
        randomWorld[x][y] = Tileset.AVATAR;
    }

    public static boolean directions(String direction) {
        return switch (direction) {
            case "W" -> randomWorld[x][y + 1].equals(Tileset.FLOOR);
            case "S" -> randomWorld[x][y - 1].equals(Tileset.FLOOR);
            case "D" -> randomWorld[x + 1][y].equals(Tileset.FLOOR);
            case "A" -> randomWorld[x - 1][y].equals(Tileset.FLOOR);
            default -> false;
        };
    }

    public void move(String direction) {
        if (directions("W")) {
            randomWorld[x][y] = Tileset.FLOOR;
            randomWorld[x][y + 1] = Tileset.AVATAR;
            y++;
        }
        if (directions("S")) {
            randomWorld[x][y] = Tileset.FLOOR;
            randomWorld[x][y - 1] = Tileset.AVATAR;
            y--;
        }
        if (directions("D")) {
            randomWorld[x][y] = Tileset.FLOOR;
            randomWorld[x + 1][y] = Tileset.AVATAR;
            x++;
        }
        if (directions("A")) {
            randomWorld[x][y] = Tileset.FLOOR;
            randomWorld[x - 1][y] = Tileset.AVATAR;
            x--;
        }
    }

//    public static String takeKeyboardCommands() {
//        input = new StringBuilder();
//        if (StdDraw.hasNextKeyTyped()) {
//            String letter = String.valueOf(StdDraw.nextKeyTyped()).toUpperCase();
//            if (letter.equals("Q") && (input.indexOf(":") == input.length() - 1)) {
//                return input.toString().toUpperCase();
//            }
//            input.append(letter);
//        }
//    }



}
