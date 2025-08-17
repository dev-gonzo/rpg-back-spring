-- Migration V15: Rename tb_equipment table to tb_equipments
-- This migration renames the table to match the entity mapping

ALTER TABLE tb_equipment RENAME TO tb_equipments;

-- Update indexes
ALTER INDEX idx_tb_equipment_character_id RENAME TO idx_tb_equipments_character_id;
ALTER INDEX idx_tb_equipment_name RENAME TO idx_tb_equipments_name;
ALTER INDEX idx_tb_equipment_classification RENAME TO idx_tb_equipments_classification;

-- Update constraints
ALTER TABLE tb_equipments RENAME CONSTRAINT pk_tb_equipment TO pk_tb_equipments;
ALTER TABLE tb_equipments RENAME CONSTRAINT fk_tb_equipment_character TO fk_tb_equipments_character;

-- Update comments
COMMENT ON TABLE tb_equipments IS 'Table to store character equipment and gear';