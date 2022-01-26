package com.yang.book.service;


import com.yang.book.domain.Book;
import com.yang.book.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


// 기능을 정의할 수 있고 트랜잭션을 관리할 수 있다.

@RequiredArgsConstructor // final 붙은 변수 자동 DI 생성
@Service // 에러 시 IoC 컨테이너에 등록이 된지, 메모리에 떴는지 체크!
public class BookService {

    private final BookRepository bookRepository;

    // 해당 메서드 수행 실패 시 rollback, else commit
    @Transactional
    // JPA에서 변경 감지라는 내부 기능이 존재한다.
    // readOnly = true란 조건은 변경 감지 내부 기능 활성화가 사라진다.
    // 이는 upadte 시 정합성을 유지시켜주나,
    // insert의 유령데이터(팬텀) 현상을 못막는다.
    public Book 저장하기(Book book) {
        return bookRepository.save(book);
        // 처리 후 자기가 save한 엔티티를 그대로 리턴해준다.
        // 리턴 타입 S : 넣은대로 그대로 리턴
    }

    @Transactional(readOnly = true)
    public Book 한건가져오기(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요"));
        // findById시 에러가 발생할 수 있기에 람다식으로 에러 처리
    }

    @Transactional(readOnly = true)
    public List<Book> 모두가져오기() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book 수정하기(Long id, Book book) {
        // 더티 체킹 -> update
        // 데이터베이스에서 데이터를 들고온다
        // == 영속화시킨다
        // == 영속성 컨텍스트에 보관된다
        Book bookEntity = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요"));
        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        return bookEntity;
        // 1. DB에서 데이터 들고와서 영속화시킨다
        // 2. 해당 데이터 변경시킨다
        // 3. 서비스 함수 종료되면 트랜잭션이 종료된다.
        // 4. 영속화되어 있는 데이터를 DB로 갱신(flush)
        // 5. 이 때 DB에 commit이 된다 == 더티체킹
        // 업데이트 시 더티체킹으로 해결한다.
    }

    @Transactional
    public String 삭제하기(Long id) {
        bookRepository.deleteById(id);
        // 오류가 터지면 Exception을 타니 신경쓰지 말기
        return "ok";
    }


}
