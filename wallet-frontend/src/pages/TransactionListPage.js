import React, {useEffect, useState} from 'react'
import {useParams} from 'react-router-dom';
import HttpRequestUtil from "../utilities/HttpRequestUtil";
import {Table} from "react-bootstrap";


const TransactionListPage = () => {
    const {id} = useParams();
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        HttpRequestUtil.get("/api/transactions/" + id)
            .then((response) => {
                setTransactions(response.data.payload);
            });
    }, []);

    return (
        <Table className={'mt-5'} striped bordered hover>
            <thead>
            <tr>
                <th>#</th>
                <th>Transaction Id</th>
                <th>Created Date</th>
                <th>Amount</th>
                <th>Description</th>
                <th>Transaction Status</th>
                <th>Transaction Type</th>
                <th>Sender Wallet</th>
                <th>Receiver Wallet</th>
            </tr>
            </thead>
            <tbody>
                {transactions?.map((transaction, index) => {
                    return (
                        <tr key={index}>
                            <td>{index}</td>
                            <td>{transaction.transactionId}</td>
                            <td>{transaction.createdAt}</td>
                            <td>{transaction.amount}</td>
                            <td>{transaction.description}</td>
                            <td>{transaction.transactionStatus}</td>
                            <td>{transaction.transactionType}</td>
                            <td>{transaction.senderWallet?.name}</td>
                            <td>{transaction.receiverWallet?.name}</td>
                        </tr>
                    );
                })}
            </tbody>
        </Table>
    )
}

export default TransactionListPage