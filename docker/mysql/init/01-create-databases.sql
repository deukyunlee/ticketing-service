-- MySQL 컨테이너 최초 기동 시 한 번 실행 (볼륨이 비어 있을 때)

CREATE DATABASE IF NOT EXISTS ticketdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS reservationdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS paymentdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'ticket_svc'@'%' IDENTIFIED BY 'ticket_pass';
GRANT ALL PRIVILEGES ON ticketdb.* TO 'ticket_svc'@'%';

CREATE USER IF NOT EXISTS 'reservation_svc'@'%' IDENTIFIED BY 'reservation_pass';
GRANT ALL PRIVILEGES ON reservationdb.* TO 'reservation_svc'@'%';

CREATE USER IF NOT EXISTS 'payment_svc'@'%' IDENTIFIED BY 'payment_pass';
GRANT ALL PRIVILEGES ON paymentdb.* TO 'payment_svc'@'%';

FLUSH PRIVILEGES;
