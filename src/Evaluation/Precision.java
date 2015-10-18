package Evaluation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by workshop on 9/18/2015.
 */
public class Precision {
    private static final int k = 20;
    /**
     * Please implement the evaluation function by yourselves.
     */
    public double getPrecision(String query, ArrayList<String> result) {
        String category = "none";
        if (query.contains("bus")) {
            category = "bus";
        } else if (query.contains("busystreet")) {
            category = "busystreet";
        } else if (query.contains("office")) {
            category = "office";
        } else if (query.contains("openairmarket")){
            category = "openairmarket";
        } else if (query.contains("park")) {
            category = "park";
        } else if (query.contains("quietstreet")) {
            category = "quietstreet";
        } else if (query.contains("restaurant")) {
            category = "restaurant";
        } else if (query.contains("supermarket")) {
            category = "supermarket";
        } else if (query.contains("tube")) {
            category = "tube";
        } else if (query.contains("tubestation")) {
            category = "tubestation";
        }

        int numRelevant = 0;
        int numIrrelevant = 0;
        for (int i=0; i<result.size(); i++) {
            if (result.get(i).contains(category)) {
                numRelevant++;
            } else {
                numIrrelevant++;
            }
        }

        double precisionScore = numRelevant/((double)numRelevant + numIrrelevant);

        return precisionScore;
    }
}
