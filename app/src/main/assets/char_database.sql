-- MySQL dump 10.13  Distrib 5.5.47, for Win32 (x86)
--
-- Host: localhost    Database: ccc
-- ------------------------------------------------------
-- Server version	5.5.47

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
-- Table structure for table `char_item`
--

DROP TABLE IF EXISTS `char_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `char_item` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `character` varchar(255) DEFAULT NULL,
  `pinyin` varchar(255) DEFAULT NULL,
  `words` varchar(255) DEFAULT NULL,
  `sentence` varchar(255) DEFAULT NULL,
  `explanation` varchar(255) DEFAULT NULL,
  `radical_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `radical_id` (`radical_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `char_item`
--

LOCK TABLES `char_item` WRITE;
/*!40000 ALTER TABLE `char_item` DISABLE KEYS */;
INSERT INTO `char_item` VALUES (1,'庄','zhuang1','村庄village/庄严的dignified/庄重的solemn','仪式在庄严的气氛中进行。The ceremony proceeded in a solemn atmosphere','village/serious/dignified','1'),(2,'庭','ting2',NULL,NULL,NULL,'1'),(3,'库','ku4',NULL,NULL,NULL,'1'),(4,'店','dian4',NULL,NULL,NULL,'1'),(5,'庙','miao4',NULL,NULL,NULL,'1'),(6,'矿','kuang4',NULL,NULL,NULL,'3'),(7,'床','chuang2',NULL,NULL,NULL,'1'),(8,'旷','kuang4',NULL,NULL,NULL,'4'),(9,'看','kan4',NULL,NULL,NULL,'5'),(10,'泪','lei4',NULL,NULL,NULL,'6'),(11,'盯','ding1','盯着stare at','每次我们停下来，每个人都会盯着我们。 Every time we stopped, everybody would stare at me.','stare','5'),(12,'眉','mei2','页眉header','eyebrow.','eyebrow','5'),(13,'眼','yan3',NULL,NULL,NULL,'5'),(14,'瞎','xia1',NULL,NULL,NULL,'5'),(15,'睡','shui4',NULL,NULL,NULL,'5'),(16,'督','du1',NULL,NULL,NULL,'5'),(17,'睹','du3',NULL,NULL,NULL,'5'),(18,'睦','mu4',NULL,NULL,NULL,'5'),(19,'瞩','zhu3',NULL,NULL,NULL,'5'),(20,'肌','ji1','肌肉muscle','有些病人可能还肌肉酸痛。Some paitients may also have muscle pain.','skin','7'),(21,'肿','zhong3','消肿subside a swelling','芦荟胶含有的药剂能自然消肿。Aloe Vera contains natural anti-swelling agent.','swell','7'),(22,'胀','zhang4','膨胀dilate','气球充气将会膨胀。 The ballon will dilate with air.','expand','7'),(23,'胞','bao1','双胞胎twins','我有一个双胞胎兄弟。I have a twin brother.','both of the same parents','7'),(24,'膏','gao1','膏油anointing','你的衣服时常洁白，你的头上也不少缺少膏油。Always be clothed in white,and always anoint your head with oil.','fat','7'),(25,'旦','dan4','元旦New year\'s day','元旦是一年的第一天。New year\'s day is the first day of a year.','day','4'),(26,'旬','xun2','上旬the first ten days of a month','今年的运动会在十月上旬举行。Sports meeting will be held on the first ten days of Octorber.','a period of ten days','4'),(27,'旱','han4','旱季dry season/抗旱combat draught','在旱季我们需要抗旱。 We need to combat draught in dry season.','draught','4'),(28,'晃','huang4','明晃晃shining','他们发现房间里除了一面明晃晃的镜子什么都没有。They found nothing in the room but a crystal mirror.','dazzle','4'),(29,'晰','xi1','清晰distinct','你的语言应该简单、清晰。Your language must be simple and clean.','clear','4'),(30,'叹','tan4','哀叹sigh in sorrow','他哀叹道，“现在和过去不一样了。”He sighed in sorrow, it was not the same.','sigh','8'),(31,'吞','tun1','吞没absorb','他把鸡蛋吞了下去。He swallowed the egg.','swallow','8'),(32,'唇','chun2','嘴唇lip/唇膏lipsticks','姑娘用唇膏乱涂了一下嘴唇。The girl daubed her lips with lipsticks.','lip','8'),(33,'唤','huan4','召唤summon','他急忙召唤他的下级去他的办公室商议。He summoned his subordinates hastily to his office.','call','8'),(34,'喉','hou2','喉咙throat/喉片throat lozenge','这种喉片可以减轻你的喉咙疼。This throat lozenge will quell your sore throat.','throat','8');
/*!40000 ALTER TABLE `char_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `component_learning`
--

DROP TABLE IF EXISTS `component_learning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `component_learning` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `shape` varchar(255) DEFAULT NULL,
  `characters` varchar(255) DEFAULT NULL,
  `voice_or_shape` varchar(255) DEFAULT NULL,
  `explanation` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `component_learning`
--

LOCK TABLES `component_learning` WRITE;
/*!40000 ALTER TABLE `component_learning` DISABLE KEYS */;
INSERT INTO `component_learning` VALUES (1,'广','矿/庄/旷/床','v',NULL),(2,'广','庄/庙/庭/库/店','s',NULL),(3,'目','看/泪/盯/眉/眼/瞎/睡','s',NULL),(4,'目','督/睹/睦/瞩','v',NULL),(5,'月','肌/肿/胀/胞/膏','s',NULL),(6,'日','旦/旬/旱/晃/晰','s',NULL),(7,'口','叹/吞/唇/唤/喉','s',NULL);
/*!40000 ALTER TABLE `component_learning` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `radical_learning`
--

DROP TABLE IF EXISTS `radical_learning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `radical_learning` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `radical_shape` varchar(255) DEFAULT NULL,
  `characters` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `radical_learning`
--

LOCK TABLES `radical_learning` WRITE;
/*!40000 ALTER TABLE `radical_learning` DISABLE KEYS */;
INSERT INTO `radical_learning` VALUES (1,'广','庄/庭/库/店/庙/床'),(2,'土',NULL),(3,'石','矿'),(4,'日','旷/旦/旬/旱/晃/晰'),(5,'目','看/盯/眉/眼/瞎/睡/督/睹/睦/瞩'),(6,'氵','泪'),(7,'月','肌/肿/胀/胞/膏'),(8,'口','叹/吞/唇/唤/喉');
/*!40000 ALTER TABLE `radical_learning` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `radical_relationship`
--

DROP TABLE IF EXISTS `radical_relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `radical_relationship` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `radical_id1` varchar(255) DEFAULT NULL,
  `radical_id2` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `radical_relationship`
--

LOCK TABLES `radical_relationship` WRITE;
/*!40000 ALTER TABLE `radical_relationship` DISABLE KEYS */;
INSERT INTO `radical_relationship` VALUES (1,'4','5'),(2,'5','7'),(3,'4','8'),(4,'7','8');
/*!40000 ALTER TABLE `radical_relationship` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-04 13:34:35
