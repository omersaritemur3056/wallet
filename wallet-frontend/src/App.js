import "./App.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

// import screen
import Dashboard from "./pages/Dashboard";
import CreateWithdrawPage from "./pages/CreateWithdrawPage";
import CreateWalletPage from "./pages/CreateWalletPage";

import ListTransactionsPage from "./pages/ListTransactionsPage";

import Login from "./pages/Login";
import ListWalletsPage from "./pages/ListWalletsPage";
import CreateDepositPage from "./pages/CreateDepositPage";
import CreateTransferPage from "./pages/CreateTransferPage";


const router = createBrowserRouter([
    {
        path: "/",
        element: <Dashboard />,
    },
    {
        path: "/wallets",
        element: <ListWalletsPage />,
    },
    {
        path: "/wallets/add",
        element: <CreateWalletPage />,
    },
    {
        path: "/login",
        element: <Login />,
    },
    {
        path: "/wallets/:id",
        element: <ListTransactionsPage />,
    },
    {
        path: "/wallets/:id/deposit",
        element: <CreateDepositPage />,
    },
    {
        path: "/wallets/:id/withdraw",
        element: <CreateWithdrawPage />,
    },
    {
        path: "/wallets/:id/transfer",
        element: <CreateTransferPage />,
    }
]);

function App() {
    return <RouterProvider router={router} />;
}

export default App;
