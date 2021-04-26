package sample.report;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.controllers.Controller;
import sample.controllers.Main;
import sample.controllers.Product;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ReportController {
private Controller controller = new Controller();
private Alert alert = new Alert(Alert.AlertType.INFORMATION);
private ArrayList<Product> arrayList = new ArrayList<Product>();

    @FXML
    private TextField path;

    @FXML
    private Button finish;

    @FXML
    void initialize(){
finish.setOnAction(actionEvent -> {
    try {
        makeSort();
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        makeReport();
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText("The report has been successfully generated");
        alert.showAndWait();
        StringBuilder stringBuilder = new StringBuilder("0");
       controller.setCashh(0);
       controller.setProfitt(0);
        controller.getArrayList().clear();
        controller.getList().getItems().clear();
        controller.writeObject();
        System.exit(0);
    } catch (Exception e) {
        alert.setTitle("WARNING");
        alert.setHeaderText(null);
        alert.setContentText("This file does not exist");
        alert.showAndWait();
    }


});
    }
    public void makeSort() throws IOException {
        for (Product product : controller.getArrayList()) {
            arrayList.add(product);
        }
        Collections.sort(arrayList, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return String.valueOf(o1.getAmount()).compareTo(String.valueOf(o2.getAmount()));
            }

        });


    }
    public void makeReport() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path.getText().trim()));
        bufferedWriter.write("Your products:" + "\n");
        bufferedWriter.write("\n");
        for (Product product : arrayList) {
            bufferedWriter.write(product.getId() + " | " + product.getName() + " | " + product.getPrice() + " | " + product.getAmount() + "\n");
        }
        bufferedWriter.write("\n");
        bufferedWriter.write("Your purchase - " + controller.getCash() + "\n");
        bufferedWriter.write("\n");
        bufferedWriter.write("Your benefit - " + controller.getProfit() + "\n");
        bufferedWriter.write("\n");
        double result = controller.getProfit() - controller.getCash();
        if(result >= 0){
            bufferedWriter.write("You earned - " + result + "\n");
        }else {
            bufferedWriter.write("You lost - " + result + "\n");
        }
        bufferedWriter.write("\n");
        bufferedWriter.write("Products that have been fully sold" + "\n");
        bufferedWriter.write("\n");
        for (Product product : arrayList) {
            if(product.getAmount() == 0){
                bufferedWriter.write(product.getId() + " | " + product.getName() + " | " + product.getPrice() + " | " + product.getAmount() + "\n");
            }
        }


    bufferedWriter.close();
    }


}

