DROP TABLE IF EXISTS urls CASCADE;

CREATE TABLE urls (
    id bigint GENERATED ALWAYS AS IDENTITY,
    name varchar(255) NOT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS url_checks CASCADE;

CREATE TABLE url_checks (
    id bigint GENERATED ALWAYS AS IDENTITY,
    url_id bigint NOT NULL,
    status_code int NOT NULL,
    title varchar(255),
    h1 varchar(255),
    description text,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);