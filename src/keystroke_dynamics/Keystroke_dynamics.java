package Keystroke_dynamics;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Keystroke_dynamics extends Application {
    static Scene scene;
    static Stage pStage;
    @Override
    public void start(Stage stage) throws Exception {
        URL res = getClass().getResource("main_gui/main_start_gui.fxml");
        Parent root = FXMLLoader.load(res);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        setPrimaryStage(stage);
        
    }
    
    public static Stage getPrimaryStage() {
        return pStage;
    }
    
    private void setPrimaryStage(Stage pStage) {
        Keystroke_dynamics.pStage = pStage;
    }
    
    public static void show_train_gui(Stage stg) throws IOException{
        Keystroke_dynamics k = new Keystroke_dynamics();
        URL res = k.getClass().getResource("train_gui/FXML.fxml");
        Parent root = FXMLLoader.load(res);
        scene = new Scene(root);
        stg.setScene(scene);
        stg.show();
    }
    
    public static void show_test_gui(Stage stg) throws IOException{
        Keystroke_dynamics k = new Keystroke_dynamics();
        URL res = k.getClass().getResource("test_gui/test_gui.fxml");
        Parent root = FXMLLoader.load(res);
        scene = new Scene(root);
        stg.setScene(scene);
        stg.show();
    }
    
    public static void show_main_gui(Stage stg) throws IOException{
        Keystroke_dynamics k = new Keystroke_dynamics();
        URL res = k.getClass().getResource("main_gui/main_start_gui.fxml");
        Parent root = FXMLLoader.load(res);
        scene = new Scene(root);
        stg.setScene(scene);
        stg.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}