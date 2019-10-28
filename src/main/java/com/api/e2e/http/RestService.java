package com.api.e2e.http;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.api.e2e.http.jwt.JWTTokenNeeded;
import com.google.gson.JsonObject;

@Path("/api")
public class RestService {

    private static HashSet<String> product = new HashSet<>();
    private static HashSet<String> category = new HashSet<>();
    private static HashMap<String, HashSet<String>> persistedData = new HashMap<>();

    @POST
    @Path("/create/product")
    @Consumes(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
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

    @POST
    @Path("/create/category")
    @Consumes(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response createCategory(Category temp) {

        if (temp != null) {

            category.add(temp.getCategoryName());
            System.out.println("Category: " + temp.getCategoryName());
            return Response.status(201).entity("Success").build();
        }
        return Response.status(400).entity("Bad Request").build();
    }

    @GET
    @Path("/read/product")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
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

    @GET
    @Path("/read/category")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
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

    @PUT
    @Path("/update/product/{id}")
    @JWTTokenNeeded
    public Response updateProduct(@PathParam("id") String p, Product temp) {
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

    @PUT
    @Path("/update/category/{id}")
    @JWTTokenNeeded
    public Response updateCategory(@PathParam("id") String c, Category temp) {
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

    @DELETE
    @Path("/delete/product/{id}")
    @JWTTokenNeeded
    public Response deleteProduct(@PathParam("id") String pname) {

        if (product.contains(pname)) {
            product.remove(pname);
            persistedData.remove(pname);
            return Response.status(200).entity("Deleted").build();
        }

        return Response.status(200).entity("Product not found").build();
    }

    @DELETE
    @Path("/delete/category/{id}")
    @JWTTokenNeeded
    public Response deleteCategory(@PathParam("id") String cname) {
        if (category.contains(cname)) {
            category.remove(cname);
            for (Entry<String, HashSet<String>> entry : persistedData.entrySet()) {
                entry.getValue().remove(cname);
            }
            return Response.status(200).entity("Deleted").build();

        }
        return Response.status(404).entity("Category not found").build();
    }

    @POST
    @Path("/oauth/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response fetchToken(@FormParam("client_id") String client_id, @FormParam("client_secret") String client_secret) {
        if (authenticate(client_id, client_secret)) {
            String token = OauthToken.createToken();
            JsonObject json = new JsonObject();
            json.addProperty("access_token", token);
            return Response.ok().header("AUTHORIZATION", "Bearer " + token).entity(json.toString()).build();
        }
        return Response.status(401).entity("Unauthorized").build();
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

    private boolean authenticate(String client_id, String client_secret) {
        if (client_id.equals("qwerty") && client_secret.equals("Secure22")) {
            return true;
        }
        return false;
    }

}
