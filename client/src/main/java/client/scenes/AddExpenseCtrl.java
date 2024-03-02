package client.scenes;

import client.utils.ServerUtils;
import commons.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class AddExpenseCtrl implements Initializable {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    @FXML
    public Label titleLabel;

    @FXML
    private ComboBox<String> personComboBox;
    @Inject
    public AddExpenseCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }



    @FXML
    public void back() {
        mainCtrl.showSplittyOverview(titleLabel.getText());
    }

    /**
     * Sets the title of the event
     * @param title event's title
     */
    public void setTitle(String title){
        titleLabel.setText(title);
    }


    @FXML
    private TextField whatFor;
    @FXML
    private ComboBox person;

    @FXML
    private DatePicker dateSelect;

    @FXML
    public void addExpense(){
        //collect information
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Person person1 = new Person("John", "Doe");
        Person person2 = new Person("Paula", "Green");
        ObservableList<String> list = FXCollections.observableArrayList(person1.firstName, person2.firstName);
        personComboBox.setItems(list);
    }
}
