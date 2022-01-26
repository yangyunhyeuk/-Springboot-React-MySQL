package com.yang.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;

// @Repository를 적어야 스프링 IoC에 등록이 되는데
// JpaRepository를 상속받으면 생략이 가능하다.
// JpaRepository는 CRUD 함수를 갖고 있다.
public interface BookRepository extends JpaRepository<Book, Long> {


}
