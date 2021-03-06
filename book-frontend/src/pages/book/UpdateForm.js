import React, { useEffect, useState } from 'react';
import { Button, Form } from 'react-bootstrap';

const UpdateForm = (props) => {
  const id = props.match.params.id;

  const [book, setBook] = useState({
    title: '',
    author: '',
  });

  useEffect(() => {
    fetch('http://localhost:8080/book/' + id)
      .then((res) => res.json())
      .then((res) => {
        setBook(res);
      }, []);
  });

  const changeValue = (e) => {
    setBook({
      ...book, // 기존 값 지워지지 말라고 기재한 것
      [e.target.name]: e.target.value,
    });
  };

  const submitBook = (e) => {
    e.preventDefault(); // submit이 action을 안타고 자기 할 일을 하게함
    fetch('http://localhost:8080/book/' + id, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(book), // js 오브젝트를 json으로 던져줌을 의미
    })
      .then((res) => {
        console.log(1, res);
        if (res.status === 200) {
          return res.json();
        } else {
          return null;
        }
      })
      .then((res) => {
        // catch는 해당 분기에서 오류 발생 시 발현!
        console.log(2, res);
        if (res !== null) {
          props.history.push('/book/' + id);
        } else {
          alert('책 수정 실패!');
        }
      });
  };

  return (
    <div>
      <h1>리턴 완료</h1>
      <Form onSubmit={submitBook}>
        <Form.Group className="mb-3" controlId="formBasicEmail">
          <Form.Label>Title</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter Title"
            onChange={changeValue}
            name="title"
          />
        </Form.Group>
        <Form.Group className="mb-3" controlId="formBasicEmail">
          <Form.Label>Author</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter Author"
            onChange={changeValue}
            name="author"
          />
        </Form.Group>

        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>
    </div>
  );
};

export default UpdateForm;
