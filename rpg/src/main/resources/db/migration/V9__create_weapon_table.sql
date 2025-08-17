-- Migration V9: Create tb_weapons table
-- This migration creates the tb_weapons table for storing character weapons

CREATE TABLE tb_weapons (
    id VARCHAR(255) NOT NULL,
    character_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    classification VARCHAR(255) NOT NULL,
    description TEXT,
    damage VARCHAR(255),
    damage_type VARCHAR(255),
    critical_range VARCHAR(255),
    critical_multiplier VARCHAR(255),
    range VARCHAR(255),
    rate_of_fire VARCHAR(255),
    capacity VARCHAR(255),
    weight VARCHAR(255),
    book_page VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_tb_weapons PRIMARY KEY (id),
    CONSTRAINT fk_tb_weapons_character FOREIGN KEY (character_id) REFERENCES tb_characters(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_tb_weapons_character_id ON tb_weapons (character_id);
CREATE INDEX idx_tb_weapons_classification ON tb_weapons (classification);
CREATE INDEX idx_tb_weapons_damage_type ON tb_weapons (damage_type);

-- Add comments for documentation
COMMENT ON TABLE tb_weapons IS 'Table to store character weapons and combat equipment';
COMMENT ON COLUMN tb_weapons.id IS 'Unique identifier for the weapon';
COMMENT ON COLUMN tb_weapons.character_id IS 'Foreign key reference to the characters table';
COMMENT ON COLUMN tb_weapons.name IS 'Name of the weapon';
COMMENT ON COLUMN tb_weapons.quantity IS 'Quantity of this weapon owned';
COMMENT ON COLUMN tb_weapons.classification IS 'Classification or type of weapon';
COMMENT ON COLUMN tb_weapons.description IS 'Detailed description of the weapon';
COMMENT ON COLUMN tb_weapons.damage IS 'Damage value or dice roll for the weapon';
COMMENT ON COLUMN tb_weapons.damage_type IS 'Type of damage dealt by the weapon';
COMMENT ON COLUMN tb_weapons.critical_range IS 'Critical hit range for the weapon';
COMMENT ON COLUMN tb_weapons.critical_multiplier IS 'Critical hit damage multiplier';
COMMENT ON COLUMN tb_weapons.range IS 'Effective range of the weapon';
COMMENT ON COLUMN tb_weapons.rate_of_fire IS 'Rate of fire for ranged weapons';
COMMENT ON COLUMN tb_weapons.capacity IS 'Ammunition capacity for ranged weapons';
COMMENT ON COLUMN tb_weapons.weight IS 'Weight of the weapon';
COMMENT ON COLUMN tb_weapons.book_page IS 'Reference to the book page where this weapon is described';
COMMENT ON COLUMN tb_weapons.created_at IS 'Timestamp when the weapon was created';
COMMENT ON COLUMN tb_weapons.updated_at IS 'Timestamp when the weapon was last updated';