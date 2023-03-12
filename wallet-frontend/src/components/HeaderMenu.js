import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import HttpRequestUtil from "../utilities/HttpRequestUtil";

const handleClick = (event) => {
    console.log("Created Wallet");
    event.preventDefault();

    HttpRequestUtil.post("/api/wallets",  {})
        .then((response) => {
            window.location.reload()
        });
}

function HeaderMenu() {
    return (
        <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
            <Container>
                <Navbar.Brand href="/">E-Wallet</Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link href="/wallets">Dashboard</Nav.Link>
                        <Nav.Link onClick={handleClick}>Create Wallet</Nav.Link>
                    </Nav>
                    <Nav>
                        <Nav.Link eventKey={2} href="/login">
                            Welcome - User
                        </Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default HeaderMenu;