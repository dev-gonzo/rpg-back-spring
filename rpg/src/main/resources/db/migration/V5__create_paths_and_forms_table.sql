-- Migration V5: Create tb_paths_and_forms table
-- This migration creates the tb_paths_and_forms table with all magical paths and forms

CREATE TABLE tb_paths_and_forms (
    character_id VARCHAR(255) NOT NULL,
    understand_form INTEGER NOT NULL,
    create_form INTEGER NOT NULL,
    control_form INTEGER NOT NULL,
    fire INTEGER NOT NULL,
    water INTEGER NOT NULL,
    earth INTEGER NOT NULL,
    air INTEGER NOT NULL,
    light INTEGER NOT NULL,
    darkness INTEGER NOT NULL,
    plants INTEGER NOT NULL,
    animals INTEGER NOT NULL,
    humans INTEGER NOT NULL,
    spiritum INTEGER NOT NULL,
    arkanun INTEGER NOT NULL,
    metamagic INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_tb_paths_and_forms PRIMARY KEY (character_id),
    CONSTRAINT fk_tb_paths_and_forms_character FOREIGN KEY (character_id) REFERENCES tb_characters(id) ON DELETE CASCADE
);

-- Create index for better performance
CREATE INDEX idx_tb_paths_and_forms_character_id ON tb_paths_and_forms (character_id);

-- Add comments for documentation
COMMENT ON TABLE tb_paths_and_forms IS 'Table to store character magical paths and forms knowledge';
COMMENT ON COLUMN tb_paths_and_forms.character_id IS 'Foreign key reference to the characters table (one-to-one)';
COMMENT ON COLUMN tb_paths_and_forms.understand_form IS 'Level of understanding form magic';
COMMENT ON COLUMN tb_paths_and_forms.create_form IS 'Level of creating form magic';
COMMENT ON COLUMN tb_paths_and_forms.control_form IS 'Level of controlling form magic';
COMMENT ON COLUMN tb_paths_and_forms.fire IS 'Fire path knowledge level';
COMMENT ON COLUMN tb_paths_and_forms.water IS 'Water path knowledge level';
COMMENT ON COLUMN tb_paths_and_forms.earth IS 'Earth path knowledge level';
COMMENT ON COLUMN tb_paths_and_forms.air IS 'Air path knowledge level';
COMMENT ON COLUMN tb_paths_and_forms.light IS 'Light path knowledge level';
COMMENT ON COLUMN tb_paths_and_forms.darkness IS 'Darkness path knowledge level';
COMMENT ON COLUMN tb_paths_and_forms.plants IS 'Plants path knowledge level';
COMMENT ON COLUMN tb_paths_and_forms.animals IS 'Animals path knowledge level';
COMMENT ON COLUMN tb_paths_and_forms.humans IS 'Humans path knowledge level';
COMMENT ON COLUMN tb_paths_and_forms.spiritum IS 'Spiritum path knowledge level';
COMMENT ON COLUMN tb_paths_and_forms.arkanun IS 'Arkanun path knowledge level';
COMMENT ON COLUMN tb_paths_and_forms.metamagic IS 'Metamagic path knowledge level';
COMMENT ON COLUMN tb_paths_and_forms.created_at IS 'Timestamp when the paths and forms were created';
COMMENT ON COLUMN tb_paths_and_forms.updated_at IS 'Timestamp when the paths and forms were last updated';