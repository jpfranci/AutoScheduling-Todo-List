package ui;

import Model.*;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import exceptions.AlreadyInTodoListException;
import exceptions.InvalidActivityException;
import exceptions.InvalidPriorityException;
import exceptions.InvalidTimeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import observer.CalendarObserver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TodoListController implements CalendarObserver, Initializable{
    @FXML private TableColumn<TodoListEntry, Double> timeColumn;
    @FXML private TableColumn<TodoListEntry, String> activityColumn;
    @FXML private TableColumn<TodoListEntry, String> priorityColumn;
    @FXML private TableColumn<TodoListEntry, String> dueDateColumn;
    @FXML private TableView<TodoListEntry> todoListTable;
    @FXML private TextField activityInput;
    @FXML private TextField timeInput;
    @FXML private DatePicker dueDateInput;
    @FXML private TextField priorityInput;
    @FXML private Button scheduleEntries;

    private TodoList todoList;
    private TodoListFile todoListFile = new TodoListFile();
    private ObservableList<TodoListEntry> observableTodoListEntries;
    private Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private Alert informationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private Alert helpAlert = new Alert(Alert.AlertType.INFORMATION);
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    private boolean isFirstItemAdded;
    private String inputActivityValue;
    private String inputTimeValue;
    private String inputDueDateValue;
    private String inputPriorityValue;

    // MODIFIES: this
    // EFFECTS: Constructs a TodoListController and creates a new TodoList with a TodoListCalendar that
    // is observed by this and sets observerTodoList to TodoList
    public TodoListController() {
        TodoListCalendar todoListCalendar = new TodoListCalendar(this);
        todoList = new TodoList();
        observableTodoListEntries = FXCollections.<TodoListEntry>observableList(todoList.getTodoArray());
        todoList.setTodoListCalendar(todoListCalendar);
        todoListFile.setTodoList(todoList);
    }

    // MODIFIES: this
    // EFFECTS: Clears the TodoList and refreshes the tble
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

    // MODIFIES: this
    // EFFECTS: Initializes the columns and sets activity, time, and priority as editable, links up the table
    // to the observable list and sets the input format for due date
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeColumn.setCellValueFactory(new PropertyValueFactory<TodoListEntry, Double>("time"));
        activityColumn.setCellValueFactory(new PropertyValueFactory<TodoListEntry, String>("activity"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<TodoListEntry, String>("dueDate"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<TodoListEntry, String>("priorityString"));

        activityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        timeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priorityColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        todoListTable.getColumns().clear();
        todoListTable.getColumns().addAll(activityColumn, dueDateColumn, priorityColumn, timeColumn);
        todoListTable.setItems(observableTodoListEntries);
        todoListTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        isFirstItemAdded = true;
        setDueDateInputFormat();
    }

    // MODIFIES: this
    // EFFECTS: Sets Due Date Input Format of this to mmmm-yy-dd
    private void setDueDateInputFormat() {
        dueDateInput.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: When load button clicked prompts user to choose a file and if confirmed and valid, loads file
    // and sets this to the contents of file
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
        getInputs();

        try {
            if (isFirstItemAdded) {
                todoList.addTodoListEntry(inputActivityValue, inputDueDateValue, inputTimeValue, inputPriorityValue);
                makeTableSelectable();
                isFirstItemAdded = false;
            } else {
               TodoListEntry entry = todoList.addTodoListEntry(inputActivityValue, inputDueDateValue, inputTimeValue, inputPriorityValue);
               if(!observableTodoListEntries.contains(entry)) {
                   observableTodoListEntries.add(entry);
               }
            }
            todoListTable.scrollTo(observableTodoListEntries.size() - 1);
            refreshTable();
            resetInputs();
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

    private void resetInputs() {
        timeInput.setText("");
        activityInput.setText("");
        dueDateInput.setValue(null);
        priorityInput.setText("");
    }

    private void getInputs() {
        inputActivityValue = activityInput.getText();
        inputTimeValue = timeInput.getText();
        if(dueDateInput.getValue() != null) {
            inputDueDateValue = dueDateInput.getValue().toString();
        } else {
            inputDueDateValue = "";
        }
        inputPriorityValue = priorityInput.getText();
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
    private void changePriority(TableColumn.CellEditEvent editCell){
        TodoListEntry entrySelected = getSelectedEntry();
        String priority = getNewValue(editCell);
        try {
            entrySelected.setPriority(priority);
        } catch (InvalidPriorityException e){
            showErrorAlert("Invalid Priority", "Priority must be low, medium, or high");
        }
        refreshTable();
    }

    private String getNewValue(TableColumn.CellEditEvent editCell) {
        return editCell.getNewValue().toString();
    }

    private TodoListEntry getSelectedEntry() {
        return todoListTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void changeTime(TableColumn.CellEditEvent editCell){
        TodoListEntry entrySelected = getSelectedEntry();
        Double time = (Double) editCell.getNewValue();
        entrySelected.setTime(time);
    }

    @FXML
    private void changeActivity(TableColumn.CellEditEvent editCell){
        TodoListEntry entrySelected = getSelectedEntry();
        String activity = editCell.getNewValue().toString();
        if (!activity.equals("") && !todoList.isInTodoList(activity)) {
            entrySelected.setActivity(activity);
        } else {
            showErrorAlert("Error Modifying Activity", "Try again with a non-empty activity and " +
                    "make sure all entries are unique");
        }
        refreshTable();

    }

    @FXML
    private void removeEntryButtonClicked(ActionEvent event) {
        List<TodoListEntry> entriesSelected = getSelectedItems();

        if (entriesSelected.size() > 0) {
            boolean confirmationChoice = showConfirmationAlert("Todo List", "Are you sure you want to remove " +
                    "these entries:\n" + listToString(entriesSelected));

            if (confirmationChoice) {
                todoList.removeTodoListEntries(entriesSelected);
                observableTodoListEntries.removeAll(entriesSelected);
                refreshTable();
            }
        }
    }

    private ObservableList<TodoListEntry> getSelectedItems() {
        return todoListTable.getSelectionModel().getSelectedItems();
    }

    private String listToString(List<TodoListEntry> todoListEntries) {
        StringBuilder todoListEntriesStringBuilder = new StringBuilder();
        for (TodoListEntry entry : todoListEntries) {
            todoListEntriesStringBuilder.append(entry.getTodoListEntryActivity().getActivity());
            todoListEntriesStringBuilder.append(", ");
        }

        String todoListEntriesString = todoListEntriesStringBuilder.toString();
        return todoListEntriesString.substring(0, todoListEntriesString.length() - 2);
    }

    @FXML
    private void modifyDateButtonClicked(ActionEvent event) {
        List<TodoListEntry> entriesSelected = getSelectedItems();
        LocalDate dueDateInputValue = dueDateInput.getValue();

        if (dueDateInputValue != null && entriesSelected.size() > 0) {
            boolean alertChoice = showConfirmationAlert("Changing Due Dates",
                    "Are you sure you want to change "
                    +listToString(entriesSelected) + "\nto " + dueDateInputValue);

            if(alertChoice) {
                for (TodoListEntry todoListEntry : entriesSelected) {
                    todoListEntry.setDueDate(dueDateInputValue);
                }
                refreshTable();
                dueDateInput.setValue(null);
            }
        }
    }

    @FXML
    private void scheduleEntriesButtonClicked(ActionEvent event) {
        if (observableTodoListEntries.size() > 0) {
            todoList.scheduleEntries();
            scheduleEntries.setText("Schedule Entries");
        } else {
            showErrorAlert("Scheduling Entries Error", "Please make sure to have at least one entry in " +
                    "the todoList before trying to schedule");
        }
    }

    @FXML
    private void helpAddEntryClicked(ActionEvent event) {
        String headerText = "Adding Entries and Kinds of Entries";
        String contentText = "To add an entry fill out the activity name and time entry fields, these fields are mandatory.\n" +
                "The optional fields: priority and due date represent a kind of entry that needs to be completed promptly " +
                "so these entries are auto-scheduled first. Default values if one is filled out is low for priority and 7 days from today " +
                "for due date.\nClick the add entry button after you are finished.";
        showHelpAlert(headerText, contentText);
    }

    @FXML
    private void helpRemoveButtonClicked(ActionEvent event) {
        String headerText = "Removing Entries";
        String contentText = "To remove entries, simply click on the rows that you wish to remove and " +
                "press the remove entry button\nA confirmation alert will appear";
        showHelpAlert(headerText, contentText);
    }

    @FXML
    private void helpModifyButtonClicked(ActionEvent event) {
        String headerText = "Modifying Entries";
        String contentText = "For all columns except Due Date, you can edit the cell you want to edit by double" +
                " clicking on it and changing its contents.\nBut for Due Date, you must use the Date Picker tool " +
                "and press the modify date button once done.";
        showHelpAlert(headerText, contentText);
    }

    @FXML
    private void helpScheduleButtonClicked(ActionEvent event) {
        String headerText = "Scheduling Entries";
        String contentText = "Scheduling entries requires you to grant permission for this application " +
                "on Google Calendar upon" + " program startup.\n\nUpon doing so, your TodoList is " +
                "scheduled for a week by default from 10 am - 10 pm by " + "order of:\n" + "1) Decreasing " +
                "Priority(if applicable), 2) Increasing Due Date(if applicable)\n" + "3) Time, " +
                "4)Alphabetically by Activity";
        showHelpAlert(headerText, contentText);
    }

    private void showHelpAlert(String headerText, String contentText) {
        showAlert(helpAlert, "Todo List Help", headerText, contentText);
    }

    private void showAlert(Alert alert, String title, String headerText, String contentText) {
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
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

    @Override
    public void update(List<Event> addedEvents) {
        if (addedEvents.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            eventsToString(addedEvents, stringBuilder);
            changeConfirmationAlertHeaderText("Events scheduled are:\n" + stringBuilder.toString());
        } else {
            showInformationAlert("No entries scheduled", "Entries have all been scheduled already");
        }
    }

    private void showInformationAlert(String headerText, String contentText) {
        showAlert(informationAlert, "Todo List", headerText, contentText);
    }

    private void eventsToString(List<Event> events, StringBuilder stringBuilder) {
        for (Event event : events){
            stringBuilder.append(event.getSummary());
            stringBuilder.append(" from ");
            stringBuilder.append(dateTimeToLocalDateTimeString(event.getStart().getDateTime()));
            stringBuilder.append(" to ");
            stringBuilder.append(dateTimeToLocalDateTimeString(event.getEnd().getDateTime()));
            stringBuilder.append("\n");
        }
    }

    private String dateTimeToLocalDateTimeString(DateTime eventDateTime) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli
                (eventDateTime.getValue()), ZoneId.systemDefault());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        return localDateTime.format(dateTimeFormatter);
    }
}
