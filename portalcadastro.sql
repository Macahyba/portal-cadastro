-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: localhost    Database: portalcadastro
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `department` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_gnqwbwwcn7x0m5jlt4158dass` (`email`),
  UNIQUE KEY `UK_8egib4wq2lnsm6u7noysivln2` (`name`),
  KEY `FKckoarj5a5jmet3b3smgdhaopw` (`customer_id`),
  CONSTRAINT `FKckoarj5a5jmet3b3smgdhaopw` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact`
--

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cnpj` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r1qk8etno2r2pkt919v3vs9qa` (`cnpj`),
  UNIQUE KEY `UK_8ptfah84oidw5hqc2t0k5nf29` (`full_name`),
  UNIQUE KEY `UK_crkjmjk1oj8gb6j6t5kt7gcxm` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `equipment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `serial_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKql211pwcgdawxb4rdlxsn1vl7` (`name`,`serial_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment`
--

LOCK TABLES `equipment` WRITE;
/*!40000 ALTER TABLE `equipment` DISABLE KEYS */;
/*!40000 ALTER TABLE `equipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment_spare_part`
--

DROP TABLE IF EXISTS `equipment_spare_part`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `equipment_spare_part` (
  `equipment_id` int(11) NOT NULL,
  `spare_part_id` int(11) NOT NULL,
  PRIMARY KEY (`equipment_id`,`spare_part_id`),
  KEY `FKgb05rem70i87dna7fr5xsreoi` (`spare_part_id`),
  CONSTRAINT `FKgb05rem70i87dna7fr5xsreoi` FOREIGN KEY (`spare_part_id`) REFERENCES `spare_part` (`id`),
  CONSTRAINT `FKrteui9oicnssse9feta55r2dg` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment_spare_part`
--

