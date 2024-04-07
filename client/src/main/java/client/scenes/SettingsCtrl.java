package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Currency;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SettingsCtrl {
    private ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Config config;
    private boolean noConnection = false;
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
    private Button changServerButton;
    @FXML
    private Label language;
    @FXML
    private Label languageText;
    @FXML
    private Label langInstructions;
    @FXML
    private Label addLangText;
    @FXML
    private Button addLanguage;
    @FXML
    private Label currency;
    @FXML
    private Label settingsText;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField bicField;

    private String newLang;

    @Inject
    public SettingsCtrl(MainCtrl mainCtrl, ServerUtils serverUtils,  Config config){
        this.mainCtrl = mainCtrl;
        this.config = config;
        this.serverUtils = serverUtils;
    }
    public void initialize() {
        mainCtrl.setButtonRedProperty(cancelButton);
        mainCtrl.setButtonGreenProperty(saveButton);
    }

    /**
     * sets all the fields to the values obtained by the config file
     */
    public void initializeFields(boolean noConnection) {
        this.noConnection = noConnection;
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
        languageText.setText(config.getLanguage());
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
        if (noConnection) {
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
    private TextArea addedLang;
    @FXML
    private VBox confirmlangBox;
    @FXML
    public void addLang(){
        progressBar.setVisible(true);
        this.newLang = langTextfield.getText();
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
                progressBar.setVisible(false);
                return;
            }
        }
        progressBar.setVisible(false);

        //TODO get jsonfile here
        confirmlangBox.setVisible(true);
        String jsonString = serverUtils.getLanguageJSON(newLang);
        jsonString = jsonString.replace(",", ",\n");
         addedLang.setText(jsonString);

    }

    @FXML
    public void confirmLang(){
        confirmlangBox.setVisible(false);
        String lang = addedLang.getText();
        String stringForJson = lang.replace("\n", "");
        JSONObject jsonObject = new JSONObject(stringForJson);
        serverUtils.setNewLang(jsonObject, newLang);

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
        mainCtrl.showServerStartup(noConnection);
    }

    public void setCancelButton(String txt) {
        this.cancelButton.setText(txt);
    }

    public void setSaveButton(String txt) {
        this.saveButton.setText(txt);
    }

    public void setLanguage(String txt) {
        this.language.setText(txt);
    }

    public void setLanguageText(String txt) {
        this.languageText.setText(txt);
    }

    public void setLangInstructions(String txt) {
        this.langInstructions.setText(txt);
    }

    public void setAddLangText(String txt) {
        this.addLangText.setText(txt);
    }

    public void setAddLanguage(String txt) {
        this.addLanguage.setText(txt);
    }

    public void setCurrency(String txt) {
        this.currency.setText(txt);
    }

    public void setSettingsText(String txt) {
        this.settingsText.setText(txt);
    }
    public void setChangServerButton(String txt){
        this.changServerButton.setText(txt);
    }
}
