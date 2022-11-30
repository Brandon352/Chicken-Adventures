package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.*;

import static java.lang.Math.abs;


public class MakeWorld {
    static int HEIGHT = 40;
    static int WIDTH = 80;
    static HashMap<Integer, Position> roomToCenter = new HashMap<>();
    static HashMap<Integer, Position> roomToInitial = new HashMap<>();

    private static KruskalMST MST;
    private WeightedQuickUnionUF wqu;
    static HashMap<Integer, Position> roomToSize = new HashMap<>();
    static int numRooms;

    //269882
    //582609882

//    public static void main(String[] args) {
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
//        tiles = new TETile[WIDTH][HEIGHT];
//        makeWorld(tiles);
//
//        ter.renderFrame(tiles);
//    }


    public MakeWorld(TETile[][] tiles, int w, int h, int seed) {
        Random RANDOM = new Random(seed);
        int height = h;
        int width = w;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
        Shape draw = new Shape(tiles);
        int NumberOfRooms = RandomUtils.uniform(RANDOM, 80, 120);
        for (int i = 0; i < NumberOfRooms; i++) {
            int Length = RandomUtils.uniform(RANDOM, 7, 16);
            int Width = RandomUtils.uniform(RANDOM, 7, 16);
            Position p = new Position(RandomUtils.uniform(RANDOM, 1, tiles.length - 15), RandomUtils.uniform(RANDOM, 1, tiles[0].length - 15));
            if (CheckRoom(tiles, Width, Length, p.x, p.y)) {
                draw.drawRoom(p, RANDOM, Width, Length);
                numRooms++;
                roomToCenter.put(numRooms, new Position(p.x + Width / 2, p.y + Length / 2));
                //Left Corner
                roomToInitial.put(numRooms, new Position(p.x, p.y));
                roomToSize.put(numRooms, new Position(Width, Length));
                //make the hallway entrance point
//                List<Integer> edge = Arrays.asList(p.x, p.y, p.x + Width - 1, p.y + Length - 1);
//                int randomLength = RandomUtils.uniform(RANDOM, 1, Length - 2);
//                int randomWidth = RandomUtils.uniform(RANDOM, 1, Width - 2);
//                int entrance = edge.get(RandomUtils.uniform(RANDOM, 0, 4));
//                if (entrance == p.x || entrance == p.x + Width - 1) {
//                    tiles[entrance][p.y + randomLength] = Tileset.NOTHING;
//                    Position hall = new Position(entrance, p.y + randomLength);
//                    roomToSize.put(numRooms, hall);
//                }
//                else {
//                    tiles[p.x + randomWidth][entrance] = Tileset.NOTHING;
//                    Position hall = new Position(p.x + randomWidth, entrance);
//                    roomToHallwayPosition.put(numRooms, hall);
//                }
                //

            }
        }
        formGraph();
        for (Edge edges : MST.edges()) {
            int edge1 = edges.either();
            int edge2 = edges.other(edge1);
//            System.out.println(edges);
            drawhallway(tiles, edge1, edge2);
        }
        drawdoor(tiles, RANDOM);

    }
    private static boolean CheckRoom(TETile[][] tiles, int Width, int Height, int xcorner, int ycorner) {
        for (int i = xcorner - 1; i <= xcorner + Width; i++) {
            for (int j = ycorner - 1; j <= ycorner + Height; j++) {
                if (!tiles[i][j].equals(Tileset.NOTHING)) {
                    return false;
                }
            }
        }
        return true;
    }


    private static void formGraph() {
        EdgeWeightedGraph graph = new EdgeWeightedGraph(numRooms + 1);
        for (int i = 1; i <= numRooms; i++) {
            for (int j = i + 1; j <= numRooms; j++) {
                int weight = findweight(roomToCenter.get(i), roomToCenter.get(j));
                Edge edge = new Edge(i, j, weight);
                graph.addEdge(edge);
            }
        }
        MST = new KruskalMST(graph);
    }

//finds distance from one hallway entrance to another
    private static int findweight(Position i, Position j) {
        int width = abs(i.x - j.x);
        int length = abs(i.y - j.y);
        return width + length;
    }

