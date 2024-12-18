
-- PRIMA INSERIRE LE DML DI TIPO E DI FASCIA_ETA

-- Inserimento prezzi

-- Prezzi per una camera Singola
INSERT INTO hotel_service.prezzo_camera (id_tipo, numero_occupanti, prezzo_totale) VALUES (1, 1, 75.00);

-- Prezzi per una camera Doppia
INSERT INTO hotel_service.prezzo_camera (id_tipo, numero_occupanti, prezzo_totale) VALUES (2, 1, 75.00);
INSERT INTO hotel_service.prezzo_camera (id_tipo, numero_occupanti, prezzo_totale) VALUES (2, 2, 100.00);

-- Prezzi per una camera Tripla
INSERT INTO hotel_service.prezzo_camera (id_tipo, numero_occupanti, prezzo_totale) VALUES (3, 1, 85.00);
INSERT INTO hotel_service.prezzo_camera (id_tipo, numero_occupanti, prezzo_totale) VALUES (3, 2, 120.00);
INSERT INTO hotel_service.prezzo_camera (id_tipo, numero_occupanti, prezzo_totale) VALUES (3, 3, 135.00);

-- Prezzi per una camera Quadrupla
INSERT INTO hotel_service.prezzo_camera (id_tipo, numero_occupanti, prezzo_totale) VALUES (4, 1, 95.00);
INSERT INTO hotel_service.prezzo_camera (id_tipo, numero_occupanti, prezzo_totale) VALUES (4, 2, 150.00);
INSERT INTO hotel_service.prezzo_camera (id_tipo, numero_occupanti, prezzo_totale) VALUES (4, 3, 180.00);
INSERT INTO hotel_service.prezzo_camera (id_tipo, numero_occupanti, prezzo_totale) VALUES (4, 4, 200.00);
