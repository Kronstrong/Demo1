CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 100;
DROP TABLE IF EXISTS shop_item;
DROP SEQUENCE IF EXISTS shop_item_key;
CREATE SEQUENCE shop_item_key;
CREATE TABLE shop_item (
    id INTEGER PRIMARY KEY NOT NULL DEFAULT nextval('shop_item_key'),
    name VARCHAR(256) NOT NULL
);
ALTER SEQUENCE shop_item_key OWNED BY shop_item.id;
INSERT INTO
    shop_item (name)
VALUES
    ('Телевизор'),
    ('Смартфон'),
    ('Соковыжималка'),
    ('Наушники'),
    ('Клавиатура');

DROP TABLE IF EXISTS purchase_item;
DROP SEQUENCE IF EXISTS purchase_item_key;
CREATE SEQUENCE purchase_item_key;
CREATE TABLE purchase_item (
    id INTEGER PRIMARY KEY NOT NULL DEFAULT nextval('purchase_item_key'),
    name VARCHAR(256) NOT NULL,
    lastname VARCHAR(256) NOT NULL,
    age INTEGER NOT NULL,
    purchase_item INTEGER NOT NULL REFERENCES shop_item(id),
    count INTEGER NOT NULL,
    amount FLOAT NOT NULL,
    purchase_date DATE NOT NULL DEFAULT CURRENT_DATE
);

-- Демонстрационные данные
DELETE FROM purchase_item;
INSERT INTO
    purchase_item(name, lastname, age, purchase_item, count, amount, purchase_date)
VALUES
    ('Михаил', 'Ильменский', 24, 1, 1, 20000.0, CURRENT_DATE),
    ('Михаил', 'Ильменский', 24, 5, 1, 999.99, CURRENT_DATE),
    ('Иван', 'Иванов', 18, 5, 1, 999.99, CURRENT_DATE),
    ('Иван', 'Иванов', 18, 5, 1, 999.99, '2013-10-08'),
    ('Иван', 'Иванов', 18, 5, 1, 999.99, '2019-11-11');
