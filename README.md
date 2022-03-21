# GraphQL Example

The project uses GraphQL SPQR (pronounce SPEAKER) for Spring Boot to auto generate the graphQL schema using the persistence layer for rapid development.

For better development workflow, a GUI is also available as a sandbox for developers to play around with their queries by. This is activated by 

```
graphql.spqr.gui.enabled=true
```

-----------------------------------------------------------------------------------

### Notes and Explanation

  * To have SPQR ignore certain fields, you can annotate it with @GraphQLIgnore.


  * The following line in pom.xml is so we can skip adding @GraphQLArgument.  By default, the name of the method parameter will be used in the schema (this is why you want -parameters javac option enabled when compiling). Using @GraphQLArgument is a way to change the name and set the default value. All of this is doable without annotations as well. 

```
        <compilerArgs>
            <arg>-parameters</arg>
        </compilerArgs>
```

  * CORS has been disabled in order for GraphQL Voyager to work.  Security has also been disabled and permits all.

-----------------------------------------------------------------------------------

```
{
product(productNumber:"TDJE843F"){
    productNumber,
    barcode
    }
}
```

-----------------------------------------------------------------------------------

### Post Query

To get token:

````
curl -X POST http://localhost:8080/oauth/token -u '<client_id>:<client_secret>' -H 'content-type: application/json' -d 'grant_type=password' -d 'username=<username>' -d 'password=<password>'
````

To get token with client credential:

````
curl -X POST http://localhost:8080/oauth/token -u 'graphqlClient:rjf394f2g' -d 'grant_type=client_credentials'
````

To post the query to the graphql service:

````
curl --location --request POST 'http://localhost:8080/graphql_demo/graphql' \
--header 'Content-Type: application/json' \
--header 'Cache-Control: no-cache' \
--header 'Authorization: Bearer 25e71cd1-e3ac-4067-a7c2-2e9590bc8d7e' \
--data-raw '{"query":"{products { productNumber barcode } }","variables":{}}'
````

The results will be like this:

````
{
  "data": {
    "products": [
      {
        "productNumber": "TDJE843F",
        "barcode": "EICO3448fcd45"
      },
      {
        "productNumber": "F45VW35H",
        "barcode": "4FIFF4MBV9ASL"
      }
    ]
  }
}
````

To create a new product:

````
mutation {
  saveProduct(product:{productNumber: "IIIIII333", barcode: "45rf4tf"}){
      productNumber
      barcode
  }
}
````

or as curl command:

````
curl --request POST 'http://localhost:8080/graphql_demo/graphql' \
--header 'Authorization: Bearer 7e3728ce-724e-4ec4-b987-2884b46c584e' \
--header 'Content-Type: application/json' \
--data-raw '{ "query": "mutation{saveProduct(product:{productNumber: \"IIIIII333\", barcode: \"45rf4tf\"}){productNumber barcode}}" }' 
````

The results will look like this:

````
{
  "data": {
    "saveProduct": {
      "productNumber": "IIIIII333",
      "barcode": "45rf4tf"
    }
  }
}
````

In playground, 
go to http://localhost:8080/graphql_demo/gui

Add token to the Http Header like this:

````
{
    "Authorization": "Bearer <auth token>"
}
````


Healthcheck:
http://localhost:8080/graphql_demo/healthcheck

H2 console:
http://localhost:8080/h2/