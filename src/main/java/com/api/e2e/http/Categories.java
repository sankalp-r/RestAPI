package com.api.e2e.http;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

//A POJO for collection of Category
@XmlRootElement
public class Categories {
    private static HashSet<Category> categories = new HashSet<>();

    public Categories() {
        // TODO Auto-generated constructor stub
    }

    public HashSet<Category> getCategories() {
        return categories;
    }

    public void setCategories(HashSet<Category> categories) {
        Categories.categories = categories;
    }

}
