package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class SettingsCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Config config;
    @FXML
    public TextField serverURLField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField currencyField;

    @Inject
    public SettingsCtrl(ServerUtils server, MainCtrl mainCtrl, Config config){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
        this.config = config;
    }

    /**
     * sets all the fields to the values obtained by the config file
     */
    public void initializeFields() {
        if (config.getConnection() != null) {
            serverURLField.setText(config.getConnection());
        }
        if (config.getEmail() != null) {
            emailField.setText(config.getEmail());
        } else {
            emailField.setText("");
        }
        currencyField.setText(config.getCurrency().toString());
    }

    /**
     * The method correlated to the save settings button. Every field is retrieved and if nothing is
     * incorrect everything will be saved by writing it to the config file.
     */
    public void saveSettings() {
        String email = emailField.getText();
        String connection = serverURLField.getText();
        String currency = currencyField.getText();
        boolean abort = false;
        if (email == null || email.isEmpty()) {
            email = null;
        }
        if (currency == null || (!currency.equals("EUR") &&
                !currency.equals("CHF") && !currency.equals("USD"))) {
            abort = true;
            // set error message
        }
        if (connection == null || connection.isEmpty()) {
            abort = true;
            // set error message
        }
        if (abort) {
            return;
        }
        config.setEmail(email);
        config.setConnection(connection);
        config.setCurrency(config.switchCurrency(currency));
        config.write();
        back();
        // set saved message
    }

    public void back() {
        mainCtrl.showStartScreen();
    }

    public void initializeConfig() {
        config.read();
    }
    public String getEmail() {
        return config.getEmail();
    }
    public String getId() {
        return config.getId();
    }

    public String getConnection() {
        return config.getConnection();
    }


}
