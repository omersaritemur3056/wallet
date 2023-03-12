import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';

function WalletCard(props) {
    return (
        <Card key={props.id} style={{width: '30rem'}} className={'m-2'}>
            <Card.Header>{props.id}</Card.Header>
            <Card.Body>
                <Card.Title>{props.name}</Card.Title>
                <Card.Text>
                    Balance: {props.balance}
                </Card.Text>

                <Button variant="success" className={'m-2'}>Deposit</Button>
                <Button variant="danger" className={'m-2'}>Withdraw</Button>
                <Button variant="primary" className={'m-2'}>Transfer</Button>

            </Card.Body>
        </Card>
    );
}

export default WalletCard;