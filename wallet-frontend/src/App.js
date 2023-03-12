import "./App.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

// import screen
import Dashboard from "./pages/Dashboard";
import CreateWithdrawPage from "./pages/CreateWithdrawPage";
import CreateWalletPage from "./pages/CreateWalletPage";

import ListTransactionsPage from "./pages/ListTransactionsPage";

import Login from "./pages/Login";


const router = createBrowserRouter([
    {
        path: "/",
        element: <Dashboard />,
    },
    {
        path: "/create-wallet",
        element: <CreateWalletPage />,
    },
    {
        path: "/create-withdraw",
        element: <CreateWithdrawPage />,
    },
    {
        path: "/login",
        element: <Login/>,
    },
    {
        path: "/wallets/:id",
        element: <ListTransactionsPage/>,
    },
]);

function App() {
    return <RouterProvider router={router} />;
}

export default App;
