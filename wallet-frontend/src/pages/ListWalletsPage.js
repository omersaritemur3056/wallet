import React, {useEffect, useState} from 'react'
import {Row} from "react-bootstrap";
import WalletList from "../components/WalletList";
import HttpRequestUtil from "../utilities/HttpRequestUtil";

const ListWalletsPage = () => {
  const [wallet, setWallet] = useState([]);

  useEffect(() => {
      HttpRequestUtil.get("/api/wallets/list/1")
        .then((response) => {
          setWallet(response.data.payload);
        });
  }, []);

  return (
      <Row className={'mt-5 mb-5'}>
        <WalletList wallets={wallet}/>
      </Row>
  )
}

export default ListWalletsPage