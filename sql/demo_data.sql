-- PIZZERIA demo data
-- Spustite tento skript az po create_script.sql!!!

-- POUZIVATELIA
-- Heslo pre vsetkych je: password123
-- Hash je pre BCrypt
INSERT INTO users (email, password, first_name, last_name, phone, address, city, postal_code, role, active) VALUES
('admin@pizzeria.sk', '$2a$10$N.zmdr9k7uOCQb386QhXpu5j0PVnGRBAWvoL3.X3rNmsYhHEa0Y8O', 'Admin', 'Pizzeria', '+421900111222', 'Hlavna 1', 'Bratislava', '81101', 'ADMIN', TRUE),
('kuchar@pizzeria.sk', '$2a$10$N.zmdr9k7uOCQb386QhXpu5j0PVnGRBAWvoL3.X3rNmsYhHEa0Y8O', 'Mario', 'Rossi', '+421900222333', 'Kuchynska 5', 'Bratislava', '81102', 'KUCHAR', TRUE),
('kurier@pizzeria.sk', '$2a$10$N.zmdr9k7uOCQb386QhXpu5j0PVnGRBAWvoL3.X3rNmsYhHEa0Y8O', 'Luigi', 'Bianchi', '+421900333444', 'Rozvozova 10', 'Bratislava', '81103', 'KURIER', TRUE),
('jan.novak@email.sk', '$2a$10$N.zmdr9k7uOCQb386QhXpu5j0PVnGRBAWvoL3.X3rNmsYhHEa0Y8O', 'Jan', 'Novak', '+421901234567', 'Dubova 15', 'Bratislava', '82108', 'ZAKAZNIK', TRUE),
('maria.horvat@email.sk', '$2a$10$N.zmdr9k7uOCQb386QhXpu5j0PVnGRBAWvoL3.X3rNmsYhHEa0Y8O', 'Maria', 'Horvat', '+421902345678', 'Lipova 22', 'Kosice', '04001', 'ZAKAZNIK', TRUE);

-- TAGY
INSERT INTO tags (name, color) VALUES
('Vegetarianska', '#4caf50'),
('Pikantna', '#f44336'),
('Oblubena', '#ff9800'),
('Novinka', '#2196f3'),
('Morske plody', '#00bcd4'),
('Klasika', '#795548');

-- INGREDIENCIE
INSERT INTO ingredients (name, description, allergens, available) VALUES
('Paradajkova omacka', 'Domaca paradajkova omacka', NULL, TRUE),
('Mozzarella', 'Talianska mozzarella', '7', TRUE),
('Bazalka', 'Cerstva bazalka', NULL, TRUE),
('Sunka', 'Dusena sunka', NULL, TRUE),
('Salama', 'Pikantna salama', NULL, TRUE),
('Sampiony', 'Cerstve sampiony', NULL, TRUE),
('Paprika', 'Cerstva paprika', NULL, TRUE),
('Olivy', 'Cierne olivy', NULL, TRUE),
('Kukurica', 'Sladka kukurica', NULL, TRUE),
('Cibula', 'Cervena cibula', NULL, TRUE),
('Cesnak', 'Cerstvy cesnak', NULL, TRUE),
('Ancovicky', 'Solene ancovicky', '4', TRUE),
('Tuniak', 'Tuniak v oleji', '4', TRUE),
('Krevety', 'Cerstve krevety', '2', TRUE),
('Rukola', 'Cerstva rukola', NULL, TRUE),
('Parmezan', 'Taliansky parmezan', '7', TRUE),
('Gorgonzola', 'Syr gorgonzola', '7', TRUE),
('Feta', 'Grecky syr feta', '7', TRUE),
('Kuracie maso', 'Grilovane kuracie maso', NULL, TRUE),
('Slanina', 'Oprazena slanina', NULL, TRUE),
('Jalapeño', 'Pikantne jalapeño', NULL, TRUE);

