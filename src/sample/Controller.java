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

    private static int ySize=28;
    private static int xSize=50;
    @FXML
    AnchorPane anchor;
    private static Rectangle[][] grid=new Rectangle[ySize][xSize];


    private boolean isGrid=false;
    private boolean isMaze=false;
    private static boolean isNodes=false;
    private boolean isSolved=false;
    private static boolean animationActive=false;
    private static int startNodeX=0, startNodeY=0, endNodeX=0, endNodeY=0;



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
            Main.setDraw(false);
            newGrid();
        }

    }

    public void generateStartAndEnd(ActionEvent actionEvent){
        if(!animationActive) {
            Main.setDraw(false);
            if(isSolved){
                resetToUnsolved();
            }
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

    public static void setStart(double x, double y){
        int xCoord= (int) (x/20);
        int yCoord= (int) ((y-28)/20);
        startNodeX=xCoord;
        startNodeY=yCoord;
        grid[yCoord][xCoord].setFill(Color.BLUE);
        var sctr = new ScaleTransition(Duration.millis(1000), grid[yCoord][xCoord]);
        Main.setDefineStart(false);
        sctr.setByX(0.2);
        sctr.setByY(0.2);
        sctr.setCycleCount(2);
        sctr.setAutoReverse(true);
        sctr.play();
        sctr.setOnFinished(e ->  Main.setDefineEnd(true));

    }

    public static void setEnd(double x, double y){
        int xCoord= (int) (x/20);
        int yCoord= (int) ((y-28)/20);
        endNodeX=xCoord;
        endNodeY=yCoord;
        grid[yCoord][xCoord].setFill(Color.RED);
        var sctr = new ScaleTransition(Duration.millis(1000), grid[yCoord][xCoord]);
        Main.setDefineStart(false);
        sctr.setByX(0.2);
        sctr.setByY(0.2);
        sctr.setCycleCount(2);
        sctr.setAutoReverse(true);
        sctr.play();
        sctr.setOnFinished(e ->  Main.setDefineEnd(false));
        isNodes=true;
        animationActive=false;
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
        Main.setDraw(false);
        if(!animationActive) {
            isSolved=false;
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
        Main.setDraw(false);
        if(!animationActive) {
            isSolved=false;
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
        Main.setDraw(false);
        if(!isNodes){
            generateStartAndEnd(actionEvent);
        }
        if(!animationActive) {
            if(isSolved){
                resetToUnsolved();
            }
            BFS.solveBFS(grid, startNodeX, startNodeY, xSize, ySize);
            isSolved=true;
        }
    }

    public void greedySearch(ActionEvent actionEvent) {
        Main.setDraw(false);
        if (!isNodes) {
            generateStartAndEnd(actionEvent);
        }

        if (!animationActive) {
            if(isSolved) {
                resetToUnsolved();
            }
            GreedySearch.solveGBFS(grid, startNodeX, startNodeY, endNodeX, endNodeY, xSize, ySize);
            isSolved=true;
        }

    }

    public void resetToUnsolved(){
        for(int i=0;i<ySize;i++){
            for(int j=0;j<xSize;j++){
                if(grid[i][j].getFill()!=Color.BLACK){
                    if(i==startNodeY && j==startNodeX){
                        grid[i][j].setFill(Color.BLUE);
                    }else if(i==endNodeY && j==endNodeX){
                        grid[i][j].setFill(Color.RED);
                    }else {
                        grid[i][j].setFill(Color.WHITE);
                    }
                }
            }
        }
        isSolved=false;
    }

    public void startPlot(ActionEvent actionEvent) {
        if(isSolved){
            resetToUnsolved();
        }
        if(isNodes){
            grid[startNodeY][startNodeX].setFill(Color.WHITE);
            grid[endNodeY][endNodeX].setFill(Color.WHITE);
        }
        Main.setDraw(false);
        Main.setDefineStart(true);
        animationActive=true;
    }

    public void toggleDraw(ActionEvent actionEvent) {
        if(!animationActive) {
            Main.toggleDraw();
        }
    }

    public static void draw(double x, double y){
        int xCoord= (int) (x/20);
        int yCoord= (int) ((y-28)/20);
        grid[yCoord][xCoord].setFill(Color.BLACK);
    }
}



