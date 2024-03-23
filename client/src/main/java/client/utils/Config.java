package client.utils;

import commons.Currency;

import java.util.UUID;
import javax.inject.Inject;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class Config {
    private String connection;
    private String language; // this will be an enum type but has not been merged yet.
    private Currency currency;
    private String email;
    private String id;


    @Inject
    public Config(String connection, String language, Currency currency, String email, String id) {
        this.connection = connection;
        this.language = language;
        this.currency = currency;
        this.email = email;
        this.id = id;
    }

    public Config() {
    }

    public void read() {
        try {
            File file = new File("./client/src/main/resources/config");
            Scanner sc = new Scanner(file);

            String con = sc.next();
            String nullString = "null";
            if (nullString.equals(con)) {
                connection = null;
            } else {
                connection = con;
            }
            String lang = sc.next();
            language = switchLanguage(lang);
            String curr = sc.next();
            currency = switchCurrency(curr);
            String em = sc.next();
            if (nullString.equals(em)) {
                email = null;
            } else {
                email = em;
            }
            id = sc.next();
        } catch (NoSuchElementException | IOException e) {
            if (!write()) {
                System.out.println("Config file could not be created");
            }
        }
    }

    public String switchLanguage(String lang) {
        return  switch (lang) {     // es, de, zh, ar, is; should still be added but will be done when
            case "nl": yield "nl";  // the enum is merged
            default: yield "en";
        };
    }

    public Currency switchCurrency(String curr) {
        return switch (curr) {
            case "USD": yield Currency.USD ;
            case "CHF": yield Currency.CHF;
            default: yield Currency.EUR;
        };
    }

    public boolean write() {
        try {
            FileWriter writer = new FileWriter("./client/src/main/resources/config");

            if (connection == null) {
                writer.write("null ");
            } else {
                writer.write(connection + " ");
            }
            if (language == null) {
                writer.write("en ");
            } else {
                writer.write(language+ " ");
            }
            if (currency == null) {
                writer.write("EUR ");
            } else {
                writer.write(currency + " ");
            }
            if (email == null) {
                writer.write("null ");
            } else {
                writer.write(email + " ");
            }
            if (id == null) {
                id = UUID.randomUUID().toString();
            }
            writer.write(id + " ");
            writer.flush();

        } catch (IOException e) {
            System.out.println("File not found");
            return false;
        }
        return true;
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
}
