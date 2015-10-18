package Distance;

/**
 * Created by workshop on 9/18/2015.
 */
public class CityBlock {
    public double getDistance(double[] query1, double[] query2){
        if (query1.length != query2.length){
            System.err.println("The dimension of the two vectors does not match!");

            System.exit(1);
        }

        double difference = 0.0;
        double sum = 0.0;
        for (int i = 0; i < query1.length; i++) {
            difference = Math.abs(query1[i] - query2[i]);
            sum += difference;
        }
        return sum;
    }
}
