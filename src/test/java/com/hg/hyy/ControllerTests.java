package com.hg.hyy;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.hg.hyy.entity.Greeting;

import com.hg.hyy.entity.Log;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getHello() throws Exception {
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString("http://localhost:8080/v1/hello-spring?name={name}").build().expand("王五").encode();
        URI uri = uriComponents.toUri();

        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "hello spring");
        // 通过map传参
        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity("https://localhost:8080/v1/hello-spring?name={name}", String.class, map);

        // url直接传参
        ResponseEntity<Greeting> response = restTemplate.getForEntity("/v1/hello-spring?name=spring", Greeting.class);
        Log.getLog(this).error(response.getBody().toString());
        assertThat(response.getBody().getContent()).isEqualTo("Hello, spring!");
        assertEquals("Hello, spring!", response.getBody().getContent());

    }

    @Autowired
    private MockMvc mvc;

    @Test
    public void getHello1() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/v1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().string(equalTo("访问host非法，已拦截")));
    }

    @Test
    public void corsWithAnnotation() throws Exception {
        ResponseEntity<Greeting> entity = this.restTemplate.exchange(
                RequestEntity.get(uri("/v1/greeting")).header(HttpHeaders.ORIGIN, "https://localhost:8080").build(),
                Greeting.class);
                Log.getLog(this).error("=================",entity.toString());

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        System.out.println(entity.getHeaders());
        Greeting greeting = entity.getBody();
        assertEquals("Hello, World!", greeting.getContent());
    }

    @Test
    public void corsWithJavaconfig() {
        ResponseEntity<Greeting> entity = this.restTemplate.exchange(
                RequestEntity.get(uri("/v1/greeting1")).header(HttpHeaders.ORIGIN, "https://localhost:8080").build(),
                Greeting.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("https://localhost:8080", entity.getHeaders().getAccessControlAllowOrigin());
        Greeting greeting = entity.getBody();
        assertEquals("Hello, World!", greeting.getContent());
    }

    private URI uri(String path) {
        return restTemplate.getRestTemplate().getUriTemplateHandler().expand(path);
    }
}