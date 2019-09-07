package Controllers;

import Sorting.Algorithm;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import javax.sound.sampled.Line;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


public class MainSceneController implements Initializable {

    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    @FXML
    private LineChart<Number,Number>  UILineChart;
    @FXML
    private Button UIClear;
    @FXML
    private TextField UIRandomCount;
    @FXML
    private Button UIGenRandom;
    @FXML
    private Button  UISort;
    @FXML
    private ComboBox<String> UIType;
    @FXML
    private Text UIInfo;

    public LineChart getUILineChart() {
        return UILineChart;
    }

    public TextField getUIRandomCount() {
        return UIRandomCount;
    }

    public Button getUIGenRandom() {
        return UIGenRandom;
    }

    public Button getUISort() {
        return UISort;
    }

    public ComboBox<String> getUIType() {
        return UIType;
    }

    public Text getUIInfo() {
        return UIInfo;
    }

    @FXML
    private void visualize(){
        if(UIType.getValue()==null||UIType.getValue()=="All By Order")return;
        Algorithm.getInstance().setCurrentAlgorithm(UIType.getValue());
        try{
            Parent root =FXMLLoader.load(getClass().getResource("/FXML/visualizeScene.fxml"));
            Stage visualizeStage=new Stage();
            visualizeStage.setTitle("Visualization");
            Scene scene=new Scene(root);
            visualizeStage.setScene(scene);
            visualizeStage.show();
            visualizeStage.setResizable(false);
        }catch(Exception e){
           System.err.println(e);
        }

    }
    @FXML
    private void clearChart(){
        UILineChart.getData().clear();
        UIInfo.setText("");
    }
    @FXML
    private void GenerateRandomSet(){
        if(UIRandomCount.getText().length()!=0)
        Algorithm.getInstance().GenerateRandomSet(Integer.parseInt(UIRandomCount.getText()));
    }
    @FXML
    private void StartSorting(){
        if(UIType.getValue()==null)return;

        switch (UIType.getValue()){
            case "Bubble Sort":
                Algorithm.getInstance().useBubbleSort(this);
                break;
            case "Selection Sort":
                Algorithm.getInstance().useSelectionSort(this);
                break;
            case "Insertion Sort":
                Algorithm.getInstance().useInsertionSort(this);
                break;
            case "Quick Sort":
                Algorithm.getInstance().useQuickSort(this);
                break;
            case "Merge Sort":
                Algorithm.getInstance().useMergeSort(this);
                break;
            case "Heap Sort":
                Algorithm.getInstance().regenerateRandomSet(0,Integer.parseInt(UIRandomCount.getText()));//as heapsort requires the array to be in a heap shape
                Algorithm.getInstance().useHeapSort(this);
                break;
            case "All By Order":
                //NO NEED TO MAKE THE USER GENERATE FOR THIS OPTION
                GenerateRandomSet();
                Algorithm.getInstance().useBubbleSort(this);
                Algorithm.getInstance().regenerateRandomSet(0,Integer.parseInt(UIRandomCount.getText()));
                Algorithm.getInstance().useSelectionSort(this);
                Algorithm.getInstance().regenerateRandomSet(0,Integer.parseInt(UIRandomCount.getText()));
                Algorithm.getInstance().useInsertionSort(this);
                Algorithm.getInstance().regenerateRandomSet(0,Integer.parseInt(UIRandomCount.getText()));
                Algorithm.getInstance().useQuickSort(this);
                Algorithm.getInstance().regenerateRandomSet(0,Integer.parseInt(UIRandomCount.getText()));
                Algorithm.getInstance().useMergeSort(this);
                Algorithm.getInstance().regenerateRandomSet(0,Integer.parseInt(UIRandomCount.getText()));
                Algorithm.getInstance().useHeapSort(this);
                Algorithm.getInstance().regenerateRandomSet(0,Integer.parseInt(UIRandomCount.getText()));
                UIInfo.setText("");
                return;
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      UIType.getItems().add("All By Order");
      UIType.getItems().add("Bubble Sort");
      UIType.getItems().add("Selection Sort");
      UIType.getItems().add("Insertion Sort");
      UIType.getItems().add("Heap Sort");
      UIType.getItems().add("Quick Sort");
      UIType.getItems().add("Merge Sort");
      UILineChart.setTitle("Algorithms Chart");
      UILineChart.getXAxis().setLabel("Elements");
      UILineChart.getYAxis().setLabel("Time(ns)");
      Pattern validDoubleText = Pattern.compile("[0-9]{0,5}?");
      TextFormatter<Integer> textFormatter = new TextFormatter<Integer>(new IntegerStringConverter(), 1500, change -> {
          String newText = change.getControlNewText() ;
          if (validDoubleText.matcher(newText).matches()) {
              return change ;
          } else return null ;});
      UIRandomCount.setTextFormatter(textFormatter);
    }


}
