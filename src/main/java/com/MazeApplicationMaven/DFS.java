package com.MazeApplicationMaven;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Stack;

public class DFS {

    static int xSize;
    static int ySize;

    Controller controller;

    public void solveDFS(Rectangle[][] grid, int x, int y, int xS, int yS, Controller controllerParsed){
        controller = controllerParsed;
        xSize=xS;
        ySize=yS;
        controller.setAnimationActive(true);
        solve(grid, x, y);
    }

    public void reconstructPath(Rectangle[][] grid, int x, int y, int endNodeX, int endNodeY, Coord[] prev){
        Coord start=new Coord(y, x);
        Coord iterator=new Coord(endNodeY, endNodeX);
        Coord[] path=new Coord[10000];
        int i=0;

        while(iterator.getX()!=x || iterator.getY()!=y){
            path[i]=iterator;
            i++;
            iterator=prev[cellValue(iterator)];
        }
        path[i]=iterator;

        Timeline tl=new Timeline();
        Duration timepoint = Duration.ZERO ;
        Duration pause = Duration.millis(100);
        KeyFrame keyFrame;
        for(int j=0;j<i;j++){
            timepoint = timepoint.add(pause);
            Coord pathNode=path[j];
            keyFrame = new KeyFrame(timepoint, e -> visit(grid, pathNode));
            tl.getKeyFrames().add(keyFrame);
        }
        tl.play();
        tl.setOnFinished(e -> controller.setAnimationActive(false));
    }

    public void solve(Rectangle[][] grid, int x, int y){
        Timeline tl=new Timeline();
        int endNodeX=0, endNodeY=0;
        boolean found=false;
        Duration timepoint = Duration.ZERO ;
        Duration pause = Duration.millis(20);
        KeyFrame keyFrame;
        Coord[] prev=new Coord[10000];
        ArrayList<Coord> visited=new ArrayList<>();
        Stack<Rectangle> stack=new Stack<>();
        stack.push(grid[y][x]);
        while(!stack.isEmpty() && !found){
            timepoint = timepoint.add(pause);
            Rectangle r=stack.pop();
            Coord c=findRect(r, grid);
            keyFrame = new KeyFrame(timepoint, e -> visit(grid, c));
            tl.getKeyFrames().add(keyFrame);
            if(grid[c.getY()][c.getX()].getFill()== Color.RED){
                found=true;
                endNodeX=c.getX();
                endNodeY=c.getY();
            }
            addNeighbours(grid, c.getX(), c.getY(), stack, prev, visited);
        }
        tl.play();

        int finalEndNodeX = endNodeX;
        int finalEndNodeY = endNodeY;
        tl.setOnFinished(e -> reconstructPath(grid, x, y, finalEndNodeX, finalEndNodeY, prev));
    }


    public static void addNeighbours(Rectangle[][] grid, int x, int y, Stack<Rectangle> stack, Coord[] prev, ArrayList<Coord> visited){
        Coord parent=new Coord(y, x);
        if(grid[y][x-1].getFill()!=Color.BLACK && !hasCoord(visited, new Coord(y, x-1))){//left
            visited.add(new Coord(y, x-1));
            stack.push(grid[y][x-1]);
            Coord node=new Coord(y, x-1);
            prev[cellValue(node)]=parent;
        }
        if(grid[y][x+1].getFill()!=Color.BLACK && !hasCoord(visited, new Coord(y, x+1))){//right
            visited.add(new Coord(y, x+1));
            stack.push(grid[y][x+1]);
            Coord node=new Coord(y, x+1);
            prev[cellValue(node)]=parent;
        }
        if(grid[y+1][x].getFill()!=Color.BLACK && !hasCoord(visited, new Coord(y+1, x))){//down
            visited.add(new Coord(y+1, x));
            stack.push(grid[y+1][x]);
            Coord node=new Coord(y+1, x);
            prev[cellValue(node)]=parent;
        }
        if(grid[y-1][x].getFill()!=Color.BLACK && !hasCoord(visited, new Coord(y-1, x))){//up
            visited.add(new Coord(y-1, x));
            stack.push(grid[y-1][x]);
            Coord node=new Coord(y-1, x);
            prev[cellValue(node)]=parent;
        }
    }

    public static Coord findRect(Rectangle rect, Rectangle[][] grid){
        for(int y=0;y<ySize;y++){
            for(int x=0;x<xSize-1;x++){
                if(rect.getX()==grid[y][x].getX() && rect.getY()==grid[y][x].getY()){
                    return new Coord(y,x);
                }
            }
        }
        return null;
    }

    public static int cellValue(Coord c){
        return c.getY()*100+c.getX()+1;
    }

    public static void visit(Rectangle[][] grid, Coord c){
        if(grid[c.getY()][c.getX()].getFill()==Color.ORANGE){
            grid[c.getY()][c.getX()].setFill(Color.BLUE);
        }else {
            grid[c.getY()][c.getX()].setFill(Color.ORANGE);
        }
    }

    public static boolean hasCoord(ArrayList<Coord> visited, Coord c){
        for(Coord coord:visited){
            if(coord.getX()==c.getX() && coord.getY()==c.getY()){
                return true;
            }
        }
        return false;
    }
}
