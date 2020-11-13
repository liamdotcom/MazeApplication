package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BFS {


    public static void solveBFS(Rectangle[][] grid, int x, int y, int endNodeX, int endNodeY){
        Timeline tl=new Timeline();
        Duration timepoint = Duration.ZERO ;
        Duration pause = Duration.millis(180);
        KeyFrame keyFrame;
        Coord[] prev;
        prev=solve(grid, x, y);


    }

    public static void reconstructPath(Rectangle[][] grid, int x, int y, int endNodeX, int endNodeY, Coord[] prev){
        Coord start=new Coord(y, x);
        Coord iterator=new Coord(endNodeY, endNodeX);
        Coord[] path=new Coord[2000];
        int i=0;

        while(iterator.getX()!=x || iterator.getY()!=y){
            path[i]=iterator;
            i++;
            iterator=prev[cellValue(iterator)];
        }
        path[i]=iterator;

        Timeline tl=new Timeline();
        Duration timepoint = Duration.ZERO ;
        Duration pause = Duration.millis(180);
        KeyFrame keyFrame;
        for(int j=0;j<i;j++){
            timepoint = timepoint.add(pause);
            Coord pathNode=path[j];
            keyFrame = new KeyFrame(timepoint, e -> visit(grid, pathNode));
            tl.getKeyFrames().add(keyFrame);
        }
        tl.play();
    }

    public static Coord[] solve(Rectangle[][] grid, int x, int y){
        Timeline tl=new Timeline();
        int endNodeX=0, endNodeY=0;
        boolean found=false;
        Duration timepoint = Duration.ZERO ;
        Duration pause = Duration.millis(30);
        KeyFrame keyFrame;
        Coord[] prev=new Coord[2000];
        ArrayList<Coord> visited=new ArrayList<>();
        Queue<Rectangle> q=new LinkedList<>();
        q.add(grid[y][x]);
        while(!q.isEmpty() && !found){
            timepoint = timepoint.add(pause);
            Rectangle r=q.remove();
            Coord c=findRect(r, grid);
            keyFrame = new KeyFrame(timepoint, e -> visit(grid, c));
            tl.getKeyFrames().add(keyFrame);
            if(grid[c.getY()][c.getX()].getFill()==Color.RED){
                found=true;
                endNodeX=c.getX();
                endNodeY=c.getY();
            }
            addNeighbours(grid, c.getX(), c.getY(), q, prev, visited);
            }
        tl.play();

        int finalEndNodeX = endNodeX;
        int finalEndNodeY = endNodeY;
        tl.setOnFinished(e -> reconstructPath(grid, x, y, finalEndNodeX, finalEndNodeY, prev));
        return prev;
    }


    public static void addNeighbours(Rectangle[][] grid, int x, int y, Queue<Rectangle> q, Coord[] prev, ArrayList<Coord> visited){
        Coord parent=new Coord(y, x);
        if(grid[y][x-1].getFill()!=Color.BLACK && !hasCoord(visited, new Coord(y, x-1))){//left
            visited.add(new Coord(y, x-1));
            q.add(grid[y][x-1]);
            Coord node=new Coord(y, x-1);
            prev[cellValue(node)]=parent;
        }
        if(grid[y][x+1].getFill()!=Color.BLACK && !hasCoord(visited, new Coord(y, x+1))){//right
            visited.add(new Coord(y, x+1));
            q.add(grid[y][x+1]);
            Coord node=new Coord(y, x+1);
            prev[cellValue(node)]=parent;
        }
        if(grid[y+1][x].getFill()!=Color.BLACK && !hasCoord(visited, new Coord(y+1, x))){//down
            visited.add(new Coord(y+1, x));
            q.add(grid[y+1][x]);
            Coord node=new Coord(y+1, x);
            prev[cellValue(node)]=parent;
        }
        if(grid[y-1][x].getFill()!=Color.BLACK && !hasCoord(visited, new Coord(y-1, x))){//up
            visited.add(new Coord(y-1, x));
            q.add(grid[y-1][x]);
            Coord node=new Coord(y-1, x);
            prev[cellValue(node)]=parent;
        }
    }

    public static Coord findRect(Rectangle rect, Rectangle[][] grid){
        for(int y=0;y<18;y++){
            for(int x=0;x<29;x++){
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
        if(grid[c.getY()][c.getX()].getFill()==Color.GREEN){
            grid[c.getY()][c.getX()].setFill(Color.YELLOW);
        }else {
            grid[c.getY()][c.getX()].setFill(Color.GREEN);
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


