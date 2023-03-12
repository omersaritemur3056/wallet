import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import {Col} from "react-bootstrap";

function WalletCard(props) {
    return (
        <Col md={4} sm={6} sx={12}>
            <Card className={'mt-3'}>
                <Card.Header>{props.id}</Card.Header>
                <Card.Body>
                    <Card.Title>{props.name}</Card.Title>
                    <Card.Text>
                        Balance: {props.balance}
                    </Card.Text>

                    <Button variant="success" className={'m-1'}>Deposit</Button>
                    <Button variant="danger" className={'m-1'}>Withdraw</Button>
                    <Button variant="primary" className={'m-1'}>Transfer</Button>

                </Card.Body>
            </Card>
        </Col>
    );
}

export default WalletCard;