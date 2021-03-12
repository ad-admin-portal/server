package com.insutil.admin.controller;

import com.insutil.admin.model.MailHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MailHistoryHandlerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getMailHistoryTest(){
        webTestClient.get()
                .uri("/history/mail")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MailHistory.class)
                .value(System.out::println);
    }


}
