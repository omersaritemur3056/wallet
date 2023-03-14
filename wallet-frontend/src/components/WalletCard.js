import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import {Col} from "react-bootstrap";
import {Link} from "react-router-dom";
import HttpRequestUtil from "../utilities/HttpRequestUtil";

const handleClick = (event, id) => {
    event.preventDefault();
    HttpRequestUtil.delete("api/wallets/" + id)
        .then(() => {
            window.location.reload();
            window.alert("Wallet " + id + " deleted successfully.")
        }).catch((error) => {
        window.alert("Not allowed to delete this wallet with a non-zero balance!");
    })
}

function WalletCard(props) {
    return (<Col md={4} sm={6} sx={12}>
            <Card className={'mt-3'}>
                <Card.Header>
                    <Link className={'text-decoration-none'} to={`/wallets/${props.id}`}>{props.id} -> Show
                        Transactions</Link>
                </Card.Header>
                <Card.Body>
                    <Card.Title>{props.name}</Card.Title>
                    <Card.Text>
                        Balance: {props.balance}
                    </Card.Text>

                    <Link to={`/wallets/${props.id}/deposit`}>
                        <Button variant="success" className={'m-1'}>Deposit</Button>
                    </Link>
                    <Link to={`/wallets/${props.id}/withdraw`}>
                        <Button variant="danger" className={'m-1'}>Withdraw</Button>
                    </Link>
                    <Link to={`/wallets/${props.id}/transfer`}>
                        <Button variant="primary" className={'m-1'}>Transfer</Button>
                    </Link>
                </Card.Body>
                <Card.Footer>
                    <Link className={'text-decoration-none'}
                          onClick={(e) => handleClick(e, props.id)}>Delete</Link>
                </Card.Footer>
            </Card>
        </Col>
    )
}

export default WalletCard;