-- Creazione dello schema se non esiste
CREATE SCHEMA IF NOT EXISTS hotel_service;

-- Rimuovo eventuali tabelle e sequenze esistenti per evitare conflitti
DROP TABLE IF EXISTS hotel_service.camera;
DROP TABLE IF EXISTS hotel_service.prezzo_camera;
DROP TABLE IF EXISTS hotel_service.fascia_eta;
DROP TABLE IF EXISTS hotel_service.tipo;
DROP TABLE IF EXISTS hotel_service.stato;

DROP SEQUENCE IF EXISTS hotel_service.seq_camera;
DROP SEQUENCE IF EXISTS hotel_service.seq_prezzo_camera;
DROP SEQUENCE IF EXISTS hotel_service.seq_fascia_eta;
DROP SEQUENCE IF EXISTS hotel_service.seq_stato;
DROP SEQUENCE IF EXISTS hotel_service.seq_tipo;

-- Creazione sequenza per l'id della camera
CREATE SEQUENCE IF NOT EXISTS hotel_service.seq_camera
START WITH 1
INCREMENT BY 1
MAXVALUE 9999
NOCACHE
NOCYCLE;

-- Creazione sequenza per l'id del prezzo della camera
CREATE SEQUENCE IF NOT EXISTS hotel_service.seq_prezzo_camera
START WITH 1
INCREMENT BY 1
MAXVALUE 9999
NOCACHE
NOCYCLE;

-- Creazione sequenza per l'id della fascia di età
CREATE SEQUENCE IF NOT EXISTS hotel_service.seq_fascia_eta
START WITH 1
INCREMENT BY 1
MAXVALUE 99
NOCACHE
NOCYCLE;

-- Creazione sequenza per l'id dello stato
CREATE SEQUENCE IF NOT EXISTS hotel_service.seq_stato
START WITH 1
INCREMENT BY 1
MAXVALUE 99
NOCACHE
NOCYCLE;

-- Creazione sequenza per l'id del tipo
CREATE SEQUENCE IF NOT EXISTS hotel_service.seq_tipo
START WITH 1
INCREMENT BY 1
MAXVALUE 99
NOCACHE
NOCYCLE;

-- Creazione tabella TIPO
CREATE TABLE IF NOT EXISTS hotel_service.tipo (
    id NUMBER(2) NOT NULL DEFAULT NEXT VALUE FOR hotel_service.seq_tipo,
    tipo VARCHAR(30) NOT NULL,
    CONSTRAINT pk_tipo PRIMARY KEY(id),
    CONSTRAINT uq_tipo UNIQUE (tipo) -- Vincolo di unicità sui tipi
);

-- Creazione tabella STATO
CREATE TABLE IF NOT EXISTS hotel_service.stato (
    id NUMBER(2) NOT NULL DEFAULT NEXT VALUE FOR hotel_service.seq_stato,
    stato VARCHAR(20) NOT NULL,
    CONSTRAINT pk_stato PRIMARY KEY(id),
    CONSTRAINT uq_stato UNIQUE (stato) -- Vincolo di unicità sugli stati
);

-- Creazione tabella FASCIA_ETA
CREATE TABLE IF NOT EXISTS hotel_service.fascia_eta (
    id NUMBER(2) NOT NULL DEFAULT NEXT VALUE FOR hotel_service.seq_fascia_eta,
    descrizione VARCHAR(50) NOT NULL,
    eta_min NUMBER(3) NOT NULL,
    eta_max NUMBER(3) NOT NULL,
    percentuale_sconto DECIMAL(5, 2) NOT NULL CHECK (percentuale_sconto BETWEEN 0 AND 100),

    CONSTRAINT pk_sconto_fascia_eta PRIMARY KEY (id),
    CONSTRAINT chk_eta_valida CHECK (eta_min <= eta_max) -- Vincolo di validità dell'intervallo di età
);

-- Creazione della tabella PREZZO_CAMERA
CREATE TABLE IF NOT EXISTS hotel_service.prezzo_camera (
    id NUMBER(10) NOT NULL DEFAULT NEXT VALUE FOR hotel_service.seq_prezzo_camera,
    id_tipo NUMBER(2) NOT NULL,                          -- FK verso la tabella TIPO
    id_fascia_eta NUMBER(2) NOT NULL,                    -- FK verso la tabella FASCIA_ETA
    prezzo_totale DECIMAL(10, 2) NOT NULL,               -- Prezzo totale con precisione e scala
    numero_occupanti NUMBER(10) NOT NULL,                -- Numero di occupanti
    CONSTRAINT pk_prezzo_camera PRIMARY KEY (id),
    CONSTRAINT fk_prezzo_camera_tipo FOREIGN KEY (id_tipo) REFERENCES hotel_service.tipo (id),
    CONSTRAINT fk_prezzo_camera_fascia_eta FOREIGN KEY (id_fascia_eta) REFERENCES hotel_service.fascia_eta (id)
);

-- Creazione tabella CAMERA
CREATE TABLE IF NOT EXISTS hotel_service.camera (
    id NUMBER(4) NOT NULL DEFAULT NEXT VALUE FOR hotel_service.seq_camera,
    numero_camera VARCHAR(20) NOT NULL,
    id_tipo NUMBER(2) NOT NULL,                         -- FK verso TIPO
    id_stato NUMBER(2) NOT NULL,                        -- FK verso STATO
    capacita NUMBER(2) NOT NULL,
    numero_alloggiati NUMBER(2) NOT NULL DEFAULT 0,
    prezzo_per_notte DECIMAL(10, 2) NOT NULL,
    data_inizio_disponibilita DATE,
    data_fine_disponibilita DATE,
    data_rimozione DATE,
    motivazione_rimozione VARCHAR(255),
    CONSTRAINT pk_camera PRIMARY KEY(id),
    CONSTRAINT fk_camera_tipo FOREIGN KEY(id_tipo) REFERENCES hotel_service.tipo(id),
    CONSTRAINT fk_camera_stato FOREIGN KEY(id_stato) REFERENCES hotel_service.stato(id)
);
