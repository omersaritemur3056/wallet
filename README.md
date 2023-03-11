# E - Wallet Application

## Run Setup
- Follow the steps below in order to run project in local:
1. Go to /docker directory
   - Run ```docker compose up``` command in it.
2. Go to /db directory and run the files below.
   - Run the scripts in the e-wallet_ddl.sql file to create the schema and tables. 
   - Run the scripts in the e-wallet_dml.sql file to insert the data necessary for the application to run.

   

```
{
   "transactionId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
   "transactionType": "TRANSFER",
   "description": "Transfer Description",
   "amount": 100,
   "senderWallet": {
      "id": 1
   },
   "receiverWallet": {
      "id": 2
   }
}

{
   "transactionId": "3fa85f64-5717-4562-b3fc-2c963f66afa7",
   "transactionType": "WITHDRAW",
   "description": "Withdraw Description",
   "amount": 100,
   "senderWallet": {
      "id": 1
   }
}

{
   "transactionId": "3fa85f64-5717-4562-b3fc-2c963f66afa8",
   "transactionType": "DEPOSIT",
   "description": "Deposit Description",
   "amount": 100,
   "receiverWallet": {
      "id": 2
   }
}
```