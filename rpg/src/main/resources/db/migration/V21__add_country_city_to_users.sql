-- Migration V2: Add country and city columns to tb_users table

ALTER TABLE tb_users 
ADD COLUMN country VARCHAR(100),
ADD COLUMN city VARCHAR(100);