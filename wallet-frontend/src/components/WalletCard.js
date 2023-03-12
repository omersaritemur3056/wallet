import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import {Col} from "react-bootstrap";
import {Link} from "react-router-dom";

function WalletCard(props) {
    return (
        <Col md={4} sm={6} sx={12}>
            <Card className={'mt-3'}>
                <Card.Header>
                    <Link to={`/wallets/${props.id}`}>{props.id}</Link>
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
            </Card>
        </Col>
    );
}

export default WalletCard;