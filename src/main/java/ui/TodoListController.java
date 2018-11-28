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
    private static final String EMPTY_INPUT = "";

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
                    return EMPTY_INPUT;
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
    // EFFECTS: Prompts user to choose a file to save to and saves the state of this TodoList
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

    // EFFECTS: Prompts user to choose a file to open or save based on type and opens the file chooser
    // on the default TodoList Saved directory with a filter set to .json files
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
    // MODIFIES: this
    // EFFECTS: Takes values from input fields and creates a TodoList entry if valid, if not
    // then shows an applicable error message
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

    // MODIFIES: this
    // EFFECTS: Resets input fields to default values
    private void resetInputs() {
        timeInput.setText(EMPTY_INPUT);
        activityInput.setText(EMPTY_INPUT);
        dueDateInput.setValue(null);
        priorityInput.setText(EMPTY_INPUT);
    }

    // MODIFIES: this
    // EFFECTS: Gets values of input fields
    private void getInputs() {
        inputActivityValue = activityInput.getText();
        inputTimeValue = timeInput.getText();
        if(dueDateInput.getValue() != null) {
            inputDueDateValue = dueDateInput.getValue().toString();
        } else {
            inputDueDateValue = EMPTY_INPUT;
        }
        inputPriorityValue = priorityInput.getText();
    }

    // MODIFIES: this
    // EFFECTS: Makes table selectable for new users by setting a TableView sort and removing it
    private void makeTableSelectable() {
        activityColumn.setSortType(TableColumn.SortType.ASCENDING);
        todoListTable.getSortOrder().add(activityColumn);
        todoListTable.getSortOrder().remove(activityColumn);
    }

    // MODIFIES: this
    // EFFECTS: Refreshes table to reflect changes to this TodoList
    private void refreshTable() {
        todoListTable.getColumns().get(0).setVisible(false);
        todoListTable.getColumns().get(0).setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: Modifies this error alert and displays the current error to the screen
    private void showErrorAlert(String headerText, String contentText) {
        showAlert(errorAlert, "Error", headerText, contentText);
    }

    @FXML
    // MODIFIES: this
    // EFFECTS: Changes priority of TodoList entry selected if valid, if not then prints error message
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

    // EFFECTS: Returns new value of cell in string format
    private String getNewValue(TableColumn.CellEditEvent editCell) {
        return editCell.getNewValue().toString();
    }

    // EFFECTS: Returns the current selected entry in TableView
    private TodoListEntry getSelectedEntry() {
        return todoListTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    // MODIFIES: this
    // EFFECTS: Changes entry selected's time to input value of user
    private void changeTime(TableColumn.CellEditEvent editCell){
        TodoListEntry entrySelected = getSelectedEntry();
        Double time = (Double) editCell.getNewValue();
        entrySelected.setTime(time);
    }

    @FXML
    // MODIFIES: this
    // EFFECTS: Changes entry selected's activity to input activity of user if valid,
    // shows error alert if activity is not unique or no input given
    private void changeActivity(TableColumn.CellEditEvent editCell){
        TodoListEntry entrySelected = getSelectedEntry();
        String activity = editCell.getNewValue().toString();
        if (!activity.equals(EMPTY_INPUT) && !todoList.isInTodoList(activity)) {
            entrySelected.setActivity(activity);
        } else {
            showErrorAlert("Error Modifying Activity", "Try again with a non-empty activity and " +
                    "make sure all entries are unique");
        }
        refreshTable();
    }

    @FXML
    // MODIFIES: this
    // EFFECTS: Gets selected items of user and gives them a confirmation alert before removing all entries from
    // this TodoList and this Observable list
    private void removeEntryButtonClicked(ActionEvent event) {
        List<TodoListEntry> entriesSelected = getSelectedItems();

        if (entriesSelected.size() > 0) {
            boolean confirmationChoice = showConfirmationAlert("Todo List", "Are you sure you want to " +
                    "remove " + "these entries:\n" + listToString(entriesSelected));

            if (confirmationChoice) {
                todoList.removeTodoListEntries(entriesSelected);
                observableTodoListEntries.removeAll(entriesSelected);
                refreshTable();
            }
        }
    }

    // EFFECTS: Returns selected items of user in TableView
    private ObservableList<TodoListEntry> getSelectedItems() {
        return todoListTable.getSelectionModel().getSelectedItems();
    }

    // EFFECTS: Converts a list of TodoListEntries into a string format of Activity1, Activity2, ..., Activityn
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
    // MODIFIES: this
    // EFFECTS: Changes dueDate value of entries selected if valid, and shows confirmation alert before doing so
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
    // EFFECTS: Schedules entries of TodoList to Google calendar and shows confirmation alert if successful, else
    // shows an error alert if TodoList is empty
    private void scheduleEntriesButtonClicked(ActionEvent event) {
        if (observableTodoListEntries.size() > 0) {
            todoList.scheduleEntries();
            scheduleEntries.setText("Schedule Entries");
        } else {
            showErrorAlert("Scheduling Entries Error", "Please make sure to have at least one entry in "
                    + "the todoList before trying to schedule");
        }
    }

    @FXML
    // MODIFIES: this
    // EFFECTS: Displays instructions on how to add entries using the UI
    private void helpAddEntryClicked(ActionEvent event) {
        String headerText = "Adding Entries and Kinds of Entries";
        String contentText = "To add an entry fill out the activity name and time entry fields, these fields " +
                "are mandatory.\n" + "The optional fields: priority and due date represent a kind of entry that needs " +
                "to be completed promptly " + "so these entries are auto-scheduled first. Default values if one is filled " +
                "out is low for priority and 7 days from today " + "for due date.\nClick the add entry button after " +
                "you are finished.";
        showHelpAlert(headerText, contentText);
    }

    @FXML
    // MODIFIES: this
    // EFFECTS: Displays instructions on how to remove entries using the UI
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
    // MODIFIES: this
    // EFFECTS: Displays instructions on how to schedule entries using the UI
    private void helpScheduleButtonClicked(ActionEvent event) {
        String headerText = "Scheduling Entries";
        String contentText = "Scheduling entries requires you to grant permission for this application " +
                "on Google Calendar upon" + " program startup.\n\nUpon doing so, your TodoList is " +
                "scheduled for a week by default from 10 am - 10 pm by " + "order of:\n" + "1) Decreasing " +
                "Priority(if applicable), 2) Increasing Due Date(if applicable)\n" + "3) Time, " +
                "4)Alphabetically by Activity";
        showHelpAlert(headerText, contentText);
    }

    // MODIFIES: this
    // EFFECTS: Displays a Help alert to the user with given HeaderText and ContentText
    private void showHelpAlert(String headerText, String contentText) {
        showAlert(helpAlert, "Todo List Help", headerText, contentText);
    }

    // MODIFIES: this
    // EFFECTS: Sets alert's title, header text, and content text and shows alert
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
    // MODIFIES: this
    // EFFECTS: Shows alert of addedEvents to TodoListCalendar and converts events into: Activity from time to time
    public void update(List<Event> addedEvents) {
        if (addedEvents.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            eventsToString(addedEvents, stringBuilder);
            changeConfirmationAlertHeaderText("Events scheduled are:\n" + stringBuilder.toString());
        } else {
            showInformationAlert("No entries scheduled", "Entries have all been scheduled already");
        }
    }

    // MODIFIES: this
    // EFFECTS: Sets informationAlert title, headerText, and contentText and shows it
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

    // EFFECTS: Returns an eventDateTime String in the format of yyyy-MM-dd hr:min AM/PM
    private String dateTimeToLocalDateTimeString(DateTime eventDateTime) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli
                (eventDateTime.getValue()), ZoneId.systemDefault());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        return localDateTime.format(dateTimeFormatter);
    }
}