-- PIZZA
INSERT INTO pizzas (name, slug, description, image_url, active) VALUES
('Margherita', 'margherita', 'Klasicka talianska pizza s paradajkovou omackou, mozzarellou a cerstvou bazalkou.', '/images/margherita.jpg', TRUE),
('Prosciutto', 'prosciutto', 'Pizza so sunkou, mozzarellou a paradajkovou omackou.', '/images/prosciutto.jpg', TRUE),
('Salami', 'salami', 'Pikantna pizza s kabanosi a mozzarellou.', '/images/salami.jpg', TRUE),
('Quattro Formaggi', 'quattro-formaggi', 'Pizza so styrmi druhmi syra - mozzarella, gorgonzola, parmezan a feta.', '/images/quattro-formaggi.jpg', TRUE),
('Funghi', 'funghi', 'Vegetarianska pizza s cerstvymi sampionmi a mozzarellou.', '/images/funghi.jpg', TRUE),
('Capricciosa', 'capricciosa', 'Pizza so sunkou, sampionmi, artycokmi a olivami.', '/images/capricciosa.jpg', TRUE),
('Tonno', 'tonno', 'Pizza s tuniakom, cibulou a olivami.', '/images/tonno.jpg', TRUE),
('Diavola', 'diavola', 'Pikantna pizza s kabanosi, jalapeño a cesnakom.', '/images/diavola.jpg', TRUE),
('Marinara', 'marinara', 'Pizza s krevetami, musalami a ancovickami.', '/images/marinara.jpg', TRUE),
('Hawaii', 'hawaii', 'Pizza so sunkou a ananasom.', '/images/hawaii.jpg', TRUE),
('Veggie', 'veggie', 'Vegetarianska pizza s paprikou, cibulou, kukuricou a olivami.', '/images/veggie.jpg', TRUE),
('Pollo', 'pollo', 'Pizza s grilovanym kuracim masom, paprikou a rukolou.', '/images/pollo.jpg', TRUE);

-- VELKOSTI PIZZ
-- Margherita
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
(1, 'Mala', 6.50, 26),
(1, 'Stredna', 8.50, 32),
(1, 'Velka', 10.50, 40);

-- Prosciutto
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
(2, 'Mala', 7.00, 26),
(2, 'Stredna', 9.00, 32),
(2, 'Velka', 11.00, 40);

-- Salami
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
(3, 'Mala', 7.00, 26),
(3, 'Stredna', 9.00, 32),
(3, 'Velka', 11.00, 40);

-- Quattro Formaggi
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
(4, 'Mala', 8.00, 26),
(4, 'Stredna', 10.00, 32),
(4, 'Velka', 12.50, 40);

-- Funghi
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
(5, 'Mala', 6.50, 26),
(5, 'Stredna', 8.50, 32),
(5, 'Velka', 10.50, 40);

-- Capricciosa
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
(6, 'Mala', 7.50, 26),
(6, 'Stredna', 9.50, 32),
(6, 'Velka', 11.50, 40);

-- Tonno
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
(7, 'Mala', 7.50, 26),
(7, 'Stredna', 9.50, 32),
(7, 'Velka', 11.50, 40);

-- Diavola
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
(8, 'Mala', 7.50, 26),
(8, 'Stredna', 9.50, 32),
(8, 'Velka', 11.50, 40);

-- Marinara
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
(9, 'Mala', 9.00, 26),
(9, 'Stredna', 11.50, 32),
(9, 'Velka', 14.00, 40);

-- Hawaii
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
(10, 'Mala', 7.00, 26),
(10, 'Stredna', 9.00, 32),
(10, 'Velka', 11.00, 40);

-- Veggie
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
(11, 'Mala', 6.50, 26),
(11, 'Stredna', 8.50, 32),
(11, 'Velka', 10.50, 40);

-- Pollo
INSERT INTO pizza_sizes (pizza_id, size_name, price, diameter_cm) VALUES
(12, 'Mala', 8.00, 26),
(12, 'Stredna', 10.00, 32),
(12, 'Velka', 12.00, 40);

-- PIZZA - INGREDIENCIE vazieb
-- Margherita: paradajkova omacka, mozzarella, bazalka
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES (1, 1), (1, 2), (1, 3);

-- Prosciutto: paradajkova omacka, mozzarella, sunka
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES (2, 1), (2, 2), (2, 4);

-- Salami: paradajkova omacka, mozzarella, salama
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES (3, 1), (3, 2), (3, 5);

-- Quattro Formaggi: paradajkova omacka, mozzarella, parmezan, gorgonzola, feta
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES (4, 1), (4, 2), (4, 16), (4, 17), (4, 18);

-- Funghi: paradajkova omacka, mozzarella, sampiony
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES (5, 1), (5, 2), (5, 6);

