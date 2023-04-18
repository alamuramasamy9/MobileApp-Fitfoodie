package edu.northeastern.fitfoodie;

import java.util.Date;

public class WorkoutTracker {
    String type;
    String startTime;
    String endTime;
    double distance;
    Date workoutDate;
    double averageSpeed;
    double caloriesBurnt; // new attribute

    long workoutDuration;

    WorkoutTracker(String type, String startTime, String endTime, double averageSpeed, double distance, Date workoutDate, long workoutDuration, double caloriesBurnt){
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.workoutDate = workoutDate;
        this.averageSpeed = averageSpeed;
        this.workoutDuration = workoutDuration;
        this.caloriesBurnt = caloriesBurnt;
    }

    // getters
    public String getType() {
        return type;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public double getDistance() {
        return distance;
    }

    public Date getWorkoutDate() {
        return workoutDate;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public long getWorkoutDuration() {
        return workoutDuration;
    }

    public double getCaloriesBurnt() {
        return caloriesBurnt;
    }

    // setters
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public void setCaloriesBurnt(double caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    public void setWorkoutDuration(long workoutDuration) {
        this.workoutDuration = workoutDuration;
    }
    public String getType() {
        return type;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public double getDistance() {
        return distance;
    }

    public Date getWorkoutDate() {
        return workoutDate;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }
}