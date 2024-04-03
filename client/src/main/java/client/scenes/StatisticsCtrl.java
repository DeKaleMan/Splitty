package client.scenes;


import client.utils.Config;
import client.utils.ServerUtils;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;

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
    private Label currency;

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
                                d.getName(), " amount ", d.pieValueProperty()
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
        currency.setText(String.valueOf(config.getCurrency()));
    }
    public void setFood() {
        this.food = stat[0];
    }
    public void setDrinks() {
        this.drinks = stat[1];
    }

    public void setTransport() {
        this.transport = stat[2];
    }

    public void setOther() {
        this.other = stat[3];
    }
    public void setTotalCost(double totalCost){
        this.totalCost.setText(String.valueOf(totalCost));
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
}
