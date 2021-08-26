package com.MazeApplicationMaven;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Prim {

    Controller controller;

    public void generatePrim(Rectangle[][] grid, int xSize, int ySize, Controller controllerparsed){
        controller = controllerparsed;
        controller.setAnimationActive(true);
        Timeline tl=new Timeline();
        Duration timepoint = Duration.ZERO ;
        Duration pause = Duration.millis(3);
        KeyFrame keyFrame;
        for(int i=0;i<ySize;i++){
            for(int j=0;j<xSize;j++){
                timepoint = timepoint.add(pause);
                Rectangle r=grid[i][j];
                keyFrame = new KeyFrame(timepoint, e -> shade(r));
                tl.getKeyFrames().add(keyFrame);
            }
        }
        tl.play();

        int randomX = ThreadLocalRandom.current().nextInt(1, (xSize-2) + 1);
        int randomY = ThreadLocalRandom.current().nextInt(1, (ySize-2) + 1);
        Coord start=new Coord(randomY, randomX);
        tl.setOnFinished(e -> startGen(grid, start, xSize, ySize));

    }

    public void startGen(Rectangle[][] grid, Coord start, int xSize, int ySize){
        Timeline tl=new Timeline();
        Duration timepoint = Duration.ZERO ;
        Duration pause = Duration.millis(10);
        KeyFrame keyFrame;
        ArrayList<Coord> q=new ArrayList<>();
        ArrayList<Coord> visited=new ArrayList<>();
        q.add(start);
        while(!q.isEmpty()){
            int randomIndex=ThreadLocalRandom.current().nextInt(0, q.size());
            Coord c=q.remove(randomIndex);
            if(makePassage(grid, c, visited)){
                timepoint = timepoint.add(pause);
                keyFrame = new KeyFrame(timepoint, e -> grid[c.getY()][c.getX()].setFill(Color.WHITE));
                tl.getKeyFrames().add(keyFrame);
                visited.add(c);
                addWalls(grid, c, q, xSize, ySize, visited);
            }
        }
        tl.play();
        tl.setOnFinished(e -> controller.setAnimationActive(false));
    }

    public static boolean makePassage(Rectangle[][] grid, Coord c, ArrayList<Coord> visited){
        int count=0;
        if(!hasVisited(visited, new Coord(c.getY()-1, c.getX()))){//up
            count++;
        }
        if(!hasVisited(visited, new Coord(c.getY()+1, c.getX()))){//down
            count++;
        }
        if(!hasVisited(visited, new Coord(c.getY(), c.getX()+1))){//right
            count++;
        }
        if(!hasVisited(visited, new Coord(c.getY(), c.getX()-1))){//left
            count++;
        }
        if(count>=3){
            return true;
        }else{
            return false;
        }

    }

    public static void addWalls(Rectangle[][] grid, Coord c, ArrayList<Coord> q, int xSize, int ySize, ArrayList<Coord> visited){
        if(!hasVisited(visited, new Coord(c.getY()-1, c.getX()))){//up
            if(c.getY()-1!=0) {
                q.add(new Coord(c.getY() - 1, c.getX()));
            }
        }
        if(!hasVisited(visited, new Coord(c.getY()+1, c.getX()))){//down
            if(c.getY()+1!=ySize-1) {
                q.add(new Coord(c.getY() + 1, c.getX()));
            }
        }
        if(!hasVisited(visited, new Coord(c.getY(), c.getX()+1))){//right
            if(c.getX()+1!=xSize-1) {
                q.add(new Coord(c.getY(), c.getX() + 1));
            }
        }
        if(!hasVisited(visited, new Coord(c.getY(), c.getX()-1))){//left
            if(c.getX()-1!=0) {
                q.add(new Coord(c.getY(), c.getX() - 1));
            }
        }
    }


    public static void shade(Rectangle r){
        r.setFill(Color.BLACK);
    }

    public static boolean hasVisited(ArrayList<Coord> visited, Coord c){
        for(Coord visit: visited){
            if(c.getX()==visit.getX() && c.getY()==visit.getY()){
                return true;
            }
        }
        return false;
    }
}
