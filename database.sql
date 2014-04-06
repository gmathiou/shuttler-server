-- phpMyAdmin SQL Dump
-- version 3.5.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 06, 2014 at 11:19 PM
-- Server version: 5.5.29
-- PHP Version: 5.4.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `shuttlerDB`
--
DROP DATABASE `shuttlerDB`;
CREATE DATABASE `shuttlerDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `shuttlerDB`;

-- --------------------------------------------------------

--
-- Table structure for table `lines`
--

CREATE TABLE `lines` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `stopID_sequence` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lines`
--

INSERT INTO `lines` (`id`, `name`, `stopID_sequence`) VALUES
(1, 'Paris - INRIA', '1-2-3-7'),
(2, 'Versailles - INRIA', '4-5-6-7');

-- --------------------------------------------------------

--
-- Table structure for table `profiles`
--

CREATE TABLE `profiles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` text NOT NULL,
  `password` varchar(42) NOT NULL,
  `views` double NOT NULL,
  `kilometers` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=18 ;

-- --------------------------------------------------------

--
-- Table structure for table `stops`
--

CREATE TABLE `stops` (
  `id` int(11) NOT NULL,
  `shortname` text NOT NULL,
  `name` text NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `line` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `stops`
--

INSERT INTO `stops` (`id`, `shortname`, `name`, `latitude`, `longitude`, `line`) VALUES
(1, 'etoile', 'Place de l''Etoile', 48.873934, 2.2949, 1),
(2, 'dauphine', 'Porte Dauphine', 48.87028, 2.274401, 1),
(3, 'auteuil', 'Porte d''Auteuil', 48.848418, 2.257299, 1),
(4, 'chantiers', 'Gare Versailles Chantiers', 48.797177, 2.135966, 2),
(5, 'prefecture', 'Préfecture', 48.802427, 2.129969, 2),
(6, 'rivedroit', 'Gare Versailles Rive-Droite', 48.809748, 2.134025, 2),
(7, 'inria', 'Inria', 48.836741, 2.102541, 1);