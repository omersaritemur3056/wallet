import WalletCard from "./WalletCard";
import {Link} from "react-router-dom";

function WalletList(props) {


    return props.wallets !== undefined ? props.wallets.map((wallet, i) => {
        return (
            <WalletCard key={i} id={wallet.id} name={wallet.name} balance={wallet.balance}/>
        );
    }): <div className={'mt-3'}>
            There is no wallet in the system.
            Please <Link to={"/wallets/add"}><b>Create Wallet</b></Link> at first.
    </div>;
}

export default WalletList;