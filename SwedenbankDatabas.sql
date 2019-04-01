-- --------------------------------------------------------
-- Värd:                         127.0.0.1
-- Serverversion:                5.7.25-log - MySQL Community Server (GPL)
-- Server-OS:                    Win64
-- HeidiSQL Version:             10.1.0.5464
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumpar databasstruktur för swedenbank
CREATE DATABASE IF NOT EXISTS `swedenbank` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_swedish_ci */;
USE `swedenbank`;

-- Dumpar struktur för händelse swedenbank.3ec07ac8ad17452f894a133ee8f3b1ad
DELIMITER //
CREATE DEFINER=`root`@`localhost` EVENT `3ec07ac8ad17452f894a133ee8f3b1ad` ON SCHEDULE EVERY 1 MONTH STARTS '2019-03-23 00:00:00' ENDS '2019-03-24 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
		 INSERT INTO transactions SET transaction_name = 'Överföring',`account` = '123.123.123-2',
		  `type` = 'Utgående', transaction_ammount = -1.0; 
		     
		  INSERT INTO transactions SET transaction_name = 'Överföring',`account` = '234.123.433-3',
		  `type` = 'Inkommande', transaction_ammount = 1.0;   
	END//
DELIMITER ;

-- Dumpar struktur för händelse swedenbank.70a052ecf24546668f353d1f5366ff8b
DELIMITER //
CREATE DEFINER=`root`@`localhost` EVENT `70a052ecf24546668f353d1f5366ff8b` ON SCHEDULE EVERY 1 MONTH STARTS '2019-03-23 00:00:00' ENDS '2019-03-24 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
		 INSERT INTO transactions SET transaction_name = 'jkhb',`account` = '234.123.433-3',
		  `type` = 'Utgående', transaction_ammount = -89.0; 
		     
		  INSERT INTO transactions SET transaction_name = 'jkhb',`account` = '123.123.123-2',
		  `type` = 'Inkommande', transaction_ammount = 89.0;   
	END//
DELIMITER ;

-- Dumpar struktur för händelse swedenbank.a2753df37931414abbb31884e33e9629
DELIMITER //
CREATE DEFINER=`root`@`localhost` EVENT `a2753df37931414abbb31884e33e9629` ON SCHEDULE AT '2019-03-28 01:00:00' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
		 INSERT INTO transactions SET transaction_name = 'f',`account` = '123.123.123-2',
		  `type` = 'Utgående', transaction_ammount = -32.0; 
		     
		  INSERT INTO transactions SET transaction_name = 'f',`account` = '232-2323',
		  `type` = 'Inkommande', transaction_ammount = 32.0;   
	END//
DELIMITER ;

-- Dumpar struktur för vy swedenbank.accountinformation
-- Skapar temporärtabell för att hantera VIEW-beroendefel
CREATE TABLE `accountinformation` (
	`id` INT(11) UNSIGNED NOT NULL,
	`account_number` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_swedish_ci',
	`account_name` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_swedish_ci',
	`type` VARCHAR(50) NULL COLLATE 'utf8mb4_swedish_ci',
	`balance` DOUBLE NOT NULL,
	`social_number` VARCHAR(13) NOT NULL COLLATE 'utf8mb4_swedish_ci',
	`first_name` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_swedish_ci',
	`last_name` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_swedish_ci'
) ENGINE=MyISAM;

