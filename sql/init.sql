-- Badran Store PostgreSQL initialization script.
-- This script creates the schema expected by the Spring Boot application.

CREATE TABLE IF NOT EXISTS roles (
    role_id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    permissions JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL REFERENCES roles(role_id),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255),
    password_hash VARCHAR(255) NOT NULL,
    preferred_language VARCHAR(255) NOT NULL,
    password_reset_token UUID,
    password_reset_expires_at TIMESTAMPTZ,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS addresses (
    address_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    label VARCHAR(255),
    city VARCHAR(255) NOT NULL,
    zone VARCHAR(255) NOT NULL,
    address_line VARCHAR(255) NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS brands (
    brand_id BIGSERIAL PRIMARY KEY,
    name_ar VARCHAR(255) NOT NULL,
    name_en VARCHAR(255) NOT NULL,
    logo_url VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
    category_id BIGSERIAL PRIMARY KEY,
    name_ar VARCHAR(255) NOT NULL,
    name_en VARCHAR(255) NOT NULL,
    parent_category_id BIGINT REFERENCES categories(category_id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
    product_id BIGSERIAL PRIMARY KEY,
    sku VARCHAR(255) NOT NULL UNIQUE,
    name_ar VARCHAR(255) NOT NULL,
    name_en VARCHAR(255) NOT NULL,
    description_ar TEXT,
    description_en TEXT,
    category_id BIGINT NOT NULL REFERENCES categories(category_id),
    brand_id BIGINT REFERENCES brands(brand_id),
    base_price NUMERIC(19, 2) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    reorder_threshold INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_on_sale BOOLEAN NOT NULL DEFAULT FALSE,
    is_new_arrival BOOLEAN NOT NULL DEFAULT FALSE,
    avg_rating NUMERIC(19, 2) NOT NULL DEFAULT 0,
    review_count INTEGER NOT NULL DEFAULT 0,
    specifications JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product_images (
    image_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    url VARCHAR(255) NOT NULL,
    sort_order INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS cart (
    cart_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    session_id VARCHAR(255),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart_items (
    cart_item_id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL REFERENCES cart(cart_id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS coupons (
    coupon_id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(255) NOT NULL,
    value NUMERIC(19, 2) NOT NULL,
    valid_from DATE NOT NULL,
    valid_to DATE NOT NULL,
    usage_limit INTEGER,
    usage_count INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS orders (
    order_id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    order_number VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT,
    guest_name VARCHAR(255),
    guest_phone VARCHAR(255),
    guest_email VARCHAR(255),
    fulfillment_method VARCHAR(255) NOT NULL,
    delivery_address_id BIGINT,
    delivery_city VARCHAR(255),
    delivery_zone VARCHAR(255),
    delivery_address_line VARCHAR(255),
    delivery_fee NUMERIC(19, 2) NOT NULL DEFAULT 0,
    status VARCHAR(255) NOT NULL,
    subtotal NUMERIC(19, 2) NOT NULL,
    coupon_id BIGINT REFERENCES coupons(coupon_id),
    discount_amount NUMERIC(19, 2) NOT NULL DEFAULT 0,
    total NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items (
    order_item_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(19, 2) NOT NULL,
    line_total NUMERIC(19, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS payments (
    payment_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    method VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    transaction_ref VARCHAR(255),
    receipt_url VARCHAR(255),
    verified_by_user_id BIGINT,
    verified_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reviews (
    review_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(product_id),
    user_id BIGINT,
    order_id BIGINT NOT NULL,
    rating SMALLINT NOT NULL,
    comment TEXT,
    status VARCHAR(255) NOT NULL DEFAULT 'pending_moderation',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS wishlist (
    wishlist_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL REFERENCES products(product_id),
    added_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_wishlist_user_product UNIQUE (user_id, product_id)
);

CREATE INDEX IF NOT EXISTS idx_users_role_id ON users(role_id);
CREATE INDEX IF NOT EXISTS idx_addresses_user_id ON addresses(user_id);
CREATE INDEX IF NOT EXISTS idx_categories_parent_id ON categories(parent_category_id);
CREATE INDEX IF NOT EXISTS idx_products_category_id ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_products_brand_id ON products(brand_id);
CREATE INDEX IF NOT EXISTS idx_product_images_product_id ON product_images(product_id);
CREATE INDEX IF NOT EXISTS idx_cart_user_id ON cart(user_id);
CREATE INDEX IF NOT EXISTS idx_cart_session_id ON cart(session_id);
CREATE INDEX IF NOT EXISTS idx_cart_items_cart_product ON cart_items(cart_id, product_id);
CREATE INDEX IF NOT EXISTS idx_orders_user_id ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_payments_order_id ON payments(order_id);
CREATE INDEX IF NOT EXISTS idx_reviews_product_status ON reviews(product_id, status);
CREATE INDEX IF NOT EXISTS idx_wishlist_user_id ON wishlist(user_id);

INSERT INTO roles (role_name, description, permissions)
VALUES
    ('admin', 'Administrator role with operational access', '{"scope":"admin"}'::jsonb),
    ('customer', 'Customer role for store shoppers', '{"scope":"customer"}'::jsonb)
ON CONFLICT (role_name) DO NOTHING;

INSERT INTO brands (name_ar, name_en, logo_url)
VALUES ('باداران', 'Badran', NULL)
ON CONFLICT DO NOTHING;

INSERT INTO categories (name_ar, name_en, parent_category_id)
VALUES ('مستلزمات الغسيل', 'Washing Supplies', NULL)
ON CONFLICT DO NOTHING;

INSERT INTO products (
    sku,
    name_ar,
    name_en,
    description_ar,
    description_en,
    category_id,
    brand_id,
    base_price,
    stock_quantity,
    reorder_threshold,
    specifications
)
SELECT
    'CW-SHAMPOO-001',
    'شامبو سيارات فاخر',
    'Premium Car Shampoo',
    'شامبو مركز لتنظيف السيارات',
    'Concentrated shampoo for exterior car cleaning.',
    c.category_id,
    b.brand_id,
    35.00,
    100,
    10,
    '{"volume":"1L","scent":"citrus"}'::jsonb
FROM categories c
LEFT JOIN brands b ON b.name_en = 'Badran'
WHERE c.name_en = 'Washing Supplies'
ON CONFLICT (sku) DO NOTHING;
