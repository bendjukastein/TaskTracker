package com.TaskTracker;

import java.util.Scanner;

/**
 * Menu class models the information required to print the task menu.
 * Data includes all current tasks.
 * It supports calls from TaskMain.
 * Written by Benjamin Djukastein
 */
public class Menu {
    private static char[] menuTitle = "My to-do list".toCharArray();
    private static String[] TodaysSpecials = {
            "List all tasks",
            "Add a new task",
            "Remove a task",
            "Mark a task as completed",
            "List overdue incomplete tasks",
            "List upcoming incomplete tasks",
            "Exit"
    };

    public static int getMenuChoice() {
        Scanner userInputScanner = new Scanner(System.in);
        System.out.print("Enter a Number (1-" + TodaysSpecials.length + "): ");
        return userInputScanner.nextInt();
    }

    public static void printMenu() {
        for (char ch : menuTitle) {
            System.out.print("#");
        }
        System.out.println();
        System.out.println(menuTitle);
        for (char ch : menuTitle) {
            System.out.print("#");
        }//print title with line of #### above and below
        System.out.println();
        for (int i = 1; i <= TodaysSpecials.length; i++) {
            System.out.print(i + ". ");
            System.out.println(TodaysSpecials[i - 1]);
        }//print out the menu options.
    }
}
