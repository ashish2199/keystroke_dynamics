/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keystroke_dynamics.train_gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Ashish Padalkar
 */
public class FXMLController implements Initializable {
    
    @FXML
    private Button btn_clear;

    @FXML
    private Button btn_next;
    
    @FXML
    private Button btn_write;
    
    @FXML
    private Button btn_generate_dataset;
    
    @FXML
    private Button btn_back;
    
    @FXML
    private TextField txt_input;
    
    @FXML
    private TextField txt_username;

    @FXML
    private Label lbl_tries;
    
    @FXML
    private Label lbl_error;

    @FXML
    private TextArea txt_area_records;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialise_events();
        initialise_btns();
    }    
    long keyPress_time=0;
    long keyRelease_time=0;
    long last_keyPress_time=0;
    long last_keyRelease_time=0;
    long up_to_up_time=0;
    long hold_time=0;
    long latency=0;
    String ch1;
    static int tries=0;
    static int PasswordSize=0;
    static String password;
    void initialise_events(){
        txt_input.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                last_keyRelease_time=keyRelease_time;
                
                Date d = new Date();
                keyPress_time=d.getTime();
                latency=calcLatency(keyPress_time, last_keyRelease_time);
                
                
            }
        });
        txt_input.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                last_keyPress_time=keyPress_time;
                Date d = new Date();
                keyRelease_time=d.getTime();
                hold_time=calcHoldTime(keyPress_time,keyRelease_time);
                up_to_up_time= calcUpToUpTime(last_keyRelease_time, keyRelease_time);
                ch1=event.getText();
                record r=null;
                String user = txt_username.getText();
                if(!user.equals("")){
                    if(!usernames.contains(user)){
                        usernames.add(user);
                    }
                    r = new record(ch1, hold_time, latency, up_to_up_time,user);
                    if(checkValidRecord(r)){
                        if(event.getCode()==KeyCode.BACK_SPACE){
                            System.out.println("backspace pressed");
                            recordsArraylist.remove(recordsArraylist.size()-1);
                        }
                        else
                        {
                            System.out.print("password so far: ");
                            for(record re : recordsArraylist){
                                System.out.print(""+re.ch);
                            }
                            System.out.println("");
                            recordsArraylist.add(r);
                            //lbl_error.setText("");
                        }
                        //printrecord(r);
                    }
                    else{
                        if(recordsArraylist.size()>1){
                        lbl_error.setText("Error Not a Valid Record Try again");
                        }
                        System.out.println("not added in arraylist ");
                        //rec.add(new record(ch1, "?", "?","?", user));
                    }
                }
            }
        });
    }
    
    void initialise_btns(){
        btn_clear.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                tries=0;
                txt_input.setText("");
                txt_area_records.setText("");
                recordsArraylist.clear();
                instances.clear();
                lbl_tries.setText(""+tries);
                txt_username.setText("");
                
            }
        });
        
        btn_next.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
                if(PasswordSize==0)
                {
                    PasswordSize=recordsArraylist.size();
                    password=txt_input.getText();
                    System.out.println("password="+password+" size="+PasswordSize);
                }
                
                if(txt_input.getText().equals(password)){
                    System.out.println("password matched");
                    if(!txt_username.getText().equals(""))
                        {
                            generateInstance();
                            lbl_error.setText("");
                        }
                        else
                        {
                             lbl_error.setText("Error Enter USERNAME");
                        }
                    }
                    else{
                        System.out.println("error wrong");
                        lbl_error.setText("Error Password Didnt Match Enter Again");
                    }
                
                lbl_tries.setText(""+tries);
                txt_input.setText("");
                recordsArraylist.clear();
            
            }
        });
        
        btn_write.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
            try {
                
                Stage savedStage = new Stage();
                
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save file");
                
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ARFF (*.arff)", "*.arff");
                fileChooser.getExtensionFilters().add(extFilter);
                
                Date d = new Date();
                String filename ="Records.arff";
                fileChooser.setInitialFileName(filename);
                File savedFile = fileChooser.showSaveDialog(savedStage);
                
                FileWriter fw=new FileWriter(savedFile);
                BufferedWriter bw= new BufferedWriter(fw);
                String writeData=txt_area_records.getText();
                writeData=writeData.replaceAll("\n", System.lineSeparator());
                bw.write(writeData);
                bw.close();
            }
            catch (Exception ex){
            }
            }
        });
        
        btn_back.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Keystroke_dynamics.Keystroke_dynamics.show_main_gui(Keystroke_dynamics.Keystroke_dynamics.getPrimaryStage());
                } catch (IOException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        btn_generate_dataset.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                data="@RELATION records \n" +
                            "@ATTRIBUTE user {";
                    
                for (String user : usernames) {
                        data += user;
                        if(usernames.indexOf(user)!=(usernames.size()-1)){
                            data+=",";
                        }
                    }
                    
                    data+="} \n";
                    data+="@ATTRIBUTE tries NUMERIC\n";    
                    
                        for (int i = 3; i < PasswordSize*3+3; i++) {
                            data += "@ATTRIBUTE "+( i/3);
                            if(i%3==0){data+="h";}
                            if(i%3==1){data+="l";}
                            if(i%3==2){data+="u";}
                            data += " NUMERIC\n";
                        }
                        
                    data += "@DATA \n";
                    
                    for (String inst : instances) {
                        data += inst;
                    }
                
                txt_area_records.setText(data);
                System.out.println(""+data);
                    
                
            }
        });
    }
    
    static String data;
    static ArrayList<record> recordsArraylist =new ArrayList<>();
    static ArrayList<String> usernames=new ArrayList<>();
    static ArrayList<String> instances=new ArrayList<>();
    
    static void generateInstance(){
        
        String inst="";
        
        System.out.println("arraylistsize"+recordsArraylist.size());
        System.out.println("password size"+PasswordSize);
        
        if(recordsArraylist.size()==PasswordSize){
            tries++;
            inst=inst.concat(""+recordsArraylist.get(0).user+","+tries+",");
            System.out.print(""+recordsArraylist.get(0).user+","+tries+",");

            for (record r : recordsArraylist) {
                inst=inst.concat(r.hold_time+","+r.latency+","+r.up_to_up_time);
                System.out.print(r.hold_time+","+r.latency+","+r.up_to_up_time);

                if(recordsArraylist.lastIndexOf(r)!=recordsArraylist.size()-1){
                    inst=inst.concat(",");
                    System.out.print(",");
                }
            }
              
            inst=inst.concat("\n");
            System.out.println("");


            instances.add(inst);
        }
        else{
            System.out.println("password size not equal so discarded");
        }
    }
    
    static void printrecord(record r){
        System.out.print("\nch= "+r.ch);
        System.out.print("  holdtime "+r.hold_time);
        System.out.print("  latency "+r.latency);
        System.out.print("  up to up time "+r.up_to_up_time);
        System.out.print("  user "+r.user);
    }
    
    static boolean checkValidRecord(record r){
        System.out.println(" Checking char="+r.ch+"  hold="+r.hold_time+"  latency="+r.latency);
        if( r.hold_time>3 && r.latency<1500 ){
            return true;
        }
        else{
            System.out.println("not matching");
            return false;
        }
    }
    
    static long calcHoldTime(long keypress,long keyrelease){
        return (keyrelease-keypress);
    }
    
    static long calcLatency(long keypress,long lastkeyrelease){
        return (keypress-lastkeyrelease);
    }
    
    static long calcUpToUpTime(long lastkeyrelease,long keyrelease){
        return (keyrelease-lastkeyrelease);
    }
    
}



class record{
    long up_to_up_time;
    long hold_time;
    long latency;
    String ch;
    String user;
    record(String ch,long holdtime,long latency,long up_to_up_time,String user){
        this.ch=ch;
        this.hold_time=holdtime;
        this.latency=latency;
        this.up_to_up_time=up_to_up_time;
        this.user=user;
    }
}