-- Migration V12: Create tb_relevant_people table
-- This migration creates the tb_relevant_people table for storing character relevant people

CREATE TABLE tb_relevant_people (
    id VARCHAR(255) NOT NULL,
    character_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_tb_relevant_people PRIMARY KEY (id),
    CONSTRAINT fk_tb_relevant_people_character FOREIGN KEY (character_id) REFERENCES tb_characters(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_tb_relevant_people_character_id ON tb_relevant_people (character_id);
CREATE INDEX idx_tb_relevant_people_name ON tb_relevant_people (name);

-- Add comments for documentation
COMMENT ON TABLE tb_relevant_people IS 'Table to store relevant people associated with characters';
COMMENT ON COLUMN tb_relevant_people.id IS 'Unique identifier for the relevant person';
COMMENT ON COLUMN tb_relevant_people.character_id IS 'Foreign key reference to the characters table';
COMMENT ON COLUMN tb_relevant_people.name IS 'Name of the relevant person';
COMMENT ON COLUMN tb_relevant_people.description IS 'Description or relationship details of the relevant person';
COMMENT ON COLUMN tb_relevant_people.created_at IS 'Timestamp when the relevant person was created';
COMMENT ON COLUMN tb_relevant_people.updated_at IS 'Timestamp when the relevant person was last updated';