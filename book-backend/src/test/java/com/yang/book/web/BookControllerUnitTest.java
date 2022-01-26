package com.yang.book.web;

// 단위 테스트 : 컨트롤러만 테스트
// == 컨트롤러 관련 로직만 띄우고 테스트 하는 것
// 필터, ControllerAdvice 등이 메모리에 뜬다.

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yang.book.domain.Book;
import com.yang.book.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest
public class BookControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // IoC 환경에 Bean 등록됨
    // 진짜 서비스를 사용하면 레포지토리를 연결해야하기에 가짜로 서비스 생성
    private BookService bookService;

    // BDD Mockito 패턴 : 모키토를 확장한 라이브러리 given, when, then
    @Test
    public void save_테스트() throws Exception {
        log.info("save_테스트 시작===========================================");
        // given (테스트를 하기 위한 준비)

        Book book = new Book(null, "스프링따라하기", "yang");

        String content = new ObjectMapper().writeValueAsString(new Book(null, "스프링따라하기", "yang"));
        when(bookService.저장하기(book)).thenReturn(new Book(1L, "스프링따라하기", "yang"));

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
        books.add(new Book(1L, "스프링부트 따라하기", "yang"));
        books.add(new Book(2L, "리액트 따라하기", "yang"));
        when(bookService.모두가져오기()).thenReturn(books);


        // when 실행
        ResultActions resultActions = mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON_UTF8));

        // then 기대하는 것
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findById_테스트() throws Exception {
        // given 준비
        Long id = 1L;
        when(bookService.한건가져오기(id)).thenReturn(new Book(1L, "자바 공부하기", "양"));

        // when
        ResultActions resultActions = mockMvc.perform(get("/book/{id}", id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("자바 공부하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_테스트() throws Exception {
        // given 준비
        Long id = 1L;
        Book book = new Book(null, "c++ 따라하기", "yang");
        String content = new ObjectMapper().writeValueAsString(book);

        when(bookService.수정하기(id, book)).thenReturn(new Book(1L, "c++ 따라하기", "yang"));

        // when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(put("/book/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("c++ 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delete_테스트() throws Exception {
        // given 준비
        Long id = 1L;

        when(bookService.삭제하기(id)).thenReturn("ok");

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
