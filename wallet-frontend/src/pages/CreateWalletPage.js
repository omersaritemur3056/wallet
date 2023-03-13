import React, {useState} from 'react'
import {Col, Form, InputGroup, Row} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import HttpRequestUtil from "../utilities/HttpRequestUtil";

const CreateWalletPage = () => {
    const [balance, setBalance] = useState();
    const handleClick = (e) => {
        e.preventDefault();

        if (balance < 0) {
            window.alert("Please enter a non-negative number!")
        } else {
            HttpRequestUtil.post("/api/wallets", {balance: balance})
                .then((response) => {
                    let wallet = response.data.payload;
                    window.alert("Wallet created successfully with this name -> [" + wallet.name + "]"
                        + " and balance -> [" + wallet.balance + "]")
                });
        }
    }

    const handleChangeBalance = (e) => {
        setBalance(e.target.value);
    };

    return (
        <Row className={'mt-5 justify-content-md-center'}>
            <Col md={4}>
                <Form.Group className="mb-4">
                    <Form.Label>
                        <b>Please Enter a Balance</b>
                    </Form.Label>
                    <InputGroup className="mb-3">
                        <InputGroup.Text id="inputGroupPrepend">$</InputGroup.Text>
                        <Form.Control value={balance}
                                      onChange={handleChangeBalance}
                                      type="number"
                                      min={0}
                                      className="form-control text-capitalize"
                                      placeholder="0"/>
                    </InputGroup>
                </Form.Group>
                <Form onSubmit={handleClick}>
                    <Button className="btn-dark btn-md" style={{width: '100%'}} variant="primary" type="submit">
                        Submit
                    </Button>
                </Form>
            </Col>
        </Row>
    )
}

export default CreateWalletPage