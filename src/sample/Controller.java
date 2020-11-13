package sample;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import javax.swing.*;
import java.util.HashMap;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Controller {

    private int ySize=18;
    private int xSize=30;
    @FXML
    AnchorPane anchor;
    Rectangle[][] grid=new Rectangle[ySize][xSize];


    private boolean isGrid=false;
    private boolean isMaze=false;
    private boolean isNodes=false;
    private int startNodeX, startNodeY, endNodeX, endNodeY;

    private void isGridSet(){
        isGrid=true;
    }

    private void isMazeSet(){
        isMaze=true;
    }

    private void isNodesSet(int startNodeX, int startNodeY, int endNodeX, int endNodeY){
        isNodes=true;
        this.startNodeX=startNodeX;
        this.startNodeY=startNodeY;
        this.endNodeX=endNodeX;
        this.endNodeY=endNodeY;
    }

    //generates new grid and populates grid array appropriately
    public void generateGrid(ActionEvent actionEvent) {

        clearGrid();
        for(int j=0;j<18;j++) {
            for (int i = 0; i < 30; i++) {
                Rectangle rect = new Rectangle(i * 20, (20*j) + 28, 20, 20);
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.BLACK);
                anchor.getChildren().add(rect);
                grid[j][i]=rect;
                var sctr = new ScaleTransition(Duration.millis(1000), grid[j][i]);
                sctr.setByX(-0.2);
                sctr.setByY(-0.2);
                sctr.setCycleCount(2);
                sctr.setAutoReverse(true);
                sctr.play();
            }
        }
        isGridSet();
    }

    public void generateStartAndEnd(ActionEvent actionEvent){
        int startX, startY, endX, endY;
        if(isNodes){
            grid[startNodeY][startNodeX].setFill(Color.WHITE);
            grid[endNodeY][endNodeX].setFill(Color.WHITE);
        }
        Random rand=new Random();
        do {
            startX = rand.nextInt(xSize - 1) + 1;
            startY = rand.nextInt(ySize - 1) + 1;
            endX=rand.nextInt(xSize-1)+1;
            endY=rand.nextInt(ySize-1)+1;
        }while(Math.sqrt(Math.pow((startX-endX), 2)+Math.pow((startY-endY), 2))<10 || startY==ySize-1 || endY==ySize-1 || startX==xSize-1 || endX==xSize-1);
        grid[startY][startX].setFill(Color.BLUE);
        grid[endY][endX].setFill(Color.RED);
        isNodesSet(startX, startY, endX, endY);
    }

    //clears old grid if new one is being generated:
    private void clearGrid(){
        if(isGrid){
            for(int i=0;i<18;i++) {
                for(int j=0;j<30;j++) {
                    anchor.getChildren().remove(grid[i][j]);
                }
            }
        }else {
            return;
        }
    }

    public void generateMaze1(ActionEvent actionEvent) {

        if(!isGrid){
            generateGrid(actionEvent);
        }

        if(isMaze){
            clearGrid();
            generateGrid(actionEvent);
        }
        isMazeSet();
        outline();
        recursiveQuadrant(0, 29, 0, 17);
    }

    public void generateMaze2(ActionEvent actionEvent) {
        int orientation=0;//1=vertical 0=horizontal
        if(!isGrid){
            generateGrid(actionEvent);
        }

        if(isMaze){
            clearGrid();
            generateGrid(actionEvent);
        }
        isMazeSet();
        outline();
        if(xSize>ySize){
            orientation=0;
        }
        recursiveDivision(0, 29, 0, 17, orientation);
    }



    //maze generation algorithims:
    public void recursiveQuadrant(int startX, int endX, int startY, int endY){
        Random rand=new Random();
        KeyFrame keyFrame;
        int randomY, randomX, randomOpening;
        int height=endY-startY;
        int width=endX-startX;
        Timeline timeline = new Timeline();
        Duration timepoint = Duration.ZERO ;
        Duration pause = Duration.millis(100);
        if((height<=3 && width<=3) || height==1 || width==1){
            return;
        }
        if(height>width){//horizontal bisection
            do{
                randomY=rand.nextInt(endY-startY)+startY;
            }while(randomY==startY || randomY==startY+1 || randomY==endY-1);
            do{
                randomOpening=rand.nextInt(endX-startX)+startX;
            }while(randomOpening==startX);
            for(int i=startX;i<endX;i++){
                if(i!=randomOpening){
                    Rectangle r=grid[randomY][i];
                    timepoint = timepoint.add(pause);
                    keyFrame = new KeyFrame(timepoint, e -> r.setFill(Color.BLACK));
                    timeline.getKeyFrames().add(keyFrame);
                }
            }
            int rY=randomY;
            int rO=randomOpening;
            if(randomOpening==endX-1 || randomOpening==startX+1) {
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(startX, endX, startY, rY));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(startX, endX, rY, endY));
                timeline.getKeyFrames().add(keyFrame);
                timeline.play();
            }else{
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(startX, rO, startY, rY));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(rO, endX, startY, rY));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(startX, rO, rY, endY));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(rO, endX, rY, endY));
                timeline.getKeyFrames().add(keyFrame);
                timeline.play();
            }
        }else{//vertical bisection
            do {
                randomX = rand.nextInt(endX-startX)+startX;
            }while(randomX==startX || randomX==startX+1 ||randomX==endX-1);
            do {
                randomOpening = rand.nextInt(endY-startY)+startY;
            }while(randomOpening==startY);
            for(int i=startY;i<endY;i++){
                if(i!=randomOpening) {
                    Rectangle r=grid[i][randomX];
                    timepoint = timepoint.add(pause);
                    keyFrame = new KeyFrame(timepoint, e -> r.setFill(Color.BLACK));
                    timeline.getKeyFrames().add(keyFrame);
                }
            }
            int rX=randomX;
            int rO=randomOpening;
            if(randomOpening==endY-1 || randomOpening==startY+1) {
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(startX, rX, startY, endY));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(rX, endX, startY, endY));
                timeline.getKeyFrames().add(keyFrame);
                timeline.play();
            }else{
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(startX, rX, startY, rO));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(rX, endX, startY, rO));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(startX, rX, rO, endY));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(rX, endX, rO, endY));
                timeline.getKeyFrames().add(keyFrame);
                timeline.play();

            }
        }

    }

    public void recursiveDivision(int startX, int endX, int startY, int endY, int orientation){

    }


    public void outline(){

        for(int i=0;i<18;i++){
            for(int j=0;j<30;j++){
                if((i!=0 && i!=17) && (j>0 && j<29)){
                    j=29;
                }
                ScaleTransition sctr =new ScaleTransition(Duration.millis(1000), grid[i][j]);
                sctr.setByX(-0.2);
                sctr.setByY(-0.2);
                sctr.setCycleCount(2);
                sctr.setAutoReverse(true);
                FillTransition ft=new FillTransition(Duration.millis(1000), grid[i][j], Color.WHITE, Color.BLACK);
                ParallelTransition pt=new ParallelTransition();
                pt.getChildren().add(sctr);
                pt.getChildren().add(ft);
                pt.play();
            }
        }
    }


    //maze solving algorithims:
    public void solveRandomly(ActionEvent actionEvent){
        solveRandom(startNodeX, startNodeY);
    }

    public void solveRandom(int x, int y){
        if(grid[y][x].getFill()==Color.RED){
            grid[y][x].setFill(Color.GREEN);
            return;
        }
        Timeline timeline = new Timeline();
        Duration timepoint = Duration.ZERO ;
        Duration pause = Duration.millis(100);
        KeyFrame keyFrame;
        if(grid[y][x].getFill()==Color.GREEN) {
            grid[y][x].setFill(Color.YELLOW);
        }else{
            grid[y][x].setFill(Color.GREEN);
        }
        int newX=0, newY=0;
        int found=0;
        do {
            int direction = ThreadLocalRandom.current().nextInt(1, 4 + 1);
            switch(direction){
                case 1://up
                    if(grid[y][x-1].getFill()!=Color.BLACK){
                        found=1;
                        newX=x;
                        newY=y-1;
                    }
                    break;
                case 2://right
                    if(grid[x+1][y].getFill()!=Color.BLACK){
                        found=1;
                        newX=x+1;
                        newY=y;
                    }
                    break;
                case 3://down
                    if(grid[x][y+1].getFill()!=Color.BLACK){
                        found=1;
                        newX=x;
                        newY=y+1;
                    }
                    break;
                case 4://left
                    if(grid[x-1][y].getFill()!=Color.BLACK){
                        found=1;
                        newX=x-1;
                        newY=y;
                    }
                    break;
            }
        }while(found==0);
        int finalX=newX, finalY=newY;
        timepoint = timepoint.add(pause);
        keyFrame = new KeyFrame(timepoint, e -> solveRandom(finalX,finalY));
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        }




        /*public void solveDFS(int x, int y){
            Timeline timeline = new Timeline();
            Duration timepoint = Duration.ZERO ;
            Duration pause = Duration.millis(180);
            KeyFrame keyFrame;
            if(x==endNodeX && y==endNodeY){
                grid[x][y].setFill(Color.GREEN);
                return;
            }
            if(grid[endNodeX][endNodeY].getFill()==Color.GREEN){
                return;
            }
            grid[x][y].setFill(Color.GREEN);
            if(grid[x][y-1].getFill()==Color.WHITE || grid[x][y-1].getFill()==Color.RED){//up
                timepoint = timepoint.add(pause);
                keyFrame = new KeyFrame(timepoint, e -> solveBFS(grid, x,y-1, endNodeX, endNodeY));
                timeline.getKeyFrames().add(keyFrame);
            }
            if(grid[x+1][y].getFill()==Color.WHITE || grid[x+1][y].getFill()==Color.RED){//right
                timepoint = timepoint.add(pause);
                keyFrame = new KeyFrame(timepoint, e -> solveBFS(grid, x+1,y, endNodeX, endNodeY));
                timeline.getKeyFrames().add(keyFrame);
            }
            if(grid[x][y+1].getFill()==Color.WHITE || grid[x][y+1].getFill()==Color.RED){//down
                timepoint = timepoint.add(pause);
                keyFrame = new KeyFrame(timepoint, e -> solveBFS(grid, x,y+1, endNodeX, endNodeY));
                timeline.getKeyFrames().add(keyFrame);
            }
            if(grid[x-1][y].getFill()==Color.WHITE || grid[x-1][y].getFill()==Color.RED){//left
                timepoint = timepoint.add(pause);
                keyFrame = new KeyFrame(timepoint, e -> solveBFS(grid, x-1,y, endNodeX, endNodeY));
                timeline.getKeyFrames().add(keyFrame);
            }
            timeline.play();
        }*/


    public void depthFirstSearch(ActionEvent actionEvent) {
        BFS.solveBFS(grid, startNodeX, startNodeY, endNodeX, endNodeY);
    }


}

