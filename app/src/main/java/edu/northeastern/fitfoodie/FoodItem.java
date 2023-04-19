package edu.northeastern.fitfoodie;

public class FoodItem {
    private String name;
    private double calories;
    private double servingSize;
    private double totalFat;
    private double saturatedFat;
    private double protein;
    private int sodium;
    private int potassium;
    private int cholesterol;
    private double totalCarbohydrates;
    private double dietaryFiber;
    private double sugars;

    public FoodItem() {}

    public FoodItem(String name, double calories, double servingSize, double totalFat, double saturatedFat, double protein, int sodium, int potassium, int cholesterol, double totalCarbohydrates, double dietaryFiber, double sugars) {
        this.name = name;
        this.calories = calories;
        this.servingSize = servingSize;
        this.totalFat = totalFat;
        this.saturatedFat = saturatedFat;
        this.protein = protein;
        this.sodium = sodium;
        this.potassium = potassium;
        this.cholesterol = cholesterol;
        this.totalCarbohydrates = totalCarbohydrates;
        this.dietaryFiber = dietaryFiber;
        this.sugars = sugars;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getServingSize() {
        return servingSize;
    }

    public void setServingSize(double servingSize) {
        this.servingSize = servingSize;
    }

    public double getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(double totalFat) {
        this.totalFat = totalFat;
    }

    public double getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(double saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public int getSodium() {
        return sodium;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public int getPotassium() {
        return potassium;
    }

    public void setPotassium(int potassium) {
        this.potassium = potassium;
    }

    public int getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(int cholesterol) {
        this.cholesterol = cholesterol;
    }

    public double getTotalCarbohydrates() {
        return totalCarbohydrates;
    }

    public void setTotalCarbohydrates(double totalCarbohydrates) {
        this.totalCarbohydrates = totalCarbohydrates;
    }

    public double getDietaryFiber() {
        return dietaryFiber;
    }

    public void setDietaryFiber(double dietaryFiber) {
        this.dietaryFiber = dietaryFiber;
    }

    public double getSugars() {
        return sugars;
    }

    public void setSugars(double sugars) {
        this.sugars = sugars;
    }
}

