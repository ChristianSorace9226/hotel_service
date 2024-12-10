-- Creazione dello schema se non esiste
CREATE SCHEMA IF NOT EXISTS hotel_service;

-- Rimuovo la tabella camera se esiste
DROP TABLE IF EXISTS hotel_service.camera;

-- Rimuovo la sequenza seq_camera se esiste
DROP SEQUENCE IF EXISTS hotel_service.seq_camera;

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
    stato VARCHAR(20) NOT NULL,
    capacita NUMBER(2) NOT NULL,
    prezzo_per_notte DECIMAL(10, 2) NOT NULL,
    data_inizio_disponibilita DATE,
    data_fine_disponibilita DATE,
    data_rimozione DATE,
    motivazione_rimozione VARCHAR(255),
    CONSTRAINT pk_camera PRIMARY KEY(id)
);

-- Rimuovo la sequenza seq_camera_numero se esiste
DROP SEQUENCE IF EXISTS hotel_service.seq_camera_numero;

-- Creazione sequenza per il numero della camera
CREATE SEQUENCE IF NOT EXISTS hotel_service.seq_camera_numero
START WITH 1
INCREMENT BY 1
MAXVALUE 9999
NOCACHE
NOCYCLE;

