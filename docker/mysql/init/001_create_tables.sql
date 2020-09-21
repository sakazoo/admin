use test;
CREATE TABLE IF NOT EXISTS m_user
(
    user_id VARCHAR(50),
    password VARCHAR(255),
    pass_update_date DATETIME,
    login_miss_times INT,
    `unlock` BOOLEAN,
    tenant_id VARCHAR(50),
    user_name VARCHAR(50),
    mail_address VARCHAR(50),
    enabled BOOLEAN,
    user_due_date DATETIME,
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS m_role
(
    role_id VARCHAR(50),
    role_name VARCHAR(100),
    PRIMARY KEY (role_id)
);

CREATE TABLE IF NOT EXISTS t_user_role
(
    id INT,
    user_id VARCHAR(50),
    role_id VARCHAR(50),
    PRIMARY KEY (id)
);