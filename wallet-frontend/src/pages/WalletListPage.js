import React, {useEffect, useState} from 'react'
import {Row} from "react-bootstrap";
import WalletList from "../components/WalletList";
import HttpRequestUtil from "../utilities/HttpRequestUtil";

const WalletListPage = () => {
  const [wallets, setWallets] = useState([]);

  useEffect(() => {
      HttpRequestUtil.get("/api/wallets/list/1")
        .then((response) => {
          setWallets(response.data.payload);
        });
  }, []);

  return (
      <Row className={'mt-3 mb-5'}>
        <WalletList wallets={wallets}/>
      </Row>
  )
}

export default WalletListPage