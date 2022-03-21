package com.graphql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
//import org.junit.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.graphql.demo.Application;

@SpringBootTest(classes = { Application.class })
@WebAppConfiguration
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class TestGraphQL {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private MockMvc mockMvc;

	public static String clientId = "graphqlClient";
	public static String clientSecret = "rjf394f2g";
	public static String redirectUri = "http://localhost:8080/login";
	public static String scope = "user_info";
	public static String username = "graphqlUser";
	public static String password = "esfd834jr";

	private String obtainAccessToken() throws Exception {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "client_credentials");
		params.add("client_id", clientId);
		params.add("client_secret", clientSecret);

		ResultActions result = mockMvc.perform(post("/oauth/token").params(params)
				.accept("application/json;charset=UTF-8")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));

		String resultString = result.andReturn().getResponse().getContentAsString();

		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(resultString).get("access_token").toString();
	}
	
	
	private String obtainAccessTokenByPassword() throws Exception {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("client_id", clientId);
		params.add("client_secret", clientSecret);
		params.add("username", username);
		params.add("password", password);

		ResultActions result = mockMvc.perform(post("/oauth/token").params(params)
				.accept("application/json;charset=UTF-8")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));

		String resultString = result.andReturn().getResponse().getContentAsString();

		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(resultString).get("access_token").toString();
	}

	@Test
	@Order(1) 
	public void testUnAuthorized() throws Exception {
		String postString = "{\"query\": \"{products { productNumber barcode } }\" }";
		mockMvc.perform(post("/graphql_demo/graphql").contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(postString))
				.andExpect(status().isUnauthorized());

	}

	@Test
	@Order(2) 
	public void testGetToken() throws Exception {
		String accessToken = obtainAccessToken();
		Assert.notNull(accessToken, "Token is null");
	}

	@Test
	@Order(3) 
	public void testGetProducts() throws Exception {
		String postString = "{\"query\": \"{products { productNumber barcode } }\" }";
		String expectedResults = "{\"data\":{\"products\":[{\"productNumber\":\"TDJE843F\",\"barcode\":\"EICO3448fcd45\"},{\"productNumber\":\"F45VW35H\",\"barcode\":\"4FIFF4MBV9ASL\"}]}}";
		
		String accessToken = obtainAccessToken();
		
		mockMvc.perform(post("/graphql_demo/graphql")
    			.contentType(MediaType.APPLICATION_JSON_UTF8)
    			.accept(MediaType.APPLICATION_JSON_UTF8)
    			.header("Authorization", "Bearer " + accessToken)
    			.content(postString))
		.andExpect(status().isOk())
	    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    .andExpect(content().string(expectedResults));
	}
	
	@Test
	@Order(4) 
	public void testCreateProduct() throws Exception {
		String postString = "{\"query\": \"mutation{saveProduct(product:{productNumber: \\\"IIIIII333\\\", barcode: \\\"45rf4tf\\\"}){productNumber barcode}}\" }";
		String expectedResults = "{\"data\":{\"saveProduct\":{\"productNumber\":\"IIIIII333\",\"barcode\":\"45rf4tf\"}}}";
		
		String accessToken = obtainAccessToken();
		
		mockMvc.perform(post("/graphql_demo/graphql")
    			.contentType(MediaType.APPLICATION_JSON_UTF8)
    			.accept(MediaType.APPLICATION_JSON_UTF8)
    			.header("Authorization", "Bearer " + accessToken)
    			.content(postString))
		.andExpect(status().isOk())
	    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    .andExpect(content().string(expectedResults));
				
	}

}