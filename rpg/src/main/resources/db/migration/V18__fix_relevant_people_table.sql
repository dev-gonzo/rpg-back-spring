-- Migration V18: Fix tb_relevant_people table structure
-- This migration adds missing columns and removes unused ones to match the entity

-- Add missing columns
ALTER TABLE tb_relevant_people ADD COLUMN category VARCHAR(255) NOT NULL DEFAULT 'Unknown';
ALTER TABLE tb_relevant_people ADD COLUMN apparent_age INTEGER;
ALTER TABLE tb_relevant_people ADD COLUMN city VARCHAR(255);
ALTER TABLE tb_relevant_people ADD COLUMN profession VARCHAR(255);
ALTER TABLE tb_relevant_people ADD COLUMN brief_description TEXT;
ALTER TABLE tb_relevant_people ADD COLUMN is_public BOOLEAN NOT NULL DEFAULT false;

-- Remove the description column as it's replaced by brief_description
ALTER TABLE tb_relevant_people DROP COLUMN IF EXISTS description;

-- Update comments
COMMENT ON COLUMN tb_relevant_people.category IS 'Category or type of the relevant person';
COMMENT ON COLUMN tb_relevant_people.apparent_age IS 'Apparent age of the relevant person';
COMMENT ON COLUMN tb_relevant_people.city IS 'City where the relevant person lives';
COMMENT ON COLUMN tb_relevant_people.profession IS 'Profession of the relevant person';
COMMENT ON COLUMN tb_relevant_people.brief_description IS 'Brief description of the relevant person';
COMMENT ON COLUMN tb_relevant_people.is_public IS 'Whether this person information is public';