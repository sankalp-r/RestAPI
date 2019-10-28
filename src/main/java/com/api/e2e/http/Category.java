package com.api.e2e.http;

import javax.xml.bind.annotation.XmlRootElement;

// POJO for Category
@XmlRootElement
public class Category {
    private String categoryName;

    public Category() {
        // TODO Auto-generated constructor stub
    }

    public Category(String name) {
        this.categoryName = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
