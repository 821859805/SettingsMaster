CREATE TABLE IF NOT EXISTS custom_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL,
    namespace VARCHAR(128) NOT NULL,
    resource_type VARCHAR(64) NOT NULL,
    yaml_content TEXT NOT NULL,
    description VARCHAR(255),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
