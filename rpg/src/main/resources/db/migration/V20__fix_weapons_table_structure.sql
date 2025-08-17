-- Migration V20: Fix tb_weapons table structure to match WeaponEntity
-- This migration removes unused columns and adds missing ones

-- Remove columns that don't exist in the entity
ALTER TABLE tb_weapons DROP COLUMN IF EXISTS quantity;
ALTER TABLE tb_weapons DROP COLUMN IF EXISTS classification;
ALTER TABLE tb_weapons DROP COLUMN IF EXISTS damage_type;
ALTER TABLE tb_weapons DROP COLUMN IF EXISTS critical_range;
ALTER TABLE tb_weapons DROP COLUMN IF EXISTS critical_multiplier;
ALTER TABLE tb_weapons DROP COLUMN IF EXISTS rate_of_fire;
ALTER TABLE tb_weapons DROP COLUMN IF EXISTS capacity;
ALTER TABLE tb_weapons DROP COLUMN IF EXISTS weight;

-- Add missing columns from the entity
ALTER TABLE tb_weapons ADD COLUMN initiative INTEGER NOT NULL DEFAULT 0;
ALTER TABLE tb_weapons ADD COLUMN rof VARCHAR(255);
ALTER TABLE tb_weapons ADD COLUMN ammunition VARCHAR(255);

-- Update existing columns to match entity constraints
ALTER TABLE tb_weapons ALTER COLUMN damage SET NOT NULL;

-- Update comments
COMMENT ON COLUMN tb_weapons.initiative IS 'Initiative modifier provided by the weapon';
COMMENT ON COLUMN tb_weapons.rof IS 'Rate of fire for the weapon';
COMMENT ON COLUMN tb_weapons.ammunition IS 'Ammunition type used by the weapon';