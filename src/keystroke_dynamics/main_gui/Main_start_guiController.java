/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keystroke_dynamics.main_gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Ashish Padalkar
 */
public class Main_start_guiController implements Initializable {

    @FXML
    private Button btn_train_start;

    @FXML
    private Button btn_exit;

    @FXML
    private Button btn_test_start;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialise_btns();
    }    
    
    void initialise_btns(){
        btn_exit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        btn_train_start.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Keystroke_dynamics.Keystroke_dynamics.show_train_gui(Keystroke_dynamics.Keystroke_dynamics.getPrimaryStage());
                } catch (IOException ex) {
                    Logger.getLogger(Main_start_guiController.class.getName()).log(Level.SEVERE, null, ex);
                }
                        
            }
        });
        btn_test_start.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Keystroke_dynamics.Keystroke_dynamics.show_test_gui(Keystroke_dynamics.Keystroke_dynamics.getPrimaryStage());
                } catch (IOException ex) {
                    Logger.getLogger(Main_start_guiController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
}
