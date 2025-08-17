-- Migration V13: Create tb_improvements table
-- This migration creates the tb_improvements table for storing character improvements

CREATE TABLE tb_improvements (
    id VARCHAR(255) NOT NULL,
    character_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    book_page VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_tb_improvements PRIMARY KEY (id),
    CONSTRAINT fk_tb_improvements_character FOREIGN KEY (character_id) REFERENCES tb_characters(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_tb_improvements_character_id ON tb_improvements (character_id);
CREATE INDEX idx_tb_improvements_name ON tb_improvements (name);

-- Add comments for documentation
COMMENT ON TABLE tb_improvements IS 'Table to store character improvements and enhancements';
COMMENT ON COLUMN tb_improvements.id IS 'Unique identifier for the improvement';
COMMENT ON COLUMN tb_improvements.character_id IS 'Foreign key reference to the characters table';
COMMENT ON COLUMN tb_improvements.name IS 'Name of the improvement';
COMMENT ON COLUMN tb_improvements.description IS 'Detailed description of the improvement';
COMMENT ON COLUMN tb_improvements.book_page IS 'Reference to the book page where this improvement is described';
COMMENT ON COLUMN tb_improvements.created_at IS 'Timestamp when the improvement was created';
COMMENT ON COLUMN tb_improvements.updated_at IS 'Timestamp when the improvement was last updated';