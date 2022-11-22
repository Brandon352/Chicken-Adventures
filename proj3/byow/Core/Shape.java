package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Shape {
    TETile[][] board;
    int index;
    public Shape (TETile[][] tiles) {
        this.board = tiles;
    }
    private static final long SEED = 7824334;
    private final Random RANDOM = new Random(SEED);
    ;
    //      Random length
//    int roomCount = RandomUtils.uniform(RANDOM, 8, 12);
//    int leftcorner = RandomUtils.uniform(RANDOM, board.length - 2);
//    int rightcorner = RandomUtils.uniform(RANDOM, board[0].length + 2);




    public void drawRoom (Position p, Random seed, int randomWidth, int randomLength) {
        for (int i = 0; i < randomWidth; i++) {
            for (int j = 0; j < randomLength; j++) {
                if (i == 0 || j == randomLength - 1 || j == 0 || i == randomWidth - 1) {
                    board[p.x + i][p.y + j] = Tileset.WALL;
                } else {
                    board[p.x + i][p.y + j] = Tileset.FLOOR;
                }
            }
        }
    }
}