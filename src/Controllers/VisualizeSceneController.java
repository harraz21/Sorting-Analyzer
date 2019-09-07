package Controllers;

import Sorting.Algorithm;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import org.omg.IOP.ExceptionDetailMessage;

import javax.sound.sampled.Line;
import javax.swing.plaf.ColorUIResource;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


public class VisualizeSceneController implements Initializable {

    final double delayTime=0.001;
    ArrayList<Bar> BarArr=new ArrayList<>(Bar.nRect);
    @FXML
    Canvas UICanvas;
    @FXML
    Text UIText;
    GraphicsContext graphContext2D;

    public void setUIText(String algorithmName){
        UIText.setText(algorithmName);
    }
    private void genArray(){
        Random random=new Random(System.nanoTime());
        for(int i=0;i<Bar.nRect;i++){
            BarArr.add(new Bar());
            Bar b=BarArr.get(i);
            b.width=Bar.rectWidth;
            b.height=random.nextInt(Bar.maxHeight+Bar.minHeight)+Bar.minHeight;
            b.x=Bar.xStart+i*Bar.xStep;
            b.y=Bar.yValue+(Bar.maxHeight-b.height);
            b.currentColor=Color.web(Bar.lazyColor);

        }
    }
    private void drawArray(){
        graphContext2D.clearRect(0,0,UICanvas.getWidth(),UICanvas.getHeight());
        for (int i = 0; i < Bar.nRect; i++) {
            Bar b = BarArr.get(i);
            graphContext2D.setFill(b.currentColor);
            graphContext2D.fillRect(b.x, b.y, b.width, b.height);
        }
    }
    private void selectElement(int index){
        BarArr.get(index).currentColor=Color.web(Bar.activeColor);
    }
    private void deSelectElement(int index){
        BarArr.get(index).currentColor=Color.web(Bar.lazyColor);
    }
    private void setElementSorted(int index){
        BarArr.get(index).currentColor= Color.web(Bar.sortedColor);
    }
    private void Swap(int i,int j){
        Bar Temp=BarArr.get(i);
        BarArr.set(i,BarArr.get(j));
        BarArr.set(j,Temp);
        int xValTemp=BarArr.get(i).x;
        BarArr.get(i).x=BarArr.get(j).x;
        BarArr.get(j).x=xValTemp;
    }
    private void finishSorting(){
        for(int i=0;i<Bar.nRect;i++){
            setElementSorted(i);
        }
        drawArray();
    }
    private AnimationTimer animTimerBubbleSort=new AnimationTimer() {
        long startNanoTime = System.nanoTime();
        int i=0;
        int j=0;
        int len=Bar.nRect-1;
        int swapsCounter=0;
        int lastSwapsCounter=0;
        @Override
        public void handle(long now) {
            double currentTime = (now - startNanoTime) / 1000000000.0;
            if(currentTime>delayTime) {
                startNanoTime=now;
                selectElement(j);
                if(bubbleSort(j)){
                    swapsCounter++;
                }
                drawArray();
                deSelectElement(j);
                if(j<len-i-1){
                    j++;
                } else{
                    if(lastSwapsCounter==swapsCounter){
                        finishSorting();
                        animTimerBubbleSort.stop();
                        return;
                    }
                    setElementSorted(j+1);
                    lastSwapsCounter=swapsCounter;
                    i++;
                    j=0;
                }
            }
            if(i==len){
                finishSorting();
                animTimerBubbleSort.stop();
            }
        }
    };
    private boolean bubbleSort(int Index){
        if (BarArr.get(Index).height > BarArr.get(Index + 1).height) {
            Swap(Index,Index+1);
            return true;
        }
        return false;
    }

    private AnimationTimer animTimerSelectionSort=new AnimationTimer() {

        long startNanoTime = System.nanoTime();
        int i=0;
        int j=i+1;
        int len=Bar.nRect-1;
        int minIndex;
        @Override
        public void handle(long now) {
            double currentTime = (now - startNanoTime) / 1000000000.0;
            if (currentTime > delayTime) {
                startNanoTime=now;
                selectElement(j);
                if(selectionSort(j,minIndex)){
                    minIndex=j;
                }

                selectElement(minIndex);
                drawArray();
                deSelectElement(j);
                deSelectElement(minIndex);
                if(j<len){
                    j++;
                } else{
                    Swap(minIndex,i);
                    setElementSorted(i);
                    i++;
                    minIndex=i;
                    j=i+1;
                }
            }
            if(i==len){
                finishSorting();
                animTimerSelectionSort.stop();
            }
        }
    };
    private boolean selectionSort(int Index,int minIndex){
        if (BarArr.get(minIndex).height > BarArr.get(Index).height) {
            return true;
        }
        return false;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     graphContext2D=UICanvas.getGraphicsContext2D();
     genArray();
     drawArray();
     String AlgrothimName=Algorithm.getInstance().getCurrentAlgorithm();
     UIText.setText(AlgrothimName);
     switch (AlgrothimName){
         case "Bubble Sort":
             animTimerBubbleSort.start();
             break;
         case "Selection Sort":
             animTimerSelectionSort.start();
             break;
     }


    }


}
class Bar{
   public static final String lazyColor="#3366ff";
   public static final String activeColor="#00cc00";
   public static final String sortedColor="#33cccc";
   public static final int maxHeight=248;
   public static final int minHeight=16;
   public static final int rectWidth=8;
   public static final int nRect=64;
   public static final int xStep=9;
   public static final int xStart=24;
   public static final int yValue=140;
   public int height;
   public int width;
   public int x;
   public int y;
   public Paint currentColor=Color.web(lazyColor);
   @Override
    public String toString(){
       return x+" "+y+" "+width+" " + height + "\n";
   }
}