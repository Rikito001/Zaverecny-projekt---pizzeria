-- =========================
-- VYTVORENIE DATABÁZY
-- =========================
CREATE DATABASE IF NOT EXISTS pizzeria_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_slovak_ci;

USE pizzeria_db;

-- =========================
-- DROP EXISTUJÚCICH TABULIEK
-- =========================
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS pizza_tags;
DROP TABLE IF EXISTS pizza_ingredients;
DROP TABLE IF EXISTS pizza_sizes;
DROP TABLE IF EXISTS pizzas;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- =========================
-- CREATE TABLES
-- =========================

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(64) NOT NULL,
                       last_name VARCHAR(64) NOT NULL,
                       phone VARCHAR(20),
                       address VARCHAR(255),
                       city VARCHAR(100),
                       postal_code VARCHAR(10),
                       profile_image VARCHAR(255),
                       role ENUM('ZAKAZNIK','KUCHAR','KURIER','ADMIN') NOT NULL DEFAULT 'ZAKAZNIK',
                       active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       INDEX idx_email (email),
                       INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

CREATE TABLE tags (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE,
                      color VARCHAR(7) DEFAULT '#6c757d',
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

CREATE TABLE ingredients (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(100) NOT NULL UNIQUE,
                             description VARCHAR(255),
                             price_extra DECIMAL(10,2) DEFAULT 0.00,
                             allergens VARCHAR(255),
                             available BOOLEAN NOT NULL DEFAULT TRUE,
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

CREATE TABLE pizzas (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        slug VARCHAR(120) NOT NULL UNIQUE,
                        description VARCHAR(500),
                        image_url VARCHAR(255),
                        active BOOLEAN NOT NULL DEFAULT TRUE,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        INDEX idx_slug (slug),
                        INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

CREATE TABLE pizza_sizes (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             pizza_id BIGINT NOT NULL,
                             size_name VARCHAR(50) NOT NULL,
                             price DECIMAL(10,2) NOT NULL,
                             diameter_cm INT,
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (pizza_id) REFERENCES pizzas(id) ON DELETE CASCADE,
                             INDEX idx_pizza (pizza_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

CREATE TABLE pizza_ingredients (
                                   pizza_id BIGINT NOT NULL,
                                   ingredient_id BIGINT NOT NULL,
                                   PRIMARY KEY (pizza_id, ingredient_id),
                                   FOREIGN KEY (pizza_id) REFERENCES pizzas(id) ON DELETE CASCADE,
                                   FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

CREATE TABLE pizza_tags (
                            pizza_id BIGINT NOT NULL,
                            tag_id BIGINT NOT NULL,
                            PRIMARY KEY (pizza_id, tag_id),
                            FOREIGN KEY (pizza_id) REFERENCES pizzas(id) ON DELETE CASCADE,
                            FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        status ENUM('CAKAJUCA','PRIPRAVUJE_SA','HOTOVA','DORUCUJE_SA','DORUCENA','ZRUSENA')
        NOT NULL DEFAULT 'CAKAJUCA',
                        total_price DECIMAL(10,2) NOT NULL,
                        delivery_address VARCHAR(255) NOT NULL,
                        delivery_city VARCHAR(100) NOT NULL,
                        delivery_phone VARCHAR(20) NOT NULL,
                        note VARCHAR(500),
                        cook_id BIGINT,
                        courier_id BIGINT,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        FOREIGN KEY (cook_id) REFERENCES users(id),
                        FOREIGN KEY (courier_id) REFERENCES users(id),
                        INDEX idx_user (user_id),
                        INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

CREATE TABLE order_items (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             pizza_name VARCHAR(100) NOT NULL,
                             size_name VARCHAR(50) NOT NULL,
                             unit_price DECIMAL(10,2) NOT NULL,
                             quantity INT NOT NULL,
                             ingredients VARCHAR(1000),
                             FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                             INDEX idx_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

CREATE TABLE cart_items (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id BIGINT NOT NULL,
                            pizza_size_id BIGINT NOT NULL,
                            quantity INT NOT NULL DEFAULT 1,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (pizza_size_id) REFERENCES pizza_sizes(id) ON DELETE CASCADE,
                            UNIQUE KEY unique_user_size (user_id, pizza_size_id),
                            INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

-- =========================
-- DEMO DATA
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

INSERT INTO users
(id,email,password,first_name,last_name,phone,address,city,postal_code,role,active)
VALUES
    (1,'admin@pizzeria.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Admin','Pizzeria','+421900111222','Hlavna 1','Bratislava','81101','ADMIN',TRUE),

    (2,'kuchar@pizzeria.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Mario','Rossi','+421900222333','Kuchynska 5','Bratislava','81102','KUCHAR',TRUE),

    (3,'kurier@pizzeria.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Luigi','Bianchi','+421900333444','Rozvozova 10','Bratislava','81103','KURIER',TRUE),

    (4,'jan.novak@email.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Jan','Novak','+421901234567','Dubova 15','Bratislava','82108','ZAKAZNIK',TRUE),

    (5,'maria.horvat@email.sk',
     '$2a$10$uVGEY6J3drkCcw/fVbh1UebdobNOPFZi34aG0CFfkJJQnWvahwjlK',
     'Maria','Horvat','+421902345678','Lipova 22','Kosice','04001','ZAKAZNIK',TRUE);

INSERT INTO orders
(user_id,status,total_price,delivery_address,delivery_city,delivery_phone,note,cook_id,courier_id)
VALUES
    (4,'DORUCENA',19.00,'Dubova 15','Bratislava','+421901234567','Prosim zazvonit.',2,3),
    (5,'PRIPRAVUJE_SA',22.50,'Lipova 22','Kosice','+421902345678',NULL,2,NULL),
    (4,'CAKAJUCA',16.00,'Dubova 15','Bratislava','+421901234567','Bez cibule prosim.',NULL,NULL);

INSERT INTO order_items
(order_id,pizza_name,size_name,unit_price,quantity,ingredients)
VALUES
    (1,'Margherita','Stredna',8.50,1,'Paradajkova omacka, Mozzarella, Bazalka'),
    (1,'Prosciutto','Velka',11.00,1,'Paradajkova omacka, Mozzarella, Sunka'),
    (2,'Quattro Formaggi','Velka',12.50,1,'Paradajkova omacka, Mozzarella, Parmezan, Gorgonzola, Feta'),
    (2,'Diavola','Stredna',9.50,1,'Paradajkova omacka, Mozzarella, Salama, Jalapeno, Cesnak'),
    (3,'Funghi','Stredna',8.50,1,'Paradajkova omacka, Mozzarella, Sampiony'),
    (3,'Veggie','Mala',6.50,1,'Paradajkova omacka, Mozzarella, Paprika, Cibula, Kukurica, Olivy');

SELECT 'DB FULL IMPORT HOTOVÝ – admin@pizzeria.sk / password123' AS Info;
