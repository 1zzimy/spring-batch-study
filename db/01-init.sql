-- 메타 DB
CREATE DATABASE IF NOT EXISTS meta_db
  CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

-- 데이터 DB
CREATE DATABASE IF NOT EXISTS data_db
  CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

-- 사용자 계정 (이미 있으면 스킵)
CREATE USER IF NOT EXISTS 'user'@'%' IDENTIFIED BY 'password';

-- 권한 부여
GRANT ALL PRIVILEGES ON meta_db.* TO 'user'@'%';
GRANT ALL PRIVILEGES ON data_db.* TO 'user'@'%';

FLUSH PRIVILEGES;
