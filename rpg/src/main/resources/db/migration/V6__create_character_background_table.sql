-- Migration V6: Create tb_character_backgrounds table
-- This migration creates the tb_character_backgrounds table to store character background stories

CREATE TABLE tb_character_backgrounds (
    id VARCHAR(255) NOT NULL,
    character_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_tb_character_backgrounds PRIMARY KEY (id),
    CONSTRAINT fk_tb_character_backgrounds_character FOREIGN KEY (character_id) REFERENCES tb_characters(id) ON DELETE CASCADE
);

-- Create index for better performance
CREATE INDEX idx_tb_character_backgrounds_character_id ON tb_character_backgrounds (character_id);
CREATE INDEX idx_tb_character_backgrounds_is_public ON tb_character_backgrounds (is_public);
CREATE INDEX idx_tb_character_backgrounds_title ON tb_character_backgrounds (title);

-- Add comments for documentation
COMMENT ON TABLE tb_character_backgrounds IS 'Table to store character background stories and information';
COMMENT ON COLUMN tb_character_backgrounds.id IS 'Primary key for the character background';
COMMENT ON COLUMN tb_character_backgrounds.character_id IS 'Foreign key reference to the characters table';
COMMENT ON COLUMN tb_character_backgrounds.title IS 'Title or name of the background entry';
COMMENT ON COLUMN tb_character_backgrounds.text IS 'The background story or information text';
COMMENT ON COLUMN tb_character_backgrounds.is_public IS 'Whether this background information is visible to other players';
COMMENT ON COLUMN tb_character_backgrounds.created_at IS 'Timestamp when the background was created';
COMMENT ON COLUMN tb_character_backgrounds.updated_at IS 'Timestamp when the background was last updated';