package Sorting;


import Controllers.MainSceneController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class Algorithm {
    private final int maxNum=10000000;
    private final int nSamples=8;
    private int sortedLength=0;
    private long comparisonCount=0;
    private long startTime =0;

    private String currentAlgorithm="None";
    private static Algorithm AlgorithmsInstance=null;
    private ArrayList<Integer> RandomSet=null;
    private ArrayList<Integer> CopyRandomSet=null;

    public String getCurrentAlgorithm(){return currentAlgorithm;}
    public void setCurrentAlgorithm(String currentAlgorithm) {
        this.currentAlgorithm = currentAlgorithm;
    }

    public static Algorithm getInstance(){
        if(AlgorithmsInstance==null){
            AlgorithmsInstance=new Algorithm();
        }
        return AlgorithmsInstance;
    }
    private Algorithm(){

    };
    public void GenerateRandomSet(int count){
        RandomSet=new ArrayList<>(count);
        CopyRandomSet=new ArrayList<>(count);
        Random  random=new Random();
        for(int i=0;i<count;i++){
            RandomSet.add(random.nextInt(maxNum)+1);

        }
        buildHeap();
        for(int i=0;i<count;i++){
           CopyRandomSet.add(RandomSet.get(i));
        }
        currentAlgorithm="None";
    }
    public void buildHeap() {
        int size = RandomSet.size();
        for (int i = (size / 2) - 1; i >= 0; i--)
            maxHeapify(i,size);
    }
    public void maxHeapify(int pos,int size) {
        int largest = pos;
        int left = (2 * pos) + 1;
        int right = (2 * pos) + 2;

        if (left < size && RandomSet.get(left) > RandomSet.get(largest))
            largest = left;

        if (right <size && RandomSet.get(right) > RandomSet.get(largest))
            largest = right;

        if (largest != pos) {
            Swap(largest, pos);
            maxHeapify(largest,size);
        }
    }
    public void regenerateRandomSet(int start,int end){
        for(int i=start;i<end;i++){
            RandomSet.set(i,CopyRandomSet.get(i));
        }
    }
   public void useBubbleSort(MainSceneController controller){
        if(RandomSet==null)return;
       ArrayList<Pair<Integer,Long>> seriesData=new ArrayList<>(nSamples);
       XYChart.Series series = new XYChart.Series();
       controller.getUILineChart().getData().add(series);

       int length=RandomSet.size()-1;
       initSorting("Bubble Sort",series);

       for(int k=1;k<=nSamples;k++) {
           float frac=((float)k/(float)nSamples);
           int len = (int)(frac * (float)length);
           regenerateRandomSet(0,len);
           sortedLength+=len;
           boolean swap;
           for (int i = 0; i < len; i++) {
               swap = false;
               for (int j = 0; j < len - i; j++) {
                   comparisonCount++;
                   if (RandomSet.get(j) > RandomSet.get(j + 1)) {
                       Swap(j, j + 1);
                       swap = true;
                   }
               }
               comparisonCount++;

               if (swap == false) {
                   break;
               }
           }
           seriesData.add(new Pair(len, System.nanoTime() - startTime));
       }
       finishSorting(controller, series, seriesData);
    }
   public void useSelectionSort(MainSceneController controller){
       if(RandomSet==null)return;
       ArrayList<Pair<Integer,Long>> seriesData=new ArrayList<>(nSamples);
       XYChart.Series series = new XYChart.Series();
       controller.getUILineChart().getData().add(series);
       int length=RandomSet.size()-1;
       int minIndex;
       initSorting("Selection Sort", series);
       for(int k=1;k<=nSamples;k++) {
           float frac=((float)k/(float)nSamples);
           int len = (int) (frac * (float) length);
           sortedLength+=len;
           regenerateRandomSet(0, len);
           for (int i = 0; i < len; i++) {
               minIndex = i;
               for (int j = i + 1; j < len; j++) {
                   comparisonCount++;
                   if (RandomSet.get(minIndex) > RandomSet.get(j)) {
                       minIndex = j;
                   }
               }
               Swap(minIndex, i);

           }
           seriesData.add(new Pair(len, System.nanoTime() - startTime));
       }
       finishSorting(controller, series, seriesData);
   }
   public void useInsertionSort(MainSceneController controller) {
       if (RandomSet == null) return;
       ArrayList<Pair<Integer, Long>> seriesData = new ArrayList<>(nSamples);
       XYChart.Series series = new XYChart.Series();
       controller.getUILineChart().getData().add(series);
       int length = RandomSet.size();
       initSorting("Insertion Sort", series);
       for(int k=1;k<=nSamples;k++) {
           float frac=((float)k/(float)nSamples);
           int len = (int) (frac * (float) length);
           sortedLength+=len;
           regenerateRandomSet(0, len);
           int currentlyInserting;
           for (int i = 1; i < len; i++) {
               currentlyInserting = RandomSet.get(i);
               int j = i - 1;
               comparisonCount++;
               while (j >= 0 && RandomSet.get(j) > currentlyInserting) {
                   RandomSet.set(j + 1, RandomSet.get(j));
                   j--;
                   comparisonCount++;
               }
               RandomSet.set(j + 1, currentlyInserting);
           }
       seriesData.add(new Pair(len, System.nanoTime() - startTime));
       }
       finishSorting(controller, series, seriesData);
   }
   public void useQuickSort(MainSceneController controller){
       if(RandomSet==null)return;
       ArrayList<Pair<Integer,Long>> seriesData=new ArrayList<>(nSamples);
       XYChart.Series series = new XYChart.Series();
       controller.getUILineChart().getData().add(series);
       int length = RandomSet.size();
       initSorting("Quick Sort", series);
       for(int k=1;k<=nSamples;k++) {
           float frac=((float)k/(float)nSamples);
           int len = (int) (frac * (float) length);
           sortedLength+=len;
           regenerateRandomSet(0, len);
           quickSort(seriesData, 0, len-1);
           seriesData.add(new Pair(len, System.nanoTime() - startTime));
       }
       finishSorting(controller,series,seriesData);
   }
    private void quickSort(ArrayList<Pair<Integer,Long>> seriesData,int start,int end) {
        if(start<end) {
            int pivotIndex = partition(seriesData, start, end);
            quickSort(seriesData, start, pivotIndex - 1);
            quickSort( seriesData,pivotIndex + 1, end);
        }
    }
    private int partition(ArrayList<Pair<Integer,Long>> seriesData,int start,int end){
        int pivot=RandomSet.get(end);
        int i=start-1;
        for (int j=start;j<end;j++)
        {
            comparisonCount++;
            if(pivot>=RandomSet.get(j))
            {
                i++;
                Swap(i,j);
            }
        }
        Swap(i+1,end);
        return i+1;
    }
   public void useMergeSort(MainSceneController controller){
       if(RandomSet==null)return;
       ArrayList<Pair<Integer,Long>> seriesData=new ArrayList<>(nSamples);
       XYChart.Series series = new XYChart.Series();
       controller.getUILineChart().getData().add(series);
       int length = RandomSet.size();
       initSorting("Merge Sort", series);
       for(int k=1;k<=nSamples;k++) {
           float frac=((float)k/(float)nSamples);
           int len = (int) (frac * (float) length);
           sortedLength+=len;
           regenerateRandomSet(0, len);
           mergeSort(seriesData,0,RandomSet.size()-1);
           seriesData.add(new Pair(len, System.nanoTime() - startTime));
       }
       finishSorting(controller,series,seriesData);
   }
   private void mergeSort(ArrayList<Pair<Integer,Long>> seriesData,int l,int r){
       if (l < r) {

           int m = (l + r) / 2;
           mergeSort(seriesData, l, m);
           mergeSort(seriesData, m + 1, r);
           merge(l, m, r);
       }
   }
   private void merge(int l,int m,int r){
       int len1 = m - l + 1;
       int len2 = r - m;
       int Left[] = new int [len1];
       int Right[] = new int [len2];
       for (int i=0; i<len1; ++i) {
           Left[i] = RandomSet.get(l + i);
       }
       for (int j=0; j<len2; ++j) {
           Right[j] = RandomSet.get(m + 1 + j);
       }
       int i = 0, j = 0;
       int k = l;
       while (i < len1 && j < len2) {
           comparisonCount++;
           if (Left[i] <= Right[j]) {
               RandomSet.set(k , Left[i]);
               i++;
           } else {
               RandomSet.set(k , Right[j]);
               j++;
           }
           k++;
       }
       while (i < len1) {
          RandomSet.set(k, Left[i]);
           i++;
           k++;
       }
       while (j < len2) {
           RandomSet.set(k , Right[j]);
           j++;
           k++;
       }
   }
   public void useHeapSort(MainSceneController controller){
       if(RandomSet==null)return;
       ArrayList<Pair<Integer,Long>> seriesData=new ArrayList<>(nSamples);
       XYChart.Series series = new XYChart.Series();
       controller.getUILineChart().getData().add(series);
       int length = RandomSet.size();
       initSorting("Heap Sort", series);
       for(int k=1;k<=nSamples;k++) {
           float frac=((float)k/(float)nSamples);
           int len = (int) (frac * (float) length);
           sortedLength+=len;
           regenerateRandomSet(0, len);
           int n=len;
           for (int i = n - 1; i >= 0; i--) {
               Swap( 0, i);
               n--;
               maxHeapify(0,n);
           }
           seriesData.add(new Pair(len, System.nanoTime() - startTime));
       }
       finishSorting(controller,series,seriesData);
    }
    public String getInfo(){
        return "Using "+currentAlgorithm+" made "+String.valueOf(comparisonCount)+" Comparison,Sorted "+String.valueOf(sortedLength)+" in "+
                String.valueOf(System.nanoTime()- startTime)+"ns";
    }
    private void initSorting(String sortingName,XYChart.Series series){
        currentAlgorithm=sortingName;
        comparisonCount=0;
        sortedLength=0;
        series.setName(currentAlgorithm);
        series.getData().add(new XYChart.Data( 0,0));
        startTime =System.nanoTime();
    }
    private void finishSorting(MainSceneController controller,XYChart.Series series,ArrayList<Pair<Integer,Long>> seriesData){
        controller.getUIInfo().setText(this.getInfo());
        for(int i=0;i<nSamples;i++){
            series.getData().add(new XYChart.Data( seriesData.get(i).getKey(),seriesData.get(i).getValue()));
        }
    }
    private void Swap(int p1,int p2){
        int temp=RandomSet.get(p1);
        RandomSet.set(p1,RandomSet.get(p2));
        RandomSet.set(p2,temp);
    }
}
