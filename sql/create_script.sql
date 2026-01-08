SET FOREIGN_KEY_CHECKS = 0;

-- Vymazanie existujucich tabuliek (ak existuju)
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

-- Tabulka USERS
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
    role ENUM('ZAKAZNIK', 'KUCHAR', 'KURIER', 'ADMIN') NOT NULL DEFAULT 'ZAKAZNIK',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

-- Tabulka TAGS
CREATE TABLE tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    color VARCHAR(7) DEFAULT '#6c757d',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

-- Tabulka INGREDIENTS
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

-- Tabulka PIZZAS
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

-- Tabulka PIZZA_SIZES
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

-- Tabulka PIZZA_INGREDIENTS (M:N)
CREATE TABLE pizza_ingredients (
    pizza_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    PRIMARY KEY (pizza_id, ingredient_id),
    FOREIGN KEY (pizza_id) REFERENCES pizzas(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

-- Tabulka PIZZA_TAGS (M:N)
CREATE TABLE pizza_tags (
    pizza_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (pizza_id, tag_id),
    FOREIGN KEY (pizza_id) REFERENCES pizzas(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

-- Tabulka ORDERS
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status ENUM('CAKAJUCA', 'PRIPRAVUJE_SA', 'HOTOVA', 'DORUCUJE_SA', 'DORUCENA', 'ZRUSENA') NOT NULL DEFAULT 'CAKAJUCA',
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

-- Tabulka ORDER_ITEMS
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

-- Tabulka CART_ITEMS
CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    pizza_size_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (pizza_size_id) REFERENCES pizza_sizes(id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    UNIQUE KEY unique_user_size (user_id, pizza_size_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

SELECT 'Tabulky boli uspesne vytvorene!' AS Status;
