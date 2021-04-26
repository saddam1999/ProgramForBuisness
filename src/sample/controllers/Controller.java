package sample.controllers;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.Parent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class Controller implements Serializable{
    public volatile static ArrayList<Product> arrayList = new ArrayList<>();
    private static int a = arrayList.size();
    private static int c = a;
    private static double cash = 0;
    private static double profit = 0;
    private StringBuilder stringBuilder;
    private StringBuilder stringBuilderProfit;


    public static ArrayList<Product> getArrayList() {
        return arrayList;
    }


    public ListView getList() {
        return list;
    }

    public static void setCashh(double cash) {
        Controller.cash = cash;
    }

    public static void setProfitt(double profit) {
        Controller.profit = profit;
    }

    public static double getCash() {
        return cash;
    }

    public static double getProfit() {
        return profit;
    }

    @FXML
    private TextField name;

    @FXML
    private TextField price;

    @FXML
    private TextField amount;

    @FXML
    private Button buttonAdd;

    @FXML
    private ListView list = new ListView<>();;

    @FXML
    private TextArea balanceMinus = new TextArea();

    @FXML
    private TextField nameSell;

    @FXML
    private TextField amountSell;

    @FXML
    private Button buttonDelete;

    @FXML
    private TextField priceSell;

    @FXML
    private Button buttonSell;

    @FXML
    private TextArea balancePlus;

    @FXML
    private Button buttonReport;

    @FXML
    void initialize() throws IOException, ClassNotFoundException {
        readObject();
        balanceMinus.setEditable(false);
        balancePlus.setEditable(false);

        buttonAdd.setOnAction(actionEvent -> {
            a = arrayList.size();
            try {
                arrayAdd(total());
                list();
                setBalance();
                c = a;
            }catch (Exception e){

            }
        });

        buttonDelete.setOnAction(actionEvent -> {

            if(c > 0) {
                delete();
                setBalance();
                a = c;
            }
        });
        buttonSell.setOnAction(actionEvent -> {
sell();
        });
        buttonReport.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/report/report.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });


    }

    public void sell(){
       int id = Integer.parseInt(nameSell.getText().trim());
       double price = Double.parseDouble(priceSell.getText().trim());
       int amount =  Integer.parseInt(amountSell.getText().trim());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        for (int i = 0; i < arrayList.size(); i++) {
           if(i == arrayList.size() - 1 && arrayList.get(i).getId() != id){
               alert.setTitle("WARNING");
               alert.setHeaderText(null);
               alert.setContentText("This id does not exist");
               alert.showAndWait();
               break;
           }

            if(arrayList.get(i).getId() == id){
                if(arrayList.get(i).getAmount() - amount > 0){
                    arrayList.get(i).setAmount(arrayList.get(i).getAmount() - amount);

                list.getItems().clear();
                for (Product product : arrayList) {
                    list.getItems().addAll(product.getId() + " | " + product.getName() + " | " + product.getPrice() + " | " + product.getAmount());
                    list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                }
                    profit += price * amount;
                    stringBuilderProfit = new StringBuilder(String.valueOf(profit));
                    setProfit();
                break;
                }else if(arrayList.get(i).getAmount() - amount == 0){
                    profit += price * amount;
                    stringBuilderProfit = new StringBuilder(String.valueOf(profit));
                    setProfit();
                    arrayList.get(i).setAmount(0);
                    arrayList.get(i).setName(arrayList.get(i).getName() + " - " + "SOLD");
                    list.getItems().clear();
                    for (Product product : arrayList) {
                        list.getItems().addAll(product.getId() + " | " + product.getName() + " | " + product.getPrice() + " | " + product.getAmount());
                    }
                    break;
                   }else if(arrayList.get(i).getAmount() - amount < 0){
                    alert.setTitle("WARNING");
                    alert.setHeaderText(null);
                    alert.setContentText("Not enough items in stock");
                    alert.showAndWait();
                    break;
                }
            }
        }
    }
    public String[] total(){

        return prod().split(",");
    }

    public String prod(){
        return String.valueOf(a) + "," + name.getText() + "," + price.getText().trim() + "," + amount.getText();
    }

    public void delete(){
        --c;
        if(arrayList.get(c).getAmount() != 0){
        cash -= arrayList.get(c).getPrice() * arrayList.get(c).getAmount();

        }
        stringBuilder = new StringBuilder(String.valueOf(cash));

        list.getItems().remove(c);
        arrayList.remove(c);
    }

    public void list(){
        if(arrayList.get(a).getAmount() != 0 && arrayList.get(a).getPrice() !=0){
        list.getItems().addAll(arrayList.get(a).getId() + " | " + arrayList.get(a).getName() + " | " + arrayList.get(a).getPrice() + " | " + arrayList.get(a).getAmount());
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        cash += arrayList.get(a).getPrice() * arrayList.get(a).getAmount();
        stringBuilder = new StringBuilder(String.valueOf(cash));
        a++;

        }
    }

    public void arrayAdd(String[] a){
        arrayList.add(new Product(Integer.parseInt(a[0]),a[1],Double.parseDouble(a[2]),Integer.parseInt(a[3])));
    }


    public void setBalance(){
            balanceMinus.setText(String.valueOf(String.format("%.3f", Double.parseDouble(stringBuilder.toString()))));

    }
    public void setProfit(){
        balancePlus.setText(String.valueOf(String.format("%.3f",Double.parseDouble(stringBuilderProfit.toString()))));

    }
    public void readObject() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("src/sample/Object.bin");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/sample/size.txt"));
        BufferedReader profi = new BufferedReader(new FileReader("src/sample/profit.txt"));
        BufferedReader minus = new BufferedReader(new FileReader("src/sample/minus.txt"));
        int count = Integer.parseInt(bufferedReader.readLine());
        double prof = Double.parseDouble(profi.readLine());
        double min = Double.parseDouble(minus.readLine());
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        for (int i = 0; i < count; i++) {
            arrayList.add((Product) objectInputStream.readObject());
            list.getItems().addAll(arrayList.get(a).getId() + " | " + arrayList.get(a).getName() + " | " + arrayList.get(a).getPrice() + " | " + arrayList.get(a).getAmount());
            a++;
            c = a;
        }
        profit = prof;
        cash = min;
        stringBuilder = new StringBuilder(String.valueOf(cash));
        stringBuilderProfit = new StringBuilder(String.valueOf(profit));
        setBalance();
        setProfit();


minus.close();
profi.close();
        bufferedReader.close();
        objectInputStream.close();
    }





    public void writeObject() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("src/sample/Object.bin");
        ObjectOutputStream obs = new ObjectOutputStream(fileOutputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/sample/size.txt"));
        BufferedWriter bufferedWriterProfit = new BufferedWriter(new FileWriter("src/sample/profit.txt"));
        BufferedWriter bufferedWriterMinus = new BufferedWriter(new FileWriter("src/sample/minus.txt"));
        bufferedWriter.write(String.valueOf(arrayList.size()));
        bufferedWriterMinus.write(String.valueOf(cash));
        bufferedWriterProfit.write(String.valueOf(profit));
        for (Product product : arrayList) {
            obs.writeObject(product);
        }
        bufferedWriterMinus.close();
        bufferedWriterProfit.close();
        bufferedWriter.close();
        obs.close();
    }


}
