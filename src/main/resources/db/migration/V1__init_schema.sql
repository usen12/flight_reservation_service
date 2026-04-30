-- Cities
CREATE TABLE tb_cities (
    id           BIGSERIAL PRIMARY KEY,
    city_name    VARCHAR(255) NOT NULL,
    state        VARCHAR(255) NOT NULL,
    lat          NUMERIC      NOT NULL,
    lon          NUMERIC      NOT NULL,
    date_created TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_update  TIMESTAMP
);

CREATE INDEX idx_cities_state_name ON tb_cities (state, city_name);

-- Suppliers
CREATE TABLE tb_suppliers (
    id            BIGSERIAL PRIMARY KEY,
    supplier_name VARCHAR(255) NOT NULL UNIQUE,
    date_created  TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_update   TIMESTAMP
);

-- Planes
CREATE TABLE tb_planes (
    id           BIGSERIAL PRIMARY KEY,
    model        VARCHAR(100) NOT NULL,
    capacity     INTEGER      NOT NULL,
    supplier_id  BIGINT       REFERENCES tb_suppliers (id),
    date_created TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_update  TIMESTAMP
);

-- Wallets
CREATE TABLE tb_wallets (
    id           BIGSERIAL PRIMARY KEY,
    funds        NUMERIC      NOT NULL DEFAULT 0,
    date_created TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_update  TIMESTAMP
);

-- Customers
CREATE TABLE tb_customer (
    id           BIGSERIAL    PRIMARY KEY,
    email        VARCHAR(100) NOT NULL UNIQUE,
    first_name   VARCHAR(100) NOT NULL,
    last_name    VARCHAR(100) NOT NULL,
    patronymic   VARCHAR(100),
    full_name    VARCHAR(255),
    phone_number VARCHAR(50)  NOT NULL,
    wallet_id    BIGINT       NOT NULL UNIQUE REFERENCES tb_wallets (id),
    date_created TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_update  TIMESTAMP
);

-- Flights
CREATE TABLE tb_flights (
    id             BIGSERIAL    PRIMARY KEY,
    flight_date    DATE         NOT NULL,
    departure_time TIME         NOT NULL,
    arrival_time   TIME         NOT NULL,
    price          NUMERIC      NOT NULL,
    city_from_id   BIGINT       REFERENCES tb_cities (id),
    city_to_id     BIGINT       REFERENCES tb_cities (id),
    plane_id       BIGINT       REFERENCES tb_planes (id),
    date_created   TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_update    TIMESTAMP
);

CREATE INDEX idx_flights_date_from_to ON tb_flights (flight_date, city_from_id, city_to_id);

-- Booking (join table)
CREATE TABLE tb_booking (
    flight_id   BIGINT NOT NULL REFERENCES tb_flights (id),
    customer_id BIGINT NOT NULL REFERENCES tb_customer (id),
    PRIMARY KEY (flight_id, customer_id)
);
