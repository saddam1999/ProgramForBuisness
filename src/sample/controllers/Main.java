package sample.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controllers.Controller;

public class Main extends Application {
private Controller controller = new Controller();
private boolean endProg = false;
    @Override
    public void start(Stage primaryStage) throws Exception{
       if(endProg == true){
           controller.writeObject();
           primaryStage.close();
       }
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Business Controller");
        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.setResizable(false);
        primaryStage.show();

    }
    @Override
    public void stop() throws Exception {
        super.stop();
        controller.writeObject();
    }
    public void end(){
        endProg = true;
    }




    public static void main(String[] args) {
        launch(args);
    }
}
