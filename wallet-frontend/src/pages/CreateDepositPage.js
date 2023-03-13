import React, {useState} from 'react'
import {Col, Form, InputGroup, Row} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import HttpRequestUtil from "../utilities/HttpRequestUtil";
import {useParams} from "react-router-dom";

const CreateDepositPage = () => {
    const {id} = useParams();
    const [amount, setAmount] = useState("");
    const [description, setDescription] = useState("");

    const handleClick = (e) => {
        e.preventDefault();
        console.log("id: ", id);

        if (amount < 0) {
            window.alert("Please enter a non-negative number!")
        } else {
            const walletTransaction = {
                amount: amount, transactionType: "DEPOSIT", receiverWallet: {id: id}, description: description
            };

            console.log("walletTransaction: ", walletTransaction);
            HttpRequestUtil.post("/api/transactions", walletTransaction)
                .then((response) => {
                    window.alert("Deposit transaction created successfully.")
                });
        }
    }

    return (<Row className={'mt-5 justify-content-md-center'}>
            <Col md={4}>
                <Form.Group className="mb-4">
                    <Form.Label>
                        <b>Enter an Amount</b>
                    </Form.Label>
                    <InputGroup className="mb-3">
                        <InputGroup.Text id="inputGroupPrepend">$</InputGroup.Text>
                        <Form.Control value={amount}
                                      onChange={(e) => setAmount(e.target.value)}
                                      type="number"
                                      min={0}
                                      className="form-control text-capitalize"
                                      placeholder="0"/>
                    </InputGroup>
                    <Form.Label>
                        <b>Enter a Description</b>
                    </Form.Label>
                    <InputGroup className="mb-3">
                        <Form.Control value={description}
                                      onChange={(e) => setDescription(e.target.value)}
                                      type="text"
                                      min={0}
                                      className="form-control text-capitalize"
                                      placeholder="Description"/>
                    </InputGroup>
                </Form.Group>
                <Form onSubmit={handleClick}>
                    <Button className="btn-dark btn-md" style={{width: '100%'}} variant="primary" type="submit">
                        Deposit
                    </Button>
                </Form>
            </Col>
        </Row>)
}

export default CreateDepositPage