    private static void drawhallway(TETile[][] tiles, int first, int next) {
        Position room1 = roomToCenter.get(first);
        Position room2 = roomToCenter.get(next);
        Position top1 = new Position(roomToCenter.get(first).x,
                roomToInitial.get(first).y + roomToSize.get(first).y);
        Position top2 = new Position(roomToCenter.get(next).x,
                roomToInitial.get(next).y + roomToSize.get(next).y);
//        if (distance(room1, room2, true) > 0 && distance(room1, top2, false) > 1) {
//            //draw up then right from room2 to room1
//            UpAndRight(next, first);
//        }
//        else if (distance(room1, room2, true) < 0 && distance(top1, room2, false) < -1) {
//            //draw up then right from room2 to room1
//            UpAndRight(first, next);
//        }
//        else if (distance(room1, room2, true) < 0 && distance(room1, top2, false) > 1) {
//            UpAndLeft(next, first);
//        }
//        else if (distance(room1, room2, true) > 0 && distance(top1, room2, false) < -1) {
//            UpAndLeft(first, next);
//        }
        if (distance(room1, room2, true) >= 0 && distance(room1, room2, false) > 3) {
            //draw up then right from room2 to room1
            UpAndRight(tiles, next, first);
        }
        else if (distance(room1, room2, true) <= 0 && distance(room1, room2, false) < -3) {
            //draw up then right from room2 to room1
            UpAndRight(tiles, first, next);
        }
        else if (distance(room1, room2, true) <= 0 && distance(room1, room2, false) > 3) {
            UpAndLeft(tiles, next, first);
        }
        else if (distance(room1, room2, true) >= 0 && distance(room1, room2, false) < -3) {
            UpAndLeft(tiles, first, next);
        }
        else {
            if (roomToCenter.get(first).x - roomToCenter.get(next).x > 0) {
//                if (top1.y <= roomToCenter.get(next).y) {
//                    hallwayLeft(first, next);
//                } else {
                hallwayRight(tiles, next, first);
            //}
            }
            else {
//                if (top2.y <= roomToCenter.get(first).y) {
//                    hallwayLeft(next, first);
//                } else {
                    hallwayRight(tiles, first, next);
                //}
            }
        }
    }

    private static int distance(Position firstSide, Position secondSide, boolean width) {
        if (width) {
            return firstSide.x - secondSide.x;
        }
        else {
            return firstSide.y - secondSide.y;
        }
    }

    private static void UpAndRight(TETile[][] tiles, int first, int second) {
        Position top = new Position(roomToCenter.get(first).x,
                roomToInitial.get(first).y + roomToSize.get(first).y);
        int xdistance = roomToCenter.get(second).x - top.x;
        int ydistance = roomToCenter.get(second).y - top.y;
        if (ydistance >= -1) {
            tiles[top.x][top.y - 1] = Tileset.FLOOR;
        }
        for (int i = 0; i < ydistance + 2; i++) {
//            if (tiles[top.x + 1][top.y + i] == Tileset.NOTHING || tiles[top.x - 1][top.y + i] == Tileset.NOTHING) {
            if (tiles[top.x + 1][top.y + i] == Tileset.FLOOR) {
                tiles[top.x + 1][top.y + i] = Tileset.FLOOR;
            } else {
                tiles[top.x + 1][top.y + i] = Tileset.WALL;
            }
            if (tiles[top.x - 1][top.y + i] == Tileset.FLOOR) {
                tiles[top.x - 1][top.y + i] = Tileset.FLOOR;
            } else {
                tiles[top.x - 1][top.y + i] = Tileset.WALL;
            }
                tiles[top.x][top.y + i] = Tileset.FLOOR;
//            }
//            else {
//                tiles[top.x][top.y + i] = Tileset.FLOOR;
//                break;
//            }
        }
        //left two columns need to be higher

        if (tiles[top.x - 1][top.y + ydistance + 1] == Tileset.FLOOR) {
            tiles[top.x - 1][top.y + ydistance + 1] = Tileset.FLOOR;
        }
        else {
            tiles[top.x - 1][top.y + ydistance + 1] = Tileset.WALL;
        }
        if (tiles[top.x - 1][top.y + ydistance + 2] == Tileset.FLOOR) {
            tiles[top.x - 1][top.y + ydistance + 2] = Tileset.FLOOR;
        }
        else {
            tiles[top.x - 1][top.y + ydistance + 2] = Tileset.WALL;
        }
        tiles[top.x][top.y + ydistance + 1] = Tileset.FLOOR;
        for (int i = 1; i < xdistance + 1; i++) {

                if (tiles[top.x - 1 + i][top.y + ydistance + 2] == Tileset.FLOOR) {
                    tiles[top.x - 1 + i][top.y + ydistance + 2] = Tileset.FLOOR;
                }
                else {
                    tiles[top.x - 1 + i][top.y + ydistance + 2] = Tileset.WALL;
                }
                tiles[top.x + i][top.y + ydistance + 1] = Tileset.FLOOR;
                if (tiles[top.x + 1 + i][top.y + ydistance] == Tileset.FLOOR) {
                    tiles[top.x + 1 + i][top.y + ydistance] = Tileset.FLOOR;
                }
                else {
                    tiles[top.x + 1 + i][top.y + ydistance] = Tileset.WALL;
                }
            }
//            else {
//                tiles[top.x + i ][top.y + ydistance + 1] = Tileset.FLOOR;
//                tiles[top.x + i + 1][top.y + ydistance + 1] = Tileset.FLOOR;
//                tiles[top.x + i - 1][top.y + ydistance + 2] = Tileset.WALL;
//                tiles[top.x + i][top.y + ydistance + 2] = Tileset.WALL;
//                return;
//            }



    }



