-- Migration V17: Add missing columns to tb_improvements table
-- This migration adds the cost and kit_value columns that are missing from the table

ALTER TABLE tb_improvements ADD COLUMN cost INTEGER NOT NULL DEFAULT 0;
ALTER TABLE tb_improvements ADD COLUMN kit_value INTEGER NOT NULL DEFAULT 0;

-- Remove the description column as it's not in the entity
ALTER TABLE tb_improvements DROP COLUMN IF EXISTS description;

-- Update comments
COMMENT ON COLUMN tb_improvements.cost IS 'Cost of the improvement in experience points';
COMMENT ON COLUMN tb_improvements.kit_value IS 'Kit value associated with the improvement';