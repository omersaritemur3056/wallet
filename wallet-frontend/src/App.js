import "./App.css";
import {createBrowserRouter, Navigate, RouterProvider} from "react-router-dom";

import Dashboard from "./pages/Dashboard";
import CreateWithdrawPage from "./pages/CreateWithdrawPage";

import TransactionListPage from "./pages/TransactionListPage";

import Login from "./pages/Login";
import WalletListPage from "./pages/WalletListPage";
import CreateDepositPage from "./pages/CreateDepositPage";
import CreateTransferPage from "./pages/CreateTransferPage";
import CreateWalletPage from "./pages/CreateWalletPage";
import React from "react";


const router = createBrowserRouter([
    {path: "/", element: <Dashboard/>},
    {index: true, element: <Navigate to="/wallets" replace />},
    {path: "/wallets", element: <WalletListPage/>},
    {path: "/login", element: <Login/>},
    {path: "/wallets/:id", element: <TransactionListPage/>},
    {path: "/wallets/add", element: <CreateWalletPage/>},
    {path: "/wallets/:id/deposit", element: <CreateDepositPage/>},
    {path: "/wallets/:id/withdraw", element: <CreateWithdrawPage/>},
    {path: "/wallets/:id/transfer", element: <CreateTransferPage/>}
]);

function App() {
    return <RouterProvider router={router}/>;
}

export default App;
