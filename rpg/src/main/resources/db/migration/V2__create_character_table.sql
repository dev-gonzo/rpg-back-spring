-- Migration V2: Create tb_characters table
-- This migration creates the tb_characters table for storing character information

CREATE TABLE tb_characters (
    id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    profession VARCHAR(255),
    birth_date DATE NOT NULL,
    birth_place VARCHAR(255),
    gender VARCHAR(255),
    height_cm INTEGER,
    weight_kg INTEGER,
    age INTEGER,
    apparent_age INTEGER,
    religion VARCHAR(255),
    secret_society VARCHAR(255),
    cabala VARCHAR(255),
    rank VARCHAR(255),
    mentor VARCHAR(255),
    hit_points INTEGER,
    current_hit_points INTEGER,
    initiative INTEGER,
    current_initiative INTEGER,
    hero_points INTEGER,
    current_hero_points INTEGER,
    magic_points INTEGER,
    current_magic_points INTEGER,
    faith_points INTEGER,
    current_faith_points INTEGER,
    protection_index INTEGER,
    current_protection_index INTEGER,
    level INTEGER,
    experience_points INTEGER,
    is_known BOOLEAN NOT NULL DEFAULT FALSE,
    control_user_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    image TEXT,
    edit BOOLEAN NOT NULL DEFAULT FALSE,
    
    CONSTRAINT pk_tb_characters PRIMARY KEY (id),
    CONSTRAINT fk_tb_characters_control_user FOREIGN KEY (control_user_id) REFERENCES tb_users(id)
);

-- Create indexes for better performance
CREATE INDEX idx_tb_characters_control_user ON tb_characters (control_user_id);
CREATE INDEX idx_tb_characters_is_known ON tb_characters (is_known);
CREATE INDEX idx_tb_characters_name ON tb_characters (name);

-- Add comments for documentation
COMMENT ON TABLE tb_characters IS 'Table to store character information for the RPG system';
COMMENT ON COLUMN tb_characters.id IS 'Unique identifier for the character';
COMMENT ON COLUMN tb_characters.name IS 'Name of the character';
COMMENT ON COLUMN tb_characters.profession IS 'Character profession or class';
COMMENT ON COLUMN tb_characters.birth_date IS 'Date of birth of the character';
COMMENT ON COLUMN tb_characters.birth_place IS 'Place where the character was born';
COMMENT ON COLUMN tb_characters.gender IS 'Gender of the character';
COMMENT ON COLUMN tb_characters.height_cm IS 'Height of the character in centimeters';
COMMENT ON COLUMN tb_characters.weight_kg IS 'Weight of the character in kilograms';
COMMENT ON COLUMN tb_characters.age IS 'Current age of the character';
COMMENT ON COLUMN tb_characters.apparent_age IS 'Apparent age of the character';
COMMENT ON COLUMN tb_characters.religion IS 'Religious affiliation of the character';
COMMENT ON COLUMN tb_characters.secret_society IS 'Secret society the character belongs to';
COMMENT ON COLUMN tb_characters.cabala IS 'Cabala affiliation';
COMMENT ON COLUMN tb_characters.rank IS 'Rank within organization';
COMMENT ON COLUMN tb_characters.mentor IS 'Character mentor';
COMMENT ON COLUMN tb_characters.hit_points IS 'Maximum hit points';
COMMENT ON COLUMN tb_characters.current_hit_points IS 'Current hit points';
COMMENT ON COLUMN tb_characters.initiative IS 'Maximum initiative';
COMMENT ON COLUMN tb_characters.current_initiative IS 'Current initiative';
COMMENT ON COLUMN tb_characters.hero_points IS 'Maximum hero points';
COMMENT ON COLUMN tb_characters.current_hero_points IS 'Current hero points';
COMMENT ON COLUMN tb_characters.magic_points IS 'Maximum magic points';
COMMENT ON COLUMN tb_characters.current_magic_points IS 'Current magic points';
COMMENT ON COLUMN tb_characters.faith_points IS 'Maximum faith points';
COMMENT ON COLUMN tb_characters.current_faith_points IS 'Current faith points';
COMMENT ON COLUMN tb_characters.protection_index IS 'Maximum protection index';
COMMENT ON COLUMN tb_characters.current_protection_index IS 'Current protection index';
COMMENT ON COLUMN tb_characters.level IS 'Character level';
COMMENT ON COLUMN tb_characters.experience_points IS 'Experience points accumulated';
COMMENT ON COLUMN tb_characters.is_known IS 'Flag indicating if character is known to other players';
COMMENT ON COLUMN tb_characters.control_user_id IS 'ID of the user who controls this character';
COMMENT ON COLUMN tb_characters.created_at IS 'Timestamp when the character was created';
COMMENT ON COLUMN tb_characters.updated_at IS 'Timestamp when the character was last updated';
COMMENT ON COLUMN tb_characters.image IS 'Character image data';
COMMENT ON COLUMN tb_characters.edit IS 'Flag indicating if character can be edited';