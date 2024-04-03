package client.scenes;


import client.utils.Config;
import client.utils.ServerUtils;
import commons.Currency;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import javax.inject.Inject;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

public class StatisticsCtrl {
    @FXML
    private PieChart pieChart;
    private double food = 0;
    private double drinks = 0;
    private double transport = 0;
    private double other = 0;
    private double[] stat;

    @FXML
    private Label titleLabel;
    @FXML
    private Label statisticsText;
    @FXML
    private Label totalCost;
    @FXML
    private Label totalCostText;
    @FXML
    private Label errorLabel;

    private int eventCode;

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Config config;
    @Inject
    public StatisticsCtrl(MainCtrl mainCtrl, ServerUtils serverUtils, Config config){
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
        this.config = config;
    }
    /**
     * initialize the chart with the current values for food, drinks, transport and other
     */
    public void setPieChart() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        if(this.food != 0.0) data.add(new PieChart.Data("Food", food));
        if(this.drinks != 0.0) data.add(new PieChart.Data("Drinks", drinks));
        if(this.transport != 0.0) data.add(new PieChart.Data("Transport", transport));
        if(this.other != 0.0) data.add(new PieChart.Data("Other", other));

        data.forEach(d -> d.nameProperty().bind(Bindings.concat(
                    d.getName(),
                    ": ",
                    mainCtrl.getFormattedDoubleString(d.pieValueProperty().getValue()),
                    java.util.Currency.getInstance(config.getCurrency().toString()).getSymbol()
                        )
                )

        );

        pieChart.setData(data);
    }
    //these all set the things for the diagram, theoretically they could all
    // be done in one method but I wasn't sure how we were going to implement this
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }
    public void fetchStat(){
        this.stat = serverUtils.getStatisticsByEventID(eventCode);
        setFood();
        setDrinks();
        setTransport();
        setOther();
        setTotalCost(serverUtils.getTotalCostEvent(eventCode));
    }
    public void setFood() {
        try {
            this.food = mainCtrl.getAmountInDifferentCurrency(Currency.EUR, config.getCurrency(),new Date(),stat[0]);
        } catch(RuntimeException e){
            displayError();
        }
    }
    public void setDrinks() {
        try {
            this.drinks = mainCtrl.getAmountInDifferentCurrency(Currency.EUR, config.getCurrency(),new Date(),stat[1]);
        } catch(RuntimeException e){
            displayError();
        }
    }

    public void setTransport() {
        try {
            this.transport = mainCtrl.getAmountInDifferentCurrency(Currency.EUR, config.getCurrency(),new Date(),stat[2]);
        } catch(RuntimeException e){
            displayError();
        }
    }

    public void setOther() {
        try {
            this.other = mainCtrl.getAmountInDifferentCurrency(Currency.EUR, config.getCurrency(),new Date(),stat[3]);
        } catch(RuntimeException e){
            displayError();
        }
    }
    public void setTotalCost(double totalCost){
        try{
            this.totalCost.setText(mainCtrl.getFormattedDoubleString(
                mainCtrl.getAmountInDifferentCurrency(Currency.EUR, config.getCurrency(),new Date(),totalCost))
            + java.util.Currency.getInstance(config.getCurrency().toString()).getSymbol());
        }catch(RuntimeException e){
            this.totalCost.setText("-");
            displayError();
        }
    }

    @FXML
    public void goBack(){
        mainCtrl.showSplittyOverview(eventCode);
    }
    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            goBack();
        }
    }

    private void displayError(){
        errorLabel.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
        visiblePause.setOnFinished(event1 -> errorLabel.setVisible(false));
        visiblePause.play();
    }
}
