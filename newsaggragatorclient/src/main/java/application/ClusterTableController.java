package application;

import datastructures.ClusterModel;
import datastructures.ItemModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * View-Controller for the person table.
 *
 * @author Marco Jakob
 */
public class ClusterTableController {

    @FXML
    public TableView clusterTableView;
    @FXML
    public Label clusterNameLabel;

    @Setter
    private PageManager pageManager;

    private ClusterModel clusterModel;

    public void updatePage(ClusterModel clusterModel) {
        this.clusterModel = clusterModel;
        clusterTableView.setItems(FXCollections.observableArrayList(clusterModel.getItemModels()));
        clusterNameLabel.setText(clusterModel.getName());
        System.out.println(clusterModel.getItemModels());
    }

    @FXML
    private void initialize() {
        TableColumn<ItemModel, Double> distanceColumn = new TableColumn<>("Дистанция");
        TableColumn<ItemModel, java.util.Date> dateColumn = new TableColumn<>("Дата");
        TableColumn<ItemModel, String> titleColumn = new TableColumn<>("Заголовок");
        TableColumn<ItemModel, String> sourceColumn = new TableColumn<>("Источник");

        clusterTableView.getColumns().add(distanceColumn);
        clusterTableView.getColumns().add(dateColumn);
        clusterTableView.getColumns().add(titleColumn);
        clusterTableView.getColumns().add(sourceColumn);

        distanceColumn.prefWidthProperty().bind(
                clusterTableView.widthProperty().multiply(0.10)); // 10% width
        //dateColumn.prefWidthProperty().bind(
                //clusterTableView.widthProperty().multiply(0.15)); // 15% width
        titleColumn.prefWidthProperty().bind(
                clusterTableView.widthProperty().multiply(0.50)); // 10% width
        sourceColumn.prefWidthProperty().bind(
                clusterTableView.widthProperty().multiply(0.25)); // 10% width

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
    }

    public void backButtonPressed(MouseEvent mouseEvent) {
        if (MouseButton.PRIMARY.equals(mouseEvent.getButton())) {
            pageManager.changeToClustersPage();
        }
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