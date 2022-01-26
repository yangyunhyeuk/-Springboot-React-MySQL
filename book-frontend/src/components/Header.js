import React from 'react';
import {
  Form,
  Button,
  FormControl,
  Nav,
  Navbar,
  Container,
  NavDropdown,
} from 'react-bootstrap';
import { Link } from 'react-router-dom';

const Header = () => {
  return (
    <>
      <Navbar bg="dark" variant="dark">
        <Container fluid>
          <Navbar.Collapse id="navbarScroll">
            <Link to="/" className="navbar-brand">
              홈
            </Link>
            <Nav
              className="me-auto my-2 my-lg-0"
              style={{ maxHeight: '100px' }}
              navbarScroll
            >
              <Link to="/JoinForm" className="nav-link">
                회원가입
              </Link>
              <Link to="/LoginForm" className="nav-link">
                로그인
              </Link>
              <Link to="/saveForm" className="nav-link">
                글쓰기
              </Link>
            </Nav>

            <Form className="d-flex">
              <FormControl
                type="search"
                placeholder="Search"
                className="me-2"
                aria-label="Search"
              />
              <Button variant="outline-info">Search</Button>
            </Form>
          </Navbar.Collapse>
        </Container>
      </Navbar>
      <br />
    </>
  );
};

export default Header;
