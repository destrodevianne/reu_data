/*
Navicat MySQL Data Transfer

Source Server         : ServerConnection
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : l2javengersgs

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2013-04-30 22:12:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `herb_droplist_groups`
-- ----------------------------
DROP TABLE IF EXISTS `herb_droplist_groups`;
CREATE TABLE `herb_droplist_groups` (
  `groupId` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `itemId` smallint(5) unsigned NOT NULL DEFAULT '0',
  `min` smallint(2) unsigned NOT NULL DEFAULT '0',
  `max` smallint(2) unsigned NOT NULL DEFAULT '0',
  `category` smallint(3) NOT NULL DEFAULT '0',
  `chance` mediumint(7) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`groupId`,`itemId`,`category`),
  KEY `key_mobId` (`groupId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of herb_droplist_groups
-- ----------------------------
INSERT INTO `herb_droplist_groups` VALUES ('1', '13028', '1', '1', '0', '20000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8600', '1', '1', '1', '100000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8601', '1', '1', '1', '40000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8602', '1', '1', '1', '8000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8603', '1', '1', '2', '100000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8604', '1', '1', '2', '40000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8605', '1', '1', '2', '8000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8612', '1', '1', '3', '2000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8613', '1', '1', '3', '2000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8614', '1', '1', '3', '2000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8606', '1', '1', '4', '150000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8607', '1', '1', '5', '150000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8608', '1', '1', '6', '150000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8609', '1', '1', '7', '150000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8610', '1', '1', '8', '150000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '8611', '1', '1', '9', '150000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '10655', '1', '1', '10', '150000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '10656', '1', '1', '11', '150000');
INSERT INTO `herb_droplist_groups` VALUES ('1', '10657', '1', '1', '12', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('2', '13028', '1', '1', '0', '20000');
INSERT INTO `herb_droplist_groups` VALUES ('2', '8600', '1', '1', '1', '200000');
INSERT INTO `herb_droplist_groups` VALUES ('2', '8601', '1', '1', '1', '80000');
INSERT INTO `herb_droplist_groups` VALUES ('2', '8602', '1', '1', '1', '16000');
INSERT INTO `herb_droplist_groups` VALUES ('2', '8603', '1', '1', '2', '150000');
INSERT INTO `herb_droplist_groups` VALUES ('2', '8604', '1', '1', '2', '60000');
INSERT INTO `herb_droplist_groups` VALUES ('2', '8605', '1', '1', '2', '12000');
INSERT INTO `herb_droplist_groups` VALUES ('2', '8613', '1', '1', '3', '4000');
INSERT INTO `herb_droplist_groups` VALUES ('2', '8614', '1', '1', '3', '4000');
INSERT INTO `herb_droplist_groups` VALUES ('2', '8607', '1', '1', '4', '150000');
INSERT INTO `herb_droplist_groups` VALUES ('2', '8609', '1', '1', '5', '150000');
INSERT INTO `herb_droplist_groups` VALUES ('2', '8611', '1', '1', '6', '150000');
INSERT INTO `herb_droplist_groups` VALUES ('2', '10657', '1', '1', '7', '75000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '13028', '1', '1', '0', '5000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8600', '1', '1', '1', '210000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8601', '1', '1', '1', '150000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8602', '1', '1', '1', '35000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8603', '1', '1', '2', '190000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8604', '1', '1', '2', '180000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8605', '1', '1', '2', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8612', '1', '1', '3', '2000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8613', '1', '1', '3', '10000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8614', '1', '1', '3', '2000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8606', '1', '1', '4', '60000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8607', '1', '1', '5', '60000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8608', '1', '1', '6', '80000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8609', '1', '1', '7', '40000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8610', '1', '1', '8', '20000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '8611', '1', '1', '9', '90000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '10655', '1', '1', '10', '60000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '10656', '1', '1', '11', '30000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '10657', '1', '1', '12', '5000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '14824', '1', '1', '13', '9000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '14825', '1', '1', '13', '9000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '14826', '1', '1', '13', '25000');
INSERT INTO `herb_droplist_groups` VALUES ('3', '14827', '1', '1', '13', '30000');
INSERT INTO `herb_droplist_groups` VALUES ('4', '13028', '1', '1', '0', '3299');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8600', '1', '1', '1', '231000');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8601', '1', '1', '1', '159600');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8602', '1', '1', '1', '29400');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8603', '1', '1', '2', '44000');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8604', '1', '1', '2', '57200');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8605', '1', '1', '2', '8800');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8612', '1', '1', '3', '3300');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8613', '1', '1', '3', '3300');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8614', '1', '1', '3', '3400');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8606', '1', '1', '4', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8608', '1', '1', '4', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8610', '1', '1', '4', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('4', '10655', '1', '1', '4', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('4', '10656', '1', '1', '4', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8607', '1', '1', '5', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8609', '1', '1', '5', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('4', '8611', '1', '1', '6', '103400');
INSERT INTO `herb_droplist_groups` VALUES ('4', '10657', '1', '1', '6', '3299');
INSERT INTO `herb_droplist_groups` VALUES ('5', '8600', '1', '1', '1', '275000');
INSERT INTO `herb_droplist_groups` VALUES ('5', '8601', '1', '1', '1', '190000');
INSERT INTO `herb_droplist_groups` VALUES ('5', '8602', '1', '1', '1', '35000');
INSERT INTO `herb_droplist_groups` VALUES ('6', '13028', '1', '1', '0', '3300');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8600', '1', '1', '1', '231000');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8601', '1', '1', '1', '159600');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8602', '1', '1', '1', '29400');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8603', '1', '1', '2', '44000');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8604', '1', '1', '2', '57200');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8605', '1', '1', '2', '8800');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8606', '1', '1', '3', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8608', '1', '1', '3', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8610', '1', '1', '3', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('6', '10655', '1', '1', '3', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('6', '10656', '1', '1', '3', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8607', '1', '1', '4', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8609', '1', '1', '4', '50000');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8612', '1', '1', '5', '3300');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8613', '1', '1', '5', '3300');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8614', '1', '1', '5', '3400');
INSERT INTO `herb_droplist_groups` VALUES ('6', '8611', '1', '1', '6', '103400');
INSERT INTO `herb_droplist_groups` VALUES ('6', '10657', '1', '1', '6', '3300');
INSERT INTO `herb_droplist_groups` VALUES ('6', '10655', '1', '1', '7', '450000');
