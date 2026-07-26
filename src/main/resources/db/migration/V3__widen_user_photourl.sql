-- V3 normalize legacy PostgreSQL schema names and widen user photo_url to text.
-- Older local databases used quoted PascalCase names, for example "UserProfiles".
-- The Spring Boot backend uses standard snake_case identifiers.

DO $$
BEGIN
  IF to_regclass('"Users"') IS NOT NULL AND to_regclass('users') IS NULL THEN
    ALTER TABLE "Users" RENAME TO users;
  END IF;

  IF to_regclass('"UserProfiles"') IS NOT NULL AND to_regclass('user_profiles') IS NULL THEN
    ALTER TABLE "UserProfiles" RENAME TO user_profiles;
  END IF;

  IF to_regclass('"CartRecords"') IS NOT NULL AND to_regclass('cart_records') IS NULL THEN
    ALTER TABLE "CartRecords" RENAME TO cart_records;
  END IF;

  IF to_regclass('"OrderRecords"') IS NOT NULL AND to_regclass('order_records') IS NULL THEN
    ALTER TABLE "OrderRecords" RENAME TO order_records;
  END IF;

  IF to_regclass('"Products"') IS NOT NULL AND to_regclass('products') IS NULL THEN
    ALTER TABLE "Products" RENAME TO products;
  END IF;

  IF to_regclass('"WishlistRecords"') IS NOT NULL AND to_regclass('wishlist_records') IS NULL THEN
    ALTER TABLE "WishlistRecords" RENAME TO wishlist_records;
  END IF;

  IF to_regclass('"PaymentRecords"') IS NOT NULL AND to_regclass('payment_records') IS NULL THEN
    ALTER TABLE "PaymentRecords" RENAME TO payment_records;
  END IF;

  IF to_regclass('"CheckoutAddressRecords"') IS NOT NULL AND to_regclass('checkout_address_records') IS NULL THEN
    ALTER TABLE "CheckoutAddressRecords" RENAME TO checkout_address_records;
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('user_profiles') IS NOT NULL THEN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profiles' AND column_name = 'Uid') THEN
      ALTER TABLE user_profiles RENAME COLUMN "Uid" TO uid;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profiles' AND column_name = 'Email') THEN
      ALTER TABLE user_profiles RENAME COLUMN "Email" TO email;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profiles' AND column_name = 'Role') THEN
      ALTER TABLE user_profiles RENAME COLUMN "Role" TO role;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profiles' AND column_name = 'WalletBalance') THEN
      ALTER TABLE user_profiles RENAME COLUMN "WalletBalance" TO wallet_balance;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profiles' AND column_name = 'DisplayName') THEN
      ALTER TABLE user_profiles RENAME COLUMN "DisplayName" TO display_name;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profiles' AND column_name = 'Phone') THEN
      ALTER TABLE user_profiles RENAME COLUMN "Phone" TO phone;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profiles' AND column_name = 'PhotoUrl') THEN
      ALTER TABLE user_profiles RENAME COLUMN "PhotoUrl" TO photo_url;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profiles' AND column_name = 'PasswordHash') THEN
      ALTER TABLE user_profiles RENAME COLUMN "PasswordHash" TO password_hash;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profiles' AND column_name = 'photo_url') THEN
      ALTER TABLE user_profiles ALTER COLUMN photo_url TYPE text;
    ELSE
      ALTER TABLE user_profiles ADD COLUMN photo_url text;
    END IF;
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('cart_records') IS NOT NULL THEN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cart_records' AND column_name = 'Id') THEN
      ALTER TABLE cart_records RENAME COLUMN "Id" TO id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cart_records' AND column_name = 'Uid') THEN
      ALTER TABLE cart_records RENAME COLUMN "Uid" TO uid;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cart_records' AND column_name = 'ProductId') THEN
      ALTER TABLE cart_records RENAME COLUMN "ProductId" TO product_id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cart_records' AND column_name = 'Quantity') THEN
      ALTER TABLE cart_records RENAME COLUMN "Quantity" TO quantity;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cart_records' AND column_name = 'ProductJson') THEN
      ALTER TABLE cart_records RENAME COLUMN "ProductJson" TO product_json;
    END IF;
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('checkout_address_records') IS NOT NULL THEN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'checkout_address_records' AND column_name = 'Id') THEN
      ALTER TABLE checkout_address_records RENAME COLUMN "Id" TO id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'checkout_address_records' AND column_name = 'Uid') THEN
      ALTER TABLE checkout_address_records RENAME COLUMN "Uid" TO uid;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'checkout_address_records' AND column_name = 'AddressJson') THEN
      ALTER TABLE checkout_address_records RENAME COLUMN "AddressJson" TO address_json;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'checkout_address_records' AND column_name = 'IsDefault') THEN
      ALTER TABLE checkout_address_records RENAME COLUMN "IsDefault" TO is_default;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'checkout_address_records' AND column_name = 'CreatedAt') THEN
      ALTER TABLE checkout_address_records RENAME COLUMN "CreatedAt" TO created_at;
    END IF;
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('order_records') IS NOT NULL THEN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'order_records' AND column_name = 'Id') THEN
      ALTER TABLE order_records RENAME COLUMN "Id" TO id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'order_records' AND column_name = 'Uid') THEN
      ALTER TABLE order_records RENAME COLUMN "Uid" TO uid;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'order_records' AND column_name = 'ItemsJson') THEN
      ALTER TABLE order_records RENAME COLUMN "ItemsJson" TO items_json;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'order_records' AND column_name = 'Amount') THEN
      ALTER TABLE order_records RENAME COLUMN "Amount" TO amount;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'order_records' AND column_name = 'Status') THEN
      ALTER TABLE order_records RENAME COLUMN "Status" TO status;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'order_records' AND column_name = 'PaymentStatus') THEN
      ALTER TABLE order_records RENAME COLUMN "PaymentStatus" TO payment_status;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'order_records' AND column_name = 'PaymentMethod') THEN
      ALTER TABLE order_records RENAME COLUMN "PaymentMethod" TO payment_method;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'order_records' AND column_name = 'CouponCode') THEN
      ALTER TABLE order_records RENAME COLUMN "CouponCode" TO coupon_code;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'order_records' AND column_name = 'PaymentReferenceId') THEN
      ALTER TABLE order_records RENAME COLUMN "PaymentReferenceId" TO payment_reference_id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'order_records' AND column_name = 'SellerIdsJson') THEN
      ALTER TABLE order_records RENAME COLUMN "SellerIdsJson" TO seller_ids_json;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'order_records' AND column_name = 'AddressJson') THEN
      ALTER TABLE order_records RENAME COLUMN "AddressJson" TO address_json;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'order_records' AND column_name = 'CreatedAt') THEN
      ALTER TABLE order_records RENAME COLUMN "CreatedAt" TO created_at;
    END IF;
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('products') IS NOT NULL THEN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'products' AND column_name = 'Id') THEN
      ALTER TABLE products RENAME COLUMN "Id" TO id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'products' AND column_name = 'Name') THEN
      ALTER TABLE products RENAME COLUMN "Name" TO name;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'products' AND column_name = 'Price') THEN
      ALTER TABLE products RENAME COLUMN "Price" TO price;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'products' AND column_name = 'Quantity') THEN
      ALTER TABLE products RENAME COLUMN "Quantity" TO quantity;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'products' AND column_name = 'Category') THEN
      ALTER TABLE products RENAME COLUMN "Category" TO category;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'products' AND column_name = 'Description') THEN
      ALTER TABLE products RENAME COLUMN "Description" TO description;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'products' AND column_name = 'ImageUrl') THEN
      ALTER TABLE products RENAME COLUMN "ImageUrl" TO image_url;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'products' AND column_name = 'SellerId') THEN
      ALTER TABLE products RENAME COLUMN "SellerId" TO seller_id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'products' AND column_name = 'CreatedAt') THEN
      ALTER TABLE products RENAME COLUMN "CreatedAt" TO created_at;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'products' AND column_name = 'UpdatedAt') THEN
      ALTER TABLE products RENAME COLUMN "UpdatedAt" TO updated_at;
    END IF;
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('wishlist_records') IS NOT NULL THEN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'wishlist_records' AND column_name = 'Id') THEN
      ALTER TABLE wishlist_records RENAME COLUMN "Id" TO id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'wishlist_records' AND column_name = 'Uid') THEN
      ALTER TABLE wishlist_records RENAME COLUMN "Uid" TO uid;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'wishlist_records' AND column_name = 'ProductId') THEN
      ALTER TABLE wishlist_records RENAME COLUMN "ProductId" TO product_id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'wishlist_records' AND column_name = 'ProductJson') THEN
      ALTER TABLE wishlist_records RENAME COLUMN "ProductJson" TO product_json;
    END IF;
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('payment_records') IS NOT NULL THEN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'payment_records' AND column_name = 'Id') THEN
      ALTER TABLE payment_records RENAME COLUMN "Id" TO id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'payment_records' AND column_name = 'OrderId') THEN
      ALTER TABLE payment_records RENAME COLUMN "OrderId" TO order_id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'payment_records' AND column_name = 'Uid') THEN
      ALTER TABLE payment_records RENAME COLUMN "Uid" TO uid;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'payment_records' AND column_name = 'PaymentMethod') THEN
      ALTER TABLE payment_records RENAME COLUMN "PaymentMethod" TO payment_method;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'payment_records' AND column_name = 'Status') THEN
      ALTER TABLE payment_records RENAME COLUMN "Status" TO status;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'payment_records' AND column_name = 'ReferenceId') THEN
      ALTER TABLE payment_records RENAME COLUMN "ReferenceId" TO reference_id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'payment_records' AND column_name = 'CreatedAt') THEN
      ALTER TABLE payment_records RENAME COLUMN "CreatedAt" TO created_at;
    END IF;
  END IF;
END $$;
