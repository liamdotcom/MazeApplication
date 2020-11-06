package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Controller {

    @FXML
    AnchorPane anchor;


    public void changeColour(ActionEvent actionEvent) {
        Rectangle rect=new Rectangle(0, 48, 20, 20);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.BLACK);
        anchor.getChildren().add(rect);
    }

    public void generateGrid(ActionEvent actionEvent) {

        for(int j=0;j<18;j++) {
            for (int i = 0; i < 30; i++) {
                Rectangle rect = new Rectangle(i * 20, (20*j) + 28, 20, 20);
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.BLACK);
                anchor.getChildren().add(rect);
            }
        }

    }
}
