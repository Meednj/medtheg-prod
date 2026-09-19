CREATE TABLE product_categories (
    product_id UUID NOT NULL,
    category VARCHAR(50) NOT NULL,

    PRIMARY KEY (product_id, category),

    CONSTRAINT fk_product_categories_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
        ON DELETE CASCADE
);