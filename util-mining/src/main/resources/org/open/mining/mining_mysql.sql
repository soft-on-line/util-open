-- MySQL dump 10.10
--
-- Host: localhost    Database: mining
-- ------------------------------------------------------
-- Server version	5.0.26-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` int(11) NOT NULL auto_increment COMMENT '自增主键',
  `digest` varchar(50) NOT NULL COMMENT '数字指纹',
  `content` mediumtext NOT NULL COMMENT '文章内容',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `digest` (`digest`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reverse`
--

DROP TABLE IF EXISTS `reverse`;
CREATE TABLE `reverse` (
  `id` int(11) NOT NULL auto_increment COMMENT '自增主键',
  `word` int(11) NOT NULL COMMENT 'word[外键]',
  `article` int(11) NOT NULL COMMENT 'article[外键]',
  `frequency` int(11) NOT NULL COMMENT '词或短语在文章中出现的频率',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `word_article` (`word`,`article`),
  KEY `word` (`word`),
  KEY `article` (`article`),
  CONSTRAINT `reverse_fk` FOREIGN KEY (`word`) REFERENCES `word` (`id`),
  CONSTRAINT `reverse_fk1` FOREIGN KEY (`article`) REFERENCES `article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `reverse`
--

LOCK TABLES `reverse` WRITE;
/*!40000 ALTER TABLE `reverse` DISABLE KEYS */;
/*!40000 ALTER TABLE `reverse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `word`
--

DROP TABLE IF EXISTS `word`;
CREATE TABLE `word` (
  `id` int(11) NOT NULL auto_increment COMMENT '自增主键',
  `name` varchar(50) NOT NULL COMMENT '词语或短句',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `word`
--

LOCK TABLES `word` WRITE;
/*!40000 ALTER TABLE `word` DISABLE KEYS */;
/*!40000 ALTER TABLE `word` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2008-06-03  2:31:40
