package com.hebaskhail.bmi.Model;

import java.util.ArrayList;

public class AllFoodApi {
    private int count = 0;
    private ArrayList<Food> foods;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }
}
