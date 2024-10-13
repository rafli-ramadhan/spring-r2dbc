# Spring-r2dbc-postgre-example-crud

### Database table sql

```
CREATE TABLE content (
    id SERIAL NOT NULL,
    content_id VARCHAR(255) UNIQUE NOT NULL,
    content_message VARCHAR(255),
    is_delete BOOLEAN DEFAULT FALSE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE SEQUENCE content_seq START 1;

CREATE OR REPLACE FUNCTION generate_content_id() RETURNS TRIGGER AS $$
DECLARE
    seq_value INTEGER;
BEGIN
    IF NEW.content_id = 'content-' THEN
        seq_value = nextval('content_seq');
        NEW.content_id := NEW.content_id || seq_value;
    ELSE
        RAISE EXCEPTION 'Unknown content type: %', NEW.content_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER content_id_trigger
BEFORE INSERT ON content
FOR EACH ROW
EXECUTE FUNCTION generate_content_id();

select * from content;

insert into content(content_id, content_message, is_delete) values ('content-', 'Thank you for your order! Your order #{order_number} has been confirmed and will be shipped on {shipping_date}.', false);

insert into content(content_id, content_message, is_delete, created_date, updated_date) values ('content-', 'Thank you for your order! Your order #{order_number} has been confirmed and will be shipped on {shipping_date}.', false, NOW(), null);


```
