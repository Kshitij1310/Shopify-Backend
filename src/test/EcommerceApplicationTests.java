package com.example.ecommerce;

import dto.AuthResponse;
import dto.LoginRequest;
import dto.ProductDto;
import dto.RegisterRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EcommerceApplicationTests {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void authAndProductFlow() {
        // Register
        RegisterRequest reg = new RegisterRequest();
        reg.setName("Test User");
        reg.setEmail("test-" + UUID.randomUUID() + "@example.com");
        reg.setPassword("pass123");

        ResponseEntity<AuthResponse> regResp = rest.postForEntity("/api/auth/register", reg, AuthResponse.class);
        assertThat(regResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        String token = regResp.getBody().getToken();
        assertThat(token).isNotBlank();

        // Create product
        ProductDto p = new ProductDto();
        p.setName("Sample");
        p.setDescription("desc");
        p.setPrice(5.5);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductDto> createReq = new HttpEntity<>(p, headers);

        ResponseEntity<ProductDto> createResp = rest.exchange("/api/products", HttpMethod.POST, createReq, ProductDto.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResp.getBody().getId()).isNotNull();

        // List products
        HttpEntity<Void> listReq = new HttpEntity<>(headers);
        ResponseEntity<ProductDto[]> listResp = rest.exchange("/api/products", HttpMethod.GET, listReq, ProductDto[].class);
        assertThat(listResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResp.getBody()).isNotEmpty();
    }
}
