package com.basicapp.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.api.e2e.http.Categories;
import com.api.e2e.http.Category;
import com.api.e2e.http.Product;
import com.api.e2e.http.Products;

// A copy of RestService for Unit testing
public class RestServiceTest {

    private HashSet<String> product = new HashSet<String>() {
        {
            add("P1");
            add("P2");
        }
    };
    private HashSet<String> category = new HashSet<String>() {
        {
            add("C1");
            add("C2");
        }
    };
    private HashMap<String, HashSet<String>> persistedData = new HashMap<String, HashSet<String>>() {
        {
            put("P1", new HashSet<String>(Arrays.asList("C1")));
            put("P2", new HashSet<String>(Arrays.asList("C2")));
        }
    };

    public Response createProduct(Product temp) {
        HashSet<String> tempSet = new HashSet<>();
        if (temp != null && check(temp.getCategoryList(), tempSet)) {

            product.add(temp.getProductName());
            persistedData.put(temp.getProductName(), tempSet);
            System.out.println("Product: " + temp.getProductName());
            return Response.status(201).entity("Success").build();
        }
        return Response.status(400).entity("Bad Request").build();
    }

    public Response createCategory(Category temp) {

        if (temp != null) {

            category.add(temp.getCategoryName());
            System.out.println("Category: " + temp.getCategoryName());
            return Response.status(201).entity("Success").build();
        }
        return Response.status(400).entity("Bad Request").build();
    }

    public Products readProduct(@QueryParam("cat") String cat) {
        if (cat == null) {
            Products temp = new Products();
            HashSet<Product> tempSet = new HashSet<>();
            for (Entry<String, HashSet<String>> entry : persistedData.entrySet()) {
                tempSet.add(transfromProduct(entry.getKey(), entry.getValue()));
            }
            temp.setProducts(tempSet);
            return temp;
        } else {
            HashSet<Product> setP = new HashSet<>();
            for (Entry<String, HashSet<String>> entry : persistedData.entrySet()) {
                if (entry.getValue().contains(cat)) {
                    setP.add(transfromProduct(entry.getKey(), entry.getValue()));
                }
            }
            Products temp = new Products();
            temp.setProducts(setP);
            return temp;

        }
    }

    public Categories readCategory() {
        Categories temp = new Categories();
        HashSet<Category> tempSet = new HashSet<>();
        for (String c : category) {
            Category cat = new Category();
            cat.setCategoryName(c);
            tempSet.add(cat);
        }
        temp.setCategories(tempSet);
        return temp;
    }

    public Response updateProduct(String p, Product temp) {
        boolean present = false;
        if (persistedData.containsKey(p)) {
            for (Category c : temp.getCategoryList()) {
                if (category.contains(c.getCategoryName())) {
                    persistedData.get(p).add(c.getCategoryName());
                } else {
                    return Response.status(404).entity("Invalid category").build();
                }
            }

            present = true;

        }
        if (present) {
            return Response.status(202).entity("Updated").build();
        }
        return Response.status(404).entity("Entity not found").build();
    }

    public Response updateCategory(String c, Category temp) {
        boolean present = false;
        if (category.contains(c)) {

            category.remove(c);
            category.add(temp.getCategoryName());
            for (Entry<String, HashSet<String>> entry : persistedData.entrySet()) {
                if (entry.getValue().contains(c)) {
                    entry.getValue().remove(c);
                    entry.getValue().add(temp.getCategoryName());
                }
            }
            present = true;
        }
        if (present) {
            return Response.status(202).entity("Updated").build();
        }
        return Response.status(404).entity("Entity not found").build();
    }

    public Response deleteProduct(String pname) {

        if (product.contains(pname)) {
            product.remove(pname);
            persistedData.remove(pname);
            return Response.status(200).entity("Deleted").build();
        }

        return Response.status(200).entity("Product not found").build();
    }

    public Response deleteCategory(String cname) {
        if (category.contains(cname)) {
            category.remove(cname);
            for (Entry<String, HashSet<String>> entry : persistedData.entrySet()) {
                entry.getValue().remove(cname);
            }
            return Response.status(200).entity("Deleted").build();

        }
        return Response.status(404).entity("Category not found").build();
    }

    private boolean check(HashSet<Category> categoryList, HashSet<String> tempSet) {
        int count = 0;
        for (Category c : categoryList) {
            if (category.contains(c.getCategoryName())) {
                tempSet.add((c.getCategoryName()));
                count++;
            }
        }
        if (count == categoryList.size()) {
            return true;
        }
        return false;
    }

    private Product transfromProduct(String pname, HashSet<String> categoryList) {
        Product res = new Product();
        res.setProductName(pname);
        HashSet<Category> catSet = new HashSet<>();
        for (String c : categoryList) {
            Category cat = new Category();
            cat.setCategoryName(c);
            catSet.add(cat);
        }
        res.setCategoryList(catSet);

        return res;
    }

}