-- Dumpar struktur för tabell swedenbank.accounts
CREATE TABLE IF NOT EXISTS `accounts` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account_number` varchar(50) COLLATE utf8mb4_swedish_ci NOT NULL DEFAULT '0',
  `account_name` varchar(50) COLLATE utf8mb4_swedish_ci NOT NULL DEFAULT '0',
  `balance` double unsigned NOT NULL DEFAULT '0',
  `type` varchar(50) COLLATE utf8mb4_swedish_ci DEFAULT 'Ingen Koppling',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_number` (`account_number`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_swedish_ci;

-- Dumpar data för tabell swedenbank.accounts: ~5 rows (ungefär)
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` (`id`, `account_number`, `account_name`, `balance`, `type`) VALUES
	(1, '123.123.123-2', 'Godiskontot', 1992116, 'Kortkonto'),
	(3, '123-1238', 'Visa', 480, 'Företagskonto'),
	(4, '2322-2322', 'Företagskonto', 1e21, 'Företagskonto'),
	(18, '243.123.543-1', 'Bilkonto', 19056, 'Ingen Koppling'),
	(19, '1234-5678', 'Volvo', 35, 'Företagskonto');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;

-- Dumpar struktur för procedur swedenbank.add_card_transaction
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_card_transaction`(
	IN `a_user` VARCHAR(50),
	IN `t_amount` DOUBLE

,
	OUT `result` INT
)
BEGIN
		DECLARE sender_number VARCHAR(50);
		DECLARE does_exist INTEGER;
		SELECT COUNT(*) FROM accountinformation WHERE social_number = a_user AND `type` = 'Kortkonto' INTO does_exist;
		IF does_exist > 0 THEN
		SET sender_number = (SELECT account_number FROM accountinformation WHERE a_user = social_number AND `type` = 'Kortkonto');
        INSERT INTO transactions SET transaction_name = 'Kortbetalning',`account` = sender_number,
		  `type` = 'Utgående', transaction_ammount = -t_amount; 
		     
		  INSERT INTO transactions SET transaction_name = 'Kortbetalning',`account` = '111.111.111-1',
		  `type` = 'Inkommande', transaction_ammount = t_amount;     
		  SET result = 1;
		ELSE
			SET result = 0;
		END IF;
   END//
DELIMITER ;

-- Dumpar struktur för procedur swedenbank.add_employee_salary
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_employee_salary`(
	IN `e_number` VARCHAR(50),
	IN `t_amount` DOUBLE,
	IN `t_sender` VARCHAR(50),
	IN `t_salary_date` DATE
,
	OUT `result` INT

)
BEGIN
		DECLARE does_exist INTEGER;
		SELECT COUNT(*) FROM users WHERE social_number = e_number INTO does_exist;
		IF does_exist > 0 THEN
			IF EXISTS (SELECT social_number FROM salarylist WHERE social_number = e_number) THEN
				SET result = 2;
			ELSE
	        INSERT INTO salarylist SET social_number = e_number ,amount = t_amount,
			  `type` = 'Lön', transaction_date = t_salary_date, sender_account = t_Sender;  
			  	SET result = 1;
		  	END IF;
		 ELSE
		 	SET result = 0;   
		END IF; 
   END//
DELIMITER ;

-- Dumpar struktur för procedur swedenbank.add_event_info
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_event_info`(e_eventID VARCHAR(50), e_userID VARCHAR(50))
BEGIN
        INSERT INTO `events` SET eventID = e_eventID , userID = e_userID;
   END//
DELIMITER ;

-- Dumpar struktur för procedur swedenbank.add_transaction
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_transaction`(
	IN `t_name` VARCHAR(50),
	IN `t_sender` VARCHAR(50),
	IN `t_reciever` VARCHAR(50),
	IN `t_amount` DOUBLE


,
	OUT `result` INT
)
BEGIN
		IF EXISTS(SELECT id FROM accounts WHERE account_number = t_reciever) THEN
        INSERT INTO transactions SET transaction_name = t_name,`account` = t_sender,
		  `type` = 'Utgående', transaction_ammount = -t_amount; 
		     
		  INSERT INTO transactions SET transaction_name = t_name,`account` = t_reciever,
		  `type` = 'Inkommande', transaction_ammount = t_amount;  
		  SET result = 1; 
		  ELSE
		  	SET result = 0;  
		  	END IF;
   END//
DELIMITER ;

-- Dumpar struktur för procedur swedenbank.adjust_account_balance
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `adjust_account_balance`(
	IN `a_number` VARCHAR(50),
	IN `a_amount` DOUBLE




)
BEGIN
    	DECLARE tupe VARCHAR(50);
    	SET tupe = (SELECT `type` FROM transactions WHERE `account` = a_number ORDER BY id DESC LIMIT 1 );
    	IF (tupe = 'Inkommande') THEN
    		UPDATE accounts SET balance = balance + a_amount WHERE accounts.account_number = a_number;
    	ELSEIF (tupe = 'Utgående') THEN
    		UPDATE accounts SET balance = balance + a_amount WHERE accounts.account_number = a_number;
    	END IF;
    END//
DELIMITER ;

-- Dumpar struktur för procedur swedenbank.change_account_name
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `change_account_name`(a_number VARCHAR(50), a_name VARCHAR(50))
BEGIN
	UPDATE accounts SET account_name = a_name WHERE account_number = a_number;
	END//
DELIMITER ;

-- Dumpar struktur för procedur swedenbank.change_account_type
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `change_account_type`(
	IN `a_type` VARCHAR(50),
	IN `a_number` VARCHAR(50),
	IN `a_owner` VARCHAR(50),
	OUT `result` INTEGER




)
BEGIN
	DECLARE my_type INTEGER;
	IF a_type = 'Ingen Koppling' THEN
		UPDATE accounts SET `type` = a_type WHERE account_number = a_number;
			SET result = 1;
	ELSE
		SELECT COUNT(*) FROM accountinformation WHERE `type` = a_type AND social_number = a_owner INTO my_type;
		IF my_type < 1 THEN
			UPDATE accounts SET `type` = a_type WHERE account_number = a_number;
			SET result = 1;
		ELSE
				SET result = 0;
		END IF;
	END IF;
	END//
DELIMITER ;

-- Dumpar struktur för procedur swedenbank.create_account
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `create_account`(
	IN `a_name` VARCHAR(50),
	IN `a_user` VARCHAR(50)




)
BEGIN
	DECLARE first_numbers INTEGER;
	DECLARE second_numbers INTEGER;
	DECLARE third_numbers INTEGER;
	DECLARE fourth_numbers INTEGER;
	DECLARE generated_account_num VARCHAR(50);
	DECLARE new_account_id INTEGER;
	DECLARE user_id INTEGER;
	SELECT FLOOR(RAND()* 899) + 100 INTO first_numbers;
	SELECT FLOOR(RAND()* 899) + 100 INTO second_numbers;
	SELECT FLOOR(RAND()* 899) + 100 INTO third_numbers;
	SELECT FLOOR(RAND()* 9) + 1 INTO fourth_numbers;
	SELECT CONCAT(first_numbers,'.',second_numbers,'.',third_numbers,'-',fourth_numbers) INTO generated_account_num;
	WHILE EXISTS(SELECT account_number FROM accounts WHERE account_number = generated_account_num) DO
		SELECT FLOOR(RAND()* 899) + 100 INTO first_numbers;
		SELECT FLOOR(RAND()* 899) + 100 INTO second_numbers;
		SELECT FLOOR(RAND()* 899) + 100 INTO third_numbers;
		SELECT FLOOR(RAND()* 9) + 1 INTO fourth_numbers;
		SELECT CONCAT(first_numbers,'.',second_numbers,'.',third_numbers,'-',fourth_numbers) INTO generated_account_num;
	END WHILE;
	INSERT INTO accounts (account_number,account_name,`type`)
	VALUES(generated_account_num, a_name,'Ingen Koppling');
	SELECT id FROM accounts WHERE account_number = generated_account_num INTO new_account_id;
	SELECT id FROM users WHERE social_number = a_user INTO user_id;
	INSERT INTO userxaccount (user_id,account_id) VALUES (user_id,new_account_id);
	END//
DELIMITER ;

-- Dumpar struktur för procedur swedenbank.delete_account
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_account`(
	IN `a_number` VARCHAR(50),
	OUT `result` INTEGER
,
	IN `input_user_id` VARCHAR(50)
)
BEGIN
	DECLARE a_balance DOUBLE;
	DECLARE my_account_id VARCHAR(50);
	DECLARE my_user_id VARCHAR(50);
	SELECT id FROM users WHERE social_number = input_user_id INTO my_user_id;
	SELECT id FROM accounts WHERE account_number = a_number INTO my_account_id; 
	SELECT balance FROM accounts WHERE account_number = a_number INTO a_balance;
	IF a_balance > 0 THEN
		SET result = 0;
	ELSE
		DELETE FROM accounts WHERE account_number = a_number;
		DELETE FROM userxaccount WHERE user_id = my_user_id AND account_id = my_account_id;
		SET result = 1;
	END IF;
	END//
DELIMITER ;

-- Dumpar struktur för tabell swedenbank.events
CREATE TABLE IF NOT EXISTS `events` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `eventID` varchar(50) COLLATE utf8mb4_swedish_ci NOT NULL,
  `userID` varchar(50) COLLATE utf8mb4_swedish_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_swedish_ci;

-- Dumpar data för tabell swedenbank.events: ~6 rows (ungefär)
/*!40000 ALTER TABLE `events` DISABLE KEYS */;
INSERT INTO `events` (`id`, `eventID`, `userID`) VALUES
	(1, '70a052ecf24546668f353d1f5366ff8b', '8902149867'),
	(2, '3ec07ac8ad17452f894a133ee8f3b1ad', '8902149867'),
	(3, 'bf413ef593f840408aff78dbb8d27f3d', '8902149867'),
	(4, 'f184d2782f104fe7a52a839cddce968f', '8902149867'),
	(5, '412fc08ad1f74d2a95ef00d68a20c8ff', '8902149867'),
	(6, '0c804a763d59469d867915b0f05399a3', '8902149867');
/*!40000 ALTER TABLE `events` ENABLE KEYS */;

-- Dumpar struktur för procedur swedenbank.pay_salary
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `pay_salary`(
	IN `user_social_number` VARCHAR(50),
	IN `t_amount` DOUBLE,
	IN `t_sender` VARCHAR(50)

)
BEGIN
		DECLARE reciever_number VARCHAR(50);
		DECLARE does_exist INTEGER;
		SELECT COUNT(*) FROM accountinformation WHERE user_social_number = social_number AND `type` = 'Lönekonto' INTO does_exist;
		IF does_exist > 0 THEN
			SELECT account_number FROM accountinformation WHERE user_social_number = social_number AND `type` = 'Lönekonto' INTO reciever_number;
		ELSE
			SELECT account_number FROM accountinformation WHERE user_social_number = social_number LIMIT 1 INTO reciever_number;
		END IF;
	        INSERT INTO transactions SET transaction_name = 'Lön',`account` = t_sender,
			  `type` = 'Utgående', transaction_ammount = -t_amount; 
			     
			  INSERT INTO transactions SET transaction_name = 'Lön',`account` = reciever_number,
			  `type` = 'Inkommande', transaction_ammount = t_amount;   
		 	  
   END//
DELIMITER ;

-- Dumpar struktur för händelse swedenbank.run_salary_checker
DELIMITER //
CREATE DEFINER=`root`@`localhost` EVENT `run_salary_checker` ON SCHEDULE EVERY 1 DAY STARTS '2019-03-22 11:28:07' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
	CALL salary_checker();
	END//
DELIMITER ;

-- Dumpar struktur för tabell swedenbank.salarylist
CREATE TABLE IF NOT EXISTS `salarylist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `social_number` varchar(13) COLLATE utf8mb4_swedish_ci DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `sender_account` varchar(50) COLLATE utf8mb4_swedish_ci DEFAULT NULL,
  `transaction_date` date NOT NULL,
  `type` varchar(50) COLLATE utf8mb4_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_swedish_ci;

-- Dumpar data för tabell swedenbank.salarylist: ~1 rows (ungefär)
/*!40000 ALTER TABLE `salarylist` DISABLE KEYS */;
INSERT INTO `salarylist` (`id`, `social_number`, `amount`, `sender_account`, `transaction_date`, `type`) VALUES
	(37, '8902149867', 25000, '232.232.232-2', '2019-03-22', 'Lön');
/*!40000 ALTER TABLE `salarylist` ENABLE KEYS */;

-- Dumpar struktur för procedur swedenbank.salary_checker
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `salary_checker`()
BEGIN
DECLARE n INT DEFAULT 0;
DECLARE i INT DEFAULT 0;
DECLARE my_number VARCHAR(50);
DECLARE t_amount DOUBLE;
DECLARE t_sender VARCHAR(50);
		SELECT COUNT(*) FROM salarylist WHERE DAYOFMONTH(salarylist.transaction_date) = DAYOFMONTH(NOW()) INTO n;
		SELECT id FROM salarylist WHERE DAYOFMONTH(salarylist.transaction_date) = DAYOFMONTH(NOW()) LIMIT 1 INTO i;
		SET n = n + i;
		WHILE i < n DO
			SELECT social_number FROM salarylist WHERE salarylist.id = i INTO my_number;
			SELECT amount FROM salarylist WHERE salarylist.id = i INTO t_amount;
			SELECT sender_account FROM salarylist WHERE salarylist.id = i INTO t_sender;
			CALL pay_salary(my_number,t_amount,t_sender);
			SET i = i + 1;
		END WHILE;

END//
DELIMITER ;

-- Dumpar struktur för tabell swedenbank.transactions
CREATE TABLE IF NOT EXISTS `transactions` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `transaction_name` varchar(50) COLLATE utf8mb4_swedish_ci NOT NULL,
  `account` varchar(50) COLLATE utf8mb4_swedish_ci NOT NULL,
  `type` varchar(50) COLLATE utf8mb4_swedish_ci NOT NULL,
  `transaction_ammount` double NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=178 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_swedish_ci;

-- Dumpar data för tabell swedenbank.transactions: ~22 rows (ungefär)
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` (`id`, `transaction_name`, `account`, `type`, `transaction_ammount`, `date`) VALUES
	(154, 'Betalning Bil', '123.123.123-2', 'Utgående', -23, '2019-03-27 16:18:25'),
	(155, 'Betalning Bil', '243.123.543-1', 'Inkommande', 23, '2019-03-27 16:18:25'),
	(157, 'Betalning Apa', '123.123.123-2', 'Utgående', -10000, '2019-03-27 16:20:12'),
	(158, 'Betalning Apa', '243.123.543-1', 'Inkommande', 10000, '2019-03-27 16:20:12'),
	(159, 'Betalning Något', '243.123.543-1', 'Utgående', -574, '2019-03-27 16:20:42'),
	(160, 'Betalning Något', '123.123.123-2', 'Inkommande', 574, '2019-03-27 16:20:42'),
	(161, 'Betalning Bil', '243.123.543-1', 'Inkommande', 23, '2019-03-27 16:18:25'),
	(162, 'Betalning Apa', '243.123.543-1', 'Inkommande', 10000, '2019-03-27 16:20:12'),
	(163, 'Betalning Något', '243.123.543-1', 'Utgående', -574, '2019-03-27 16:20:42'),
	(164, 'Betalning Bil', '123.123.123-2', 'Utgående', -23, '2019-03-27 16:18:25'),
	(165, 'sad', '123.123.123-2', 'Utgående', -23, '2019-03-27 16:22:15'),
	(166, 'sad', '243.123.543-1', 'Inkommande', 23, '2019-03-27 16:22:15'),
	(168, 'awsdf', '123.123.123-2', 'Utgående', -12, '2019-03-27 16:22:32'),
	(169, 'awsdf', '243.123.543-1', 'Inkommande', 12, '2019-03-27 16:22:32'),
	(170, 'easd', '123.123.123-2', 'Utgående', -12, '2019-04-01 12:00:12'),
	(171, 'easd', '123.123.123-2', 'Inkommande', 12, '2019-04-01 12:00:12'),
	(172, 'adsf', '123.123.123-2', 'Utgående', -234, '2019-04-01 12:00:18'),
	(173, 'adsf', '123.123.123-2', 'Inkommande', 234, '2019-04-01 12:00:18'),
	(174, 'wqfewqe', '123.123.123-2', 'Utgående', -1234231, '2019-04-01 12:00:25'),
	(175, 'wqfewqe', '123.123.123-2', 'Inkommande', 1234231, '2019-04-01 12:00:25'),
	(176, 'das', '123.123.123-2', 'Utgående', -1342342, '2019-04-01 12:00:31'),
	(177, 'das', '123.123.123-2', 'Inkommande', 1342342, '2019-04-01 12:00:31');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;

-- Dumpar struktur för vy swedenbank.transactions_user
-- Skapar temporärtabell för att hantera VIEW-beroendefel
CREATE TABLE `transactions_user` (
	`id` INT(11) UNSIGNED NOT NULL,
	`transaction_name` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_swedish_ci',
	`account` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_swedish_ci',
	`type` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_swedish_ci',
	`transaction_ammount` DOUBLE NOT NULL,
	`date` TIMESTAMP NOT NULL,
	`social_number` VARCHAR(13) NULL COLLATE 'utf8mb4_swedish_ci'
) ENGINE=MyISAM;

-- Dumpar struktur för tabell swedenbank.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) COLLATE utf8mb4_swedish_ci NOT NULL DEFAULT '0',
  `last_name` varchar(50) COLLATE utf8mb4_swedish_ci NOT NULL DEFAULT '0',
  `social_number` varchar(13) COLLATE utf8mb4_swedish_ci NOT NULL DEFAULT '0',
  `email` varchar(255) COLLATE utf8mb4_swedish_ci NOT NULL DEFAULT '0',
  `password` varchar(255) COLLATE utf8mb4_swedish_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `social_number` (`social_number`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_swedish_ci;

-- Dumpar data för tabell swedenbank.users: ~3 rows (ungefär)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `first_name`, `last_name`, `social_number`, `email`, `password`) VALUES
	(1, 'Rickard', 'Andersson', '8902149867', 'rickard98@gmail.com', 'password1234'),
	(2, 'Visa', 'Visa', '1111111111', 'visa@visa.se', 'password1234'),
	(3, 'Test', 'Nisse', '7802138947', 'testnisse@nisse.com', 'password1234');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

