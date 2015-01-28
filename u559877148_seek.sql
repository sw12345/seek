
-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 28, 2015 at 08:15 PM
-- Server version: 10.0.12-MariaDB
-- PHP Version: 5.2.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `u559877148_seek`
--

-- --------------------------------------------------------

--
-- Table structure for table `event`
--

CREATE TABLE IF NOT EXISTS `event` (
  `event_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `e_name` varchar(50) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `address` varchar(255) NOT NULL,
  `post_code1` varchar(50) NOT NULL,
  `post_code2` varchar(50) NOT NULL,
  `post_code` varchar(50) NOT NULL,
  `latitude` decimal(9,6) NOT NULL,
  `longitude` decimal(9,6) NOT NULL,
  `max_cap` int(11) NOT NULL,
  `reg_link` varchar(255) NOT NULL,
  `e_desc` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`event_id`),
  KEY `user_id` (`user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=100 ;

--
-- Dumping data for table `event`
--

INSERT INTO `event` (`event_id`, `user_id`, `e_name`, `start_date`, `end_date`, `address`, `post_code1`, `post_code2`, `post_code`, `latitude`, `longitude`, `max_cap`, `reg_link`, `e_desc`, `created_at`, `updated_at`) VALUES
(12, 1, 'Dream, Act, Inspire!', '2014-11-22', '2014-11-23', '5, Gower Place, Holburn, London', 'WC1E', '6BS', 'WC1E6BS', '53.710116', '-1.858415', 100, 'http://www.eventbrite.co.uk/e/dream-act-inspire-tickets-10369416203', 'Testing...', '2014-12-17 12:39:31', '0000-00-00 00:00:00'),
(13, 8, 'Social Futures - Future of Enterprise Education', '2015-02-11', '2015-02-13', 'Flat 9, 453, Caledonian Road, Holloway, London', 'N7', '9BA', 'N79BA', '51.547040', '-0.118184', 75, 'http://www.eventbrite.co.uk', 'TEsting.....', '2014-12-17 12:51:29', '0000-00-00 00:00:00'),
(14, 8, 'Talking social enterprise with like-minded people', '2014-08-25', '2014-08-25', 'The Wharf, 6 Slate Wharf, Castlefield, Manchester', 'M15', '4ST ', 'M154ST ', '53.471358', '-2.251843', 60, 'http://www.eventbrite.co.uk', 'TEsting.....', '2014-12-17 13:02:23', '0000-00-00 00:00:00'),
(73, 1, 'Webinars', '2015-03-06', '2015-03-06', '283, High Road Leyton, Leyton, London', 'E10', '5QN', 'E105QN', '51.559313', '-0.008217', 25, 'https://unltd.org.uk/unltd-together/', 'bnj', '2015-01-16 01:19:24', '0000-00-00 00:00:00'),
(74, 1, 'Social Entrepreneur Week', '2015-05-05', '2015-05-10', '15 Gordon Street, Bloomsbury, London', 'WC1H', '0AH', 'WC1H0AH', '51.524918', '-0.132273', 89, 'https://unltd.org.uk/unltd-together/', 'team', '2015-01-16 01:29:35', '0000-00-00 00:00:00'),
(99, 23, 'testing', '0000-00-00', '0000-00-00', 'for deletion', 'hello', '', '', '0.000000', '0.000000', 0, '', '', '2015-01-17 10:07:40', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email_address` varchar(50) NOT NULL,
  `user_pass` varchar(50) NOT NULL,
  `terms_cond` varchar(10) NOT NULL,
  `phone_nmbr` varchar(50) NOT NULL,
  `address` varchar(255) NOT NULL,
  `post_code1` varchar(50) NOT NULL,
  `post_code2` varchar(50) NOT NULL,
  `post_code` varchar(50) NOT NULL,
  `latitude` decimal(9,6) NOT NULL,
  `longitude` decimal(9,6) NOT NULL,
  `birth_date` date NOT NULL,
  `under_18` varchar(50) NOT NULL,
  `gender` varchar(50) NOT NULL,
  `int_tags` varchar(255) NOT NULL,
  `use_currloc` varchar(50) NOT NULL,
  `see_currloc` varchar(50) NOT NULL,
  `see_details` varchar(50) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=30 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `email_address`, `user_pass`, `terms_cond`, `phone_nmbr`, `address`, `post_code1`, `post_code2`, `post_code`, `latitude`, `longitude`, `birth_date`, `under_18`, `gender`, `int_tags`, `use_currloc`, `see_currloc`, `see_details`, `created_at`, `updated_at`) VALUES
(1, 'admin', 'seek', 'adminuser', 'tester123', 'true', '2147483647', '283, High Road Leyton, Leyton, London', 'N7', '9BA', 'N79BA', '51.547139', '-0.118320', '1985-11-24', 'true', 'Female', 'Hiking', 'true', 'false', 'true', '2014-12-18 06:01:26', '2014-12-23 01:28:45'),
(6, 'Harry', 'Potter', 'harry.potter@hogwarts.co.uk', 'harrypotter', 'true', '07147483647', '283, High Road Leyton, Leyton, London', 'EC1Y', '8QH', 'EC1Y8QH', '51.524061', '-0.093064', '1990-07-05', 'true', 'Male', 'repairing, computers', 'true', 'false', 'true', '2014-12-19 09:14:04', '2014-12-23 01:39:26'),
(7, 'Hermione', 'Granger', 'hermione.granger@hogwarts.co.uk', 'hermione', 'true', '07415896223', '283, High Road Leyton, Leyton, London', 'EC1Y', '8QJ', 'EC1Y8QJ', '51.524011', '-0.093354', '1989-05-20', 'false', 'Female', 'Books, Literature', 'false', 'false', 'false', '2014-12-19 09:22:29', '2014-12-23 01:40:01'),
(8, 'Ron', 'Weasley', 'ron.weasley@hogwarts.co.uk', 'weasley', 'true', '07415896223', '283, High Road Leyton, Leyton, London', 'WC1E', '6BT', 'WC1E6BT', '51.524773', '-0.133412', '1998-02-04', 'false', 'Male', 'fishing, biking', 'true', 'true', 'true', '2014-12-19 09:31:20', '2014-12-23 01:39:45'),
(9, 'Bruce', 'Wayne', 'brucewayne', 'iambatman', 'true', '07415896223', '283, High Road Leyton, Leyton, London', 'E10', '5QN', 'E105QN', '51.558995', '-0.007507', '1998-09-24', 'true', '', '', '', '', '', '2014-12-23 18:16:00', '0000-00-00 00:00:00'),
(19, 'Sheldon', 'Cooper', 'sheldon.cooper@bigbangtheory.com', 'shelly', 'true', '07415896223', '283, High Road Leyton, Leyton, London', 'WC2A', '2AE', 'WC2A2AE', '51.513966', '-0.116732', '1970-01-30', 'false', '', '', '', '', '', '2014-12-27 01:15:28', '0000-00-00 00:00:00'),
(23, 'her', 'herty', 'asdf', 'tere', 'true', '', '', '', '', '', '0.000000', '0.000000', '0000-00-00', '', '', '', '', '', '', '2015-01-17 06:02:27', '0000-00-00 00:00:00'),
(24, 'hec', 'fds', 'asd22', 'qwer', 'true', '', '', '', '', '', '0.000000', '0.000000', '0000-00-00', '', '', '', '', '', '', '2015-01-17 06:06:57', '0000-00-00 00:00:00'),
(25, 'hello', 'test', 'tesf', 'qasd', 'true', '', '', '', '', '', '0.000000', '0.000000', '0000-00-00', '', '', '', '', '', '', '2015-01-17 06:29:24', '0000-00-00 00:00:00'),
(26, 'a', 'a', 'a', 'a', 'true', '', '', '', '', '', '0.000000', '0.000000', '0000-00-00', '', '', '', '', '', '', '2015-01-17 09:27:34', '0000-00-00 00:00:00'),
(27, 'etestst', 'adafds', 'addffas', 'asdasfa', '', '', '', '', '', '', '0.000000', '0.000000', '0000-00-00', '', '', '', '', '', '', '2015-01-17 10:08:17', '0000-00-00 00:00:00'),
(28, 'etestst', 'adafds', 'addffas', 'asdasfa', '', '', '', '', '', '', '0.000000', '0.000000', '0000-00-00', '', '', '', '', '', '', '2015-01-17 10:08:58', '0000-00-00 00:00:00'),
(29, 'etestst', 'adafds', 'addffas', 'asdasfa', '', '', '', '', '', '', '0.000000', '0.000000', '0000-00-00', '', '', '', '', '', '', '2015-01-17 10:09:26', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `venue`
--

CREATE TABLE IF NOT EXISTS `venue` (
  `venue_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `v_name` varchar(50) NOT NULL,
  `v_type` varchar(50) NOT NULL,
  `address` varchar(255) NOT NULL,
  `post_code1` varchar(50) NOT NULL,
  `post_code2` varchar(50) NOT NULL,
  `post_code` varchar(50) NOT NULL,
  `latitude` decimal(9,6) NOT NULL,
  `longitude` decimal(9,6) NOT NULL,
  `max_cap` int(11) NOT NULL,
  `v_desc` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`venue_id`),
  KEY `user_id` (`user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=64 ;

--
-- Dumping data for table `venue`
--

INSERT INTO `venue` (`venue_id`, `user_id`, `v_name`, `v_type`, `address`, `post_code1`, `post_code2`, `post_code`, `latitude`, `longitude`, `max_cap`, `v_desc`, `created_at`, `updated_at`) VALUES
(54, 6, '8 Eastcheap meeting & training space', 'Meeting Room', 'Flat 9, 453, Caledonian Road, Holloway, London', 'WC1H', '0AY', 'WC1H0AY', '51.525635', '-0.133348', 240, 'Situated adjacent to the impressive "Walkie Talkie" and just minutes from Monument, Bank & Fenchurch Street Stations. WIth 14,000 sqft of space over 2 floors the venue offers a contemporary design and high quality meeting, training & conference space. Situated in a prime location at 8 Eastcheap making it easy to reach from all parts of The City.', '2014-12-08 20:51:35', '0000-00-00 00:00:00'),
(56, 6, 'Maple House', 'Conference Room', 'Flat 9, 453, Caledonian Road, Holloway, London', 'B4', '6TB', 'B46TB', '52.482390', '-1.893643', 160, 'Award winning training, conference and meeting venue located in Corporation Street, Birmingham. Wide range of meeting, training & conference rooms suitable for all types of events and exhibitions. Multiple Winner of Industry ''Gold Award'' for customer service. Our chefs are award winners at the prestigious CCE/IACC Chef''s Challenge Awards.', '2014-12-08 20:57:09', '0000-00-00 00:00:00'),
(57, 7, 'Hallam Conference Centre', 'Board Room', 'Flat 9, 453, Caledonian Road, Holloway, London', 'E10', '5PE', 'E105PE', '51.558913', '-0.009014', 250, 'PA systems, recording equipment & projection systems ensure a successful event. Ethernet, wireless LAN connections for external communications & visual fold back is provided for presentation ease.', '2014-12-08 21:04:25', '0000-00-00 00:00:00'),
(53, 1, '155 Bishopsgate Large conference venue', 'Conference Room', 'Flat 9, 453, Caledonian Road, Holloway, London', 'E10', '5NN', 'E105NN', '51.558603', '-0.008767', 550, 'Three conference suites with the largest accommodating up to 550 people, all adjacecnt to an exhibition space that can accommodate up to 35+ exhibition stands. Ideal for tech-based events, 155 Bishopsgate has all the latest AV and IT systems, including superfast WiFi and video conferencing. Fantastic location next to Liverpool Street Station.', '2014-12-08 20:37:46', '0000-00-00 00:00:00');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
