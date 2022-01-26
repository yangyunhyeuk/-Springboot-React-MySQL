package com.yang.book.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yang.book.domain.Book;
import com.yang.book.domain.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// 통합 테스트 : 컨트롤러로 전체 스프링을 테스트
// == 모든 Bean들을 똑같이 IoC에 올리고 테스트 하는 것
/*
 * WebEnvironment.MOCK = 실제 톰캣을 올리는 게 아니라 다른 톰캣으로 테스트
 * WebEnvironment.RANDOM_PORT = 실제 톰캣으로 테스트
 * @AutoConfigureMockMvc = MockMvc를 IoC에 등록해준다.
 * @Transactional = 각각의 테스트 함수가 종료될 때마다 트랜잭션을 rollback해주는 어노테이션
 * */
@Slf4j
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class BookControllerIntegreTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired // JPA는 entityManager의 구현체이다.
    private EntityManager entityManager;

    @BeforeEach // 모든 테스트 함수 실행되기 전 각각 수행
    public void init() {
        // entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE book AUTO_INCREMENT = 1").executeUpdate();
    }


    // BDD Mockito 패턴 : 모키토를 확장한 라이브러리 given, when, then
    @Test
    public void save_테스트() throws Exception {
        log.info("save_테스트 시작===========================================");
        // given (테스트를 하기 위한 준비)

        Book book = new Book(null, "스프링따라하기", "yang");

        String content = new ObjectMapper().writeValueAsString(new Book(null, "스프링따라하기", "yang"));

        // when (테스트 실행)
        ResultActions resultActions =
                mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON_UTF8));

        // then (검증)
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("스프링따라하기"))
                .andDo(MockMvcResultHandlers.print());


    }


    @Test
    public void findAll_테스트() throws Exception {
        // given 준비
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "스프링부트 따라하기", "yang1"));
        books.add(new Book(2L, "리액트 따라하기", "yang2"));
        books.add(new Book(3L, "Junit 따라하기", "yang3"));
        bookRepository.saveAll(books);

        // when 실행
        ResultActions resultActions = mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON_UTF8));

        // then 기대하는 것
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.[2].title").value("Junit 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findById_테스트() throws Exception {
        // given 준비
        Long id = 2L;

        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "스프링부트 따라하기", "yang1"));
        books.add(new Book(2L, "리액트 따라하기", "yang2"));
        books.add(new Book(3L, "Junit 따라하기", "yang3"));
        bookRepository.saveAll(books);

        // when
        ResultActions resultActions = mockMvc.perform(get("/book/{id}", id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("리액트 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_테스트() throws Exception {
        // given 준비
        Long id = 3L;

        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "스프링부트 따라하기", "yang1"));
        books.add(new Book(2L, "리액트 따라하기", "yang2"));
        books.add(new Book(3L, "Junit 따라하기", "yang3"));
        bookRepository.saveAll(books);

        Book book = new Book(null, "c++ 따라하기", "yang");
        String content = new ObjectMapper().writeValueAsString(book);


        // when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(put("/book/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.title").value("c++ 따라하기"))
                .andExpect(jsonPath("$.author").value("yang"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delete_테스트() throws Exception {
        // given 준비
        Long id = 1L;

        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "스프링부트 따라하기", "yang1"));
        books.add(new Book(2L, "리액트 따라하기", "yang2"));
        books.add(new Book(3L, "Junit 따라하기", "yang3"));
        bookRepository.saveAll(books);


        // when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(delete("/book/{id}", id)
                .accept(MediaType.TEXT_PLAIN));

        // then
        // 상태코드가 맞는지 ok가 맞는지 다음을 체크
        // 상태코드가 맞는지 ok가 맞는지 다음을 체크
        resultAction
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // 문자열을 리턴받으면 다음으로 받아야 한다.
        // 문자열을 리턴받으면 다음으로 받아야 한다.
        MvcResult requestResult = resultAction.andReturn();
        String result = requestResult.getResponse().getContentAsString();

        assertEquals("ok", result);
    }
}
