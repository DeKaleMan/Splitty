package client.scenes;


import client.utils.Config;
import client.utils.ServerUtils;
import commons.Currency;
import commons.Participant;
import commons.Tag;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.Duration;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class StatisticsCtrl {
    @FXML
    private PieChart pieChart;

    private List<Tag> tagList;
    private double total;
    ObservableList<Participant> listViewData;
    private int eventId;

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Config config;

    @FXML
    private Button back;
    @FXML
    private Label titleLabel;
    @FXML
    private Label statisticsText;
    @FXML
    private Label totalCost;
    @FXML
    private Label totalCostText;
    @FXML
    private Label currencyErrorLabel;
    @FXML
    private ListView<Participant> shareListView;
    @FXML
    private Label shareOfExpensesLabel;
    @FXML
    private Label hoverLabel;

    @Inject
    public StatisticsCtrl(MainCtrl mainCtrl, ServerUtils serverUtils, Config config) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
        this.config = config;
        listViewData = FXCollections.observableArrayList();
    }

    private void setListViewUp() {
        shareListView.setVisible(!(total < 0.001));
        shareOfExpensesLabel.setVisible(!(total < 0.001));
        setPieChart();
        shareListView.setCellFactory(new Callback<ListView<Participant>, ListCell<Participant>>() {
            @Override
            public ListCell<Participant> call(ListView<Participant> participantListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Participant participant, boolean b) {
                        super.updateItem(participant, b);
                        if (participant == null || b) {
                            setText(null);
                        } else {
                            double amount = 0.0;
                            try {
                                amount = serverUtils.getExpenseByUuid(eventId,
                                        participant.getUuid())
                                    .stream()
                                    .mapToDouble(
                                        x -> mainCtrl.getAmountInDifferentCurrency(Currency.EUR,
                                            config.getCurrency(), x.getDate(), x.getTotalExpense()))
                                    .sum();
                            } catch (RuntimeException e) {
                                setText("-");
                                displayError();
                                return;
                            }
                            setText(participant.getName()
                                + ": "
                                + mainCtrl.getFormattedDoubleString(amount)
                                + java.util.Currency.getInstance(config.getCurrency().toString())
                                .getSymbol()
                            );
                        }
                    }
                };
            }
        });
    }

    /**
     * initialize the chart with the current values for food, drinks, transport and other
     */
    public void setPieChart() {
        pieChart.setLegendVisible(false);
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        this.tagList = serverUtils.getTagsByEvent(eventId);
        for (Tag t : tagList) {
            double d = serverUtils.getSumByTag(eventId, t.getName());
            if (d < 0.001) {
                continue;
            }
            data.add(new PieChart.Data(t.getName(), d));
        }
        pieChart.setData(data);
        for (PieChart.Data d : data) {
            List<Tag> tags = tagList.stream()
                    .filter(tag -> tag.getName().equals(d.getName()))
                    .toList();
            if (tags.isEmpty()) {
                continue;
            }
            d.getNode().setStyle("-fx-pie-color: " + tags.getFirst().getColour() + ";");
        }
        data.forEach(d -> d.nameProperty().bind(Bindings.concat(
                    d.getName(),
                    ": ",
                    mainCtrl.getFormattedDoubleString(d.pieValueProperty().getValue()),
                    java.util.Currency.getInstance(config.getCurrency().toString()).getSymbol()
                )
            )
        );
        pieChart.getData().forEach(d -> {
            Tooltip.install(d.getNode(), new Tooltip(
                mainCtrl.getFormattedDoubleString(100.0 * d.pieValueProperty().getValue() / total)
                    + "%"));
        });
    }

    //these all set the things for the diagram, theoretically they could all
    // be done in one method, but I wasn't sure how we were going to implement this
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setEventCode(int eventCode) {
        this.eventId = eventCode;
    }

    public void fetchStat() {
        setPieChart();
        setTotalCost(total);
    }


    public void setTotalCost(double totalCost) {
        try {
            this.totalCost.setText(mainCtrl.getFormattedDoubleString(totalCost)
                + java.util.Currency.getInstance(config.getCurrency().toString()).getSymbol());
        } catch (RuntimeException e) {
            this.totalCost.setText("-");
            displayError();
        }
    }

    @FXML
    public void goBack() {
        mainCtrl.showSplittyOverview(eventId);
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            goBack();
        }
    }

    private void displayError() {
        currencyErrorLabel.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
        visiblePause.setOnFinished(event1 -> currencyErrorLabel.setVisible(false));
        visiblePause.play();
    }

    public void refresh() {
        listViewData.clear();
        listViewData.addAll(serverUtils.getParticipants(eventId));
        total = serverUtils.getTotalCostEvent(eventId);
        try {
            total = mainCtrl.getAmountInDifferentCurrency(Currency.EUR, config.getCurrency(),
                new Date(), total);
            shareListView.setItems(listViewData);
            setListViewUp();
        } catch (RuntimeException e) {
            this.totalCost.setText("-");
            displayError();
        }
    }

    public void setStatisticsText(String txt) {
        this.statisticsText.setText(txt);
    }

    public void setTotalCostText(String txt) {
        this.totalCostText.setText(txt);
    }

    public void setBackButton(String txt) {
        this.back.setText(txt);
    }

    public void setHoverLabel(String t){
        this.hoverLabel.setText(t);
    }

    public void showHoverLabel(){
        hoverLabel.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(8));
        visiblePause.setOnFinished(
            event1 -> hoverLabel.setVisible(false)
        );
        visiblePause.play();
    }
}
