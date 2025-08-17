-- Migration V10: Create tb_skills table
-- This migration creates the tb_skills table for storing character skills

CREATE TABLE tb_skills (
    id VARCHAR(255) NOT NULL,
    character_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    skill_value INTEGER NOT NULL,
    book_page VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_tb_skills PRIMARY KEY (id),
    CONSTRAINT fk_tb_skills_character FOREIGN KEY (character_id) REFERENCES tb_characters(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_tb_skills_character_id ON tb_skills (character_id);
CREATE INDEX idx_tb_skills_name ON tb_skills (name);
CREATE INDEX idx_tb_skills_value ON tb_skills (skill_value);

-- Add comments for documentation
COMMENT ON TABLE tb_skills IS 'Table to store character skills and their values';
COMMENT ON COLUMN tb_skills.id IS 'Unique identifier for the skill';
COMMENT ON COLUMN tb_skills.character_id IS 'Foreign key reference to the characters table';
COMMENT ON COLUMN tb_skills.name IS 'Name of the skill';
COMMENT ON COLUMN tb_skills.skill_value IS 'Current value or level of the skill';
COMMENT ON COLUMN tb_skills.book_page IS 'Reference to the book page where this skill is described';
COMMENT ON COLUMN tb_skills.created_at IS 'Timestamp when the skill was created';
COMMENT ON COLUMN tb_skills.updated_at IS 'Timestamp when the skill was last updated';