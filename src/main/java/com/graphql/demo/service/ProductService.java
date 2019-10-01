package com.graphql.demo.service;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.stereotype.Service;

import com.graphql.demo.beans.Product;
import com.graphql.demo.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@GraphQLApi
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @GraphQLQuery(name = "products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }


    @GraphQLQuery(name = "product")
    public Optional<Product> getProductByProductNumber(@GraphQLArgument(name = "productNumber") String productNumber) {
        return productRepository.findById(productNumber);
    }
    
    @GraphQLMutation(name = "saveProduct")
    public Product saveProduct(@GraphQLArgument(name = "product") Product product) {
        return productRepository.save(product);
    }

}
