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
        if (direction.equals("W") && directions("W")) {
            randomWorld[x][y] = Tileset.FLOOR;
            randomWorld[x][y + 1] = Tileset.AVATAR;
//            System.out.println("Moving Up");
            y++;
        }
        if (direction.equals("S") && directions("S")) {
            randomWorld[x][y] = Tileset.FLOOR;
            randomWorld[x][y - 1] = Tileset.AVATAR;
//            System.out.println("Moving Down");
            y--;
        }
        if (direction.equals("D") && directions("D")) {
            randomWorld[x][y] = Tileset.FLOOR;
            randomWorld[x + 1][y] = Tileset.AVATAR;
//            System.out.println("Moving Right");
            x++;
        }
        if (direction.equals("A") && directions("A")) {
            randomWorld[x][y] = Tileset.FLOOR;
            randomWorld[x - 1][y] = Tileset.AVATAR;
//            System.out.println("Moving Left");
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
