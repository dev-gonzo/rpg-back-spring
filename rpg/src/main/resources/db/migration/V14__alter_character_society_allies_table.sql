-- Migration V14: Alter tb_character_society_allies table structure
-- This migration changes the table from many-to-many relationship to element collection

-- Drop existing constraints and indexes
ALTER TABLE tb_character_society_allies DROP CONSTRAINT IF EXISTS fk_tb_character_society_allies_ally;
ALTER TABLE tb_character_society_allies DROP CONSTRAINT IF EXISTS chk_tb_character_society_allies_not_self;
DROP INDEX IF EXISTS idx_tb_character_society_allies_ally_id;

-- Rename ally_character_id column to value
ALTER TABLE tb_character_society_allies RENAME COLUMN ally_character_id TO "value";

-- Update primary key constraint
ALTER TABLE tb_character_society_allies DROP CONSTRAINT pk_tb_character_society_allies;
ALTER TABLE tb_character_society_allies ADD CONSTRAINT pk_tb_character_society_allies PRIMARY KEY (character_id, "value");

-- Update comments
COMMENT ON TABLE tb_character_society_allies IS 'Table for storing character society allies as a collection of strings';
COMMENT ON COLUMN tb_character_society_allies."value" IS 'Society ally name or identifier';