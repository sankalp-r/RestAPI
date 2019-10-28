package com.api.e2e.http;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

//POJO for Product
@XmlRootElement
public class Product {
    private String productName;
    private HashSet<Category> categoryList = new HashSet<>();

    public Product() {
        // TODO Auto-generated constructor stub
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public HashSet<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(HashSet<Category> categoryList) {
        this.categoryList = categoryList;
    }

}
