package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.inject.Inject;

public class AddExpenseCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    @FXML
    public Label titleLabel;

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
}
