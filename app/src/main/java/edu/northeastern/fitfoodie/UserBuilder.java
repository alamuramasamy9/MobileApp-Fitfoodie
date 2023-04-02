package edu.northeastern.fitfoodie;

public class UserBuilder {
    String _name;
    String _gender;
    String _goalType;
    String _activityLevel;

    int _age;
    int _height;
    int _weight;
    int _calorieInTakeTarget;

    public UserBuilder(){}

    public User buildUser(){
        return new User(_name, _gender, _goalType, _activityLevel, _age, _height, _weight, _calorieInTakeTarget);
    }

    public UserBuilder name(String _name){
        this._name = _name;
        return this;
    }

    public UserBuilder gender(String _gender){
        this._gender = _gender;
        return this;
    }

    public UserBuilder goalType(String _goalType){
        this._goalType = _goalType;
        return this;
    }

    public UserBuilder activityLevel(String _activityLevel){
        this._activityLevel = _activityLevel;
        return this;
    }

    public UserBuilder age(int _age){
        this._age = _age;
        return this;
    }

    public UserBuilder height(int _height){
        this._height = _height;
        return this;
    }

    public UserBuilder weight(int _weight){
        this._weight = _weight;
        return this;
    }

    public UserBuilder calorieInTakeTarget(int _calorieInTakeTarget){
        this._calorieInTakeTarget = _calorieInTakeTarget;
        return this;
    }

}
