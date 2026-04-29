CREATE TABLE if not exists PLAYERS(
                                      id BIGINT GENERATED  BY DEFAULT AS IDENTITY PRIMARY KEY,
                                      name VARCHAR(128) UNIQUE NOT NULL
    );

INSERT INTO PLAYERS (name) VALUES ('Алексей');
INSERT INTO PLAYERS (name) VALUES ('Мария');
INSERT INTO PLAYERS (name) VALUES ('Иван');
INSERT INTO PLAYERS (name) VALUES ('Елена');
INSERT INTO PLAYERS (name) VALUES ('Дмитрий');

CREATE TABLE if not exists MATCHES(
                                      id BIGINT GENERATED  BY DEFAULT AS IDENTITY PRIMARY KEY,
                                      player1 BIGINT,
                                      player2 BIGINT,
                                      winner BIGINT,
                                      FOREIGN KEY (player1) REFERENCES PLAYERS(id),
    FOREIGN KEY (player2) REFERENCES PLAYERS(id),
    FOREIGN KEY (winner) REFERENCES PLAYERS(id)
    );