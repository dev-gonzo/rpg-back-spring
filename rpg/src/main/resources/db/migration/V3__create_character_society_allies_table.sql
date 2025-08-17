-- Migration V3: Create tb_character_society_allies table
-- This migration creates the tb_character_society_allies junction table for many-to-many relationship

CREATE TABLE tb_character_society_allies (
    character_id VARCHAR(255) NOT NULL,
    "value" VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_tb_character_society_allies PRIMARY KEY (character_id, "value"),
    CONSTRAINT fk_tb_character_society_allies_character FOREIGN KEY (character_id) REFERENCES tb_characters(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_tb_character_society_allies_character_id ON tb_character_society_allies (character_id);

-- Add comments for documentation
COMMENT ON TABLE tb_character_society_allies IS 'Table for storing character society allies as a collection of strings';
COMMENT ON COLUMN tb_character_society_allies.character_id IS 'Foreign key reference to the main character';
COMMENT ON COLUMN tb_character_society_allies."value" IS 'Society ally name or identifier';
COMMENT ON COLUMN tb_character_society_allies.created_at IS 'Timestamp when the relationship was created';
COMMENT ON COLUMN tb_character_society_allies.updated_at IS 'Timestamp when the relationship was last updated';