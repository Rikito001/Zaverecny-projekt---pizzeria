-- =========================
-- RESET DATABÁZY (DEMO)
-- =========================

SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM cart_items;
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM pizza_tags;
DELETE FROM pizza_ingredients;
DELETE FROM pizza_sizes;
DELETE FROM pizzas;
DELETE FROM tags;
DELETE FROM ingredients;
DELETE FROM users;

ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE tags AUTO_INCREMENT = 1;
ALTER TABLE ingredients AUTO_INCREMENT = 1;
ALTER TABLE pizzas AUTO_INCREMENT = 1;
ALTER TABLE pizza_sizes AUTO_INCREMENT = 1;
ALTER TABLE orders AUTO_INCREMENT = 1;
ALTER TABLE order_items AUTO_INCREMENT = 1;
ALTER TABLE cart_items AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;

-- =========================
-- POUŽÍVATELIA
-- =========================
-- Heslo pre všetkých: Password123!

INSERT INTO users
(id, email, password, first_name, last_name, phone, address, city, postal_code, role, active)
VALUES
    (1, 'admin@pizzeria.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Admin', 'Pizzeria', '+421900111222',
     'Hlavná 1', 'Bratislava', '81101', 'ADMIN', TRUE),

    (2, 'kuchar@pizzeria.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Mario', 'Rossi', '+421900222333',
     'Kuchynská 5', 'Bratislava', '81102', 'KUCHAR', TRUE),

    (3, 'kurier@pizzeria.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Luigi', 'Bianchi', '+421900333444',
     'Rozvozová 10', 'Bratislava', '81103', 'KURIER', TRUE),

    (4, 'jan.novak@email.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Ján', 'Novák', '+421901234567',
     'Dubová 15', 'Bratislava', '82108', 'ZAKAZNIK', TRUE),

    (5, 'maria.horvat@email.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Mária', 'Horváthová', '+421902345678',
     'Lipová 22', 'Košice', '04001', 'ZAKAZNIK', TRUE),

    (6, 'peter.kovac@email.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Peter', 'Kováč', '+421903456789',
     'Javorová 8', 'Nitra', '94901', 'ZAKAZNIK', TRUE);

-- =========================
-- TAGY (5 tagov)
-- =========================

INSERT INTO tags (id, name, color) VALUES
    (1, 'Vegetariánska', '#28a745'),
    (2, 'Ostrá', '#dc3545'),
    (3, 'Bezlepková', '#17a2b8'),
    (4, 'Obľúbená', '#ffc107'),
    (5, 'Novinka', '#6f42c1');

-- =========================
-- INGREDIENCIE (15 ingrediencií)
-- =========================

INSERT INTO ingredients (id, name, description, price_extra, allergens, available) VALUES
    (1, 'Paradajková omáčka', 'Talianska paradajková omáčka zo San Marzano paradajok', 0.00, NULL, TRUE),
    (2, 'Mozzarella', 'Čerstvá mozzarella', 0.00, 'mlieko', TRUE),
    (3, 'Bazalka', 'Čerstvá talianska bazalka', 0.00, NULL, TRUE),
    (4, 'Šampiňóny', 'Čerstvé krájané šampiňóny', 1.00, NULL, TRUE),
    (5, 'Šunka', 'Kvalitná dušená šunka', 1.50, NULL, TRUE),
    (6, 'Slanina', 'Chrumkavá slanina', 1.50, NULL, TRUE),
    (7, 'Ananás', 'Sladký ananás v kocke', 1.00, NULL, TRUE),
    (8, 'Feferónky', 'Pikantné feferónky', 0.50, NULL, TRUE),
    (9, 'Saláma', 'Pikantná saláma', 1.50, NULL, TRUE),
    (10, 'Artičoky', 'Marinované artičoky', 1.50, NULL, TRUE),
    (11, 'Olivy', 'Čierne olivy', 1.00, NULL, TRUE),
    (12, 'Smotanový základ', 'Krémový smotanový základ', 0.50, 'mlieko', TRUE),
    (13, 'Gorgonzola', 'Talianska gorgonzola', 2.00, 'mlieko', TRUE),
    (14, 'Parmezán', 'Strúhaný parmezán', 1.50, 'mlieko', TRUE),
    (15, 'Vajíčko', 'Čerstvé farmárske vajíčko', 1.00, 'vajcia', TRUE);

-- =========================
-- PIZZE (10 pizz)
-- =========================

