package com.insutil.admin.controller;

import com.insutil.admin.model.Mail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureWebTestClient
public class MailHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void saveMailTest(){

        Mail mail = Mail.builder().type(0).smsContent("smsCOntent").sendTime(LocalDateTime.now())
                .sendDueDate(LocalDateTime.now()).registUser(1L).build();

        webTestClient
                .post()
                .uri("/api/management/mail")
                .bodyValue(mail)
                .exchange()
                .expectStatus().isCreated();

    }

}
