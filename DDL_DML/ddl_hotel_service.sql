-- Creazione dello schema se non esiste
CREATE SCHEMA IF NOT EXISTS hotel_service;

-- Rimuovo eventuali tabelle e sequenze esistenti per evitare conflitti
DROP TABLE IF EXISTS hotel_service.camera;
DROP TABLE IF EXISTS hotel_service.prezzo_camera;
DROP TABLE IF EXISTS hotel_service.tipo;
DROP TABLE IF EXISTS hotel_service.stato;

DROP SEQUENCE IF EXISTS hotel_service.seq_camera;
DROP SEQUENCE IF EXISTS hotel_service.seq_prezzo_camera;
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

-- Creazione tabella PREZZI
CREATE TABLE IF NOT EXISTS hotel_service.prezzo_camera (
    id NUMBER(4) NOT NULL DEFAULT NEXT VALUE FOR hotel_service.seq_prezzo_camera, -- ID autoincrementale
    tipo VARCHAR(30) NOT NULL, -- Riferisce al tipo della camera (es. 'singola', 'doppia')
    numero_occupanti NUMBER(2) NOT NULL, -- Numero di persone che la occupano
    prezzo_totale DECIMAL(10, 2) NOT NULL, -- Prezzo totale per il numero di occupanti
    CONSTRAINT pk_prezzo_camera PRIMARY KEY (id), -- Chiave primaria
    CONSTRAINT uq_prezzo_camera UNIQUE (tipo, numero_occupanti), -- Unicità su tipo e numero occupanti
    CONSTRAINT fk_prezzo_tipo FOREIGN KEY (tipo) REFERENCES hotel_service.tipo(tipo) -- FK verso il tipo della camera
);

-- Creazione tabella CAMERA
CREATE TABLE IF NOT EXISTS hotel_service.camera (
    id NUMBER(4) NOT NULL DEFAULT NEXT VALUE FOR hotel_service.seq_camera,
    numero_camera VARCHAR(20) NOT NULL,
    id_tipo NUMBER(2) NOT NULL, -- FK su TIPO
    id_stato NUMBER(2) NOT NULL, -- FK su STATO
    capacita NUMBER(2) NOT NULL,
    numero_alloggiati NUMBER(2) NOT NULL DEFAULT 0,
    prezzo_per_notte DECIMAL(10, 2) NOT NULL,
    data_inizio_disponibilita DATE,
    data_fine_disponibilita DATE,
    data_rimozione DATE,
    motivazione_rimozione VARCHAR(255),
    CONSTRAINT pk_camera PRIMARY KEY(id),
    CONSTRAINT fk_camera_tipo FOREIGN KEY(id_tipo) REFERENCES hotel_service.tipo(id), -- Vincolo di chiave esterna su TIPO
    CONSTRAINT fk_camera_stato FOREIGN KEY(id_stato) REFERENCES hotel_service.stato(id) -- Vincolo di chiave esterna su STATO
);
