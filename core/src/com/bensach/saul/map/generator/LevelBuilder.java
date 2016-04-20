package com.bensach.saul.map.generator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bensach.saul.map.CellType;
import com.bensach.saul.map.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by saul- on 18/04/2016.
 */
public class LevelBuilder {

    private CellType[][] cells;
    private ArrayList<Room> rooms;
    private boolean firstRoom = false, endRoom = false;
    private int width, height, numRooms, roomMaxWidth, roomMinWidth, roomMaxHeight, roomMinHeight;
    private Vector2 playerStart;
    private ArrayList<Rectangle> walls;

    private int pointNumber;
    private ArrayList<Point> points;
    private ArrayList<Edge> edges;
    private ArrayList<Edge> mstEdges;

    private TiledMap map;
    private int cellSize = 16;
    private TextureRegion grass = new TextureRegion(new Texture(Gdx.files.internal("map/grass.png")));
    private TextureRegion dirt = new TextureRegion(new Texture(Gdx.files.internal("map/dirt.png")));
    private TextureRegion stone = new TextureRegion(new Texture(Gdx.files.internal("map/stone.png")));

    public LevelBuilder(int width, int height, int numRooms, int roomMaxWidth, int roomMinWidth, int roomMaxHeight, int roomMinHeight) {
        this.roomMinHeight  = roomMinHeight;
        this.roomMaxHeight  = roomMaxHeight;
        this.roomMinWidth   = roomMinWidth;
        this.roomMaxWidth   = roomMaxWidth;
        this.numRooms       = numRooms;
        this.height         = height;
        this.width          = width;
        pointNumber         = 0;
        playerStart         = new Vector2();
        map                 = new TiledMap();
        cells               = new CellType[width][height];
        walls               = new ArrayList<Rectangle>();
        rooms               = new ArrayList<Room>();
        edges               = new ArrayList<Edge>();
        mstEdges            = new ArrayList<Edge>();
        points              = new ArrayList<Point>();
        buildLevel();
    }

    public TiledMap getMap(){
        return map;
    }

    private void buildLevel(){
        blankWorld();
        generateRooms();
        delaunayVertex();
        mst();
        generateCorridors();
        toTiled();
    }

