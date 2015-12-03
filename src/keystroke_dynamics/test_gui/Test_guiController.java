/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keystroke_dynamics.test_gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import static keystroke_dynamics.data_miner.readDataFile;
import static keystroke_dynamics.data_miner.removeTries;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 * FXML Controller class
 *
 * @author Ashish Padalkar
 */
public class Test_guiController implements Initializable {

    @FXML
    private Button btn_load_model;
    
    @FXML
    private Button btn_test;

    @FXML
    private Button btn_back;
    
    @FXML
    private Label lbl_model;

    @FXML
    private Button btn_browse_test;

    @FXML
    private TextField txt_model_loc;

    @FXML
    private Button btn_load_test;

    @FXML
    private Button btn_browse_model;

    @FXML
    private TextField txt_test_loc;
    
    @FXML
    private TextArea txt_area_result;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialise_btns();
        // TODO
    }    
    
    static File TestFile;
    static File ModelFile;
    void initialise_btns(){
        btn_browse_model.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("browse");
                Stage savedStage = new Stage();
                
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open file");
                
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ARFF (*.arff)", "*.arff");
                fileChooser.getExtensionFilters().add(extFilter);
                
                String filename ="Records.arff";
                fileChooser.setInitialFileName(filename);
                ModelFile = fileChooser.showOpenDialog(savedStage);
                txt_model_loc.setText(ModelFile.getAbsolutePath());
                
            }
        });
        
        btn_browse_test.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Stage savedStage = new Stage();
                
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Test file");
                
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ARFF (*.arff)", "*.arff");
                fileChooser.getExtensionFilters().add(extFilter);
                
                String filename ="Test.arff";
                fileChooser.setInitialFileName(filename);
                TestFile = fileChooser.showOpenDialog(savedStage);
                if(TestFile.isFile()){
                    txt_test_loc.setText(TestFile.getAbsolutePath());
                }
            }
        });
        
        btn_load_model.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(txt_model_loc.getText().equals("")){}
                    else
                    {
                        BufferedReader data_model=readDataFile(ModelFile.getAbsolutePath());
                        data=new Instances(data_model);
                        
                        data=removeTries(data);
                        
                        //Attribute usr = data.attribute("user");
                        //usr.enumerateValues();
                        
                        //System.out.println(""+data);
                        txt_area_result.setText(data.toString());
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Test_guiController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        btn_load_test.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
               if(txt_test_loc.getText().equals("")){}
               else{
                    try {

                        BufferedReader data_test=readDataFile(TestFile.getAbsolutePath());
                        test=new Instances(data_test);
                        test=removeTries(test);
                        //System.out.println(""+test);
                        txt_area_result.setText(test.toString());

                    } 
                    catch (IOException ex) {
                    Logger.getLogger(Test_guiController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
              
        btn_back.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Keystroke_dynamics.Keystroke_dynamics.show_main_gui(Keystroke_dynamics.Keystroke_dynamics.getPrimaryStage());
                } catch (IOException ex) {
                    Logger.getLogger(Test_guiController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        btn_test.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                        
                    System.out.println("clicked on button");
                    if(txt_model_loc.getText().equals("")||txt_test_loc.getText().equals("")){}    
                    else
                    {
                        
                        kmeans = new SimpleKMeans();
                        
                        Enumeration users = data.attribute(0).enumerateValues();
                        int usersize=0;
                        while(users.hasMoreElements()){
                            System.out.println(""+users.nextElement().toString());
                            usersize++;
                        }
                        
                        
                        System.out.println("Number of clusters"+usersize);
                        kmeans.setNumClusters(usersize/2);
                        kmeans.setSeed(10);
                        kmeans.setPreserveInstancesOrder(true);
                        
                        //set user as class index
                        //data.setClassIndex(0);
                        
                        //removing user attribute for cluster to class evaluation
                        /*
                        Remove filter = new Remove();
                        filter.setAttributeIndicesArray(new int[]{0});
                        filter.setInputFormat(data);
                        Instances dataClusterer = Filter.useFilter(data, filter);
                        
                        System.out.println(""+dataClusterer);
                        */
                        
                        kmeans.buildClusterer(data);
                        
                        
                        /*
                        ClusterEvaluation eval = new ClusterEvaluation();
                        eval.setClusterer(kmeans);
                        */
                        
                        /*
                        //using test
                        eval.evaluateClusterer(data);
                        
                        String result = eval.clusterResultsToString();
                        String []res=result.split("\n");
                        String re ="";
                        int i =0;
                        for (String r : res) {
                            r.equals("Classes to Clusters:\n");
                            i++;
                        }
                        i--;
                        for (; i < res.length; i++) {
                            re += res[i]+"\n";
                        }
                        txt_area_result.setText(re);
                        System.out.println(result);
                    
                        
                        
                        double[] assignments=eval.getClusterAssignments();
                        i =0;
                        
                        ArrayList <Double>al = new ArrayList<>();
                        for (i = 0; i < assignments.length; i++) {
                            String u = data.get(i).toString(0);
                            if(!al.contains(assignments[i])){
                                clst.add(new cluster(assignments[i],u));
                            }
                        }
                        */
                        
                        int assignments[]=kmeans.getAssignments();
                        
                        
                        ArrayList<cluster> clstnum  = new ArrayList<>();
                        ArrayList<Integer> clusterNum= new ArrayList<>();
                        
                        txt_area_result.setText("");
                        
                        for (int j = 0; j < assignments.length; j++) {
                              if(!clusterNum.contains(assignments[j])){
                                  clusterNum.add(assignments[j]);
                                  String u = data.get(j).stringValue(0);
                                  clstnum.add(new cluster(assignments[j], j, u));
                                  String result="cluster="+assignments[j]+" instance "+j+" user "+ u;
                                  System.out.println(result);
                              }  
                        }
                        
                        
                        txt_area_result.setText("");
                        int i=0;
                        
                        /*
                        for(int asg: assignments){
                            String u=null;
                            for (cluster clstobj : clstnum) {
                                if(clstobj.clusternum==asg){
                                    u=clstobj.user;
                                    break;
                                }
                            }
                            String result = "Instance "+i+"-> User "+u+" \n";
                            txt_area_result.appendText(result);
                            System.out.println(result);
                            i++;
                        }
                        */

                        for (int j = 0; j < test.numInstances(); j++) {
                            System.out.println("Clustering "+test.get(j));

                            int clst = kmeans.clusterInstance( test.get(j) );

                            String u=null;
                            for ( cluster clstobj : clstnum ){
                                if(clstobj.clusternum==clst){
                                    u=clstobj.user;
                                    break;
                                }
                            }

                            String result = "Instance "+j+"-> User "+u+" \n";
                            txt_area_result.appendText(result);
                            System.out.print(""+result);
                        }
                        
                        
                    //Instances centroids = kmeans.getClusterCentroids();
                    //printCentroids(centroids);
                    //printClusterInstance(test, kmeans);
                    
                            
                        }
                } catch (Exception ex) {
                    Logger.getLogger(Test_guiController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    SimpleKMeans kmeans;
    Instances data,test;
    
    class cluster{
        public double clusternum;
        public double instanceNum;
        public String user;
        cluster(double clusternum,double instancenum,String u){
            this.clusternum=clusternum;
            this.instanceNum=instancenum;
            this.user=u;
        }
    }
    ArrayList<cluster> clst = new ArrayList<>();
}
