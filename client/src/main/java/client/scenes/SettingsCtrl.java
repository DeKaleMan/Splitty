package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Currency;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.inject.Inject;
import java.io.File;
import java.util.concurrent.CountDownLatch;


public class SettingsCtrl {
    private ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    public final Config config;
    private boolean noConnection = false;

    private File flag;
    @FXML
    public Button cancelButton;
    @FXML
    public Button saveButton;
    @FXML
    private TextField emailField;
    @FXML
    public TextField currencyField;
    @FXML
    private Button uploadFlag;
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
    public SettingsCtrl(MainCtrl mainCtrl, ServerUtils serverUtils, Config config) {
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
        String iban = ibanField.getText();
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
    private CountDownLatch latch = new CountDownLatch(1);

    public void setLatch() {
        latch.countDown();
    }

    @FXML
    public void addLang() {
        //TODO check if it is a language or not to make it imaginary?

        this.newLang = langTextfield.getText();
        if (newLang != null && !newLang.isBlank()) {
            progressBar.setVisible(true);
            //setLanguage to new found language, we can no longer use an enum
            if (mainCtrl.languages.contains(newLang)) {
                langTextfield.setPromptText("This language already exists");
                langTextfield.setText("");
                return;
            }
            try {
                config.setLanguage(newLang);
                config.write();
                mainCtrl.languages.add(newLang);
                mainCtrl.addLang(newLang);
                this.latch = new CountDownLatch(1);
                mainCtrl.changeLanguage(newLang);
                langTextfield.setText("");

                // Wait for the changeLanguage method to complete
                //latch.await();

                new Thread(() -> {
                    try {
                        progressBar.setVisible(true);
                        latch.await();
                        Platform.runLater(() -> {
                            confirmlangBox.setVisible(true);
                            progressBar.setVisible(false);

                        });
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    getJSONFile();
                }).start();

            } catch (Exception e) {
                langTextfield.setText("no valid languageCode");
                System.out.println(e);
            } finally {
                progressBar.setVisible(false);
            }
        } else {
            progressBar.setVisible(false);
        }
    }


    public void getJSONFile() {
        String jsonString = serverUtils.getLanguageJSON(newLang);
        jsonString = jsonString.replace(",", ",\n");
        jsonString.replace("not a valid language", "failed to retrieve language");


        addedLang.setText(jsonString);
        progressBar.setVisible(false);
    }

    @FXML
    public void confirmLang() {
        progressBar.setVisible(true);
        confirmlangBox.setVisible(false);
        String lang = addedLang.getText();
        String stringForJson = lang.replace("\n", "");
        serverUtils.setNewLang(stringForJson, newLang);
        if (this.flag != null) {
            mainCtrl.addFlag(flag);
        }
        this.flag = null;
//        mainCtrl.addFlag(flag);
        mainCtrl.changeLanguage(newLang);
        this.progressBar.setVisible(false);
        this.uploadFlag.setStyle("-fx-background-color: #91a691;");
    }

    @FXML
    public void setFlag() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image to set as flag");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        flag = fileChooser.showOpenDialog(null);

        this.uploadFlag.setStyle("-fx-background-color: #2a8dff;");
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
        Platform.runLater(() -> {

            this.cancelButton.setText(txt);
        });
    }

    public void setSaveButton(String txt) {
        Platform.runLater(() -> {
            this.saveButton.setText(txt);

        });
    }

    public void setLanguage(String txt) {
        Platform.runLater(() -> {
            this.language.setText(txt);

        });
    }

    public void setLanguageText(String txt) {
        Platform.runLater(() -> {

            this.languageText.setText(txt);
        });
    }

    public void setLangInstructions(String txt) {
        Platform.runLater(() -> {

            this.langInstructions.setText(txt);
        });
    }

    public void setAddLangText(String txt) {
        Platform.runLater(() -> {

            this.addLangText.setText(txt);
        });
    }

    public void setAddLanguage(String txt) {
        Platform.runLater(() -> {

            this.addLanguage.setText(txt);
        });
    }

    public void setCurrency(String txt) {
        Platform.runLater(() -> {

            this.currency.setText(txt);
        });
    }

    public void setSettingsText(String txt) {
        Platform.runLater(() -> {

            this.settingsText.setText(txt);
        });
    }

    public void setChangServerButton(String txt) {
        Platform.runLater(() -> {

            this.changServerButton.setText(txt);
        });
    }
    public void setUploadFlag(String txt) {
        Platform.runLater(() -> {
            this.uploadFlag.setText(txt);
        });
    }
}
