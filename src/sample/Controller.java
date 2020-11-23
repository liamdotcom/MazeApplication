package sample;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.Random;


public class Controller {

    private int ySize=28;
    private int xSize=50;
    @FXML
    AnchorPane anchor;
    Rectangle[][] grid=new Rectangle[ySize][xSize];


    private boolean isGrid=false;
    private boolean isMaze=false;
    private boolean isNodes=false;
    private static boolean animationActive=false;
    private int startNodeX=0, startNodeY=0, endNodeX=0, endNodeY=0;

    public static void setAnimationActive(boolean state){
        animationActive=state;
    }

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
        if(!animationActive){
            newGrid();
        }

    }

    public void generateStartAndEnd(ActionEvent actionEvent){
        if(!animationActive) {
            int startX, startY, endX, endY;
            if (isNodes) {
                grid[startNodeY][startNodeX].setFill(Color.WHITE);
                grid[endNodeY][endNodeX].setFill(Color.WHITE);
            }
            Random rand = new Random();
            do {
                startX = rand.nextInt(xSize - 1) + 1;
                startY = rand.nextInt(ySize - 1) + 1;
                endX = rand.nextInt(xSize - 1) + 1;
                endY = rand.nextInt(ySize - 1) + 1;
            } while (Math.sqrt(Math.pow((startX - endX), 2) + Math.pow((startY - endY), 2)) < 20 || startY == ySize - 1 || endY == ySize - 1 || startX == xSize - 1 || endX == xSize - 1);
            grid[startY][startX].setFill(Color.BLUE);
            grid[endY][endX].setFill(Color.RED);
            isNodesSet(startX, startY, endX, endY);
        }
    }

    //clears old grid if new one is being generated:
    private void clearGrid(){
        isNodes=false;
        if(isGrid){
            for(int i=0;i<ySize;i++) {
                for(int j=0;j<xSize;j++) {
                    anchor.getChildren().remove(grid[i][j]);
                }
            }
        }else {
            return;
        }
    }

    //Recursive Quadrant maze generator:
    public void generateMaze1(ActionEvent actionEvent) {
        if(!animationActive) {
            if (isNodes) {
                isNodes = false;
            }
            if (!isGrid) {
                generateGrid(actionEvent);
            }

            if (isMaze) {
                clearGrid();
                generateGrid(actionEvent);
            }
            isMazeSet();
            outline();
            RecursiveQuadrant.recursiveQuadrant(0, xSize - 1, 0, ySize - 1, grid);
        }
    }

    //Prim's Algorithim maze generator:
    public void generateMaze2(ActionEvent actionEvent) {
        if(!animationActive) {
            if (isNodes) {
                isNodes = false;
            }
            if (!isGrid) {
                generateGrid(actionEvent);
            }

            if (isMaze) {
                clearGrid();
                generateGrid(actionEvent);
            }
            isMazeSet();
            Prim.generatePrim(grid, xSize, ySize);
        }
    }



    //maze generation algorithims:

    public void newGrid(){
        clearGrid();
        for(int j=0;j<ySize;j++) {
            for (int i = 0; i < xSize; i++) {
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
        outline();
    }


    //Outine the maze:
    public void outline(){

        for(int i=0;i<ySize;i++){
            for(int j=0;j<xSize;j++){
                if((i!=0 && i!=ySize-1) && (j>0 && j<xSize-1)){
                    j=xSize-1;
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

    public void breadthFirstSearch(ActionEvent actionEvent) {
        if(!isNodes){
            generateStartAndEnd(actionEvent);
        }
        if(!animationActive) {
            BFS.solveBFS(grid, startNodeX, startNodeY, xSize, ySize);
        }
    }


    public void greedySearch(ActionEvent actionEvent) {
        if(!isNodes){
            generateStartAndEnd(actionEvent);
        }
        if(!animationActive) {
            GreedySearch.solveGBFS(grid, startNodeX, startNodeY, endNodeX, endNodeY, xSize, ySize);
        }
    }
}

