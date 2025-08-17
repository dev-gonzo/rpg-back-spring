-- Migration V4: Create tb_attributes table
-- This migration creates the tb_attributes table with all character attributes and modifiers

CREATE TABLE tb_attributes (
    id VARCHAR(255) NOT NULL,
    character_id VARCHAR(255) NOT NULL UNIQUE,
    con INTEGER NOT NULL,
    fr INTEGER NOT NULL,
    dex INTEGER NOT NULL,
    agi INTEGER NOT NULL,
    int INTEGER NOT NULL,
    will INTEGER NOT NULL,
    per INTEGER NOT NULL,
    car INTEGER NOT NULL,
    con_mod INTEGER,
    fr_mod INTEGER,
    dex_mod INTEGER,
    agi_mod INTEGER,
    int_mod INTEGER,
    will_mod INTEGER,
    per_mod INTEGER,
    car_mod INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_tb_attributes PRIMARY KEY (id),
    CONSTRAINT fk_tb_attributes_character FOREIGN KEY (character_id) REFERENCES tb_characters(id) ON DELETE CASCADE,
    CONSTRAINT uk_tb_attributes_character UNIQUE (character_id)
);

-- Create index for better performance
CREATE INDEX idx_tb_attributes_character_id ON tb_attributes (character_id);

-- Add comments for documentation
COMMENT ON TABLE tb_attributes IS 'Table to store character attributes and their modifiers';
COMMENT ON COLUMN tb_attributes.id IS 'Unique identifier for the attribute record';
COMMENT ON COLUMN tb_attributes.character_id IS 'Foreign key reference to the characters table (one-to-one)';
COMMENT ON COLUMN tb_attributes.con IS 'Constitution attribute value';
COMMENT ON COLUMN tb_attributes.fr IS 'Strength (Força) attribute value';
COMMENT ON COLUMN tb_attributes.dex IS 'Dexterity attribute value';
COMMENT ON COLUMN tb_attributes.agi IS 'Agility attribute value';
COMMENT ON COLUMN tb_attributes.int IS 'Intelligence attribute value';
COMMENT ON COLUMN tb_attributes.will IS 'Will attribute value';
COMMENT ON COLUMN tb_attributes.per IS 'Perception attribute value';
COMMENT ON COLUMN tb_attributes.car IS 'Charisma attribute value';
COMMENT ON COLUMN tb_attributes.con_mod IS 'Constitution modifier';
COMMENT ON COLUMN tb_attributes.fr_mod IS 'Strength modifier';
COMMENT ON COLUMN tb_attributes.dex_mod IS 'Dexterity modifier';
COMMENT ON COLUMN tb_attributes.agi_mod IS 'Agility modifier';
COMMENT ON COLUMN tb_attributes.int_mod IS 'Intelligence modifier';
COMMENT ON COLUMN tb_attributes.will_mod IS 'Will modifier';
COMMENT ON COLUMN tb_attributes.per_mod IS 'Perception modifier';
COMMENT ON COLUMN tb_attributes.car_mod IS 'Charisma modifier';
COMMENT ON COLUMN tb_attributes.created_at IS 'Timestamp when the attribute was created';
COMMENT ON COLUMN tb_attributes.updated_at IS 'Timestamp when the attribute was last updated';