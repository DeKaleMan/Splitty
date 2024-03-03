package client.scenes;

import client.utils.ServerUtils;
import commons.Participant;
import commons.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddExpenseCtrl implements Initializable {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final SplittyOverviewCtrl splittyCtrl;

    @FXML
    public Label titleLabel;

    @FXML
    private ComboBox<String> personComboBox;
    @Inject
    public AddExpenseCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, SplittyOverviewCtrl splittyCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.splittyCtrl = splittyCtrl;
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
    private DatePicker dateSelect;

    @FXML
    private ListView splitList;

    @FXML
    private TextField amount;

    @FXML
    private ComboBox category;



    private List<Participant> participant;

    @FXML
    public void addExpense(){
        //collect information
        List<String> participants = getSelected();//get all the names of the participants
        //link these to participants and then add the expense

        String dateText = this.dateSelect.getAccessibleText();
        try{
            Double amountDouble = Double.parseDouble(amount.getText());
        }catch (Exception e){
            amount.setText("NO VALID AMOUNT");
        }
        String payer = personComboBox.getPromptText();
        splittyCtrl.addExpense();

        back();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Person person1 = new Person("John", "Doe");
        Person person2 = new Person("Paula", "Green");
        ObservableList<String> list = FXCollections.observableArrayList(person1.firstName, person2.firstName);
        personComboBox.setItems(list);
        createSplitList(list);
        this.category.setItems(FXCollections.observableArrayList("Food", "Drink", "Transport", "Other"));
    }

    public void setParticipant(List<Participant> participant) {
        this.participant = participant;
    }

    public void createSplitList(ObservableList<String> people){
        for(String p : people){
            RadioButton button = new RadioButton();
            button.setText(p.toString());
            splitList.getItems().add(button);
        }
    }



    public List<String> getSelected() {
        List<String> res = new ArrayList<>();
        for(Object b : splitList.getItems()){
            if(((RadioButton) b).isSelected()){
                res.add(((RadioButton) b).getText());
            }
        }
        return res;
    }
    @FXML
    public void selectAll(){
        for(Object button: splitList.getItems()){
            ((RadioButton) button).setSelected(true);
        }
    }

}
