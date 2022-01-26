import React, { useEffect, useState } from 'react';
import BookItem from '../../components/BookItem';

const Home = () => {
  const [books, setBooks] = useState([]);

  // 함수 실행 시 최초 1번 실행되는 것 + 상태 값이 변경될 때마다 실행된다.
  // useEffect
  useEffect(() => {
    fetch('http://localhost:8080/book')
      .then((res) => res.json())
      .then((res) => {
        console.log(1, res);
        setBooks(res);
      }); // 비동기함수
  }, []); // 어디에도 의존하고 있지 않아 == 1번만 실행될 수 있도록 빈 배열을 넣는 것이다.

  return (
    <div>
      <h1>책 리스트 보기</h1>
      {books.map((book) => (
        <BookItem key={book.id} book={book} />
      ))}
    </div>
  );
};

export default Home;
