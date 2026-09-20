CREATE TABLE product_assets (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    storage_key VARCHAR(500) NOT NULL UNIQUE,

    CONSTRAINT fk_product_assets_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_product_assets_product_id
    ON product_assets(product_id);