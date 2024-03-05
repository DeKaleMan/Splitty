package client.scenes;
import client.utils.ServerUtils;

import commons.Transaction;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

public class DebtCtrl implements Initializable {

    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;

    @FXML
    private ListView listView;
    @FXML
    private Label titlelabel;

    @FXML
    private Button undo;

    private Transaction undone;

    @Inject
    public DebtCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;


    }

    @FXML
    public void back(){
        mainCtrl.showSplittyOverview(titlelabel.getText());
    }
    @FXML
    public void markReceived() throws NoSuchElementException, IndexOutOfBoundsException {
        if(listView == null || listView.getItems().isEmpty()) System.out.println("empty list");
        else{
            ObservableList selected = listView.getSelectionModel().getSelectedItems();


            if(selected.isEmpty()) {
                System.out.println("none selected");
                return;
            }
            System.out.println("remove" + selected);
            undone = (Transaction) selected.getFirst();
            removeFromDebts((Transaction) selected.getFirst());
            undo.setVisible(true);
        }

    }


    public void setTitlelabel(String title){
        titlelabel.setText((title));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        List<Transaction> list = new ArrayList<>();
//        list.add(new Transaction("John", 10.55));
//        list.add(new Transaction("Linda", 5.55));
//
//        this.listView.getItems().addAll(list);
//
//        undo.setVisible(false);
    }

    /**
     * removes a debt from the list and the database
     * @param t
     */
    public void removeFromDebts(Transaction t){
        //DO SOME DATABASE STUFF

        this.listView.getItems().remove(t);
    }

    @FXML
    public void sendMessage(){

    }
    @FXML
    public void undo(){
        this.listView.getItems().add(undone);
        undo.setVisible(false);
    }
}
