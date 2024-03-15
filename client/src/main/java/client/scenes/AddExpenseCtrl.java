package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import commons.Person;
import commons.Type;
import commons.dto.ParticipantDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.inject.Inject;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AddExpenseCtrl implements Initializable {
    private final ServerUtils serverUtils;
    private final int eventCode = 1;
    private final MainCtrl mainCtrl;
    private final SplittyOverviewCtrl splittyCtrl;

    @FXML
    public Label titleLabel;

    @FXML
    private ComboBox<Participant> personComboBox;

    //all the things needed for the addExpense
    @FXML
    private TextArea whatFor;

    @FXML
    private DatePicker dateSelect;

    @FXML
    private ListView splitList;

    @FXML
    private TextField amount;

    @FXML
    private ComboBox category;
    private List<Participant> participant;
    @Inject
    public AddExpenseCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, SplittyOverviewCtrl splittyCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.splittyCtrl = splittyCtrl;
    }
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        Participant person1 = new Participant("name", 1000.00, "iBAN", "bIC", "holder", "email", new Event());
//        Person person2 = new Person("Paula", "Green");
        ObservableList<Participant> list = FXCollections.observableArrayList();
        List<Participant> allparticipants;
        try{
            allparticipants = serverUtils.getParticipants(eventCode);
        }
        catch (Exception e){
            allparticipants = new ArrayList<>();
        }

        list.addAll(allparticipants);
        list.add(person1);
        personComboBox.setItems(list);

        personComboBox.setCellFactory(param -> new ListCell<Participant>() {
            @Override
            protected void updateItem(Participant item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        personComboBox.setButtonCell(new ListCell<Participant>() {
            @Override
            protected void updateItem(Participant item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });


        createSplitList(list);
        category.setCellFactory(param -> new ListCell<Type>(){
            @Override
            protected void updateItem(Type item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });
        this.category.setItems(FXCollections.observableArrayList(Type.Food, Type.Drinks, Type.Travel, Type.Other));

        //this.category.setItems(FXCollections.observableArrayList("Food", "Drink", "Transport", "Other"));


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





    /**
     *     this collects all data and asks the splittycontroller to add the
     *     expense to the list but that might not be necessary when the database works
     */
    @FXML
    public void addExpense(){
        //collect information
        List<String> participants = getSelected();//get all the names of the participants

        //link these to participants and then add the expense
        LocalDate localDate = dateSelect.getValue();
        Date date = java.sql.Date.valueOf(localDate);
        Type type = (Type) category.getValue();
        double amountDouble = 0;
        try{
            amountDouble = Double.parseDouble(amount.getText());
        }catch (Exception e){
            amount.setText("NO VALID AMOUNT");
        }
        Participant payer = personComboBox.getValue();
        String description = whatFor.getText();
        //add to database
        splittyCtrl.addExpense(description, Type.Drinks, date, amountDouble, payer.getEmail());
        back();
    }

    public void setParticipant(List<Participant> participant) {
        this.participant = participant;
    }

    public void createSplitList(ObservableList<Participant> people){
        for(Participant p : people){
            RadioButton button = new RadioButton();
            button.setText(p.toString());
            splitList.getItems().add(button);
        }
    }


    /**
     *
     * @return a list with all the selected participants who should contribute to this expense
     */
    public List<String> getSelected() {
        List<String> res = new ArrayList<>();
        for(Object b : splitList.getItems()){
            if(((RadioButton) b).isSelected()){
                res.add(((RadioButton) b).getText());
            }
        }
        return res;
    }


    /**
     * select all buttons to split between
     */
    @FXML
    public void selectAll(){
        for(Object button: splitList.getItems()){
            ((RadioButton) button).setSelected(true);
        }
    }

}
