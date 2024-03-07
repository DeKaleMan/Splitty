package client.scenes;

import client.utils.ServerUtils;
import commons.Participant;
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
import java.util.ResourceBundle;

public class ManageParticipantsCtrl implements Initializable {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    @FXML
    private Label titleLabel;

    @FXML
    private ListView participantsList;

    @FXML
    private Button undo;
    private Participant undone;
    @FXML
    private Button sendInvites;

    List<Participant> list;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //for now this is hardcoded but this should eventually be passed on
        this.list = new ArrayList<>();
//        list.add(new Participant("John", 30.65, "IDK some iban",
//        "Not sure what this is", "Me?", "email@email.nl"));
//        list.add(new Participant("Linda", 30.65, "IDK some iban",
//        "Not sure what this is", "Me?", "email@email.nl"));
        for(Participant p : list){
            this.participantsList.getItems().add(p.getName());
        }
    }

    @Inject
    public ManageParticipantsCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
    }


    public void backEventOverview(){
        mainCtrl.showSplittyOverview(titleLabel.getText());
    }

    /**
     * removes the participant from the list
     */
    @FXML
    public void removeParticipantFromList(){
        if(participantsList == null || participantsList.getItems().isEmpty()) System.out.println("empty list");
        else{
            ObservableList selected = participantsList.getSelectionModel().getSelectedItems();


            if(selected.isEmpty()) {
                System.out.println("none selected");
                return;
            }
            System.out.println("remove" + selected);
            remove((String) selected.getFirst());
        }

    }

    public void remove(String p){
        //make get the person linked to this name and delete from db
        participantsList.getItems().remove(p);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }


    @FXML
    public void showInvitation(){
        mainCtrl.showInvitation(titleLabel.getText());
    }

}
