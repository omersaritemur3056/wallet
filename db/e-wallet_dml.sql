INSERT INTO users (ID,EMAIL,NAME,SURNAME,PASSWORD,DELETED) VALUES
    (nextval('user_seq'),'foo@bar.com','Foo','Bar','$2a$10$Tx79lr3lF/TskKTxUvVhBeJ0fWWT7pR.5dHqIu8LAUP9Hm0exxvQi', false);