    private static void UpAndLeft(TETile[][] tiles, int first, int second) {
        Position top = new Position(roomToCenter.get(first).x,
                roomToInitial.get(first).y + roomToSize.get(first).y);
        int xdistance = -(roomToCenter.get(second).x - top.x);
        int ydistance = roomToCenter.get(second).y - top.y;
        if (ydistance >= -1) {
            tiles[top.x][top.y - 1] = Tileset.FLOOR;
        }
        for (int i = 0; i < ydistance + 1; i++) {

                if (tiles[top.x + 1][top.y + i] == Tileset.FLOOR) {
                    tiles[top.x + 1][top.y + i] = Tileset.FLOOR;
                } else {
                    tiles[top.x + 1][top.y + i] = Tileset.WALL;
                }
                if (tiles[top.x - 1][top.y + i] == Tileset.FLOOR) {
                    tiles[top.x - 1][top.y + i] = Tileset.FLOOR;
                } else {
                    tiles[top.x - 1][top.y + i] = Tileset.WALL;
                }
                tiles[top.x][top.y + i] = Tileset.FLOOR;
            }
//            else {
//                tiles[top.x][top.y + i] = Tileset.FLOOR;
//                return;
//            }

        //right two columns need to be higher
        if (tiles[top.x + 1][top.y + ydistance + 1] == Tileset.FLOOR) {
            tiles[top.x + 1][top.y + ydistance + 1] = Tileset.FLOOR;
        }
        else {
            tiles[top.x + 1][top.y + ydistance + 1] = Tileset.WALL;
        }
        if (tiles[top.x + 1][top.y + ydistance + 2] == Tileset.FLOOR) {
            tiles[top.x + 1][top.y + ydistance + 2] = Tileset.FLOOR;
        }
        else {
            tiles[top.x + 1][top.y + ydistance + 2] = Tileset.WALL;
        }
        tiles[top.x][top.y + ydistance + 1] = Tileset.FLOOR;
        for (int i = 1; i < xdistance + 1; i++) {
                if (tiles[top.x - 1 - i][top.y + ydistance] == Tileset.FLOOR) {
                    tiles[top.x - 1 - i][top.y + ydistance] = Tileset.FLOOR;
                }
                else {
                    tiles[top.x - 1 - i][top.y + ydistance] = Tileset.WALL;
                }
                if (tiles[top.x + 1 - i][top.y + ydistance + 2] == Tileset.FLOOR) {
                    tiles[top.x + 1 - i][top.y + ydistance + 2] = Tileset.FLOOR;
                }
                else {
                    tiles[top.x + 1 - i][top.y + ydistance + 2] = Tileset.WALL;
                }
                tiles[top.x - i][top.y + ydistance + 1] = Tileset.FLOOR;
            }
//            else {
//                tiles[top.x - i][top.y + ydistance + 1] = Tileset.FLOOR;
//                tiles[top.x - i - 1][top.y + ydistance + 1] = Tileset.FLOOR;
//                tiles[top.x - i + 1][top.y + ydistance + 2] = Tileset.WALL;
//                tiles[top.x - i][top.y + ydistance + 2] = Tileset.WALL;
//                return;
//            }


    }

