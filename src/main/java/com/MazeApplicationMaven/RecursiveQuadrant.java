package com.MazeApplicationMaven;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Random;

public class RecursiveQuadrant {

    public void recursiveQuadrant(int startX, int endX, int startY, int endY, Rectangle[][] grid, Controller controller){
        controller.setAnimationActive(true);
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
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(startX, endX, startY, rY, grid, controller));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(startX, endX, rY, endY, grid,controller));
                timeline.getKeyFrames().add(keyFrame);
            }else{
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(startX, rO, startY, rY, grid,controller));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(rO, endX, startY, rY, grid,controller));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(startX, rO, rY, endY, grid,controller));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(rO, endX, rY, endY, grid,controller));
                timeline.getKeyFrames().add(keyFrame);
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
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(startX, rX, startY, endY, grid,controller));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(rX, endX, startY, endY, grid,controller));
                timeline.getKeyFrames().add(keyFrame);
            }else{
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(startX, rX, startY, rO, grid,controller));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(rX, endX, startY, rO, grid,controller));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(startX, rX, rO, endY, grid,controller));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(timepoint, e -> recursiveQuadrant(rX, endX, rO, endY, grid,controller));
                timeline.getKeyFrames().add(keyFrame);

            }
        }

        timeline.play();
        timeline.setOnFinished(e -> controller.setAnimationActive(false));
    }
}
