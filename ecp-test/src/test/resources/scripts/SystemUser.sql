--
-- Table structure for table `system_user`
--

DROP TABLE IF EXISTS `system_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_user` (
  `GID` bigint NOT NULL COMMENT 'Id auto increment for false',
  `CIRCLE_ID` bigint NOT NULL DEFAULT '1' COMMENT 'circle id',
  `ROLE_IDS` char(32) NOT NULL DEFAULT '0' COMMENT 'user roles',
  `NICK_NAME` varchar(32) NOT NULL COMMENT 'user nick name',
  `HEAD_PORTRAIT` varchar(200) NOT NULL COMMENT 'user head portrait',
  `USER_NAME` char(32) DEFAULT NULL COMMENT 'system user name',
  `MOBILE` char(32) DEFAULT NULL COMMENT 'system mobeil account name',
  `EMAIL` char(32) DEFAULT NULL COMMENT 'system mail account name',
  `PASSWORD` char(100) DEFAULT NULL COMMENT 'system account password certificate',
  `IS_LOCKED` bit(1) NOT NULL DEFAULT b'0' COMMENT 'user is lock',
  `CREATED_ID` bigint DEFAULT '0',
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create current timestamp',
  `UPDATED_ID` bigint DEFAULT NULL COMMENT 'operator account id',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL COMMENT 'update current timestamp',
  PRIMARY KEY (`GID`),
  UNIQUE KEY `UNIQUE_USER_NAME_KEY` (`USER_NAME`) USING BTREE,
  UNIQUE KEY `UNIQUE_MAIL_KEY` (`EMAIL`) USING BTREE,
  UNIQUE KEY `UNIQUE_MOBILE_KEY` (`MOBILE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sytem user center table';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;













