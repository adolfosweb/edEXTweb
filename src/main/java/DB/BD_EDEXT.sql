CREATE DATABASE  IF NOT EXISTS `adolfosweb_edext` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;
USE `adolfosweb_edext`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: adolfosweb.uy    Database: adolfosweb_edext
-- ------------------------------------------------------
-- Server version	5.5.5-10.6.28-MariaDB-cll-lve

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Curso`
--

DROP TABLE IF EXISTS `Curso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Curso` (
  `cantCreditos` int(11) NOT NULL,
  `cantHoras` int(11) NOT NULL,
  `fecRegistro` date DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `duracion` varchar(255) DEFAULT NULL,
  `instituto_nombre` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`nombre`),
  KEY `FKox7lsbhcfx01wkiyb5sajpywr` (`instituto_nombre`),
  CONSTRAINT `FKox7lsbhcfx01wkiyb5sajpywr` FOREIGN KEY (`instituto_nombre`) REFERENCES `Instituto` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Curso`
--

LOCK TABLES `Curso` WRITE;
/*!40000 ALTER TABLE `Curso` DISABLE KEYS */;
INSERT INTO `Curso` VALUES (42,60,'2026-05-24','Se realizarán visitas a escuelas rurales participantes en un proyecto conjunto del grupo PLN y el Programa de Políticas Lingüísticas de ANEP, en el marco del cual se desarrollaron diferentes herramientas para uso de maestros que enseñan inglés con apoyo remoto de profesores especializados desde Montevideo.','12 semanas','INCO','\"Herrramientas de apoyo a la enseñanza de inglés. Instalación y evaluación\"','https://eva.fing.edu.uy/mod/folder/view.php?id=89398'),(42,60,'2024-06-25','Dalavuelta es un proyecto de extensión que nace en el Instituto de Ingeniería Mecánica y Producción Industrial (IMPI) de Fing, que, si bien inicia su trabajo en el desarrollo de bicicletas accesibles para personas en situación de discapacidad motriz a partir de bicicletas abandonadas, se propuso diseñar otras herramientas para fomentar la accesibilidad.','10 semanas','IMPI','Dalavuelta','https://eva.fing.edu.uy/course/view.php?id=783#section-2'),(51,75,'2025-06-16','El proyecto tiene como objetivo desarrollar intervenciones curriculares en pequeños emprendimientos productivos de diferentes sectores de la industria nacional. La metodología de trabajo permite articular diversas intervenciones, combinando actividades de enseñanza, extensión e investigación por parte de docentes del IMPI.','12 semanas','IMPI','Extensionismo Industrial','https://eva.fing.edu.uy/course/view.php?id=783#section-2'),(10,150,'2008-07-27','Flor del Ceibo es un proyecto central de la Universidad de la República, que tiene misión por movilizar la participación de estudiantes universitarios en diversas tareas vinculadas con la puesta en funcionamiento del Plan Ceibal en el territorio nacional.','15 semanas','DISI','Flor del Ceibo','http://www.flordeceibo.edu.uy/'),(30,45,'2026-02-01','En el proyecto se conjuga el trabajo de docentes y estudiantes de la carrera Ingeniería Industrial Mecánica a través del Módulo de Extensión, en donde se trabaja en el diseño, construcción y prueba de un prototipo de colector solar adquiriendo conocimientos relevantes para luego poder replicarlos junto a las familias en los talleres. Las premisas fundamentales a la hora de pensar los diseños son: por un lado el bajo costo de los materiales y por otro la fácil construcción de forma de poder construirlos ellos mismos.','6 semanas','IMPI','Inclusión Energética','https://eva.fing.edu.uy/course/view.php?id=783#section-2'),(71,105,'2026-03-13','El Centro Ceibal se encuentra distribuyendo placas micro:bit (https://microbit.ceibal.edu.uy/) para que estudiantes de primaria y secundaria aprendan nociones básicas de robótica, electrónica y programación de forma autónoma y lúdica.','15 semanas','Eléctrica','MicroBit','https://www.fing.edu.uy/noticias/extension/modulo-de-tallerextension-microbit'),(31,45,'2026-06-15','Se propone desarrollar una aplicación interactiva para tablet Android basada en el juego de tablero Komikan (versión web del juego https://codepen.io/Borborem/full/OvZBvZ/), que incluya los distintos aspectos concernientes al juego, así como a situaciones específicas particulares.','9 semanas','INCO','Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela','https://eva.fing.edu.uy/mod/folder/view.php?id=89398'),(21,30,'2026-07-12','Seminario, \"todos los jueves\" en Facultad de Ingeniería a partir del jueves 25 de Julio, en las áreas en que se desarrollan los problemas de las Olimpíadas de Matemática.','5 semanas','IMERL','Seminarios de Resolución de Problemas','www.tmu.edu.uy'),(60,90,'2024-02-02','La asignatura se organiza en dos etapas. La primer etapa se dicta a través de clases teórico-prácticas, donde se espera además que cada estudiante le dedique horas de estudio. La segunda etapa consiste en que los estudiantes trabajen en grupo sobre el diseño e implementación de una experiencia didáctica de inclusión del robot Butiá en el aula, utilizando los conocimientos aprendidos en clase.','8 semanas','INCO','Taller de robótica educativa.','https://eva.fing.edu.uy/course/view.php?id=1187'),(10,15,'2026-02-01','Talleres plenarios: presentados por cuatro reconocidos matemáticos uruguayos, plantearán diversos tópicos de matemática en el marco de los cuales se realizarán actividades fomentando la integración entre estudiantes, docentes e investigadores.','3 semanas','IMERL','Talleres plenarios','www.tmu.edu.uy');
/*!40000 ALTER TABLE `Curso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Docente`
--

DROP TABLE IF EXISTS `Docente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Docente` (
  `nickname` varchar(255) NOT NULL,
  PRIMARY KEY (`nickname`),
  CONSTRAINT `FKmapxmpx4otmyo7pvs1ah20wry` FOREIGN KEY (`nickname`) REFERENCES `Usuario` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Docente`
--

LOCK TABLES `Docente` WRITE;
/*!40000 ALTER TABLE `Docente` DISABLE KEYS */;
INSERT INTO `Docente` VALUES ('adri'),('benkenobi'),('bruces'),('danny'),('heisenberg'),('house'),('phils'),('timmy'),('waston');
/*!40000 ALTER TABLE `Docente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Docente_Instituto`
--

DROP TABLE IF EXISTS `Docente_Instituto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Docente_Instituto` (
  `docentes_nickname` varchar(255) NOT NULL,
  `institutos_nombre` varchar(255) NOT NULL,
  KEY `FK63uln2d1bfl8d87kws06jbvdd` (`institutos_nombre`),
  KEY `FKa963axcj4qdm9p7q038bw9afs` (`docentes_nickname`),
  CONSTRAINT `FK63uln2d1bfl8d87kws06jbvdd` FOREIGN KEY (`institutos_nombre`) REFERENCES `Instituto` (`nombre`),
  CONSTRAINT `FKa963axcj4qdm9p7q038bw9afs` FOREIGN KEY (`docentes_nickname`) REFERENCES `Docente` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Docente_Instituto`
--

LOCK TABLES `Docente_Instituto` WRITE;
/*!40000 ALTER TABLE `Docente_Instituto` DISABLE KEYS */;
INSERT INTO `Docente_Instituto` VALUES ('heisenberg','INCO'),('benkenobi','INCO'),('waston','INCO'),('house','INCO'),('timmy','INCO'),('danny','IMERL'),('phils','IMPI'),('bruces','IMPI'),('adri','DISI');
/*!40000 ALTER TABLE `Docente_Instituto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EdicionCurso`
--

DROP TABLE IF EXISTS `EdicionCurso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EdicionCurso` (
  `cupo` int(11) NOT NULL,
  `fechaFin` date DEFAULT NULL,
  `fechaInicio` date DEFAULT NULL,
  `fechaPublicacion` date DEFAULT NULL,
  `curso_nombre` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`nombre`),
  KEY `FK7oywxj889rnybw7nmmyqhnae1` (`curso_nombre`),
  CONSTRAINT `FK7oywxj889rnybw7nmmyqhnae1` FOREIGN KEY (`curso_nombre`) REFERENCES `Curso` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EdicionCurso`
--

LOCK TABLES `EdicionCurso` WRITE;
/*!40000 ALTER TABLE `EdicionCurso` DISABLE KEYS */;
INSERT INTO `EdicionCurso` VALUES (15,'2024-11-10','2024-08-20','2024-07-20','Dalavuelta','Dalavuelta - 2025'),(15,'2025-11-10','2025-08-10','2025-07-08','Extensionismo Industrial','Extensionismo Industrial - 2025'),(-1,'2010-07-07','2010-03-15','2010-02-16','Flor del Ceibo','Flor del Ceibo - 2010'),(-1,'2012-11-20','2012-08-01','2012-07-10','Flor del Ceibo','Flor del Ceibo - 2012'),(-1,'2025-08-07','2025-04-10','2025-03-06','Flor del Ceibo','Flor del Ceibo - 2025'),(50,'2026-12-15','2026-09-15','2026-06-02','\"Herrramientas de apoyo a la enseñanza de inglés. Instalación y evaluación\"','Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26'),(30,'2026-04-30','2026-03-15','2026-02-20','Inclusión Energética','Inclusión Energética - 2026'),(30,'2026-12-05','2026-08-12','2026-07-02','MicroBit','MicroBit - 2026'),(5,'2026-10-07','2026-07-29','2026-07-10','Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela','Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026'),(-1,'2026-10-20','2026-09-10','2026-07-12','Seminarios de Resolución de Problemas','Seminarios de Resolución de Problemas - 2026'),(10,'2024-05-10','2024-03-10','2024-02-15','Taller de robótica educativa.','Taller de robótica educativa - 2024'),(10,'2026-05-10','2026-03-10','2026-02-15','Taller de robótica educativa.','Taller de robótica educativa - 2026'),(20,'2026-11-08','2026-09-10','2026-08-15','Taller de robótica educativa.','Taller de robótica educativa - 2026-2'),(-1,'2026-03-30','2026-03-10','2026-03-02','Talleres plenarios','Talleres plenarios - 2026');
/*!40000 ALTER TABLE `EdicionCurso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EdicionCurso_Docente`
--

DROP TABLE IF EXISTS `EdicionCurso_Docente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EdicionCurso_Docente` (
  `docentes_nickname` varchar(255) NOT NULL,
  `edicionesParticipante_nombre` varchar(255) NOT NULL,
  KEY `FKom37e1if3po6j38l9jbjiddr2` (`docentes_nickname`),
  KEY `FK95etueacm3jh1m91n2k1dc86x` (`edicionesParticipante_nombre`),
  CONSTRAINT `FK95etueacm3jh1m91n2k1dc86x` FOREIGN KEY (`edicionesParticipante_nombre`) REFERENCES `EdicionCurso` (`nombre`),
  CONSTRAINT `FKom37e1if3po6j38l9jbjiddr2` FOREIGN KEY (`docentes_nickname`) REFERENCES `Docente` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EdicionCurso_Docente`
--

LOCK TABLES `EdicionCurso_Docente` WRITE;
/*!40000 ALTER TABLE `EdicionCurso_Docente` DISABLE KEYS */;
INSERT INTO `EdicionCurso_Docente` VALUES ('bruces','Flor del Ceibo - 2010'),('bruces','Flor del Ceibo - 2012'),('adri','Flor del Ceibo - 2012'),('bruces','Flor del Ceibo - 2025'),('adri','Flor del Ceibo - 2025'),('phils','Dalavuelta - 2025'),('phils','Extensionismo Industrial - 2025'),('phils','Inclusión Energética - 2026'),('heisenberg','Taller de robótica educativa - 2024'),('heisenberg','Taller de robótica educativa - 2026'),('benkenobi','Taller de robótica educativa - 2026'),('benkenobi','Taller de robótica educativa - 2026-2'),('waston','Taller de robótica educativa - 2026-2'),('waston','Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026'),('heisenberg','Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26'),('house','MicroBit - 2026'),('timmy','Talleres plenarios - 2026'),('danny','Talleres plenarios - 2026'),('timmy','Seminarios de Resolución de Problemas - 2026');
/*!40000 ALTER TABLE `EdicionCurso_Docente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Estudiante`
--

DROP TABLE IF EXISTS `Estudiante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Estudiante` (
  `nickname` varchar(255) NOT NULL,
  PRIMARY KEY (`nickname`),
  CONSTRAINT `FK8h97ronow27xt21hr8adnvwji` FOREIGN KEY (`nickname`) REFERENCES `Usuario` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Estudiante`
--

LOCK TABLES `Estudiante` WRITE;
/*!40000 ALTER TABLE `Estudiante` DISABLE KEYS */;
INSERT INTO `Estudiante` VALUES ('chechi'),('costas'),('eleven11'),('jeffw'),('roro'),('weiss');
/*!40000 ALTER TABLE `Estudiante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `InscripcionCurso`
--

DROP TABLE IF EXISTS `InscripcionCurso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `InscripcionCurso` (
  `fechaInscripcion` date DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `edicionCurso_nombre` varchar(255) DEFAULT NULL,
  `estudiante_nickname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKstq5o90d9yckkon7kkbgy012f` (`edicionCurso_nombre`),
  KEY `FKmypysucjecfqy6md1qlb591od` (`estudiante_nickname`),
  CONSTRAINT `FKmypysucjecfqy6md1qlb591od` FOREIGN KEY (`estudiante_nickname`) REFERENCES `Estudiante` (`nickname`),
  CONSTRAINT `FKstq5o90d9yckkon7kkbgy012f` FOREIGN KEY (`edicionCurso_nombre`) REFERENCES `EdicionCurso` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `InscripcionCurso`
--

LOCK TABLES `InscripcionCurso` WRITE;
/*!40000 ALTER TABLE `InscripcionCurso` DISABLE KEYS */;
INSERT INTO `InscripcionCurso` VALUES ('2010-02-20',1,'Flor del Ceibo - 2010','eleven11'),('2010-02-25',2,'Flor del Ceibo - 2010','chechi'),('2012-07-12',3,'Flor del Ceibo - 2012','costas'),('2012-07-15',4,'Flor del Ceibo - 2012','roro'),('2012-07-30',5,'Flor del Ceibo - 2012','weiss'),('2025-03-10',6,'Flor del Ceibo - 2025','roro'),('2025-03-15',7,'Flor del Ceibo - 2025','jeffw'),('2024-07-25',8,'Dalavuelta - 2025','chechi'),('2024-07-28',9,'Dalavuelta - 2025','eleven11'),('2024-08-02',10,'Dalavuelta - 2025','roro'),('2024-08-10',11,'Dalavuelta - 2025','costas'),('2024-08-15',12,'Dalavuelta - 2025','jeffw'),('2025-07-18',13,'Extensionismo Industrial - 2025','costas'),('2025-07-20',14,'Extensionismo Industrial - 2025','chechi'),('2025-07-29',15,'Extensionismo Industrial - 2025','eleven11'),('2025-08-05',16,'Extensionismo Industrial - 2025','weiss'),('2026-02-23',17,'Inclusión Energética - 2026','roro'),('2026-02-25',18,'Inclusión Energética - 2026','weiss'),('2026-02-28',19,'Inclusión Energética - 2026','chechi'),('2026-03-03',20,'Inclusión Energética - 2026','eleven11'),('2017-02-18',21,'Taller de robótica educativa - 2024','weiss'),('2024-02-20',22,'Taller de robótica educativa - 2024','roro'),('2024-03-03',23,'Taller de robótica educativa - 2024','eleven11'),('2024-03-05',24,'Taller de robótica educativa - 2024','chechi'),('2026-02-18',25,'Taller de robótica educativa - 2026','jeffw'),('2026-02-22',26,'Taller de robótica educativa - 2026','costas'),('2026-08-18',27,'Taller de robótica educativa - 2026-2','weiss'),('2026-08-22',28,'Taller de robótica educativa - 2026-2','chechi'),('2026-09-03',29,'Taller de robótica educativa - 2026-2','roro'),('2026-07-13',30,'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026','chechi'),('2026-07-20',31,'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026','weiss'),('2026-07-22',32,'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026','roro'),('2026-06-04',33,'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26','weiss'),('2026-07-18',34,'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26','eleven11'),('2026-08-20',35,'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26','jeffw'),('2026-07-12',36,'MicroBit - 2026','chechi'),('2026-07-14',37,'MicroBit - 2026','roro'),('2026-07-25',38,'MicroBit - 2026','eleven11'),('2026-08-05',39,'MicroBit - 2026','jeffw'),('2026-03-05',40,'Talleres plenarios - 2026','costas'),('2026-03-04',41,'Talleres plenarios - 2026','weiss'),('2026-03-07',42,'Talleres plenarios - 2026','roro'),('2026-07-15',43,'Seminarios de Resolución de Problemas - 2026','weiss'),('2026-07-20',44,'Seminarios de Resolución de Problemas - 2026','costas'),('2026-08-06',45,'Seminarios de Resolución de Problemas - 2026','roro'),('2026-08-30',46,'Seminarios de Resolución de Problemas - 2026','chechi');
/*!40000 ALTER TABLE `InscripcionCurso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `InscripcionPrograma`
--

DROP TABLE IF EXISTS `InscripcionPrograma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `InscripcionPrograma` (
  `fechaInscripcion` date DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `estudianteInscripto_nickname` varchar(255) DEFAULT NULL,
  `programaInscripto_nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKac03lt1lvr9wpkvyd2ax0bh0t` (`estudianteInscripto_nickname`),
  KEY `FK8g5yl7d2cup3jbko1161bweuc` (`programaInscripto_nombre`),
  CONSTRAINT `FK8g5yl7d2cup3jbko1161bweuc` FOREIGN KEY (`programaInscripto_nombre`) REFERENCES `ProgramaFormacion` (`nombre`),
  CONSTRAINT `FKac03lt1lvr9wpkvyd2ax0bh0t` FOREIGN KEY (`estudianteInscripto_nickname`) REFERENCES `Estudiante` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `InscripcionPrograma`
--

LOCK TABLES `InscripcionPrograma` WRITE;
/*!40000 ALTER TABLE `InscripcionPrograma` DISABLE KEYS */;
/*!40000 ALTER TABLE `InscripcionPrograma` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Instituto`
--

DROP TABLE IF EXISTS `Instituto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Instituto` (
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Instituto`
--

LOCK TABLES `Instituto` WRITE;
/*!40000 ALTER TABLE `Instituto` DISABLE KEYS */;
INSERT INTO `Instituto` VALUES ('DISI'),('Eléctrica'),('Física'),('IMERL'),('IMPI'),('INCO');
/*!40000 ALTER TABLE `Instituto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ProgramaFormacion`
--

DROP TABLE IF EXISTS `ProgramaFormacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ProgramaFormacion` (
  `fechaAlta` date DEFAULT NULL,
  `fechaFin` date DEFAULT NULL,
  `fechaInicio` date DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ProgramaFormacion`
--

LOCK TABLES `ProgramaFormacion` WRITE;
/*!40000 ALTER TABLE `ProgramaFormacion` DISABLE KEYS */;
INSERT INTO `ProgramaFormacion` VALUES (NULL,'2026-10-31','2026-05-01','Programa mecánica','EFI Ingeniería Mecánica'),(NULL,'2026-11-18','2026-09-03','Programa robótica','EFI Robótica'),(NULL,'2027-01-01','2026-07-15','Programa varios institutos','Formación integral');
/*!40000 ALTER TABLE `ProgramaFormacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ProgramaFormacion_Curso`
--

DROP TABLE IF EXISTS `ProgramaFormacion_Curso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ProgramaFormacion_Curso` (
  `cursos_nombre` varchar(255) NOT NULL,
  `programas_nombre` varchar(255) NOT NULL,
  KEY `FKgjuwoqpgtf02snxfm6dqwu47p` (`cursos_nombre`),
  KEY `FKmo6pils6egvh3u2lnyyu28l4f` (`programas_nombre`),
  CONSTRAINT `FKgjuwoqpgtf02snxfm6dqwu47p` FOREIGN KEY (`cursos_nombre`) REFERENCES `Curso` (`nombre`),
  CONSTRAINT `FKmo6pils6egvh3u2lnyyu28l4f` FOREIGN KEY (`programas_nombre`) REFERENCES `ProgramaFormacion` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ProgramaFormacion_Curso`
--

LOCK TABLES `ProgramaFormacion_Curso` WRITE;
/*!40000 ALTER TABLE `ProgramaFormacion_Curso` DISABLE KEYS */;
INSERT INTO `ProgramaFormacion_Curso` VALUES ('Dalavuelta','EFI Ingeniería Mecánica'),('Extensionismo Industrial','EFI Ingeniería Mecánica'),('Inclusión Energética','EFI Ingeniería Mecánica'),('Seminarios de Resolución de Problemas','Formación integral'),('Extensionismo Industrial','Formación integral'),('Flor del Ceibo','Formación integral'),('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela','Formación integral'),('Taller de robótica educativa.','EFI Robótica'),('MicroBit','EFI Robótica');
/*!40000 ALTER TABLE `ProgramaFormacion_Curso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Usuario`
--

DROP TABLE IF EXISTS `Usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Usuario` (
  `fecNac` date DEFAULT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `correoElectronico` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuario`
--

LOCK TABLES `Usuario` WRITE;
/*!40000 ALTER TABLE `Usuario` DISABLE KEYS */;
INSERT INTO `Usuario` VALUES ('1978-07-28','García','agarcia@gmail.com','adri','Adriana'),('1914-04-02','Kenobi','benKenobi@gmail.com','benkenobi','Obi-Wan'),('1959-12-03','Sewell','sewell@gmail.com','bruces','Bruce'),('1987-09-12','Garrido','cgarrido@hotmail.com','chechi','Cecilia'),('1983-11-15','Costas','gcostas@gmail.com','costas','Gerardo'),('1963-07-05','Riccio','dan.riccio@gmail.com','danny','Daniel'),('1971-12-31','Twelve','eleven11@gmail.com','eleven11','Eleven'),('1956-03-07','White','heisenberg@gmail.com','heisenberg','Walter'),('1959-05-15','House','greghouse@gmail.com','house','Gregory'),('1964-11-27','Williams','jwilliams@gmail.com','jeffw','Jeff'),('1961-10-07','Schiller','schiller@gmail.com','phils','Philip'),('1975-08-02','Cotelo','rcotelo@yahoo.com','roro','Rodrigo'),('1960-11-01','Cook','tim.cook@apple.com','timmy','Tim'),('1990-04-15','Watson','e.watson@gmail.com','waston','Emma'),('1978-12-23','Weiss','aweiss@hotmail.com','weiss','Adrian');
/*!40000 ALTER TABLE `Usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curso_previas`
--

DROP TABLE IF EXISTS `curso_previas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `curso_previas` (
  `curso_nombre` varchar(255) NOT NULL,
  `previa_nombre` varchar(255) NOT NULL,
  KEY `FKb6p73uxy93xw5dqsl2gaevceb` (`previa_nombre`),
  KEY `FKmajtwf1sfjyycknsbs0d1gc65` (`curso_nombre`),
  CONSTRAINT `FKb6p73uxy93xw5dqsl2gaevceb` FOREIGN KEY (`previa_nombre`) REFERENCES `Curso` (`nombre`),
  CONSTRAINT `FKmajtwf1sfjyycknsbs0d1gc65` FOREIGN KEY (`curso_nombre`) REFERENCES `Curso` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curso_previas`
--

LOCK TABLES `curso_previas` WRITE;
/*!40000 ALTER TABLE `curso_previas` DISABLE KEYS */;
INSERT INTO `curso_previas` VALUES ('Seminarios de Resolución de Problemas','Talleres plenarios'),('Dalavuelta','Talleres plenarios'),('Extensionismo Industrial','Talleres plenarios'),('Inclusión Energética','Talleres plenarios');
/*!40000 ALTER TABLE `curso_previas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-12 15:50:23
