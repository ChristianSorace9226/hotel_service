
-- PRIMA INSERIRE LE DML DI TIPO


-- Inserimento dei prezzi delle camere per ogni tipo

-- Prezzi per una camera Singola
INSERT INTO hotel_service.prezzo_camera (tipo, numero_occupanti, prezzo_totale) VALUES ('singola', 1, 75.00);

-- Prezzi per una camera Doppia
INSERT INTO hotel_service.prezzo_camera (tipo, numero_occupanti, prezzo_totale) VALUES ('doppia', 1, 75.00);
INSERT INTO hotel_service.prezzo_camera (tipo, numero_occupanti, prezzo_totale) VALUES ('doppia', 2, 100.00);

-- Prezzi per una camera Tripla
INSERT INTO hotel_service.prezzo_camera (tipo, numero_occupanti, prezzo_totale) VALUES ('tripla', 1, 85.00);
INSERT INTO hotel_service.prezzo_camera (tipo, numero_occupanti, prezzo_totale) VALUES ('tripla', 2, 120.00);
INSERT INTO hotel_service.prezzo_camera (tipo, numero_occupanti, prezzo_totale) VALUES ('tripla', 3, 135.00);

-- Prezzi per una camera Quadrupla
INSERT INTO hotel_service.prezzo_camera (tipo, numero_occupanti, prezzo_totale) VALUES ('quadrupla', 1, 95.00);
INSERT INTO hotel_service.prezzo_camera (tipo, numero_occupanti, prezzo_totale) VALUES ('quadrupla', 2, 150.00);
INSERT INTO hotel_service.prezzo_camera (tipo, numero_occupanti, prezzo_totale) VALUES ('quadrupla', 3, 180.00);
INSERT INTO hotel_service.prezzo_camera (tipo, numero_occupanti, prezzo_totale) VALUES ('quadrupla', 4, 200.00);