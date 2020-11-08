package sample;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class Controller {

    @FXML
    AnchorPane anchor;
    Rectangle[][] grid=new Rectangle[18][30];

    private boolean isGrid=false;
    
    private void isGridSet(){
        isGrid=true;
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
                var ptr=new ParallelTransition();
                ptr.getChildren().add(sctr);
                ptr.play();
            }
        }
        isGridSet();
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
}
