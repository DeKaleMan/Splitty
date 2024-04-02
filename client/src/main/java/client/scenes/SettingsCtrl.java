package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Currency;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;

public class SettingsCtrl {
    private ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Config config;
    private boolean startup = false;
    @FXML
    public Button cancelButton;
    @FXML
    public Button saveButton;
    @FXML
    private TextField emailField;
    @FXML
    public TextField currencyField;
    @FXML
    private TextField langTextfield;
    @FXML
    private Label languageTextField;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField bicField;

    @Inject
    public SettingsCtrl(MainCtrl mainCtrl, Config config){
        this.mainCtrl = mainCtrl;
        this.config = config;
    }
    public void initialize() {
        mainCtrl.setButtonRedProperty(cancelButton);
        mainCtrl.setButtonGreenProperty(saveButton);
    }

    /**
     * sets all the fields to the values obtained by the config file
     */
    public void initializeFields(boolean startup) {
        this.startup = startup;
        if (startup) {
            cancelButton.setVisible(false);
        } else {
            cancelButton.setVisible(true);
        }
        if (config.getEmail() != null) {
            emailField.setText(config.getEmail());
        } else {
            emailField.setText("");
        }
        if (config.getName() != null) {
            nameField.setText(config.getName());
        } else {
            nameField.setText("");
        }
        if (config.getIban() != null) {
            ibanField.setText(config.getIban());
        } else {
            ibanField.setText("");
        }
        if (config.getBic() != null) {
            bicField.setText(config.getBic());
        } else {
            bicField.setText("");
        }
        currencyField.setText(config.getCurrency().toString());
        languageTextField.setText(config.getLanguage());
    }

    /**
     * The method correlated to the save settings button. Every field is retrieved and if nothing is
     * incorrect everything will be saved by writing it to the config file.
     */
    public void saveSettings() {
        String email = emailField.getText();
        String currency = currencyField.getText();
        String name = nameField.getText();
        String iban  = ibanField.getText();
        String bic = bicField.getText();
        boolean abort = false;
        if (email == null || email.isEmpty()) {
            email = null;
        }
        if (currency == null || (!currency.equals("EUR") &&
                !currency.equals("CHF") && !currency.equals("USD"))) {
            abort = true;
            // set error message
        }
        if (abort) {
            return;
        }
        config.setEmail(email);
        config.setCurrency(Currency.valueOf(currency));
        config.setName(name);
        config.setIban(iban);
        config.setBic(bic);
        config.write();
        if (startup) {
            mainCtrl.showServerStartup(true);
        } else {
            back();
            mainCtrl.setConfirmationSettings();
        }

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

    public String getName() {
        return config.getName();
    }

    public String getIban() {
        return config.getIban();
    }

    public String getBic() {
        return config.getBic();
    }

    @FXML
    public void addLang(){
        progressBar.setVisible(true);
        String newLang = langTextfield.getText();
        if(newLang != null || !newLang.isBlank()){
            //setLanguage to new found language, we can no longer use an enum
            if(mainCtrl.languages.contains(newLang)){
                langTextfield.setPromptText("This language already exists");
                langTextfield.setText("");
                return;
            }
            try{
                mainCtrl.changeLanguage(newLang);
                mainCtrl.languages.add(newLang);
                mainCtrl.language = newLang;
                langTextfield.setText("");
            }catch (Exception e){
                progressBar.setVisible(false);
                langTextfield.setText("no valid languageCode");
                System.out.println(e);
            }
        }
        progressBar.setVisible(false);
    }

    public String getLanguage() {
        return config.getLanguage().toString();
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            back();
        }
        KeyCodeCombination k = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        if (k.match(press)) {
            saveSettings();
        }
    }

    public void changeServer() {
        mainCtrl.showServerStartup(false);
    }
}
