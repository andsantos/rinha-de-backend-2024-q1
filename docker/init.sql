CREATE UNLOGGED TABLE CLIENTES (
    CLIENTE_ID SERIAL PRIMARY KEY,
    NOME VARCHAR(50),
    LIMITE INTEGER NOT NULL,
    SALDO INTEGER NOT NULL DEFAULT 0
);

CREATE UNLOGGED TABLE TRANSACOES (
    ID SERIAL PRIMARY KEY,
    VALOR INTEGER NOT NULL,
    DESCRICAO VARCHAR(10) NOT NULL,
    TIPO CHAR(1) NOT NULL,
    CLIENTE_ID INTEGER NOT NULL,
    REALIZADA_EM TIMESTAMP,
    FOREIGN KEY (CLIENTE_ID) REFERENCES CLIENTES (CLIENTE_ID)
);

INSERT INTO CLIENTES (NOME, LIMITE, SALDO) VALUES ('o barato sai caro', 1000 * 100, 0);
INSERT INTO CLIENTES (NOME, LIMITE, SALDO) VALUES ('zan corp ltda', 800 * 100, 0);
INSERT INTO CLIENTES (NOME, LIMITE, SALDO) VALUES ('les cruders', 10000 * 100, 0);
INSERT INTO CLIENTES (NOME, LIMITE, SALDO) VALUES ('padaria joia de cocaia', 100000 * 100, 0);
INSERT INTO CLIENTES (NOME, LIMITE, SALDO) VALUES ('kid mais', 5000 * 100, 0);