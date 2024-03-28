package client.scenes;


import client.utils.ServerUtils;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import javax.inject.Inject;

public class StatisticsCtrl {
    @FXML
    private PieChart pieChart;
    private int food = 0;
    private int drinks = 0;
    private int transport = 0;
    private int other = 0;
    private int[] stat;

    @FXML
    private Label titleLabel;

    private int eventCode;

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    @Inject
    public StatisticsCtrl(MainCtrl mainCtrl, ServerUtils serverUtils){
        this.mainCtrl = mainCtrl;
    }
    /**
     * initialize the chart with the current values for food, drinks, transport and other
     */
    public void setPieChart() {
        ObservableList<PieChart.Data> data =
                FXCollections.observableArrayList(
                        new PieChart.Data("Food", food),
                        new PieChart.Data("Drinks", drinks),
                        new PieChart.Data("Transport", transport),
                        new PieChart.Data("other", other)
                );

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

    @FXML
    public void goBack(){
        mainCtrl.showSplittyOverview(eventCode);
    }
}
