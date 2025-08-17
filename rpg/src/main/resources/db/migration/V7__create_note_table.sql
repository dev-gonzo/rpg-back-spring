-- Migration V7: Create tb_notes table
-- This migration creates the tb_notes table for storing character notes

CREATE TABLE tb_notes (
    id VARCHAR(255) NOT NULL,
    character_id VARCHAR(255) NOT NULL,
    note TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_tb_notes PRIMARY KEY (id),
    CONSTRAINT fk_tb_notes_character FOREIGN KEY (character_id) REFERENCES tb_characters(id) ON DELETE CASCADE
);

-- Create index for better performance
CREATE INDEX idx_tb_notes_character_id ON tb_notes (character_id);

-- Add comments for documentation
COMMENT ON TABLE tb_notes IS 'Table to store character notes and annotations';
COMMENT ON COLUMN tb_notes.id IS 'Unique identifier for the note';
COMMENT ON COLUMN tb_notes.character_id IS 'Foreign key reference to the Character table';
COMMENT ON COLUMN tb_notes.note IS 'Content of the note';
COMMENT ON COLUMN tb_notes.created_at IS 'Timestamp when the note was created';
COMMENT ON COLUMN tb_notes.updated_at IS 'Timestamp when the note was last updated';