    private void blankWorld(){
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                cells[x][y] = CellType.Empty;
                if(y == 0 || x == 0 || x == width - 1 || y == height - 1){
                    cells[x][y] = CellType.Wall;
                }
            }
        }
    }

    private void generateRooms(){
        int roomsGenerated = 0, roomWidth, roomHeight, px, py;
        Random rand = new Random();
        while (roomsGenerated < numRooms){
            while(true){
                roomWidth   = rand.nextInt((roomMaxWidth   - roomMinWidth)    + 1) + roomMinWidth;
                roomHeight  = rand.nextInt((roomMaxHeight  - roomMinHeight)   + 1) + roomMinHeight;
                px          = rand.nextInt(width);
                py          = rand.nextInt(height);

                if(checkValues(px,py,roomWidth,roomHeight))
                    if(!overlapRoom(px,py,roomWidth,roomHeight))
                        break;
            }
            placeRoom(px,py,roomWidth,roomHeight);
            points.add(new Point(px + roomWidth / 2, py + roomHeight / 2, pointNumber++));
            rooms.add(new Room(px, roomHeight, roomWidth, py));
            roomsGenerated++;
        }
    }

    private void delaunayVertex(){
        for(int i = 0; i < points.size(); i++){
            for(int j = i + 1; j < points.size(); j++){
                for(int k = j + 1; k < points.size(); k++){
                    boolean isTriangle = true;
                    for(int a = 0; a < points.size(); a++){
                        if(a == i || a == j || a == k)continue;
                        if(points.get(a).inside(points.get(i), points.get(j), points.get(k))){
                            isTriangle = false;
                            break;
                        }
                    }
                    if(isTriangle){
                        edges.add(new Edge(points.get(i), points.get(j)));
                        edges.add(new Edge(points.get(i), points.get(k)));
                        edges.add(new Edge(points.get(j), points.get(j)));
                    }
                }
            }
        }
    }

    private void mst(){
        Collections.sort(edges);
        Graph graph = new Graph(points.size(), edges.size());

        for(int i = 0; i < edges.size(); i++){
            graph.edge[i].src = edges.get(i).getP1().getValue();
            graph.edge[i].dest = edges.get(i).getP2().getValue();
            graph.edge[i].weight = (int) edges.get(i).getWeight();
        }

        Edge[] result = graph.KruskalMST();
        for(int i = 0; i < result.length; i++){
            for(int j = 0; j < edges.size(); j++){
                if(edges.get(j).weight == result[i].weight){
                    mstEdges.add(edges.get(j));
                }
            }
        }

    }

    private void generateCorridors(){
        Pathfinder pathfinder = new Pathfinder(width, height, 1);
        for(Edge e: mstEdges){
            pathfinder.SetGridNode((int) e.getP1().getX(), (int) e.getP1().getY(), CellType.Start);
            pathfinder.SetGridNode((int) e.getP2().getX(), (int) e.getP2().getY(), CellType.End);
            pathfinder.findPath();

            Array<GridNode> nodes = pathfinder.GetPath();
            for(GridNode n : nodes){
                for(int i = 0; i < 3; i++){
                    cells[(int)n.X][(int)n.Y] = CellType.Corridor;
                    cells[(int)n.X + i][(int)n.Y] = CellType.Corridor;
                    cells[(int)n.X - i][(int)n.Y] = CellType.Corridor;
                    cells[(int)n.X][(int)n.Y + i] = CellType.Corridor;
                    cells[(int)n.X][(int)n.Y - i] = CellType.Corridor;
                    cells[(int)n.X + i][(int)n.Y + i] = CellType.Corridor;
                    cells[(int)n.X - i][(int)n.Y - i] = CellType.Corridor;
                    cells[(int)n.X + i][(int)n.Y - i] = CellType.Corridor;
                    cells[(int)n.X - i][(int)n.Y + i] = CellType.Corridor;
                }
            }

            for(Room r : rooms){
                for(int y = r.getY(); y < r.getHeight() + r.getY(); y++){
                    for(int x = r.getX(); x < r.getWidth() + r.getX(); x++){
                        if(y != 0 || x != 0 || x != width - 1 || y != height - 1){
                            cells[x][y] = CellType.Floor;
                        }
                    }
                }
            }


        }
    }

    //TODO comprobar si lo necesido realmente ya que box2d me proporciona hitbox
    //?¿?¿?¿
    /*private void generateHitbox(){

    }*/

    private void toTiled(){
        MapLayers layers = map.getLayers();
        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, cellSize,cellSize);
        for(int y = 0; y < layer.getHeight(); y++){
            for(int x = 0; x < layer.getWidth(); x++){
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                //TODO añadir todas las texturas correctas!
                switch (cells[x][y]){
                    case Empty: cell.setTile(new StaticTiledMapTile(dirt));break;
                    case Floor: cell.setTile(new StaticTiledMapTile(grass));break;
                    case Wall: cell.setTile(new StaticTiledMapTile(dirt));break;
                    case Corridor: cell.setTile(new StaticTiledMapTile(stone));break;
                }
                layer.setCell(x,y,cell);
            }
        }
        layers.add(layer);
    }

    //TODO necesario?
    private void toBox2d(){

    }

    private void placeRoom(int px, int py, int width, int height){
        for(int y = py; y < height + py; y++){
            for(int x = px; x < width + px; x++){
                cells[x][y] = CellType.Floor;
            }
        }

        if(!endRoom && firstRoom){
            cells[px + width / 2][py + height / 2] = CellType.End;
            endRoom = true;
        }

        if(!firstRoom){
            cells[px + width / 2][py + height / 2] = CellType.Start;
            playerStart.set((px + width / 2) * cellSize, (py + height / 2) * cellSize);
            firstRoom = true;
        }

    }

    private boolean checkValues(int x, int y, int width, int height){
        if(x < 0 || y < 0)          return false;
        if(x + width > this.width)  return false;
        if(y + height > this.height)return false;
        return true;
    }

    private void generateHitbox(){
        for(int y = 1; y < height - 1; y++){
            for(int x = 1; x < width - 1; x++){

                //Coloca las paredes de las salas y los pasillos
                if(cells[x][y] == CellType.Empty){
                    if(
                            cells[x + 1][y] == CellType.Floor || cells[x + 1][y] == CellType.Corridor ||
                                    cells[x - 1][y] == CellType.Floor || cells[x - 1][y] == CellType.Corridor ||
                                    cells[x][y + 1] == CellType.Floor || cells[x][y + 1] == CellType.Corridor ||
                                    cells[x][y - 1] == CellType.Floor || cells[x][y - 1] == CellType.Corridor ||
                                    cells[x + 1][y + 1] == CellType.Floor || cells[x][y + 1] == CellType.Corridor ||
                                    cells[x - 1][y - 1] == CellType.Floor || cells[x][y - 1] == CellType.Corridor ||
                                    cells[x - 1][y + 1] == CellType.Floor || cells[x][y + 1] == CellType.Corridor ||
                                    cells[x + 1][y - 1] == CellType.Floor || cells[x][y - 1] == CellType.Corridor
                            ){
                        walls.add(new Rectangle(x,y,1,1));
                        cells[x][y] = CellType.Wall;
                    }
                }

            }
        }
    }

    private boolean overlapRoom(int px, int py, int width, int height){
        for(int y = py; y < height + py; y++){
            for(int x = px; x < width + px; x++){
                if(cells[x][y] == CellType.Floor)return true;
            }
        }
        return false;
    }

    public Vector2 getPlayerStart() {
        return playerStart;
    }
    public ArrayList<Rectangle> getWalls() {
        return walls;
    }
}
