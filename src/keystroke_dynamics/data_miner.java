package keystroke_dynamics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class data_miner {
    public static void main(String[] args) throws Exception {
        
        SimpleKMeans  kmeans= new SimpleKMeans();
        kmeans.setNumClusters(2);
        kmeans.setSeed(10);
        kmeans.setPreserveInstancesOrder(true);
        
        BufferedReader datafile=readDataFile("D:\\new records\\records.arff");
         
        Instances data=new Instances(datafile);
        
        //remove tries 
        //data.deleteAttributeAt(1);
    
        System.out.println("lowest value= "+data.attribute(0).getLowerNumericBound());
        
        kmeans.buildClusterer(data);
        ManhattanDistance md = new ManhattanDistance(data);
        kmeans.setDistanceFunction(md);
        int[] assignments=kmeans.getAssignments();
        int i=0;
        
        /*
            for(int clusterNum: assignments){
                System.out.println("Instance"+i+"-> Cluster"+clusterNum+" \n");
                i++;
            }
        */
                
        BufferedReader testfile=readDataFile("D:\\new records\\test.arff");
         
        Instances test=new Instances(testfile);
        //test.deleteAttributeAt(1);
    
        Instances centroids = kmeans.getClusterCentroids();
        //printCentroids(centroids);
        printClusterInstance(test, kmeans);
        
        //System.out.println("the instance got clustered at "+k);
    }
    
    public static Instances removeTries(Instances data){
        if(data.attribute(1).toString().equals("@attribute tries numeric")){
                        
            try {
                Remove remove=new Remove();
                remove.setAttributeIndicesArray(new int[]{1});
                remove.setInputFormat(data);
                data = Filter.useFilter(data, remove);
            
            }
            catch(Exception ex) {
                Logger.getLogger(data_miner.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            else{
                System.out.println("doesnt have tries ");
              }
        return data;
    }
    static public void printCentroids(Instances centroids){
        for (Instance cen : centroids) {
            System.out.println("centroid : ");
            for (int j = 0; j < centroids.numAttributes(); j++) {
                System.out.print(centroids.attribute(j).toString()+"  ");
                System.out.println(" "+cen.value(j));
            }
            System.out.println("");
        }
    }
    
    public static void printInstances(Instances data){
                    for (int i = 0; i < data.numInstances(); i++) {
                            for (int j = 0; j < data.get(i).numAttributes(); j++) {
                                System.out.print(""+data.get(i).toString(j)+",");
                            }
                            System.out.println("");
                        }
    }
    
    public static void printClusterInstance(Instances test,SimpleKMeans kmeans) throws Exception{
        
        for (int j = 0; j < test.numAttributes(); j++) {
         //   System.out.print(test.get(2).toString(j)+" ");
        }
        
        Instance ist = test.get(0);
        
        //System.out.println("\n\ndistance="+md.distance(test.get(0), test.get(1)));
        int i=0;
        for (Instance t : test) {
            int clst = kmeans.clusterInstance(ist);
            
            System.out.println("\n\n"+t.toString(t.attribute(0))+" clustered into "+clst);
            i++;
        }
        
        System.out.println("");    
    }
    
    public static BufferedReader readDataFile(String filename){
        BufferedReader inputreader=null;
        try
        {
            inputreader=new BufferedReader(new FileReader(filename));
        }
        catch(Exception ex){
            System.out.println("File not Found"+filename);
        }
        return inputreader;
    }
    
}
