package com.TaskTracker;

import java.util.GregorianCalendar;
/**
 * Task class models a task's data, and the updating and  printing of that data.
 * Data includes a task's name, notes, due date, and whether it is complete.
 * It supports calls from TaskMain.
 * Written by Benjamin Djukastein
 */
public class Task implements Comparable<Task>{

    private String name;
    private String notes;
    private GregorianCalendar dueDate;
    private boolean isComplete;

    public Task(String name, String notes, GregorianCalendar dueDate, boolean isComplete) {
        this.name = name;
        this.notes = notes;
        this.dueDate = dueDate;
        this.isComplete = isComplete;
    }
    @Override
    public int compareTo(Task otherTask)
    {
        return this.getDueDate().compareTo( otherTask.getDueDate() );
    }

    public String getName() {
        return name;
    }

    public GregorianCalendar getDueDate() {
        return dueDate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    @Override
    public String toString() {
        return "Task:" +
                name + '\n' +
                "Notes:" +
                notes + '\n' +
                "DueDate:" +
                dueDate.getTime() + '\n';
    }

    public String toStringWithCompletionStatus() {
        return "Task:" +
                name + '\n' +
                "Notes:" +
                notes + '\n' +
                "DueDate:" +
                dueDate.getTime() + '\n'+
                "Task Complete? :" +
                isComplete + '\n';
    }
//
//    public String toStringWithCompletionStatus() {
//        DateFormat Hour12 = new SimpleDateFormat("hh:mm aa");
//        formattedTime = Hour12.format(time).toString();
//        meridian = "am";//default to am time
//        return "Task:" +
//                name + '\n' +
//                "Notes:" +
//                notes + '\n' +
//                "DueDate:" +
//                if(hour >12){
//                    updatedHour = hour - 12; //fix to pm time
//                    meridian = "pm";
//                }
//                year + "-" + month + "-" day +" "+ formattedTime \n'+
//                "Task Complete? :" +
//                isComplete + '\n';
//    }

}
