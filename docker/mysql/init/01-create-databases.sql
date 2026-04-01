-- MySQL 컨테이너 최초 기동 시 한 번 실행 (볼륨이 비어 있을 때)
CREATE DATABASE IF NOT EXISTS ticketdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS reservationdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS paymentdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'ticketing'@'%' IDENTIFIED BY 'ticketing';
GRANT ALL PRIVILEGES ON ticketdb.* TO 'ticketing'@'%';
GRANT ALL PRIVILEGES ON reservationdb.* TO 'ticketing'@'%';
GRANT ALL PRIVILEGES ON paymentdb.* TO 'ticketing'@'%';
FLUSH PRIVILEGES;
