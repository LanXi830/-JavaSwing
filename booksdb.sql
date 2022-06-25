/*
 Navicat Premium Data Transfer

 Source Server         : 蓝曦
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : booksdb

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 25/06/2022 11:07:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `bkID` char(9) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '书号',
  `bkName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '书名',
  `bkAuthor` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作者',
  `bkPress` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出版社',
  `bkPrice` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单价',
  `bkStatus` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否在馆',
  PRIMARY KEY (`bkID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES ('bk2021001', '数据库原理及应用教程', '王凯', '机械功业出版社', '38.56', '在馆');
INSERT INTO `book` VALUES ('bk2021002', '高等数学', '吴迪', '同济大学出版社', '51.46', '在馆');
INSERT INTO `book` VALUES ('bk2021003', '心理学', '刘心', '北京师范大学出版社', '22.00', '在馆');
INSERT INTO `book` VALUES ('bk2021004', '古代诗词鉴赏', '陈荣', '中华书局', '34.55', '在馆');
INSERT INTO `book` VALUES ('bk2021005', '离散数学', '崔艳荣', '长江出版社', '32.00', '在馆');

-- ----------------------------
-- Table structure for borrow
-- ----------------------------
DROP TABLE IF EXISTS `borrow`;
CREATE TABLE `borrow`  (
  `rdID` char(9) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '读者编号',
  `bkID` char(9) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '书号',
  `DateBorrow` datetime(0) NULL DEFAULT NULL COMMENT '借书日期',
  `DateLendPlan` datetime(0) NULL DEFAULT NULL COMMENT '应还日期',
  `DateLendAct` datetime(0) NULL DEFAULT NULL COMMENT '实际还书日期',
  INDEX `rdID`(`rdID`) USING BTREE,
  INDEX `bkID`(`bkID`) USING BTREE,
  CONSTRAINT `borrow_ibfk_1` FOREIGN KEY (`rdID`) REFERENCES `reader` (`rdID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `borrow_ibfk_2` FOREIGN KEY (`bkID`) REFERENCES `book` (`bkID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of borrow
-- ----------------------------
INSERT INTO `borrow` VALUES ('rd2021001', 'bk2021001', '2021-11-09 16:30:31', '2021-12-09 16:30:31', '2021-11-09 16:30:50');
INSERT INTO `borrow` VALUES ('rd2021005', 'bk2021001', '2021-11-09 16:40:50', '2021-11-16 16:40:50', '2021-11-09 16:41:25');
INSERT INTO `borrow` VALUES ('rd2021001', 'bk2021004', '2021-11-09 16:42:09', '2021-12-09 16:42:09', '2021-11-09 16:42:31');
INSERT INTO `borrow` VALUES ('rd2021001', 'bk2021001', '2021-11-09 16:50:12', '2021-12-09 16:50:12', '2021-11-09 16:50:41');
INSERT INTO `borrow` VALUES ('rd2021001', 'bk2021001', '2021-11-09 16:51:18', '2021-12-09 16:51:18', '2021-11-09 16:52:16');
INSERT INTO `borrow` VALUES ('rd2021001', 'bk2021001', '2021-11-13 18:15:48', '2021-12-13 18:15:48', '2021-11-13 18:19:48');
INSERT INTO `borrow` VALUES ('rd2021002', 'bk2021001', '2021-11-13 18:18:21', '2021-11-18 18:18:21', '2021-11-13 21:16:21');
INSERT INTO `borrow` VALUES ('rd2021003', 'bk2021001', '2021-11-13 18:18:42', '2021-11-23 18:18:42', '2021-11-13 21:16:39');
INSERT INTO `borrow` VALUES ('rd2021004', 'bk2021001', '2021-11-13 18:19:02', '2021-11-28 18:19:02', '2021-11-13 21:17:00');
INSERT INTO `borrow` VALUES ('rd2021005', 'bk2021001', '2022-06-06 18:03:15', '2022-06-13 18:03:15', '2022-06-06 18:06:47');
INSERT INTO `borrow` VALUES ('rd2021001', 'bk2021001', '2022-06-06 18:07:30', '2022-07-06 18:07:30', '2022-06-25 09:57:55');
INSERT INTO `borrow` VALUES ('rd2021001', 'bk2021002', '2022-06-06 18:08:24', '2022-07-06 18:08:24', '0000-00-00 00:00:00');

-- ----------------------------
-- Table structure for reader
-- ----------------------------
DROP TABLE IF EXISTS `reader`;
CREATE TABLE `reader`  (
  `rdID` char(9) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '读者编号',
  `rdType` int(0) NULL DEFAULT NULL COMMENT '读者类别号',
  `rdName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '读者姓名',
  `rdDept` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '读者单位',
  `rdQQ` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '读者QQ',
  `rdBorrowQty` int(0) NULL DEFAULT 0 COMMENT '已借书数量',
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`rdID`) USING BTREE,
  INDEX `rdType`(`rdType`) USING BTREE,
  CONSTRAINT `reader_ibfk_1` FOREIGN KEY (`rdType`) REFERENCES `readertype` (`rdType`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reader
-- ----------------------------
INSERT INTO `reader` VALUES ('rd2021001', 1, '罗翔', '北京大学法学系', '1235446', 1, '1234');
INSERT INTO `reader` VALUES ('rd2021002', 2, '张三', '长江小学', '115639', 0, '1234');
INSERT INTO `reader` VALUES ('rd2021003', 3, '牛顿', '清华大学物理系', '125786', 0, '1234');
INSERT INTO `reader` VALUES ('rd2021004', 4, '爱因斯坦', '中国科学院', '123789', 0, '1234');
INSERT INTO `reader` VALUES ('rd2021005', 5, '蓝曦', '长江大学', '1290314053', 0, '1234');
INSERT INTO `reader` VALUES ('rd2021006', 6, '吴亦凡', '北京市朝阳公安局', '已退网', 0, '1234');

-- ----------------------------
-- Table structure for readertype
-- ----------------------------
DROP TABLE IF EXISTS `readertype`;
CREATE TABLE `readertype`  (
  `rdType` int(0) NOT NULL COMMENT '读者类别号',
  `rdTypeName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '读者类别名称',
  `canLendQty` int(0) NULL DEFAULT NULL COMMENT '可借书数量',
  `canLendDay` int(0) NULL DEFAULT NULL COMMENT '可借书天数',
  PRIMARY KEY (`rdType`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of readertype
-- ----------------------------
INSERT INTO `readertype` VALUES (1, '教师', 10, 30);
INSERT INTO `readertype` VALUES (2, '小学生', 1, 5);
INSERT INTO `readertype` VALUES (3, '硕士', 5, 10);
INSERT INTO `readertype` VALUES (4, '博士', 7, 15);
INSERT INTO `readertype` VALUES (5, '本科生', 2, 7);
INSERT INTO `readertype` VALUES (6, '犯人', 0, 0);

-- ----------------------------
-- Procedure structure for sp_get
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_get`;
delimiter ;;
CREATE PROCEDURE `sp_get`()
BEGIN
select rdName, canLendQty, canLendDay, rdBorrowQty from reader, readertype  where reader.rdType = readerType.rdType;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for sp_getName
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_getName`;
delimiter ;;
CREATE PROCEDURE `sp_getName`(IN sID CHAR(9),OUT sName VARCHAR(20))
BEGIN
SELECT rdName INTO sName FROM reader WHERE sID=rdID;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
