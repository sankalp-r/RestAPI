
1. To deploy the webapp, build the project using command "mvn clean install".This will generate a deployable WAR file.
    (WAR file is already in the repo, you can directly use it). Put this WAR file in your local TOMCAT server's webapps folder
    and extract it into folder "basicapp"(This basicapp will be the context of the app). After this start the TOMCAT server.
    
2. Open a rest-client like POSTMAN to test the APIs.

3. Data is persisted in-memory, so initially there are no products and categories.

4. To call API endpoints for create or update or delete of product/category, you have to fetch the "JWT" token first.
    Make a call to "localhost:<port>/basicapp/api/oauth/token" endpoint with client_id="qwerty" and cliend_secret="Secure22" and 
    this content will be of type "application/x-www-form-urlencoded". After getting the token, add this to authorization header
    as "Bearer <token>". example "Bearer eyJhbGciOiJSUzI1NiIsImprdSI6Imh0dHBzOi8vd2Fhc3"

5. To create a category, make a POST call to "/basicapp/api/create/category" endpoint with Content-Type as "application/json"
    and body as:  ```json
             {"categoryName":"C2"}
            ```
    Similarly, to create a product make a POST call to "/basicapp/api/create/product" with body as:
     ```json
      {"productName":"P1","categoryList":[{"categoryName":"C1"}]}
      ```
      Note that while creating a product, the mentioned category must exist.
      
6. To update a category, make a PUT call to "/basicapp/api/update/category/&ltname of category&gt" and body as:
    ```json
    {"categoryName": "C4"}
     ```
    This call will update the existing category to "C4".
    Similarly to update a product, make a PUT call to "/basicapp/api/update/product/&ltproductName&gt" and body as:
     ```json
    {"productName":"P2","categoryList":[{"categoryName":"C3"}]}
     ```
    Above call will update Product "P2" and add new category "C3" to it(given that category exists).

7. To delete a category, make a DELETE call to "/basicapp/api/delete/category/&ltcategoryName&gt"(replace <categoryName> with category to           delete).
    Simlarly to delete a product, make a DELETE call to "basicapp/api/delete/product/&ltproductName&gt".

8. To read all catgories, make a GET call to "/basicapp/api/read/category".(You can change header "ACCEPT" to "application/json" or          "application/xml").
    Similarly to read all products, make a GET call to "/basicapp/api/read/product". To read products of specific category make GET call     as "/basicapp/api/read/product?cat=C2", where you can specify query parameter "cat" to filter the product of specified category.
    
10. Please note that creat/update/delete requires JWT token for authorization and token expiry limit is 5 minutes. Read of category and     prodcuts does not require any token.


References:
* [Rest api using Jersey](https://antoniogoncalves.org/2016/10/03/securing-jax-rs-endpoints-with-jwt/).</br>
* [JWT library](https://github.com/jwtk/jjwt).</br>

    
