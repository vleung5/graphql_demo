package com.graphql.demo.beans;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "product", catalog = "xxx")
public class Product {
	
    @Id
    @GraphQLQuery(name = "productNumber", description = "A product's number")
    @Column(name = "prod_no")
    private String productNumber;
        

    @Column(name = "barcode")
    @GraphQLQuery(name = "barcode", description = "A product's barcode")
    private String barcode;

}