    private static void hallwayRight(TETile[][] tiles, int first, int second) {
        Position right = new Position(roomToInitial.get(first).x + roomToSize.get(first).x,
                roomToCenter.get(first).y);
        int xdistance = roomToCenter.get(second).x - right.x;
        tiles[right.x - 1][right.y] = Tileset.FLOOR;
        for (int i = 0; i < xdistance + 1; i++) {
//            if (tiles[right.x + i][right.y + 1] == Tileset.NOTHING || tiles[right.x + i][right.y - 1] == Tileset.NOTHING) {
                if (tiles[right.x + i][right.y - 1] == Tileset.FLOOR) {
                    tiles[right.x + i][right.y - 1] = Tileset.FLOOR;
                } else {
                    tiles[right.x + i][right.y - 1] = Tileset.WALL;
                }
                if (tiles[right.x + i][right.y + 1] == Tileset.FLOOR) {
                    tiles[right.x + i][right.y + 1] = Tileset.FLOOR;
                } else {
                    tiles[right.x + i][right.y + 1] = Tileset.WALL;
                }
                tiles[right.x + i][right.y] = Tileset.FLOOR;
            }
//            else {
//                tiles[right.x + i][right.y] = Tileset.FLOOR;
//                return;
//            }
        //}
    }

//    private static void hallwayLeft(int first, int second) {
//        Position right = new Position(roomToInitial.get(second).x,
//                roomToCenter.get(second).y);
//        int xdistance = roomToCenter.get(first).x - right.x;
//        tiles[right.x + 1][right.y] = Tileset.FLOOR;
//        for (int i = 0; i > xdistance - 1; i--) {
//            if (tiles[right.x - i][right.y + 1] == Tileset.NOTHING || tiles[right.x - i][right.y - 1] == Tileset.NOTHING) {
//                tiles[right.x - i][right.y - 1] = Tileset.WALL;
//                tiles[right.x - i][right.y + 1] = Tileset.WALL;
//                tiles[right.x - i][right.y] = Tileset.FLOOR;
//            }
//            else {
//                tiles[right.x - i][right.y] = Tileset.FLOOR;
//                return;
//            }
//        }
//    }
    private static void drawdoor(TETile[][] tiles, Random rand) {
        int randomRoom = RandomUtils.uniform(rand, 1, numRooms);
        Position p = roomToInitial.get(randomRoom);
        Position size = roomToSize.get(randomRoom);
        List<Integer> edge = Arrays.asList(p.x, p.y, p.x + size.x - 1, p.y + size.y - 1);
        int randomLength = RandomUtils.uniform(rand, 1, size.y - 2);
        int randomWidth = RandomUtils.uniform(rand, 1, size.x - 2);
        int entrance = edge.get(RandomUtils.uniform(rand, 0, 4));
        Boolean goldendoor = false;
        while (goldendoor == false) {
            if (entrance == p.x || entrance == p.x + size.x - 1 && tiles[entrance][p.y + randomLength] != Tileset.FLOOR) {
                if (tiles[entrance + 1][p.y + randomLength] == Tileset.NOTHING ||
                        tiles[entrance - 1][p.y + randomLength] == Tileset.NOTHING ||
                        tiles[entrance][p.y + randomLength + 1] == Tileset.NOTHING ||
                        tiles[entrance][p.y + randomLength - 1] == Tileset.NOTHING) {
                    tiles[entrance][p.y + randomLength] = Tileset.LOCKED_DOOR;
                    goldendoor = true;
                }
            }
            else if (entrance == p.y || entrance == p.y + size.y - 1 && tiles[p.x + randomWidth][entrance] != Tileset.FLOOR) {
                if (tiles[p.x + randomWidth + 1][entrance] == Tileset.NOTHING ||
                        tiles[p.x + randomWidth - 1][entrance] == Tileset.NOTHING ||
                        tiles[p.x + randomWidth][entrance + 1] == Tileset.NOTHING ||
                        tiles[p.x + randomWidth + 1][entrance - 1] == Tileset.NOTHING) {
                    tiles[p.x + randomWidth][entrance] = Tileset.LOCKED_DOOR;
                    goldendoor = true;
                }
            }
            randomLength = RandomUtils.uniform(rand, 1, size.y - 2);
            randomWidth = RandomUtils.uniform(rand, 1, size.x - 2);
            entrance = edge.get(RandomUtils.uniform(rand, 0, 4));
        }
    }

}


