-- Adminer 4.8.1 MySQL 8.0.29 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

CREATE TABLE `transaction` (
  `transactionid` int NOT NULL AUTO_INCREMENT,
  `from` varchar(31) NOT NULL,
  `to` varchar(31) NOT NULL,
  `amount` decimal(10,0) NOT NULL,
  `transaction_timestamp` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`transactionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 2022-07-16 21:03:22