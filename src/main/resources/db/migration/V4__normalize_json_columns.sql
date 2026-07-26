-- V4 ensure JSON payload columns use PostgreSQL jsonb.

DO $$
BEGIN
  IF to_regclass('cart_records') IS NOT NULL THEN
    ALTER TABLE cart_records
      ALTER COLUMN product_json TYPE jsonb USING product_json::jsonb;
  END IF;

  IF to_regclass('order_records') IS NOT NULL THEN
    ALTER TABLE order_records
      ALTER COLUMN items_json TYPE jsonb USING items_json::jsonb,
      ALTER COLUMN seller_ids_json TYPE jsonb USING seller_ids_json::jsonb,
      ALTER COLUMN address_json TYPE jsonb USING address_json::jsonb;
  END IF;

  IF to_regclass('wishlist_records') IS NOT NULL THEN
    ALTER TABLE wishlist_records
      ALTER COLUMN product_json TYPE jsonb USING product_json::jsonb;
  END IF;

  IF to_regclass('checkout_address_records') IS NOT NULL THEN
    ALTER TABLE checkout_address_records
      ALTER COLUMN address_json TYPE jsonb USING address_json::jsonb;
  END IF;
END $$;
