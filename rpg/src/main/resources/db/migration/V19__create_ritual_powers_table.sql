-- Migration V19: Create tb_ritual_powers table
-- This migration creates the tb_ritual_powers table for storing character ritual powers

CREATE TABLE tb_ritual_powers (
    id VARCHAR(255) NOT NULL,
    character_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    paths_forms VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    book_page VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_tb_ritual_powers PRIMARY KEY (id),
    CONSTRAINT fk_tb_ritual_powers_character FOREIGN KEY (character_id) REFERENCES tb_characters(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_tb_ritual_powers_character_id ON tb_ritual_powers (character_id);
CREATE INDEX idx_tb_ritual_powers_name ON tb_ritual_powers (name);

-- Add comments for documentation
COMMENT ON TABLE tb_ritual_powers IS 'Table to store character ritual powers';
COMMENT ON COLUMN tb_ritual_powers.id IS 'Unique identifier for the ritual power';
COMMENT ON COLUMN tb_ritual_powers.character_id IS 'Foreign key reference to the characters table';
COMMENT ON COLUMN tb_ritual_powers.name IS 'Name of the ritual power';
COMMENT ON COLUMN tb_ritual_powers.paths_forms IS 'Paths and forms associated with the ritual power';
COMMENT ON COLUMN tb_ritual_powers.description IS 'Detailed description of the ritual power';
COMMENT ON COLUMN tb_ritual_powers.book_page IS 'Reference to the book page where this ritual power is described';
COMMENT ON COLUMN tb_ritual_powers.created_at IS 'Timestamp when the ritual power was created';
COMMENT ON COLUMN tb_ritual_powers.updated_at IS 'Timestamp when the ritual power was last updated';