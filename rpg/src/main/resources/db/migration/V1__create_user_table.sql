-- Migration V1: Create tb_users table
-- This migration creates the tb_users table for storing system users

CREATE TABLE tb_users (
    id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_master BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_tb_users PRIMARY KEY (id),
    CONSTRAINT uk_tb_users_email UNIQUE (email)
);

-- Create indexes for better performance
CREATE INDEX idx_tb_users_email ON tb_users (email);
CREATE INDEX idx_tb_users_is_master ON tb_users (is_master);

-- Add comments for documentation
COMMENT ON TABLE tb_users IS 'Table to store system users and their authentication information';
COMMENT ON COLUMN tb_users.id IS 'Unique identifier for the user';
COMMENT ON COLUMN tb_users.name IS 'Full name of the user';
COMMENT ON COLUMN tb_users.email IS 'Email address used for authentication (unique)';
COMMENT ON COLUMN tb_users.password IS 'Encrypted password for authentication';
COMMENT ON COLUMN tb_users.is_master IS 'Flag indicating if the user has master/admin privileges';
COMMENT ON COLUMN tb_users.created_at IS 'Timestamp when the user was created';
COMMENT ON COLUMN tb_users.updated_at IS 'Timestamp when the user was last updated';