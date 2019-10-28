package com.basicapp.test;

import java.util.HashSet;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

import com.api.e2e.http.Categories;
import com.api.e2e.http.Category;
import com.api.e2e.http.Product;
import com.api.e2e.http.Products;

public class MyTest {

    @Test
    public void test_read_category() {
        RestServiceTest serv = new RestServiceTest();
        Categories res = serv.readCategory();
        Assert.assertTrue(res.getCategories().size() > 0);

    }

    @Test
    public void test_read_product_with_category_filter() {
        RestServiceTest serv = new RestServiceTest();
        Products res = serv.readProduct("C1");
        Assert.assertTrue(res.getProducts().size() == 1);

    }

    @Test
    public void test_read_all_product() {
        RestServiceTest serv = new RestServiceTest();
        Products res = serv.readProduct(null);
        Assert.assertTrue(res.getProducts().size() == 2);

    }

    @Test
    public void test_create_category_badrequest() {
        RestServiceTest serv = new RestServiceTest();
        Response response = serv.createCategory(null);
        Assert.assertTrue(response.getStatus() == 400);

    }

    @Test
    public void test_create_category() {
        RestServiceTest serv = new RestServiceTest();
        Category cat = new Category();
        cat.setCategoryName("C3");
        Response response = serv.createCategory(cat);
        Assert.assertTrue(response.getStatus() == 201);

    }

    @Test
    public void test_create_product_badrequest() {
        RestServiceTest serv = new RestServiceTest();
        Response response = serv.createProduct(null);
        Assert.assertTrue(response.getStatus() == 400);

    }

    @Test
    public void test_create_product() {
        RestServiceTest serv = new RestServiceTest();

        Category cat = new Category();
        cat.setCategoryName("C1");

        HashSet<Category> catList = new HashSet<>();
        catList.add(cat);

        Product pro = new Product();
        pro.setProductName("P3");
        pro.setCategoryList(catList);
        Response response = serv.createProduct(pro);
        Assert.assertTrue(response.getStatus() == 201);

    }

    @Test
    public void test_update_product_entityNotFound() {
        RestServiceTest serv = new RestServiceTest();
        Response res = serv.updateProduct(null, null);

        Assert.assertTrue(res.getEntity().equals("Entity not found"));

    }

    @Test
    public void test_update_product_InvalidCategory() {
        RestServiceTest serv = new RestServiceTest();

        Category cat = new Category();
        cat.setCategoryName("C3");

        HashSet<Category> catList = new HashSet<>();
        catList.add(cat);

        Product pro = new Product();
        pro.setProductName("P1");
        pro.setCategoryList(catList);

        Response res = serv.updateProduct("P1", pro);

        Assert.assertTrue(res.getEntity().equals("Invalid category"));

    }

    @Test
    public void test_update_product() {
        RestServiceTest serv = new RestServiceTest();

        Category cat = new Category();
        cat.setCategoryName("C2");

        HashSet<Category> catList = new HashSet<>();
        catList.add(cat);

        Product pro = new Product();
        pro.setProductName("P1");
        pro.setCategoryList(catList);

        Response res = serv.updateProduct("P1", pro);
        Assert.assertTrue(res.getEntity().equals("Updated"));

    }

    @Test
    public void test_update_category_entityNotFound() {
        RestServiceTest serv = new RestServiceTest();
        Response res = serv.updateCategory(null, null);

        Assert.assertTrue(res.getEntity().equals("Entity not found"));

    }

    @Test
    public void test_update_category() {
        RestServiceTest serv = new RestServiceTest();

        Category cat = new Category();
        cat.setCategoryName("C4");

        Response res = serv.updateCategory("C1", cat);

        Assert.assertTrue(res.getEntity().equals("Updated"));

    }

    @Test
    public void test_delete_product_NotFound() {
        RestServiceTest serv = new RestServiceTest();
        Response res = serv.deleteProduct(null);
        Assert.assertTrue(res.getEntity().equals("Product not found"));
    }

    @Test
    public void test_delete_product() {
        RestServiceTest serv = new RestServiceTest();
        Response res = serv.deleteProduct("P1");
        Assert.assertTrue(res.getEntity().equals("Deleted"));
    }

    @Test
    public void test_delete_category_NotFound() {
        RestServiceTest serv = new RestServiceTest();
        Response res = serv.deleteCategory(null);
        Assert.assertTrue(res.getEntity().equals("Category not found"));
    }

    @Test
    public void test_delete_category() {
        RestServiceTest serv = new RestServiceTest();
        Response res = serv.deleteCategory("C1");
        Assert.assertTrue(res.getEntity().equals("Deleted"));
    }

}