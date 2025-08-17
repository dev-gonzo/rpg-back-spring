-- Migration V16: Fix column names in tb_equipments table
-- This migration renames columns to match the entity mapping

ALTER TABLE tb_equipments RENAME COLUMN kinetic_protection TO "kineticProtection";
ALTER TABLE tb_equipments RENAME COLUMN ballistic_protection TO "ballisticProtection";
ALTER TABLE tb_equipments RENAME COLUMN dexterity_penalty TO "dexterityPenalty";
ALTER TABLE tb_equipments RENAME COLUMN agility_penalty TO "agilityPenalty";
ALTER TABLE tb_equipments RENAME COLUMN initiative_penalty TO initiative;

-- Update comments
COMMENT ON COLUMN tb_equipments."kineticProtection" IS 'Kinetic protection value provided by the equipment';
COMMENT ON COLUMN tb_equipments."ballisticProtection" IS 'Ballistic protection value provided by the equipment';
COMMENT ON COLUMN tb_equipments."dexterityPenalty" IS 'Dexterity penalty imposed by the equipment';
COMMENT ON COLUMN tb_equipments."agilityPenalty" IS 'Agility penalty imposed by the equipment';
COMMENT ON COLUMN tb_equipments.initiative IS 'Initiative modifier provided by the equipment';