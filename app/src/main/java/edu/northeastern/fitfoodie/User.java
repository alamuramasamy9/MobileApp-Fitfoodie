package edu.northeastern.fitfoodie;

import java.util.ArrayList;

public class User {
    String username;
    String name;
    String gender;
    String goalType;
    String activityLevel;

    int age;
    int height;
    int weight;
    float calorieInTakeTarget;



    ArrayList<WorkoutTracker> workouts;

    String profilePicUri;

    public User(){
        //Required empty constructor.
    }

    public User(String username, String name, String gender, String goalType, String activityLevel, int age, int height, int weight, float calorieInTakeTarget, String profilePicUri) {
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.goalType = goalType;
        this.activityLevel = activityLevel;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.calorieInTakeTarget = calorieInTakeTarget;
        this.profilePicUri = profilePicUri;
    }
    public String getUsername(){return username; }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getGoalType() {
        return goalType;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public int getAge() {
        return age;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public float getCalorieInTakeTarget() {
        return calorieInTakeTarget;
    }

    public ArrayList<WorkoutTracker> getWorkouts() {
        return workouts;
    }


    // Getters and setters for the profile picture URI
    public String getProfilePicUri() {
        return profilePicUri;
    }

    public void setProfilePicUri(String profilePicUri) {
        this.profilePicUri = profilePicUri;
    }
}
