package ui;

import Model.*;
import exceptions.AlreadyInTodoListException;
import exceptions.InvalidActivityException;
import exceptions.InvalidTimeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TodoListController implements Initializable{
    private TodoList todoList;
    private TodoListFile todoListFile = new TodoListFile();
    private ListPrinter listPrinter = new ListPrinter();
    private ObservableList<TodoListEntry> observableTodoListEntries;

    @FXML private MenuItem loadButton;
    @FXML private MenuItem createNewFileButton;
    @FXML private MenuItem saveButton;
    @FXML private TableColumn<TodoListEntry, String> timeColumn;
    @FXML private TableColumn<TodoListEntry, String> activityColumn;
    @FXML private TableColumn<TodoListEntry, String> priorityColumn;
    @FXML private TableColumn<TodoListEntry, String> dueDateColumn;
    @FXML private TableView<TodoListEntry> todoListTable;
    @FXML private TextField activityInput;
    @FXML private TextField timeInput;
    @FXML private TextField dueDateInput;
    @FXML private TextField priorityInput;

    private Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private Alert helpAlert = new Alert(Alert.AlertType.INFORMATION);
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    private boolean isFirstItemAdded = true;

    public TodoListController() {
        TodoListCalendar todoListCalendar = new TodoListCalendar(listPrinter);
        todoList = new TodoList();
        observableTodoListEntries = FXCollections.<TodoListEntry>observableList(todoList.getTodoArray());
        todoList.setTodoListCalendar(todoListCalendar);
        todoListFile.setTodoList(todoList);
    }

    @FXML
    private void createNewFile() {
        boolean alertChoice = showConfirmationAlert("Creating a new Todo List",
                "Please press OK to create a new Todo List" + "\nor press Cancel to" + " exit this window");

        if (alertChoice) {
            changeConfirmationAlertHeaderText("Creating a new Todo List!");
            todoList.createNewTodoList();
            observableTodoListEntries.clear();
            observableTodoListEntries.setAll(todoList.getTodoArray());
            refreshTable();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeColumn.setCellValueFactory(new PropertyValueFactory<TodoListEntry, String>("time"));
        activityColumn.setCellValueFactory(new PropertyValueFactory<TodoListEntry, String>("activity"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<TodoListEntry, String>("dueDate"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<TodoListEntry, String>("priorityString"));

        todoListTable.getColumns().clear();
        todoListTable.getColumns().addAll(activityColumn, dueDateColumn, priorityColumn, timeColumn);
        todoListTable.setItems(observableTodoListEntries);
        todoListTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void handleLoadButtonClicked(ActionEvent event) {
        File file = chooseFile("load");

        if (file != null) {
            boolean alertChoice =  showConfirmationAlert("Loading a TodoList File", "Please press " +
                    "OK to load " + file.getName() + " \nor press Cancel to" + " pick another file or create a new file");

            if(alertChoice) {
                try {
                    todoListFile.load(file);
                    changeConfirmationAlertHeaderText(file.getName() + " loaded successfully");
                    isFirstItemAdded = false;
                    if (todoList.getTodoArray().size() > 0) {
                        observableTodoListEntries.clear();
                        observableTodoListEntries.addAll(todoList.getTodoArray());
                    }
                } catch(IOException e) {
                    changeConfirmationAlertHeaderText(file.getName() + " could not be loaded");
                }
            }
        }
    }

    @FXML
    private void handleSaveButtonClicked(ActionEvent event) {
        File file = chooseFile("save");

        if (file != null) {
            try {
                todoListFile.save(file);
                showConfirmationAlert("Saving a TodoList File", "Success saving file to " + file.getName());
            } catch(IOException e) {
                changeConfirmationAlertHeaderText("Todo list could not be saved to " + file.getName() + " successfully");
            }
        }
    }

    private File chooseFile(String type) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TodoList Files", "*.json"));
        fileChooser.setInitialDirectory(new File("Saved"));

        if (type.equals("load")) {
            return fileChooser.showOpenDialog(null);
        } else if(type.equals("save")) {
            return fileChooser.showSaveDialog(null);
        }
        return null;
    }

    @FXML
    private void addEntryButtonClicked(ActionEvent event) {
        String activity = activityInput.getText();
        String time = timeInput.getText();
        String dueDate = dueDateInput.getText();
        String priority = priorityInput.getText();

        try {
            if (isFirstItemAdded) {
                todoList.addTodoListEntry(activity, dueDate, time, priority);
                makeTableSelectable();
                isFirstItemAdded = false;
            } else {
               TodoListEntry entry = todoList.addTodoListEntry(activity, dueDate, time, priority);
               if(!observableTodoListEntries.contains(entry)) {
                   observableTodoListEntries.add(entry);
               }
            }
            todoListTable.scrollTo(observableTodoListEntries.size() - 1);
            refreshTable();
        } catch(InvalidActivityException e) {
            showErrorAlert("Activity cannot be empty", "Please try again with an activity value");
        } catch(InvalidTimeException e) {
            showErrorAlert("Time must be a valid number",
                    "Please try again with a valid number as input");
        } catch(AlreadyInTodoListException e) {
            showErrorAlert("No duplicate activities allowed",
                    "Please try again with an entry that doesn't have the same name");
        }
    }

    private void makeTableSelectable() {
        activityColumn.setSortType(TableColumn.SortType.ASCENDING);
        todoListTable.getSortOrder().add(activityColumn);
        todoListTable.getSortOrder().remove(activityColumn);
    }

    private void refreshTable() {
        todoListTable.getColumns().get(0).setVisible(false);
        todoListTable.getColumns().get(0).setVisible(true);
    }

    private void showErrorAlert(String headerText, String contentText) {
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText(headerText);
        errorAlert.setContentText(contentText);
        errorAlert.showAndWait();
    }

    @FXML
    private void removeEntryButtonClicked(ActionEvent event) {
        List<TodoListEntry> entriesSelected;

        entriesSelected = todoListTable.getSelectionModel().getSelectedItems();

        if (entriesSelected != null) {
            boolean confirmationChoice = showConfirmationAlert("Todo List", "Are you sure you want to remove " +
                    "these entries:\n" + listToString(entriesSelected));

            if (confirmationChoice) {
                todoList.removeTodoListEntries(entriesSelected);
                observableTodoListEntries.removeAll(entriesSelected);
                refreshTable();
            }
        }
    }

    private String listToString(List<TodoListEntry> todoListEntries) {
        StringBuilder todoListEntriesString = new StringBuilder();
        for (TodoListEntry entry : todoListEntries) {
            todoListEntriesString.append(entry.getTodoListEntryActivity().getActivity());
            todoListEntriesString.append("\t");
        }

        return todoListEntriesString.toString();
    }

    @FXML
    private void modifyEntryButtonClicked(ActionEvent event) {

    }

    @FXML
    private void scheduleEntriesButtonClicked(ActionEvent event) {

    }

    @FXML
    private void helpAddEntryClicked(ActionEvent event) {
        String headerText = "Adding Entries and Kinds of Entries";
        String contentText = "To add an entry fill out the activity name and time entry fields, these fields are mandatory.\n\n" +
                "The optional fields: priority and due date represent a kind of entry that needs to be completed promptly " +
                "so these entries are auto-scheduled first. Default values if one is filled out is low for priority and 7 days from today " +
                "for due date.\n\nClick the add entry button after you are finished.";
        createAndShowHelpAlert(headerText, contentText);
    }

    @FXML
    private void helpRemoveButtonClicked(ActionEvent event) {
        String headerText = "Removing Entries";
        String contentText = "To remove entries, simply click on the rows that you wish to remove and " +
                "press the remove entry button\n\n A confirmation alert will appear";
        createAndShowHelpAlert(headerText, contentText);
    }

    @FXML
    private void helpModifyButtonClicked(ActionEvent event) {

    }

    @FXML
    private void helpScheduleButtonClicked(ActionEvent event) {
        String headerText = "Scheduling Entries";
        String contentText = "Scheduling entries requires you to grant permission for this application " +
                "on Google Calendar upon" + " program startup.\n\nUpon doing so, your TodoList is " +
                "scheduled for a week by default from 10 am - 10 pm by " + "order of:\n" + "1) Decreasing " +
                "Priority(if applicable), 2) Increasing Due Date(if applicable)\n" + "3) Time, " +
                "4)Alphabetically by Activity";
        createAndShowHelpAlert(headerText, contentText);
    }

    private void createAndShowHelpAlert(String headerText, String contentText) {
        helpAlert.setTitle("Todo List Help");
        helpAlert.setHeaderText(headerText);
        helpAlert.setContentText(contentText);
        helpAlert.showAndWait();
    }

    private void changeConfirmationAlertHeaderText(String headerText) {
        confirmationAlert.setHeaderText(headerText);
        confirmationAlert.showAndWait();
    }

    private boolean showConfirmationAlert(String title, String headerText) {
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(headerText);
        Optional<ButtonType> buttonResult = confirmationAlert.showAndWait();

        return buttonResult.isPresent() && buttonResult.get() == ButtonType.OK;
    }
}
