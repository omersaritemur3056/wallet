import "./App.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

// import screen
import Dashboard from "./pages/Dashboard";
import CreateWithdrawPage from "./pages/CreateWithdrawPage";
import CreateWalletPage from "./pages/CreateWalletPage";

import ListTransactionsPage from "./pages/ListTransactionsPage";

import Login from "./pages/Login";
import ListWalletsPage from "./pages/ListWalletsPage";


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
        path: "/create-withdraw",
        element: <CreateWithdrawPage />,
    },
    {
        path: "/login",
        element: <Login />,
    },
    {
        path: "/wallets/:id",
        element: <ListTransactionsPage />,
    }
]);

function App() {
    return <RouterProvider router={router} />;
}

export default App;
