-- Creazione dello schema se non esiste
CREATE SCHEMA IF NOT EXISTS hotel_service;

-- Rimuovo la tabella camera se esiste
DROP TABLE IF EXISTS hotel_service.camera;

-- Rimuovo la tabella stato se esiste
DROP TABLE IF EXISTS hotel_service.stato;

-- Rimuovo la sequenza seq_camera se esiste
DROP SEQUENCE IF EXISTS hotel_service.seq_camera;

-- Rimuovo la sequenza seq_stato se esiste
DROP SEQUENCE IF EXISTS hotel_service.seq_stato;

-- Creazione sequenza per l'id della camera
CREATE SEQUENCE IF NOT EXISTS hotel_service.seq_camera
START WITH 1
INCREMENT BY 1
MAXVALUE 9999
NOCACHE
NOCYCLE;

-- Creazione tabella camera con aggiunta del campo motivazione_rimozione
CREATE TABLE IF NOT EXISTS hotel_service.camera (
    id NUMBER(4) NOT NULL DEFAULT NEXT VALUE FOR hotel_service.seq_camera,
    numero_camera VARCHAR(20) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    id_stato NUMBER(2) NOT NULL,
    capacita NUMBER(2) NOT NULL,
    prezzo_per_notte DECIMAL(10, 2) NOT NULL,
    data_inizio_disponibilita DATE,
    data_fine_disponibilita DATE,
    data_rimozione DATE,
    numero_alloggiati NUMBER(2) NOT NULL DEFAULT 0,
    motivazione_rimozione VARCHAR(255),
    CONSTRAINT pk_camera PRIMARY KEY(id),
    CONSTRAINT fk_camera_stato FOREIGN KEY(id_stato) REFERENCES hotel_service.stato(id)  -- Vincolo di chiave esterna
);

-- Creazione sequenza per l'id dello stato
CREATE SEQUENCE IF NOT EXISTS hotel_service.seq_stato
START WITH 1
INCREMENT BY 1
MAXVALUE 99
NOCACHE
NOCYCLE;

-- Creazione tabella stato con i valori "libera" e "occupata"
CREATE TABLE IF NOT EXISTS hotel_service.stato (
    id NUMBER(2) NOT NULL DEFAULT NEXT VALUE FOR hotel_service.seq_stato,
    stato VARCHAR(20) NOT NULL,
    CONSTRAINT pk_stato PRIMARY KEY(id),
    CONSTRAINT uq_stato UNIQUE (stato)
);