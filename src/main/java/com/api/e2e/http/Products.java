package com.api.e2e.http;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

//POJO for collection of Products
@XmlRootElement
public class Products {
    private static HashSet<Product> products = new HashSet<>();

    public Products() {
        // TODO Auto-generated constructor stub
    }

    public HashSet<Product> getProducts() {
        return products;
    }

    public void setProducts(HashSet<Product> products) {
        Products.products = products;
    }
}
