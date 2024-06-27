
DROP TABLE IF EXISTS delivery_item CASCADE;
DROP TABLE IF EXISTS delivery CASCADE;
DROP TABLE IF EXISTS orderItem CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS supplier CASCADE;

-- ALTER TABLE supplier DROP COLUMN IF EXISTS contact_person;
-- ALTER TABLE supplier DROP COLUMN IF EXISTS phone_number;
--
-- ALTER TABLE ingredient DROP COLUMN IF EXISTS ingredient_number;
-- ALTER TABLE ingredient DROP COLUMN IF EXISTS expiration_date;
--
-- ALTER TABLE orders DROP COLUMN IF EXISTS order_date;

-- Supplier tabel
CREATE TABLE supplier (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          contactPerson VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          phoneNumber VARCHAR(20) NOT NULL
);

-- Ingredient tabel
CREATE TABLE ingredient (
                            id SERIAL PRIMARY KEY,
                            ingredientNumber VARCHAR(50) UNIQUE NOT NULL,
                            name VARCHAR(255) NOT NULL,
                            stock INTEGER NOT NULL,
                            status VARCHAR(50) NOT NULL,
                            supplierId INTEGER REFERENCES supplier(id) ON DELETE SET NULL,
                            expirationDate DATE
);

-- Order tabel
CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        orderDate TIMESTAMP NOT NULL,
                        status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'IN_PROGRESS', 'FULFILLED', 'PARTIALLY_FULFILLED', 'FAILED', 'CANCELLED', 'SHIPPED', 'DELIVERED'))
);

-- Order_item tabel
CREATE TABLE orderItem (
                           id SERIAL PRIMARY KEY,
                           orderId UUID REFERENCES orders(id) ON DELETE CASCADE NOT NULL,
                           ingredientNumber VARCHAR(50) NOT NULL,
                           quantity INTEGER NOT NULL
);

-- Delivery tabel
CREATE TABLE delivery (
                          id SERIAL PRIMARY KEY,
                          orderNumber UUID REFERENCES orders(id) ON DELETE CASCADE NOT NULL,
                          destination VARCHAR(255),
                          deliveryDate TIMESTAMP NOT NULL,
                          status VARCHAR(50) NOT NULL CHECK (status IN ('SUCCESS', 'FAILED'))
);

-- Delivery_item tabel
CREATE TABLE delivery_item (
                               id SERIAL PRIMARY KEY,
                               delivery_id INTEGER REFERENCES delivery(id) ON DELETE CASCADE NOT NULL,
                               ingredientNumber VARCHAR(50) NOT NULL,
                               quantity INTEGER NOT NULL
);