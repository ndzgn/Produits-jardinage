DROP TABLE IF EXISTS product;

CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(20) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    description VARCHAR(500),
    active INT NOT NULL,
    creation_date DATE DEFAULT CURRENT_DATE
);