-- Capricciosa: paradajkova omacka, mozzarella, sunka, sampiony, olivy
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES (6, 1), (6, 2), (6, 4), (6, 6), (6, 8);

-- Tonno: paradajkova omacka, mozzarella, tuniak, cibula, olivy
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES (7, 1), (7, 2), (7, 13), (7, 10), (7, 8);

-- Diavola: paradajkova omacka, mozzarella, salama, jalapeno, cesnak
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES (8, 1), (8, 2), (8, 5), (8, 21), (8, 11);

-- Marinara: paradajkova omacka, mozzarella, krevety, ancovicky, cesnak
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES (9, 1), (9, 2), (9, 14), (9, 12), (9, 11);

-- Hawaii: paradajkova omacka, mozzarella, sunka (ananás nie je v ingredients)
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES (10, 1), (10, 2), (10, 4);

-- Veggie: paradajkova omacka, mozzarella, paprika, cibula, kukurica, olivy
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES (11, 1), (11, 2), (11, 7), (11, 10), (11, 9), (11, 8);

-- Pollo: paradajkova omacka, mozzarella, kuracie maso, paprika, rukola
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) VALUES (12, 1), (12, 2), (12, 19), (12, 7), (12, 15);

-- PIZZA - TAGY vazieb
-- Margherita: Klasika, Vegetarianska
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (1, 6), (1, 1);

-- Prosciutto: Klasika
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (2, 6);

-- Salami: Pikantna
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (3, 2);

-- Quattro Formaggi: Oblubena, Vegetarianska
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (4, 3), (4, 1);

-- Funghi: Vegetarianska
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (5, 1);

-- Capricciosa: Oblubena
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (6, 3);

-- Tonno: Morske plody
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (7, 5);

-- Diavola: Pikantna, Novinka
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (8, 2), (8, 4);

-- Marinara: Morske plody
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (9, 5);

-- Hawaii: Klasika
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (10, 6);

-- Veggie: Vegetarianska, Novinka
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (11, 1), (11, 4);

-- Pollo: Oblubena
INSERT INTO pizza_tags (pizza_id, tag_id) VALUES (12, 3);

-- DUMMY OBJEDNAVKY
INSERT INTO orders (user_id, status, total_price, delivery_address, delivery_city, delivery_phone, note, cook_id, courier_id) VALUES
(4, 'DORUCENA', 19.00, 'Dubova 15', 'Bratislava', '+421901234567', 'Prosim zazvonit.', 2, 3),
(5, 'PRIPRAVUJE_SA', 22.50, 'Lipova 22', 'Kosice', '+421902345678', NULL, 2, NULL),
(4, 'CAKAJUCA', 16.00, 'Dubova 15', 'Bratislava', '+421901234567', 'Bez cibule prosim.', NULL, NULL);

-- Polozky objednavok
INSERT INTO order_items (order_id, pizza_name, size_name, unit_price, quantity, ingredients) VALUES
(1, 'Margherita', 'Stredna', 8.50, 1, 'Paradajkova omacka, Mozzarella, Bazalka'),
(1, 'Prosciutto', 'Velka', 11.00, 1, 'Paradajkova omacka, Mozzarella, Sunka'),
(2, 'Quattro Formaggi', 'Velka', 12.50, 1, 'Paradajkova omacka, Mozzarella, Parmezan, Gorgonzola, Feta'),
(2, 'Diavola', 'Stredna', 9.50, 1, 'Paradajkova omacka, Mozzarella, Salama, Jalapeño, Cesnak'),
(3, 'Funghi', 'Stredna', 8.50, 1, 'Paradajkova omacka, Mozzarella, Sampiony'),
(3, 'Veggie', 'Mala', 6.50, 1, 'Paradajkova omacka, Mozzarella, Paprika, Cibula, Kukurica, Olivy');

-- Done
SELECT 'Demo data boli uspesne vlozene!' AS Status;
SELECT 'Prihlasovacie udaje:' AS Info;
SELECT 'admin@pizzeria.sk / password123 (ADMIN)' AS Login1;
SELECT 'kuchar@pizzeria.sk / password123 (KUCHAR)' AS Login2;
SELECT 'kurier@pizzeria.sk / password123 (KURIER)' AS Login3;
SELECT 'jan.novak@email.sk / password123 (ZAKAZNIK)' AS Login4;
