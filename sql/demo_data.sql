-- =========================
-- RESET DATABÁZY (DEMO)
-- =========================

SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM cart_items;
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM users;

ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE orders AUTO_INCREMENT = 1;
ALTER TABLE order_items AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;

-- =========================
-- POUŽÍVATELIA
-- =========================
-- Heslo pre všetkých: password123

INSERT INTO users
(id, email, password, first_name, last_name, phone, address, city, postal_code, role, active)
VALUES
    (1, 'admin@pizzeria.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Admin', 'Pizzeria', '+421900111222',
     'Hlavna 1', 'Bratislava', '81101', 'ADMIN', TRUE),

    (2, 'kuchar@pizzeria.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Mario', 'Rossi', '+421900222333',
     'Kuchynska 5', 'Bratislava', '81102', 'KUCHAR', TRUE),

    (3, 'kurier@pizzeria.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Luigi', 'Bianchi', '+421900333444',
     'Rozvozova 10', 'Bratislava', '81103', 'KURIER', TRUE),

    (4, 'jan.novak@email.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Jan', 'Novak', '+421901234567',
     'Dubova 15', 'Bratislava', '82108', 'ZAKAZNIK', TRUE),

    (5, 'maria.horvat@email.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Maria', 'Horvat', '+421902345678',
     'Lipova 22', 'Kosice', '04001', 'ZAKAZNIK', TRUE);

-- =========================
-- OBJEDNÁVKY
-- =========================

INSERT INTO orders
(user_id, status, total_price, delivery_address, delivery_city, delivery_phone, note, cook_id, courier_id)
VALUES
-- doručená objednávka (kompletný proces)
(4, 'DORUCENA', 19.00,
 'Dubova 15', 'Bratislava', '+421901234567',
 'Prosim zazvonit.', 2, 3),

-- objednávka v príprave
(5, 'PRIPRAVUJE_SA', 22.50,
 'Lipova 22', 'Kosice', '+421902345678',
 NULL, 2, NULL),

-- čakajúca objednávka (ešte nepriradená)
(4, 'CAKAJUCA', 16.00,
 'Dubova 15', 'Bratislava', '+421901234567',
 'Bez cibule prosim.', NULL, NULL);

-- =========================
-- POLOŽKY OBJEDNÁVOK
-- =========================

INSERT INTO order_items
(order_id, pizza_name, size_name, unit_price, quantity, ingredients)
VALUES
-- Objednávka 1
(1, 'Margherita', 'Stredna', 8.50, 1,
 'Paradajkova omacka, Mozzarella, Bazalka'),

(1, 'Prosciutto', 'Velka', 11.00, 1,
 'Paradajkova omacka, Mozzarella, Sunka'),

-- Objednávka 2
(2, 'Quattro Formaggi', 'Velka', 12.50, 1,
 'Paradajkova omacka, Mozzarella, Parmezan, Gorgonzola, Feta'),

(2, 'Diavola', 'Stredna', 9.50, 1,
 'Paradajkova omacka, Mozzarella, Salama, Jalapeno, Cesnak'),

-- Objednávka 3
(3, 'Funghi', 'Stredna', 8.50, 1,
 'Paradajkova omacka, Mozzarella, Sampiony'),

(3, 'Veggie', 'Mala', 6.50, 1,
 'Paradajkova omacka, Mozzarella, Paprika, Cibula, Kukurica, Olivy');

-- =========================
-- INFO PRE DEMO
-- =========================
SELECT 'Hotovo! Prihlasenie: admin@pizzeria.sk / password123' AS Info;