INSERT INTO pizzas (id, name, slug, description, image_url, active) VALUES
    (1, 'Margherita Classica', 'margherita-classica', 
     'Klasická talianska pizza s paradajkovou omáčkou, mozzarellou a čerstvou bazalkou. Jednoduchosť v najlepšom prevedení.',
     '/images/pizzas/margherita-classica.webp', TRUE),

    (2, 'Funghi', 'funghi', 
     'Pizza pre milovníkov húb. Šampiňóny na paradajkovom základe s mozzarellou.',
     '/images/pizzas/funghi.webp', TRUE),

    (3, 'Funghi al Panna', 'funghi-al-panna', 
     'Krémová verzia pizze Funghi na smotanovom základe s množstvom šampiňónov a mozzarellou.',
     '/images/pizzas/funghi-al-panna.webp', TRUE),

    (4, 'Capricciosa', 'capricciosa', 
     'Tradičná pizza so šunkou, šampiňónmi, artičokmi a olivami na paradajkovom základe.',
     '/images/pizzas/capricciosa.webp', TRUE),

    (5, 'Hawaii Classic', 'hawaii-classic', 
     'Kontroverzná, ale obľúbená kombinácia šunky a ananásu na paradajkovom základe s mozzarellou.',
     '/images/pizzas/hawaii-classic.webp', TRUE),

    (6, 'Diavola Piccante', 'diavola-piccante', 
     'Ostrá pizza s pikantnou salámou a feferónkami. Pre tých, čo majú radi oheň.',
     '/images/pizzas/diavola-piccante.webp', TRUE),

    (7, 'Carbonara Pizza', 'carbonara-pizza', 
     'Inšpirovaná legendárnym talianskym jedlom. Smotanový základ, slanina, vajíčko a parmezán.',
     '/images/pizzas/carbonara-pizza.webp', TRUE),

    (8, 'La Crema Bianca', 'la-crema-bianca', 
     'Biela pizza na smotanovom základe s mozzarellou, gorgonzolou a parmezánom.',
     '/images/pizzas/la-crema-bianca.webp', TRUE),

    (9, 'Delicatezza Rustica', 'delicatezza-rustica', 
     'Rustikálna pizza so slaninou, šampiňónmi a čerstvými bylinkami na paradajkovom základe.',
     '/images/pizzas/delicatezza-rustica.webp', TRUE),

    (10, 'Primavera Bezlepková', 'gluten-free-primavera', 
     'Jarná vegetariánska pizza na bezlepkovom ceste s čerstvou zeleninou a mozzarellou.',
     '/images/pizzas/gluten-free-primavera.webp', TRUE);

-- =========================
-- VEĽKOSTI PIZZ (3 veľkosti pre každú pizzu = 30)
-- =========================

-- Margherita Classica (pizza_id = 1)
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
    (1, 'Malá', 5.90, 26),
    (1, 'Stredná', 7.90, 32),
    (1, 'Veľká', 9.90, 40);

-- Funghi (pizza_id = 2)
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
    (2, 'Malá', 6.50, 26),
    (2, 'Stredná', 8.50, 32),
    (2, 'Veľká', 10.50, 40);

-- Funghi al Panna (pizza_id = 3)
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
    (3, 'Malá', 6.90, 26),
    (3, 'Stredná', 8.90, 32),
    (3, 'Veľká', 10.90, 40);

-- Capricciosa (pizza_id = 4)
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
    (4, 'Malá', 7.50, 26),
    (4, 'Stredná', 9.50, 32),
    (4, 'Veľká', 11.90, 40);

-- Hawaii Classic (pizza_id = 5)
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
    (5, 'Malá', 6.90, 26),
    (5, 'Stredná', 8.90, 32),
    (5, 'Veľká', 10.90, 40);

-- Diavola Piccante (pizza_id = 6)
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
    (6, 'Malá', 7.50, 26),
    (6, 'Stredná', 9.50, 32),
    (6, 'Veľká', 11.90, 40);

-- Carbonara Pizza (pizza_id = 7)
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
    (7, 'Malá', 7.90, 26),
    (7, 'Stredná', 9.90, 32),
    (7, 'Veľká', 12.50, 40);

-- La Crema Bianca (pizza_id = 8)
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
    (8, 'Malá', 7.90, 26),
    (8, 'Stredná', 9.90, 32),
    (8, 'Veľká', 12.50, 40);

-- Delicatezza Rustica (pizza_id = 9)
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
    (9, 'Malá', 7.50, 26),
    (9, 'Stredná', 9.50, 32),
    (9, 'Veľká', 11.90, 40);

-- Primavera Bezlepková (pizza_id = 10)
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
    (10, 'Malá', 8.50, 26),
    (10, 'Stredná', 10.90, 32),
    (10, 'Veľká', 13.50, 40);

-- =========================
-- PIZZA - INGREDIENCIE
-- =========================

-- Margherita Classica: Paradajková omáčka, Mozzarella, Bazalka
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES
    (1, 1), (1, 2), (1, 3);

-- Funghi: Paradajková omáčka, Mozzarella, Šampiňóny
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES
    (2, 1), (2, 2), (2, 4);

-- Funghi al Panna: Smotanový základ, Mozzarella, Šampiňóny
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES
    (3, 12), (3, 2), (3, 4);

-- Capricciosa: Paradajková omáčka, Mozzarella, Šunka, Šampiňóny, Artičoky, Olivy
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES
    (4, 1), (4, 2), (4, 5), (4, 4), (4, 10), (4, 11);

-- Hawaii Classic: Paradajková omáčka, Mozzarella, Šunka, Ananás
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES
    (5, 1), (5, 2), (5, 5), (5, 7);

