package edu.northeastern.fitfoodie;

import java.util.Date;

public class WorkoutTracker {
    String type;
    String startTime;
    String endTime;



    double distance;
    Date workoutDate;
    double averageSpeed;

    WorkoutTracker(String type, String startTime, String endTime, double averageSpeed, double distance, Date workoutDate){
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.workoutDate = workoutDate;
        this.averageSpeed = averageSpeed;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }
}