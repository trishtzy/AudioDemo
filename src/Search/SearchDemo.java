package Search;

import Distance.CityBlock;
import Distance.Euclidean;
import Feature.MagnitudeSpectrum;
import Feature.Energy;
import Feature.FFTProcess;
import Feature.MFCC;
import Feature.ZeroCrossing;
import SignalProcess.WaveIO;
import Distance.Cosine;
import Tool.SortHashMapByValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by workshop on 9/18/2015.
 */
public class SearchDemo {
    /**
     * Please replace the 'trainPath' with the specific path of train set in your PC.
     */
    protected final static String trainPath = "/Users/vincenttan/Documents/CS2108/Assignment2/AudioData/input/train/";


    /***
     * Get the feature of train set via the specific feature extraction method, and write it into offline file for efficiency;
     * Please modify this function, select or combine the methods (in the Package named 'Feature') to extract feature, such as Zero-Crossing, Energy, Magnitude-
     * Spectrum and MFCC by yourself.
     * @return the map of training features, Key is the name of file, Value is the array/vector of features.
     */
    public HashMap<String,double[]> trainFeatureList(){
        File trainFolder = new File(trainPath);
        File[] trainList = trainFolder.listFiles();

        HashMap<String, double[]> featureList = new HashMap<String, double[]>();
        try {

            FileWriter fw = new FileWriter("data/feature/zeroCrossing.txt");

            for (int i = 0; i < trainList.length; i++) {
                WaveIO waveIO = new WaveIO();
                short[] signal = waveIO.readWave(trainList[i].getAbsolutePath());

                /**
                 * Example of extracting feature via MagnitudeSpectrum, modify it by yourself.
                 */
//                MagnitudeSpectrum ms = new MagnitudeSpectrum();
//                double[] msFeature = ms.getFeature(signal);
//                Energy energy = new Energy();
//                double[] energyFeature = energy.getFeature(signal);
//                MFCC mfcc = new MFCC();
//                double[][] mfccFeature = mfcc.process(signal);
//                double[] mfccMeanFeature = mfcc.getMeanFeature();
                ZeroCrossing zc = new ZeroCrossing();
                double[] zcFeature = zc.getFeature(signal);

                /**
                 * Write the extracted feature into offline file;
                 */
                featureList.put(trainList[i].getName(), zcFeature);

                String line = trainList[i].getName() + "\t";
                for (double f: zcFeature){
                    line += f + "\t";
                }

                fw.append(line+"\n");

                System.out.println("@=========@" + i);
            }
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {


        }catch (Exception e){
            e.printStackTrace();
        }


        return featureList;
    }

    /***
     * Get the distances between features of the selected query audio and ones of the train set;
     * Please modify this function, select or combine the suitable and feasible methods (in the package named 'Distance') to calculate the distance,
     * such as CityBlock, Cosine and Euclidean by yourself.
     * @param query the selected query audio file;
     * @return the top 20 similar audio files;
     */
    public ArrayList<String> resultList(String query, String[] feature, String simMeasure){
        WaveIO waveIO = new WaveIO();
        short[] inputSignal = waveIO.readWave(query);

        System.out.println("simMeasure: " + simMeasure);
        for (int index=0; index<feature.length; index++) {
            System.out.println("feature: " + feature[index]);
        }

        MagnitudeSpectrum ms = new MagnitudeSpectrum();
        double[] msFeature1 = ms.getFeature(inputSignal);
        Energy energy = new Energy();
        double[] energyFeature = energy.getFeature(inputSignal);
        MFCC mfcc = new MFCC();
        double[][] mfccFeature = mfcc.process(inputSignal);
        double[] mfccMeanFeature = mfcc.getMeanFeature();
        ZeroCrossing zc = new ZeroCrossing();
        double[] zcFeature = zc.getFeature(inputSignal);

        HashMap<String, Double> energySimList = new HashMap<String, Double>();
        HashMap<String, Double> mfccSimList = new HashMap<String, Double>();
        HashMap<String, Double> msSimList = new HashMap<String, Double>();
        HashMap<String, Double> zcSimList = new HashMap<String, Double>();
        ArrayList<HashMap<String, Double>> combinedSimList = new ArrayList<HashMap<String, Double>>();

        /**
         * Example of calculating the distance via Cosine Similarity, modify it by yourself please.
         */
        Cosine cosine = new Cosine();
        Euclidean euclidean = new Euclidean();
        CityBlock cityblock = new CityBlock();

        /**
         * Load the offline file of features (the result of function 'trainFeatureList()'), modify it by yourself please;
         */
        HashMap<String, double[]> energyTrainFeatureList = readFeature("data/feature/energy.txt");
        HashMap<String, double[]> mfccTrainFeatureList   = readFeature("data/feature/mfcc.txt");
        HashMap<String, double[]> msTrainFeatureList     = readFeature("data/feature/MS.txt");
        HashMap<String, double[]> zcTrainFeatureList     = readFeature("data/feature/zeroCrossing.txt");

        if (simMeasure.equalsIgnoreCase("cityblock")) {
            for (Map.Entry f: energyTrainFeatureList.entrySet()){
                energySimList.put((String)f.getKey(), cityblock.getDistance(energyFeature, (double[]) f.getValue()));
            }
            for (Map.Entry f: mfccTrainFeatureList.entrySet()){
                mfccSimList.put((String)f.getKey(), cityblock.getDistance(mfccMeanFeature, (double[]) f.getValue()));
            }
            for (Map.Entry f: msTrainFeatureList.entrySet()){
                msSimList.put((String)f.getKey(), cityblock.getDistance(msFeature1, (double[]) f.getValue()));
            }
            for (Map.Entry f: zcTrainFeatureList.entrySet()){
                zcSimList.put((String)f.getKey(), cityblock.getDistance(zcFeature, (double[]) f.getValue()));
            }
        } else if (simMeasure.equalsIgnoreCase("cosine")) {
            for (Map.Entry f: energyTrainFeatureList.entrySet()){
                energySimList.put((String)f.getKey(), cosine.getDistance(energyFeature, (double[]) f.getValue()));
            }
            for (Map.Entry f: mfccTrainFeatureList.entrySet()){
                mfccSimList.put((String)f.getKey(), cosine.getDistance(mfccMeanFeature, (double[]) f.getValue()));
            }
            for (Map.Entry f: msTrainFeatureList.entrySet()){
                msSimList.put((String)f.getKey(), cosine.getDistance(msFeature1, (double[]) f.getValue()));
            }
            for (Map.Entry f: zcTrainFeatureList.entrySet()){
                zcSimList.put((String)f.getKey(), cosine.getDistance(zcFeature, (double[]) f.getValue()));
            }
        } else {
            for (Map.Entry f: energyTrainFeatureList.entrySet()){
                energySimList.put((String)f.getKey(), euclidean.getDistance(energyFeature, (double[]) f.getValue()));
            }
            for (Map.Entry f: mfccTrainFeatureList.entrySet()){
                mfccSimList.put((String)f.getKey(), euclidean.getDistance(mfccMeanFeature, (double[]) f.getValue()));
            }
            for (Map.Entry f: msTrainFeatureList.entrySet()){
                msSimList.put((String)f.getKey(), euclidean.getDistance(msFeature1, (double[]) f.getValue()));
            }
            for (Map.Entry f: zcTrainFeatureList.entrySet()){
                zcSimList.put((String)f.getKey(), euclidean.getDistance(zcFeature, (double[]) f.getValue()));
            }
        }

        for (int i=0; i<feature.length; i++) {
            if (feature[i].equalsIgnoreCase("energy")) {
                combinedSimList.add(energySimList);
            } else if (feature[i].equalsIgnoreCase("mfcc")) {
                combinedSimList.add(mfccSimList);
            } else if (feature[i].equalsIgnoreCase("ms")) {
                combinedSimList.add(msSimList);
            } else if (feature[i].equalsIgnoreCase("zero")) {
                combinedSimList.add(zcSimList);
            } else if (feature[i].equalsIgnoreCase("null")) {
                continue;
            } else {
                System.err.print("Error: No feature selected");
            }
        }

        ArrayList<String> result = computeIntersection(combinedSimList);

        String out = query + ":";
        for(int j = 0; j < result.size(); j++){
            out += "\t" + result.get(j);
        }

        System.out.println(out);
        return result;
    }

    /**
     * Load the offline file of features (the result of function 'trainFeatureList()');
     * @param featurePath the path of offline file including the features of training set.
     * @return the map of training features, Key is the name of file, Value is the array/vector of features.
     */
    private HashMap<String, double[]> readFeature(String featurePath){
        HashMap<String, double[]> fList = new HashMap<String, double[]>();
        try{
            FileReader fr = new FileReader(featurePath);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while(line != null){

                String[] split = line.trim().split("\t");
                if (split.length < 2)
                    continue;
                double[] fs = new double[split.length - 1];
                for (int i = 1; i < split.length; i ++){
                    fs[i-1] = Double.valueOf(split[i]);
                }

                fList.put(split[0], fs);

                line = br.readLine();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return fList;
    }

    private ArrayList<String> computeIntersection(ArrayList<HashMap<String, Double>> combinedList) {
        SortHashMapByValue sortHM = new SortHashMapByValue(100);
        ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
        ArrayList<String> finalResult;

        if (combinedList.size() == 1) {
            SortHashMapByValue sortHM2 = new SortHashMapByValue(20);
            finalResult = sortHM2.sort(combinedList.get(0));
            System.out.println("finalResult size: " + finalResult.size());
        } else {
            for (int i = 0; i < combinedList.size(); i++) {
                results.add(sortHM.sort(combinedList.get(i)));
            }

            for (int j = 1; j < results.size(); j++) {
                results.get(0).retainAll(results.get(j));
            }
            System.out.println("results.get(0) size" + results.get(0).size());

            finalResult = results.get(0);
        }

        return finalResult;
    }

    public static void main(String[] args){
        long startTime = System.currentTimeMillis();
        SearchDemo searchDemo = new SearchDemo();
        /**
         * Example of searching, selecting 'bus2.wav' as query;
         */
        String[] feature = new String[4];
        feature[0] = "energy";
        feature[1] = "mfcc";
        feature[2] = "null";
        feature[3] = "null";
        String simMeasure = "euclidean";
        searchDemo.resultList("data/input/test/bus2.wav", feature, simMeasure);
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime + "ms");
//        searchDemo.trainFeatureList();
    }
}