-- Diavola Piccante: Paradajková omáčka, Mozzarella, Saláma, Feferónky
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES
    (6, 1), (6, 2), (6, 9), (6, 8);

-- Carbonara Pizza: Smotanový základ, Mozzarella, Slanina, Vajíčko, Parmezán
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES
    (7, 12), (7, 2), (7, 6), (7, 15), (7, 14);

-- La Crema Bianca: Smotanový základ, Mozzarella, Gorgonzola, Parmezán
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES
    (8, 12), (8, 2), (8, 13), (8, 14);

-- Delicatezza Rustica: Paradajková omáčka, Mozzarella, Slanina, Šampiňóny, Bazalka
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES
    (9, 1), (9, 2), (9, 6), (9, 4), (9, 3);

-- Primavera Bezlepková: Paradajková omáčka, Mozzarella, Bazalka, Artičoky, Olivy
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES
    (10, 1), (10, 2), (10, 3), (10, 10), (10, 11);

-- =========================
-- PIZZA - TAGY
-- =========================

-- Margherita Classica: Vegetariánska, Obľúbená
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (1, 1), (1, 4);

-- Funghi: Vegetariánska
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (2, 1);

-- Funghi al Panna: Vegetariánska
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (3, 1);

-- Capricciosa: Obľúbená
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (4, 4);

-- Hawaii Classic: Obľúbená
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (5, 4);

-- Diavola Piccante: Ostrá, Novinka
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (6, 2), (6, 5);

-- Carbonara Pizza: Novinka
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (7, 5);

-- La Crema Bianca: Vegetariánska, Novinka
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (8, 1), (8, 5);

-- Delicatezza Rustica: Obľúbená
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (9, 4);

-- Primavera Bezlepková: Vegetariánska, Bezlepková
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (10, 1), (10, 3);

-- =========================
-- OBJEDNÁVKY (5 objednávok v rôznych stavoch)
-- =========================

INSERT INTO orders
(user_id, status, total_price, delivery_address, delivery_city, delivery_phone, note, cook_id, courier_id)
VALUES
-- 1. DORUČENÁ objednávka (kompletný proces)
(4, 'DORUCENA', 19.80,
 'Dubová 15', 'Bratislava', '+421901234567',
 'Prosím zazvoniť, nefunguje zvonček.', 2, 3),

-- 2. DORUČUJE SA objednávka (kuriér na ceste)
(5, 'DORUCUJE_SA', 22.40,
 'Lipová 22', 'Košice', '+421902345678',
 'Volať pred príchodom.', 2, 3),

-- 3. HOTOVÁ objednávka (čaká na kuriéra)
(6, 'HOTOVA', 31.80,
 'Javorová 8', 'Nitra', '+421903456789',
 NULL, 2, NULL),

-- 4. PRIPRAVUJE SA objednávka
(4, 'PRIPRAVUJE_SA', 17.80,
 'Dubová 15', 'Bratislava', '+421901234567',
 'Bez cibule prosím.', 2, NULL),

-- 5. ČAKAJÚCA objednávka (ešte nepriradená)
(5, 'CAKAJUCA', 10.90,
 'Lipová 22', 'Košice', '+421902345678',
 'Prosím extra syr.', NULL, NULL);

-- =========================
-- POLOŽKY OBJEDNÁVOK
-- =========================

INSERT INTO order_items
(order_id, pizza_name, size_name, unit_price, quantity, ingredients)
VALUES
-- Objednávka 1 (DORUČENÁ)
(1, 'Margherita Classica', 'Stredná', 7.90, 1,
 'Paradajková omáčka, Mozzarella, Bazalka'),
(1, 'Capricciosa', 'Veľká', 11.90, 1,
 'Paradajková omáčka, Mozzarella, Šunka, Šampiňóny, Artičoky, Olivy'),

-- Objednávka 2 (DORUČUJE SA)
(2, 'Diavola Piccante', 'Stredná', 9.50, 1,
 'Paradajková omáčka, Mozzarella, Saláma, Feferónky'),
(2, 'La Crema Bianca', 'Veľká', 12.90, 1,
 'Smotanový základ, Mozzarella, Gorgonzola, Parmezán'),

-- Objednávka 3 (HOTOVÁ)
(3, 'Carbonara Pizza', 'Veľká', 12.50, 2,
 'Smotanový základ, Mozzarella, Slanina, Vajíčko, Parmezán'),
(3, 'Funghi', 'Malá', 6.80, 1,
 'Paradajková omáčka, Mozzarella, Šampiňóny'),

-- Objednávka 4 (PRIPRAVUJE SA)
(4, 'Hawaii Classic', 'Stredná', 8.90, 2,
 'Paradajková omáčka, Mozzarella, Šunka, Ananás'),

-- Objednávka 5 (ČAKAJÚCA)
(5, 'Primavera Bezlepková', 'Stredná', 10.90, 1,
 'Paradajková omáčka, Mozzarella, Bazalka, Artičoky, Olivy');

-- =========================
-- INFO PRE DEMO
-- =========================
SELECT 'Hotovo! Prihlásenie: admin@pizzeria.sk / password123' AS Info;
