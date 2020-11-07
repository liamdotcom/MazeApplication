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

    public void generateGrid(ActionEvent actionEvent) {
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


    }

    public void clearGrid(){
        grid[0][0].rem
    }
}
