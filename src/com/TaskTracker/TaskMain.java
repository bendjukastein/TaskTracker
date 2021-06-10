package com.TaskTracker;


import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Scanner;

/**
 * TaskMain class handles user interaction, and calls the other classes to serve and update task data.
 * Data includes user input.
 * It calls TaskMain and Menu.
 * Written by Benjamin Djukastein
 */
public class TaskMain {
    private static ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        //setting up enums
        final int PRINT_ALL_TASKS = 1;
        final int ADD_NEW_TASK = 2;
        final int REMOVE_TASK = 3;
        final int MARK_TASK_COMPLETE = 4;
        final int LIST_OVERDUE_INCOMPLETE_TASKS = 5;
        final int LIST_UPCOMING_INCOMPLETE_TASKS = 6;
        final int EXIT = 7;

        Scanner userInputScanner = new Scanner(System.in);//scanner instance that reads user input

        Reader reader; // reader instance that accesses json file.
        try {
            reader = Files.newBufferedReader(Paths.get("savedTasks.json"));
            File jsonText = new File("savedTasks.json");
            if (jsonText.length() > 1) {//checks if the json file exists and isn't empty
                Gson gson = new Gson();
                Task[] taskArray = gson.fromJson(reader, Task[].class);
                Collections.addAll(tasks, taskArray);
            }//read the json file and store existing tasks in "tasks" arraylist
        } catch (FileNotFoundException e) {
        }//update tasks with existing task objects if they exist in the current directory.
        catch (IOException e) {
            //These exceptions occur when there is no previous tasks stored as json, and are simply caught here to prevent error
        }

