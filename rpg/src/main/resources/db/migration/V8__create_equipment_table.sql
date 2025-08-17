-- Migration V8: Create tb_equipment table
-- This migration creates the tb_equipment table for storing character equipment

CREATE TABLE tb_equipment (
    id VARCHAR(255) NOT NULL,
    character_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    classification VARCHAR(255),
    description TEXT,
    kinetic_protection INTEGER DEFAULT 0,
    ballistic_protection INTEGER DEFAULT 0,
    dexterity_penalty INTEGER DEFAULT 0,
    agility_penalty INTEGER DEFAULT 0,
    initiative_penalty INTEGER DEFAULT 0,
    book_page VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_tb_equipment PRIMARY KEY (id),
    CONSTRAINT fk_tb_equipment_character FOREIGN KEY (character_id) REFERENCES tb_characters(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_tb_equipment_character_id ON tb_equipment (character_id);
CREATE INDEX idx_tb_equipment_name ON tb_equipment (name);
CREATE INDEX idx_tb_equipment_classification ON tb_equipment (classification);

-- Add comments for documentation
COMMENT ON TABLE tb_equipment IS 'Table to store character equipment and gear';
COMMENT ON COLUMN tb_equipment.id IS 'Unique identifier for the equipment';
COMMENT ON COLUMN tb_equipment.character_id IS 'Foreign key reference to the character';
COMMENT ON COLUMN tb_equipment.name IS 'Name of the equipment';
COMMENT ON COLUMN tb_equipment.quantity IS 'Quantity of this equipment item';
COMMENT ON COLUMN tb_equipment.classification IS 'Classification or type of equipment';
COMMENT ON COLUMN tb_equipment.description IS 'Detailed description of the equipment';
COMMENT ON COLUMN tb_equipment.kinetic_protection IS 'Kinetic protection value provided by the equipment';
COMMENT ON COLUMN tb_equipment.ballistic_protection IS 'Ballistic protection value provided by the equipment';
COMMENT ON COLUMN tb_equipment.dexterity_penalty IS 'Dexterity penalty imposed by the equipment';
COMMENT ON COLUMN tb_equipment.agility_penalty IS 'Agility penalty imposed by the equipment';
COMMENT ON COLUMN tb_equipment.initiative_penalty IS 'Initiative penalty imposed by the equipment';
COMMENT ON COLUMN tb_equipment.book_page IS 'Reference to the book page where this equipment is described';
COMMENT ON COLUMN tb_equipment.created_at IS 'Timestamp when the equipment was created';
COMMENT ON COLUMN tb_equipment.updated_at IS 'Timestamp when the equipment was last updated';