-- Dumpar struktur för tabell swedenbank.userxaccount
CREATE TABLE IF NOT EXISTS `userxaccount` (
  `user_id` int(11) unsigned NOT NULL,
  `account_id` int(11) unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_swedish_ci;

-- Dumpar data för tabell swedenbank.userxaccount: ~5 rows (ungefär)
/*!40000 ALTER TABLE `userxaccount` DISABLE KEYS */;
INSERT INTO `userxaccount` (`user_id`, `account_id`) VALUES
	(1, 1),
	(2, 3),
	(1, 4),
	(3, 18),
	(3, 4);
/*!40000 ALTER TABLE `userxaccount` ENABLE KEYS */;

-- Dumpar struktur för trigger swedenbank.call_adjust_balance
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `call_adjust_balance` AFTER INSERT ON `transactions` FOR EACH ROW BEGIN
            CALL adjust_account_balance(NEW.`account`, NEW.transaction_ammount);
        END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Dumpar struktur för vy swedenbank.accountinformation
-- Tar bort temporärtabell och skapar slutgiltlig VIEW-struktur
DROP TABLE IF EXISTS `accountinformation`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `accountinformation` AS select `accounts`.`id` AS `id`,`accounts`.`account_number` AS `account_number`,`accounts`.`account_name` AS `account_name`,`accounts`.`type` AS `type`,`accounts`.`balance` AS `balance`,`users`.`social_number` AS `social_number`,`users`.`first_name` AS `first_name`,`users`.`last_name` AS `last_name` from ((`accounts` join `userxaccount` on((`accounts`.`id` = `userxaccount`.`account_id`))) join `users` on((`userxaccount`.`user_id` = `users`.`id`)));

-- Dumpar struktur för vy swedenbank.transactions_user
-- Tar bort temporärtabell och skapar slutgiltlig VIEW-struktur
DROP TABLE IF EXISTS `transactions_user`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `transactions_user` AS select `transactions`.`id` AS `id`,`transactions`.`transaction_name` AS `transaction_name`,`transactions`.`account` AS `account`,`transactions`.`type` AS `type`,`transactions`.`transaction_ammount` AS `transaction_ammount`,`transactions`.`date` AS `date`,`accountinformation`.`social_number` AS `social_number` from (`transactions` left join `accountinformation` on((`transactions`.`account` = `accountinformation`.`account_number`)));

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
