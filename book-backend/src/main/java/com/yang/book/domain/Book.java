package com.yang.book.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


// 서버 실행 시 Object Relation Mapping이 된다.
// == 서버 실행 시 테이블이 h2에 생성된다.
@Entity
@Data // 게터 세터
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id // PK를 해당 변수로 지정하겠다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 DB 번호 증가 전략을 따라가겠다는 의미
    private Long id;

    private String title;

    private String author;

}
