package edu.northeastern.fitfoodie;

public class User {
    String username;
    String name;
    String gender;
    String goalType;
    String activityLevel;

    int age;
    int height;
    int weight;
    int calorieInTakeTarget;



    WorkoutTracker[] workouts;

    public User(){
        //Required empty constructor.
    }

    public User(String username, String name, String gender, String goalType, String activityLevel, int age, int height, int weight, int calorieInTakeTarget) {
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.goalType = goalType;
        this.activityLevel = activityLevel;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.calorieInTakeTarget = calorieInTakeTarget;
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

    public int getCalorieInTakeTarget() {
        return calorieInTakeTarget;
    }

    public WorkoutTracker[] getWorkouts() { return workouts;}
}
