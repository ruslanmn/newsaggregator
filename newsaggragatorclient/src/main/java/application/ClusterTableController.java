package application;

import datastructures.ClusterModel;
import datastructures.ItemModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * View-Controller for the person table.
 *
 * @author Marco Jakob
 */
public class ClusterTableController {

    @FXML
    public TableView<ItemModel> clusterTableView;
    @FXML
    public TextField titleFilterTextField;

    @Setter
    private PageManager pageManager;

    private FilteredList<ItemModel> items;
    private SortedList<ItemModel> sortableWrappedItems;
    private TableColumn<ItemModel, Double> distanceColumn;

    private ClusterModel clusterModel;

    public void updatePage(ClusterModel clusterModel, boolean showDistance) {
        titleFilterTextField.setText(StringUtils.EMPTY);

        this.clusterModel = clusterModel;
        items = new FilteredList<>(FXCollections.observableArrayList(clusterModel.getItemModels()), item -> true);

        sortableWrappedItems = new SortedList<>(items);
        clusterTableView.setItems(sortableWrappedItems);
        sortableWrappedItems.comparatorProperty().bind(clusterTableView.comparatorProperty());

        ObservableList<TableColumn<ItemModel, ?>> columns = clusterTableView.getColumns();

        if (columns.contains(distanceColumn) && !showDistance) {
            columns.remove(distanceColumn);
        } else if (!columns.contains(distanceColumn) && showDistance) {
            columns.add(0, distanceColumn);
        }

        clusterTableView.scrollTo(0);
    }

    @FXML
    private void initialize() {
        distanceColumn = new TableColumn<>("Дистанция");
        TableColumn<ItemModel, java.util.Date> dateColumn = new TableColumn<>("Дата");
        TableColumn<ItemModel, String> titleColumn = new TableColumn<>("Заголовок");
        TableColumn<ItemModel, String> sourceColumn = new TableColumn<>("Источник");

        clusterTableView.getColumns().add(distanceColumn);
        clusterTableView.getColumns().add(dateColumn);
        clusterTableView.getColumns().add(titleColumn);
        clusterTableView.getColumns().add(sourceColumn);

        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        sourceColumn.setCellValueFactory(new PropertyValueFactory<>("source"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(tableColumn -> new TableCell<ItemModel, Date>() {
            @Override
            protected void updateItem(Date date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty ? null : new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date));
            }
        });

        clusterTableView.setRowFactory( tv -> {
            TableRow<ItemModel> row = new TableRow<>();
            row.setOnMouseClicked(this::onMouseClick);
            return row ;
        });


        titleFilterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            items.setPredicate(item -> {
                if (StringUtils.isEmpty(newValue))
                    return true;
                return item == null ? false : item.getTitle().toLowerCase().contains(newValue.toLowerCase());
            });

        //clusterTableView.setItems(sortableWrappedItems);
        });
    }





    public static boolean openWebpage(String url) throws URISyntaxException {
        URI uri = new URI(url);
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                new Thread(() -> {
                    try {
                        desktop.browse(uri);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }).start();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void onMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            try {
                handleItemChoose();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void onKeyPressed(KeyEvent keyEvent) throws URISyntaxException {
        if(KeyCode.ENTER.equals(keyEvent.getCode())) {
            handleItemChoose();
        }
    }

    private void handleItemChoose() throws URISyntaxException {
        ItemModel itemModel = clusterTableView.getSelectionModel().getSelectedItem();
        if(itemModel != null) {
            openWebpage(itemModel.getUrl());
        }
    }

    public void backButtonPressed(ActionEvent actionEvent) {
        pageManager.changeToClustersPage();
    }

/*
    @FXML
    private TextField filterField;
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    private ObservableList<ItemModel> clusterItems = FXCollections.observableArrayList();

    public ClustersTableController() {
        masterData.add(new Person("Hans", "Muster"));
        masterData.add(new Person("Ruth", "Mueller"));
        masterData.add(new Person("Heinz", "Kurz"));
        masterData.add(new Person("Cornelia", "Meier"));
        masterData.add(new Person("Werner", "Meyer"));
        masterData.add(new Person("Lydia", "Kunz"));
        masterData.add(new Person("Anna", "Best"));
        masterData.add(new Person("Stefan", "Meier"));
        masterData.add(new Person("Martin", "Mueller"));
    }

    @FXML
    private void initialize() {
        // 0. Initialize the columns.
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Person> filteredData = new FilteredList<>(masterData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches first name.
                } else if (person.getLastName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Person> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(personTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        personTable.setItems(sortedData);
    }*/
}