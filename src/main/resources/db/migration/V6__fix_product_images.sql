-- Migration: V6__fix_product_images.sql
-- Purpose: Replace missing SVG product images with available PNG/webp images

-- First, replace all SVG images with default Watch.png
UPDATE products 
SET image_url = '/uploads/products/Watch.png' 
WHERE image_url LIKE '%.svg%';

-- Map products by category and name to appropriate images
UPDATE products 
SET image_url = '/uploads/products/Lamp.png'
WHERE (name ILIKE '%lamp%' OR name ILIKE '%light%' OR category ILIKE '%HOME%');

UPDATE products 
SET image_url = '/uploads/products/Shirt.png'
WHERE (category ILIKE '%FASHION%' OR name ILIKE '%shirt%' OR name ILIKE '%cloth%');

UPDATE products 
SET image_url = '/uploads/products/Bottel.png'
WHERE (category ILIKE '%GROCERY%' OR name ILIKE '%bottle%' OR name ILIKE '%drink%');

UPDATE products 
SET image_url = '/uploads/products/Book.png'
WHERE (category ILIKE '%BOOKS%' OR name ILIKE '%book%' OR name ILIKE '%notebook%');

UPDATE products 
SET image_url = '/uploads/products/Earphone.png'
WHERE (name ILIKE '%earphone%' OR name ILIKE '%earbud%');

UPDATE products 
SET image_url = '/uploads/products/Headphone.png'
WHERE (name ILIKE '%headphone%' OR name ILIKE '%headset%');

UPDATE products 
SET image_url = '/uploads/products/Bag.png'
WHERE (name ILIKE '%bag%' OR name ILIKE '%backpack%');

UPDATE products 
SET image_url = '/uploads/products/Coffee.png'
WHERE (name ILIKE '%coffee%' OR name ILIKE '%brew%');

UPDATE products 
SET image_url = '/uploads/products/yoga-mat.webp'
WHERE (name ILIKE '%yoga%' OR name ILIKE '%mat%');

UPDATE products 
SET image_url = '/uploads/products/bowl-set.webp'
WHERE (name ILIKE '%bowl%' OR name ILIKE '%ceramic%');
