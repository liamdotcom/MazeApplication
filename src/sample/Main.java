package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Main extends Application {

    private static boolean defineStart=false, defineEnd=false;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setTitle("Maze Application");
        primaryStage.setScene(new Scene(root, 1000, 588));
        primaryStage.show();

        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(defineStart){
                    Controller.setStart(event.getX(), event.getY());
                }
                if(defineEnd){
                    Controller.setEnd(event.getX(), event.getY());
                }
            }
        });

    }



    public static void main(String[] args) {
        launch(args);
    }

    public static void setDefineStart(boolean state){
        defineStart=state;
    }

    public static void setDefineEnd(boolean state){
        defineEnd=state;
    }

}
