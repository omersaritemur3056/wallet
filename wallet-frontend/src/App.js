import './App.css';
import HeaderMenu from "./component/HeaderMenu";
import {Row} from "react-bootstrap";
import Container from "react-bootstrap/Container";
import axios from "axios";
import {useEffect, useState} from "react";
import WalletList from "./component/WalletList";

function App() {

    const [wallet, setWallet] = useState([]);

    useEffect(() => {
        axios.get("http://localhost:9899/api/wallets/list/1")
            .then((response) => {
            setWallet(response.data.payload);
        });
    }, []);

    return (<div className="App">
        <HeaderMenu/>
        <Container>
            <Container className={'m-4'}>
                <Row>
                    <WalletList wallets={wallet}/>
                </Row>
            </Container>
        </Container>
    </div>);
}

export default App;
