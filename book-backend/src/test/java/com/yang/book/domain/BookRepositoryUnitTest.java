package com.yang.book.domain;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


// 단위 테스트 (DB 관련된 Bean이 IoC에 등록되면 된다.)

// 가짜 DB로 테스트 한다는 의미, Replace.ANY == 실제 DB로 테스트

// 실제DB테스트 Replace.NONE, 내장DB테스트 Replace.ANY
@Slf4j
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@DataJpaTest
public class BookRepositoryUnitTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void save_테스트() {
        // given
        Book book = new Book(null, "책 제목", "책 저자");

        // when
        Book bookEntity = bookRepository.save(book);

        // then
        assertEquals("책 제목", bookEntity.getTitle());
    }

}