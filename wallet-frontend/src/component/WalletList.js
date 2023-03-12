import WalletCard from "./WalletCard";

function WalletList(props) {
    return props.wallets.map((wallet, i) => {
        return (
            <WalletCard key={i} id={wallet.id} name={wallet.name} balance={wallet.balance}/>
        );
    });
}

export default WalletList;