LOCK TABLES `equipment_spare_part` WRITE;
/*!40000 ALTER TABLE `equipment_spare_part` DISABLE KEYS */;
/*!40000 ALTER TABLE `equipment_spare_part` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jwt_user_details`
--

DROP TABLE IF EXISTS `jwt_user_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `jwt_user_details` (
  `password` varchar(255) DEFAULT NULL,
  `profile` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_44d2t4do1prs0fn62chim5vap` (`username`),
  CONSTRAINT `FKko98k4cqopfdph1b0aff2y9hr` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jwt_user_details`
--

LOCK TABLES `jwt_user_details` WRITE;
/*!40000 ALTER TABLE `jwt_user_details` DISABLE KEYS */;
INSERT INTO `jwt_user_details` VALUES ('$2a$10$hRdUcB7b4ug60DqKfCQjQup428vYthAnx63dyjUB78hn4WvcmtszO','admin','admin',1);
/*!40000 ALTER TABLE `jwt_user_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quotation`
--

DROP TABLE IF EXISTS `quotation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `quotation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `approval_date` datetime DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `label` varchar(255) DEFAULT NULL,
  `total_discount` float DEFAULT '0',
  `total_price` float DEFAULT '0',
  `approval_user_id` int(11) DEFAULT NULL,
  `contact_id` int(11) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `equipment_id` int(11) DEFAULT NULL,
  `status_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sr0tqe3mw2uq549mtj96ku11n` (`label`),
  KEY `FKradurokk0s1xm34o65nid9qrc` (`approval_user_id`),
  KEY `FKhun8mn52en78via8y3ccagt7c` (`contact_id`),
  KEY `FKlf5ti8fw77uew5ivt1rh1dlrd` (`customer_id`),
  KEY `FK3gadvhn9uy0kaqgkptvv69ba9` (`equipment_id`),
  KEY `FKti90serek6afv1alamcwt7ipv` (`status_id`),
  KEY `FKo7tw1pxuf4nai3kgk7hh7x935` (`user_id`),
  CONSTRAINT `FK3gadvhn9uy0kaqgkptvv69ba9` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`id`),
  CONSTRAINT `FKhun8mn52en78via8y3ccagt7c` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`),
  CONSTRAINT `FKlf5ti8fw77uew5ivt1rh1dlrd` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FKo7tw1pxuf4nai3kgk7hh7x935` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKradurokk0s1xm34o65nid9qrc` FOREIGN KEY (`approval_user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKti90serek6afv1alamcwt7ipv` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quotation`
--

LOCK TABLES `quotation` WRITE;
/*!40000 ALTER TABLE `quotation` DISABLE KEYS */;
/*!40000 ALTER TABLE `quotation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quotation_service`
--

DROP TABLE IF EXISTS `quotation_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `quotation_service` (
  `quotation_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  PRIMARY KEY (`quotation_id`,`service_id`),
  KEY `FKop8p2de740s6qqfx1ye5mt9lh` (`service_id`),
  CONSTRAINT `FKheesi17qucthynx8vla0t9nno` FOREIGN KEY (`quotation_id`) REFERENCES `quotation` (`id`),
  CONSTRAINT `FKop8p2de740s6qqfx1ye5mt9lh` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quotation_service`
--

LOCK TABLES `quotation_service` WRITE;
/*!40000 ALTER TABLE `quotation_service` DISABLE KEYS */;
/*!40000 ALTER TABLE `quotation_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repair`
--

DROP TABLE IF EXISTS `repair`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `repair` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sap_notification` varchar(255) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `nota_de_entrada` varchar(255) DEFAULT NULL,
  `nota_fiscal` varchar(255) DEFAULT NULL,
  `part_arrival_date` datetime DEFAULT NULL,
  `part_request_date` datetime DEFAULT NULL,
  `tat` float DEFAULT NULL,
  `tracking_number` varchar(255) DEFAULT NULL,
  `warranty` bit(1) DEFAULT NULL,
  `contact_id` int(11) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `equipment_id` int(11) DEFAULT NULL,
  `status_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkg23vsw8gbox0uira4af4effq` (`contact_id`),
  KEY `FKs492o3a20go94q78s6b8091nl` (`customer_id`),
  KEY `FK8msyiq3s9v8tj5aniwer3kdcn` (`equipment_id`),
  KEY `FK3k5mmcx7j6eia6ehsy7rc4fxe` (`status_id`),
  KEY `FKjgskdm5a8tcx17ood1vshp3ed` (`user_id`),
  CONSTRAINT `FK3k5mmcx7j6eia6ehsy7rc4fxe` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  CONSTRAINT `FK8msyiq3s9v8tj5aniwer3kdcn` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`id`),
  CONSTRAINT `FKjgskdm5a8tcx17ood1vshp3ed` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKkg23vsw8gbox0uira4af4effq` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`),
  CONSTRAINT `FKs492o3a20go94q78s6b8091nl` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repair`
--

LOCK TABLES `repair` WRITE;
/*!40000 ALTER TABLE `repair` DISABLE KEYS */;
/*!40000 ALTER TABLE `repair` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repair_fup`
--

DROP TABLE IF EXISTS `repair_fup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `repair_fup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `equipment_id` int(11) DEFAULT NULL,
  `repair_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9t47t4kwy515yq2glcof88kdk` (`equipment_id`),
  KEY `FKsco5b69chlcytv8r0v16seovx` (`repair_id`),
  KEY `FKbrce28yf087yn3i1aik5lxi0s` (`user_id`),
  CONSTRAINT `FK9t47t4kwy515yq2glcof88kdk` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`id`),
  CONSTRAINT `FKbrce28yf087yn3i1aik5lxi0s` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKsco5b69chlcytv8r0v16seovx` FOREIGN KEY (`repair_id`) REFERENCES `repair` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repair_fup`
--

LOCK TABLES `repair_fup` WRITE;
/*!40000 ALTER TABLE `repair_fup` DISABLE KEYS */;
/*!40000 ALTER TABLE `repair_fup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repair_fup_spare_part`
--

DROP TABLE IF EXISTS `repair_fup_spare_part`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `repair_fup_spare_part` (
  `repair_fup_id` int(11) NOT NULL,
  `spare_part_id` int(11) NOT NULL,
  PRIMARY KEY (`repair_fup_id`,`spare_part_id`),
  KEY `FKe4fec1ypgojn1kf9gvquhosdy` (`spare_part_id`),
  CONSTRAINT `FK2ghrvrcm3wx3lnmuw787vgtbj` FOREIGN KEY (`repair_fup_id`) REFERENCES `repair_fup` (`id`),
  CONSTRAINT `FKe4fec1ypgojn1kf9gvquhosdy` FOREIGN KEY (`spare_part_id`) REFERENCES `spare_part` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repair_fup_spare_part`
--

LOCK TABLES `repair_fup_spare_part` WRITE;
/*!40000 ALTER TABLE `repair_fup_spare_part` DISABLE KEYS */;
/*!40000 ALTER TABLE `repair_fup_spare_part` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_njew1c9fl5n5u2fmteo291087` (`description`),
  UNIQUE KEY `UK_adgojnrwwx9c3y3qa2q08uuqp` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spare_part`
--

DROP TABLE IF EXISTS `spare_part`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `spare_part` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `part_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_438tqaa2a21w82qrvpdt6vjxa` (`part_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spare_part`
--

LOCK TABLES `spare_part` WRITE;
/*!40000 ALTER TABLE `spare_part` DISABLE KEYS */;
/*!40000 ALTER TABLE `spare_part` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_kxaj0dvn13fwjuimg3y2j0oa2` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (2,'APROVADO'),(4,'CANCELADO'),(5,'FINALIZADO'),(1,'NOVO'),(3,'REJEITADO');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'portalinvoice@gmail.com','Administrador do Sistema','+552121872318','admin');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-07 15:30:41
