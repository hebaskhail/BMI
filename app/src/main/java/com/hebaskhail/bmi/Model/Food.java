package com.hebaskhail.bmi.Model;

import java.io.Serializable;

public class Food implements Serializable {
    private String _id,name,category,calory_type,createdAt,image;
    private int calory;

    @Override
    public String toString() {
        return name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCalory() {
        return calory;
    }

    public void setCalory(int calory) {
        this.calory = calory;
    }

    public String getCalory_type() {
        return calory_type;
    }

    public void setCalory_type(String calory_type) {
        this.calory_type = calory_type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