        while (true) {
            Menu.printMenu();
            int menuChoice = Menu.getMenuChoice();
            switch (menuChoice) {//get an option(1-7) from the user and perform accordingly
                case PRINT_ALL_TASKS: // list all tasks
                    if (tasks.isEmpty()) {
                        System.out.println("There are no existing tasks to display\n");
                    } else {
                        printArrayList(tasks, true);
                    }
                    break;
                case ADD_NEW_TASK:
                    addTask(userInputScanner);
                    break;
                case REMOVE_TASK:
                    removeTask(userInputScanner);
                    break;
                case MARK_TASK_COMPLETE:
                    markTaskComplete(userInputScanner);
                    break;
                case LIST_OVERDUE_INCOMPLETE_TASKS:
                    listOverdueIncompleteTasks();
                    break;
                case LIST_UPCOMING_INCOMPLETE_TASKS:
                    listUpcomingIncompleteTasks();
                    break;
                case EXIT:
                    exitSaveJson();
                    return; //end the program
                default:
                    System.out.println("Please enter a valid instruction number, or to quit press 7\n");
                    break;
            }
        }

    }

    //print each item in the given arraylist to console with proper formatting
    private static void printArrayList(ArrayList<Task> tasks, boolean showCompletionStatus) {
        if (!showCompletionStatus) {
            for (int i = 1; i <= tasks.size(); i++) {
                System.out.println("Task #" + i);
                System.out.println(tasks.get(i - 1).toString());
            }
        }//some functions do not want the completion status printed
        else {
            for (int i = 1; i <= tasks.size(); i++) {
                System.out.println("Task #" + i);
                System.out.println(tasks.get(i - 1).toStringWithCompletionStatus());
            }
        }
    }

    //gets user input for all the fields of a task, makes a new task, then adds it to the task list "tasks"
    private static void addTask(Scanner userInputScanner) {
        String name = "";
        while (name.equals("")) {
            System.out.print("Please enter a name for this Task: ");
            name = userInputScanner.nextLine();
            if (!name.equals("")) {
                break;
            }
            System.out.println("ERROR: name cannot be empty\n");
        }//get a non empty name

        String notes;
        while (true) {
            System.out.print("Please describe this Task with additional notes: ");
            notes = userInputScanner.nextLine();
            if (notes != null) {
                break;
            }
            System.out.println("ERROR: please enter a valid note to describe the task.\n");
        }//get a realistic year

        int year = -1;
        while (year < 1900) {
            System.out.print("Enter the year of the due date:");
            year = userInputScanner.nextInt();
            if (year >= 1900) {
                break;
            }
            System.out.println("ERROR: please enter a valid year (e.g. 2000) \n");
        }//get a realistic year

        int month;
        while (true) {
            System.out.print("Enter the month of the due date(1-12):");
            month = userInputScanner.nextInt();
            if (month >= 1 && month <= 12) {
                break;
            }
            System.out.println("ERROR: please enter a valid month (e.g. January is 5) \n");
        }//get a realistic month

        int day;
        boolean isValidDate = false;
        while (true) {
            System.out.print("Enter the day of the due date(1-31):");
            day = userInputScanner.nextInt();
            try {
                LocalDate test = LocalDate.of(year, month, day);//throws DateTimeException if date is invalid, e.g. feb 31st
                isValidDate = true;
            } catch (DateTimeException e) {
                System.out.println(e);
                System.out.println("ERROR: Please enter a day that is valid");
            }
            if (day >= 1 && day <= 31 && isValidDate) {
                break;
            }
        }//get a somewhat realistic day

        int hour = -1;
        while (true) {
            System.out.print("Enter the hour of the due date(0-23):");
            hour = userInputScanner.nextInt();
            if (hour >= 0 && hour <= 24) {
                break;
            }
            System.out.println("ERROR: please enter a valid hour (midnight is 0) \n");
        }//get a realistic hour

        int minute = -1;
        while (true) {
            System.out.print("Enter the minute of the due date(0-59):");
            minute = userInputScanner.nextInt();
            if (minute >= 0 && minute <= 59) {
                break;
            }
            System.out.println("ERROR: please enter a valid number of minutes \n");
        }//get a realistic minute

        GregorianCalendar dueDate = new GregorianCalendar(year, month, day, hour, minute);

        GregorianCalendar now = new GregorianCalendar();
        boolean isComplete = false;
        Task newTask = new Task(name, notes, dueDate, isComplete);
        tasks.add(newTask);
        System.out.println("Task \"" + name + "\" has been added to the list of tasks.");
        Collections.sort(tasks);
    }

    private static void removeTask(Scanner userInputScanner) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to show.");
            return;
        }
        System.out.println("Here are the tasks that can be removed:");
        printArrayList(tasks, true);
        int removeIndex;
        while (true) {
            System.out.println("please select a task to remove (1-" + tasks.size() + ") or press 0 to cancel:");
            removeIndex = userInputScanner.nextInt();
            if (removeIndex == 0) {
                System.out.println("Cancelling.. Returning to main menu.");
                return;
            }
            if (removeIndex >= 1 && removeIndex <= tasks.size()) {
                break;
            }
            System.out.println("ERROR: please enter a valid task index, (e.g. 1) \n");
        }
        System.out.println("Task \"" + tasks.get(removeIndex - 1).getName() + "\" has been removed from the list of tasks.");
        tasks.remove(removeIndex - 1);
    }

    private static void markTaskComplete(Scanner userInputScanner) {
        ArrayList<Task> incompleteTasks = new ArrayList<>();
        for (Task t : tasks) {
            if (!t.isComplete()) {
                incompleteTasks.add(t);
            }
        }//creates an array-list of all incomplete tasks to display

        if (incompleteTasks.isEmpty()) {
            System.out.println("No upcoming incomplete tasks to show.");
            return;
        }
        System.out.println("Here are the tasks marked as incomplete:");
        printArrayList(incompleteTasks, false);
        int markIndex;
        while (true) {
            System.out.println("please select a task to mark as complete (1-" + incompleteTasks.size() + "), or enter 0 to cancel:");
            markIndex = userInputScanner.nextInt();
            if (markIndex == 0) {
                break;
            }
            if (markIndex >= 1 && markIndex <= incompleteTasks.size()) {
                break;
            }
            System.out.println("ERROR: please enter a valid task index, (e.g. 1) \n");
        }//gets a mark index for which task to mark as complete

        if (markIndex >= 1 && markIndex <= incompleteTasks.size()) {
            incompleteTasks.get(markIndex - 1).setComplete(true); //mark the selected task as complete
            System.out.println("Task \"" + incompleteTasks.get(markIndex - 1).getName() + "\" has been marked as complete!");
            return;
        }
        System.out.println("Cancelled successfully.");
    }

    private static void listOverdueIncompleteTasks() {
        ArrayList<Task> overdueIncompleteTasks = new ArrayList<>();
        GregorianCalendar now = new GregorianCalendar();
        for (Task t : tasks) {
            if (!t.isComplete() && now.after(t.getDueDate())) {
                overdueIncompleteTasks.add(t);
            }
        }//create an array-list of all overdue tasks that are incomplete

        if (overdueIncompleteTasks.isEmpty()) {
            System.out.println("No overdue incomplete tasks to show. Good Job!\n");
        } else {
            Collections.sort(overdueIncompleteTasks);//sort with oldest first
            System.out.println("Here are the Incomplete Tasks that are overdue:\n");
            printArrayList(overdueIncompleteTasks, false);
        }
    }

    private static void listUpcomingIncompleteTasks() {
        ArrayList<Task> upcomingIncompleteTasks = new ArrayList<>();
        GregorianCalendar now = new GregorianCalendar();
        for (Task t : tasks) {
            if (!t.isComplete() && t.getDueDate().after(now)) {
                upcomingIncompleteTasks.add(t);
            }
        }//create an array-list of all overdue tasks that are incomplete

        if (upcomingIncompleteTasks.isEmpty()) {
            System.out.println("No upcoming incomplete tasks to show.\n");
        } else {
            Collections.sort(upcomingIncompleteTasks);//sort with oldest first
            System.out.println("Here are the incomplete Tasks, prioritizing those with the least time left to complete:\n");
            printArrayList(upcomingIncompleteTasks, false);
        }
    }

    private static void exitSaveJson() {
        System.out.println("Thank you for using the system.");
        Gson gsonTasks = new Gson();
        try {
            Writer writer = new FileWriter("savedTasks.json");
            writer.write("\n");
            gsonTasks.toJson(tasks, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//gracefully thanks the user, saves the json to a file, and exits.

}

