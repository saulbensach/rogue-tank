package com.bensach.saul.map.generator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.bensach.saul.map.CellType;
import com.bensach.saul.map.Room;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by saul- on 18/04/2016.
 */
public class LevelBuilder {

    private CellType[][] cells;
    private ArrayList<Room> rooms;
    private boolean firstRoom = false, endRoom = false;
    private int width, height, numRooms, roomMaxWidth, roomMinWidth, roomMaxHeight, roomMinHeight;

    private int pointNumber;
    private ArrayList<Point> points;

    private TiledMap map;
    private int cellSize = 16;
    private TextureRegion grass = new TextureRegion(new Texture(Gdx.files.internal("map/grass.png")));
    private TextureRegion dirt = new TextureRegion(new Texture(Gdx.files.internal("map/dirt.png")));

    public LevelBuilder(int width, int height, int numRooms, int roomMaxWidth, int roomMinWidth, int roomMaxHeight, int roomMinHeight) {
        this.roomMinHeight  = roomMinHeight;
        this.roomMaxHeight  = roomMaxHeight;
        this.roomMinWidth   = roomMinWidth;
        this.roomMaxWidth   = roomMaxWidth;
        this.numRooms       = numRooms;
        this.height         = height;
        this.width          = width;
        pointNumber         = 0;
        map                 = new TiledMap();
        cells               = new CellType[width][height];
        rooms               = new ArrayList<Room>();
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
            roomsGenerated++;
        }
    }

    private void delaunayVertex(){

    }

    private void mst(){

    }

    private void generateCorridors(){

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
    }

    private boolean checkValues(int x, int y, int width, int height){
        if(x < 0 || y < 0)          return false;
        if(x + width > this.width)  return false;
        if(y + height > this.height)return false;
        return true;
    }

    private boolean overlapRoom(int px, int py, int width, int height){
        for(int y = py; y < height + py; y++){
            for(int x = px; x < width + px; x++){
                if(cells[x][y] == CellType.Floor)return true;
            }
        }
        return false;
    }

}
