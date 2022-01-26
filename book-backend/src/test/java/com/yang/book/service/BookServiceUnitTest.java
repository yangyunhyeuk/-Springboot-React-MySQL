package com.yang.book.service;


import com.yang.book.domain.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


/*
 *
 * // 단위 테스트 (Service와 관련된 애들만 IoC(메모리)에 띄우면 된다. )
 * 서비스와 관련된 BoardRepository 필요
 * 가짜 객체로 만들 수 있다.
 * MockitoExtension가 해당 환경을 지원
 * */

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

    @InjectMocks // 해당 파일에 @Mock로 등록된 모든 애들을 DI해준다 == BookService가 모키토 메모리에 뜰 때
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void 저장하기_테스트() {

        /*


        //BDD Mocikto 방식
        // given
        Book book = new Book();
        book.setTitle("책 제목");
        book.setAuthor("책 저자");

        // stub - 동작 지정
        when(bookRepository.save(book)).thenReturn(book);

        // then
        assertEquals(bookEntity,book);


        */


    }

}
