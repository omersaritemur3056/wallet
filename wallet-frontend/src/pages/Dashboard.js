import React, {useEffect, useState} from 'react'
import axios from "axios";
import {Row} from "react-bootstrap";
import WalletList from "../component/WalletList";

const Dashboard = () => {
  const [wallet, setWallet] = useState([]);
  const baseUrl = "http://localhost:9899";

  useEffect(() => {
    axios.get(baseUrl + "/api/wallets/list/1")
        .then((response) => {
          setWallet(response.data.payload);
        });
  }, []);

  return (
      <Row className={'mt-5'}>

        <WalletList wallets={wallet}/>
      </Row>
  )
}

export default Dashboard