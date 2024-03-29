package client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Currency;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Config {
    private String connection;
    private String language;
    private Currency currency;
    private String email;
    private String id;

    private String name;
    private String iban;
    private String bic;


    public Config(String connection, String language, Currency currency, String email,
                  String id, String name, String iban, String bic) {
        this.connection = connection;
        this.language = language;
        this.currency = currency;
        this.email = email;
        this.id = id;
        this.name = name;
        this.iban = iban;
        this.bic = bic;
    }

    public Config() {
    }

    /**
     * This method is a bit more complicated because of the case where no file exists or a corrupt one.
     * The format of the text file is this: connection language currency email id
     * Every field is set to the retrieved string, if there are not exactly 5 fields or the file does not exist
     * the write method is called which sets every field to default right after the program starts.
     */
    public void read() {
        ClientFileIOutil fileIOutil = new ClientFileIOutilActual();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String filepath = fileIOutil.getConfigFile();
            Config tmpConfig = objectMapper.readValue(new File(filepath), Config.class);

            // Assign all fields to the config singleton
            this.bic = tmpConfig.getBic();
            this.id = tmpConfig.getId();
            this.email = tmpConfig.getEmail();
            this.iban = tmpConfig.getIban();
            this.connection = tmpConfig.getConnection();
            this.currency = tmpConfig.getCurrency();
            this.language = tmpConfig.getLanguage();
            this.name = tmpConfig.getName();
        } catch (NoSuchElementException | IOException e) {
            if (!write()) {
                System.out.println("Config file could not be created");
            }
        }
    }


    /**
     * write the fields in the most simple format: connection language currency email id
     * the fields are only written when saved
     * @return
     */
    public boolean write() {
        ClientFileIOutil fileIOutil = new ClientFileIOutilActual();
        fileIOutil.createFileStructure();
        try {
            String filepath = fileIOutil.getConfigFile();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(this);

            fileIOutil.write(jsonString, new File(filepath));

        } catch (IOException e) {
            System.out.println("File not found");
            return false;
        }
        return true;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config that = (Config) o;
        return Objects.equals(connection, that.connection) && Objects.equals(language, that.language)
                && currency == that.currency && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connection, language, currency, email);
    }

    public String getName() {
        return name;
    }

    public String getIban() {
        return iban;
    }

    public String getBic() {
        return bic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }
}
