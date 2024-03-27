package client.scenes;


import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;

public class StatisticsCtrl {
    @FXML
    private PieChart pieChart;
    private int food = 1;
    private int drinks = 1;
    private int transport = 1;
    private int other = 1;

    @FXML
    private Label titleLabel;

    private int eventCode = 1;

    MainCtrl mainCtrl;
    @Inject
    public StatisticsCtrl(MainCtrl mainCtrl){
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

    public void setFood(int food) {
        this.food = food;
    }

    public void setDrinks(int drinks) {
        this.drinks = drinks;
    }

    public void setTransport(int transport) {
        this.transport = transport;
    }

    public void setOther(int other) {
        this.other = other;
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
