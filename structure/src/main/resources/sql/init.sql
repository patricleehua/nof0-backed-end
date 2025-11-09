-- MySQL dump 10.13  Distrib 8.0.39, for Win64 (x86_64)
--
-- Host: localhost    Database: nof0-backed-end
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account_equity_snapshots`
--

DROP TABLE IF EXISTS `account_equity_snapshots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_equity_snapshots` (
                                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                            `model_id` varchar(255) NOT NULL COMMENT '模型ID',
                                            `ts_ms` bigint NOT NULL COMMENT '时间戳(毫秒)',
                                            `dollar_equity` double NOT NULL COMMENT '美元权益',
                                            `realized_pnl` double NOT NULL DEFAULT '0' COMMENT '已实现盈亏',
                                            `total_unrealized_pnl` double NOT NULL DEFAULT '0' COMMENT '总未实现盈亏',
                                            `cum_pnl_pct` double DEFAULT NULL COMMENT '累计盈亏百分比',
                                            `sharpe_ratio` double DEFAULT NULL COMMENT '夏普比率',
                                            `since_inception_hourly_marker` int DEFAULT NULL COMMENT '起始小时标记',
                                            `since_inception_minute_marker` int DEFAULT NULL COMMENT '起始分钟标记',
                                            `metadata` json NOT NULL DEFAULT (json_object()) COMMENT '元数据',
                                            `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
                                            PRIMARY KEY (`id`),
                                            UNIQUE KEY `uk_model_id_ts_ms` (`model_id`,`ts_ms`) COMMENT '模型时间唯一索引',
                                            KEY `idx_equity_snapshots_model_ts_desc` (`model_id`,`ts_ms` DESC) COMMENT '权益曲线查询索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账户权益快照表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_equity_snapshots`
--

LOCK TABLES `account_equity_snapshots` WRITE;
/*!40000 ALTER TABLE `account_equity_snapshots` DISABLE KEYS */;
/*!40000 ALTER TABLE `account_equity_snapshots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
                            `model_id` varchar(255) NOT NULL COMMENT '关联模型',
                            `exchange_provider` varchar(255) NOT NULL COMMENT '交易所',
                            `account_tag` varchar(255) DEFAULT NULL COMMENT '账户标签',
                            `margin_mode` varchar(255) DEFAULT NULL COMMENT '保证金模式',
                            `base_currency` varchar(255) DEFAULT NULL COMMENT '基础货币',
                            `leverage_mode` varchar(255) DEFAULT NULL COMMENT '杠杆模式',
                            `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
                            `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
                            `metadata` json NOT NULL DEFAULT (json_object()) COMMENT '元数据',
                            PRIMARY KEY (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='交易账户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conversation_messages`
--

DROP TABLE IF EXISTS `conversation_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conversation_messages` (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息ID',
                                         `conversation_id` bigint NOT NULL COMMENT '对话ID',
                                         `role` varchar(50) NOT NULL COMMENT '角色: system/user/assistant',
                                         `content` text NOT NULL COMMENT '消息内容',
                                         `ts_ms` bigint DEFAULT NULL COMMENT '时间戳(毫秒)',
                                         `metadata` json NOT NULL DEFAULT (json_object()) COMMENT '元数据',
                                         `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
                                         PRIMARY KEY (`id`),
                                         KEY `idx_conversation_messages_conv_ts` (`conversation_id`,`ts_ms`) COMMENT '对话时间索引',
                                         CONSTRAINT `conversation_messages_ibfk_1` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='对话消息详情表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conversation_messages`
--

LOCK TABLES `conversation_messages` WRITE;
/*!40000 ALTER TABLE `conversation_messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `conversation_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conversations`
--

DROP TABLE IF EXISTS `conversations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conversations` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '对话ID',
                                 `model_id` varchar(255) NOT NULL COMMENT '模型ID',
                                 `topic` text COMMENT '对话主题',
                                 `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_conversations_model` (`model_id`) COMMENT '模型索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='对话记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conversations`
--

LOCK TABLES `conversations` WRITE;
/*!40000 ALTER TABLE `conversations` DISABLE KEYS */;
/*!40000 ALTER TABLE `conversations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `decision_cycles`
--

DROP TABLE IF EXISTS `decision_cycles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `decision_cycles` (
                                   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '决策周期ID',
                                   `model_id` varchar(255) NOT NULL COMMENT '模型ID',
                                   `cycle_number` int DEFAULT NULL COMMENT '周期编号',
                                   `prompt_digest` text COMMENT '提示摘要',
                                   `cot_trace` text COMMENT '思维链追踪',
                                   `decisions` json DEFAULT NULL COMMENT '决策数据',
                                   `success` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否成功',
                                   `error_message` text COMMENT '错误信息',
                                   `executed_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '执行时间',
                                   PRIMARY KEY (`id`),
                                   KEY `idx_decision_cycles_model_executed_at_desc` (`model_id`,`executed_at` DESC) COMMENT '模型执行时间索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AI决策周期表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `decision_cycles`
--

LOCK TABLES `decision_cycles` WRITE;
/*!40000 ALTER TABLE `decision_cycles` DISABLE KEYS */;
/*!40000 ALTER TABLE `decision_cycles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `market_asset_ctx`
--

DROP TABLE IF EXISTS `market_asset_ctx`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `market_asset_ctx` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '上下文ID',
                                    `provider` varchar(255) NOT NULL COMMENT '数据提供商',
                                    `symbol` varchar(255) NOT NULL COMMENT '交易对',
                                    `funding` double DEFAULT NULL COMMENT '资金费率',
                                    `open_interest` double DEFAULT NULL COMMENT '未平仓量',
                                    `oracle_px` double DEFAULT NULL COMMENT '预言机价格',
                                    `mark_px` double DEFAULT NULL COMMENT '标记价格',
                                    `mid_px` double DEFAULT NULL COMMENT '中间价格',
                                    `impact_pxs` json DEFAULT NULL COMMENT '影响价格',
                                    `prev_day_px` double DEFAULT NULL COMMENT '前一日价格',
                                    `day_ntl_vlm` double DEFAULT NULL COMMENT '日名义交易量',
                                    `day_base_vlm` double DEFAULT NULL COMMENT '日基础交易量',
                                    `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `uk_provider_symbol` (`provider`,`symbol`) COMMENT '提供商交易对唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='市场资产上下文表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `market_asset_ctx`
--

LOCK TABLES `market_asset_ctx` WRITE;
/*!40000 ALTER TABLE `market_asset_ctx` DISABLE KEYS */;
/*!40000 ALTER TABLE `market_asset_ctx` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `market_assets`
--

DROP TABLE IF EXISTS `market_assets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `market_assets` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '资产ID',
                                 `provider` varchar(255) NOT NULL COMMENT '数据提供商',
                                 `symbol` varchar(255) NOT NULL COMMENT '交易对',
                                 `name` text COMMENT '资产名称',
                                 `sz_decimals` int DEFAULT NULL COMMENT '规模小数位数',
                                 `max_leverage` double DEFAULT NULL COMMENT '最大杠杆',
                                 `only_isolated` tinyint(1) DEFAULT NULL COMMENT '仅隔离保证金',
                                 `margin_table_id` int DEFAULT NULL COMMENT '保证金表ID',
                                 `is_delisted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否下架',
                                 `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `uk_provider_symbol` (`provider`,`symbol`) COMMENT '提供商交易对唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='市场资产元数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `market_assets`
--

LOCK TABLES `market_assets` WRITE;
/*!40000 ALTER TABLE `market_assets` DISABLE KEYS */;
/*!40000 ALTER TABLE `market_assets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `model_analytics`
--

DROP TABLE IF EXISTS `model_analytics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `model_analytics` (
                                   `model_id` varchar(255) NOT NULL COMMENT '模型ID',
                                   `payload` json NOT NULL COMMENT '分析结果数据',
                                   `server_time_ms` bigint NOT NULL COMMENT '服务器时间(毫秒)',
                                   `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
                                   `metadata` json NOT NULL DEFAULT (json_object()) COMMENT '元数据',
                                   PRIMARY KEY (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='模型分析数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `model_analytics`
--

LOCK TABLES `model_analytics` WRITE;
/*!40000 ALTER TABLE `model_analytics` DISABLE KEYS */;
/*!40000 ALTER TABLE `model_analytics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `models`
--

DROP TABLE IF EXISTS `models`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `models` (
                          `id` varchar(255) NOT NULL COMMENT '模型唯一标识',
                          `display_name` text NOT NULL COMMENT '显示名称',
                          `description` text COMMENT '描述',
                          `metadata` json NOT NULL DEFAULT (json_object()) COMMENT '模型元数据',
                          `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
                          `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AI模型信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `models`
--

LOCK TABLES `models` WRITE;
/*!40000 ALTER TABLE `models` DISABLE KEYS */;
INSERT INTO `models` VALUES ('1','gpt-5','gpt-5','{}','2025-11-09 16:38:58.521866','2025-11-09 16:38:58.521866'),('2','deepseek-chat-v3.1','deepseek-chat-v3.1','{}','2025-11-09 16:38:58.521866','2025-11-09 16:38:58.521866'),('3','qwen3-max','qwen3-max','{}','2025-11-09 16:38:58.521866','2025-11-09 16:40:12.813580'),('4','grok-4','grok-4','{}','2025-11-09 16:38:58.521866','2025-11-09 16:38:58.521866'),('5','gemini-2.5-pro','gemini-2.5-pro','{}','2025-11-09 16:38:58.521866','2025-11-09 16:38:58.521866'),('6','claude-sonnet-4-5','claude-sonnet-4-5','{}','2025-11-09 16:38:58.521866','2025-11-09 16:38:58.521866');
/*!40000 ALTER TABLE `models` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `positions`
--

DROP TABLE IF EXISTS `positions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `positions` (
                             `id` varchar(255) NOT NULL COMMENT '持仓ID',
                             `model_id` varchar(255) NOT NULL COMMENT '模型ID',
                             `exchange_provider` varchar(255) NOT NULL COMMENT '交易所',
                             `symbol` varchar(255) NOT NULL COMMENT '交易对',
                             `side` varchar(50) NOT NULL DEFAULT 'long' COMMENT '多空方向: long/short/flat',
                             `status` varchar(50) NOT NULL DEFAULT 'open' COMMENT '状态: open/closed',
                             `entry_oid` bigint DEFAULT NULL COMMENT '入场订单ID',
                             `risk_usd` double DEFAULT NULL COMMENT '风险金额(USD)',
                             `confidence` double DEFAULT NULL COMMENT '置信度',
                             `index_col` json DEFAULT NULL COMMENT '索引列',
                             `exit_plan` json DEFAULT NULL COMMENT '出场计划',
                             `entry_time_ms` bigint NOT NULL COMMENT '入场时间(毫秒)',
                             `entry_price` double NOT NULL COMMENT '入场价格',
                             `tp_oid` bigint DEFAULT NULL COMMENT '止盈订单ID',
                             `margin` double DEFAULT NULL COMMENT '保证金',
                             `wait_for_fill` tinyint(1) NOT NULL DEFAULT '0' COMMENT '等待成交',
                             `sl_oid` bigint DEFAULT NULL COMMENT '止损订单ID',
                             `current_price` double DEFAULT NULL COMMENT '当前价格',
                             `closed_pnl` double DEFAULT NULL COMMENT '已平仓盈亏',
                             `liquidation_price` double DEFAULT NULL COMMENT '强平价格',
                             `commission` double DEFAULT NULL COMMENT '手续费',
                             `leverage` double DEFAULT NULL COMMENT '杠杆',
                             `slippage` double DEFAULT NULL COMMENT '滑点',
                             `quantity` double NOT NULL COMMENT '数量',
                             `unrealized_pnl` double DEFAULT NULL COMMENT '未实现盈亏',
                             `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
                             `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
                             PRIMARY KEY (`id`),
                             KEY `idx_positions_model` (`model_id`) COMMENT '模型索引',
                             KEY `idx_positions_symbol` (`symbol`) COMMENT '交易对索引',
                             KEY `idx_positions_model_exchange_open` (`model_id`,`exchange_provider`,`status`) COMMENT '模型交易所开仓状态索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='持仓信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `positions`
--

LOCK TABLES `positions` WRITE;
/*!40000 ALTER TABLE `positions` DISABLE KEYS */;
/*!40000 ALTER TABLE `positions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `price_latest`
--

DROP TABLE IF EXISTS `price_latest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `price_latest` (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                `provider` varchar(255) NOT NULL COMMENT '数据源',
                                `symbol` varchar(255) NOT NULL COMMENT '交易对',
                                `price` double NOT NULL COMMENT '价格',
                                `ts_ms` bigint NOT NULL COMMENT '时间戳(毫秒)',
                                `raw` json DEFAULT NULL COMMENT '原始数据',
                                `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `uk_provider_symbol` (`provider`,`symbol`) COMMENT '数据源交易对唯一索引',
                                KEY `idx_price_latest_symbol` (`symbol`) COMMENT '交易对索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='最新价格缓存表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `price_latest`
--

LOCK TABLES `price_latest` WRITE;
/*!40000 ALTER TABLE `price_latest` DISABLE KEYS */;
/*!40000 ALTER TABLE `price_latest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `price_ticks`
--

DROP TABLE IF EXISTS `price_ticks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `price_ticks` (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               `provider` varchar(255) NOT NULL COMMENT '数据源',
                               `symbol` varchar(255) NOT NULL COMMENT '交易对',
                               `price` double NOT NULL COMMENT '价格',
                               `ts_ms` bigint NOT NULL COMMENT '时间戳(毫秒)',
                               `volume` double DEFAULT NULL COMMENT '成交量',
                               `raw` json DEFAULT NULL COMMENT '原始数据',
                               `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
                               PRIMARY KEY (`id`),
                               KEY `idx_price_ticks_symbol_ts_desc` (`symbol`,`ts_ms` DESC) COMMENT '价格查询优化索引',
                               KEY `idx_price_ticks_provider_symbol_ts_desc` (`provider`,`symbol`,`ts_ms` DESC) COMMENT '数据源交易对时间索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='价格历史数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `price_ticks`
--

LOCK TABLES `price_ticks` WRITE;
/*!40000 ALTER TABLE `price_ticks` DISABLE KEYS */;
/*!40000 ALTER TABLE `price_ticks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `symbols`
--

DROP TABLE IF EXISTS `symbols`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `symbols` (
                           `symbol` varchar(255) NOT NULL COMMENT '交易对符号',
                           `base_asset` varchar(255) DEFAULT NULL COMMENT '基础资产',
                           `quote_asset` varchar(255) DEFAULT NULL COMMENT '计价资产',
                           `base_precision` int DEFAULT NULL COMMENT '基础资产精度',
                           `quote_precision` int DEFAULT NULL COMMENT '计价资产精度',
                           `tick_size` double DEFAULT NULL COMMENT '最小价格变动单位',
                           `metadata` json NOT NULL DEFAULT (json_object()) COMMENT '元数据',
                           `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
                           `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
                           PRIMARY KEY (`symbol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='交易对信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `symbols`
--

LOCK TABLES `symbols` WRITE;
/*!40000 ALTER TABLE `symbols` DISABLE KEYS */;
/*!40000 ALTER TABLE `symbols` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trader_state`
--

DROP TABLE IF EXISTS `trader_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trader_state` (
                                `trader_id` varchar(255) NOT NULL COMMENT '交易员ID',
                                `exchange_provider` varchar(255) NOT NULL COMMENT '交易所',
                                `market_provider` varchar(255) NOT NULL COMMENT '市场数据提供商',
                                `allocation_pct` double DEFAULT NULL COMMENT '资金分配百分比',
                                `cooldown` json NOT NULL DEFAULT (json_object()) COMMENT '冷却期配置',
                                `risk_guards` json NOT NULL DEFAULT (json_object()) COMMENT '风险控制',
                                `last_decision_at` datetime(6) DEFAULT NULL COMMENT '最后决策时间',
                                `pause_until` datetime(6) DEFAULT NULL COMMENT '暂停直到',
                                `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
                                PRIMARY KEY (`trader_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='交易员状态管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trader_state`
--

LOCK TABLES `trader_state` WRITE;
/*!40000 ALTER TABLE `trader_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `trader_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trades`
--

DROP TABLE IF EXISTS `trades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trades` (
                          `id` varchar(255) NOT NULL COMMENT '交易ID',
                          `model_id` varchar(255) NOT NULL COMMENT '模型ID',
                          `exchange_provider` varchar(255) NOT NULL COMMENT '交易所',
                          `symbol` varchar(255) NOT NULL COMMENT '交易对',
                          `side` varchar(50) NOT NULL COMMENT '方向',
                          `trade_type` varchar(255) DEFAULT NULL COMMENT '交易类型',
                          `trade_id` varchar(255) DEFAULT NULL COMMENT '交易ID',
                          `quantity` double DEFAULT NULL COMMENT '数量',
                          `leverage` double DEFAULT NULL COMMENT '杠杆',
                          `confidence` double DEFAULT NULL COMMENT '置信度',
                          `entry_price` double DEFAULT NULL COMMENT '入场价格',
                          `entry_ts_ms` bigint NOT NULL COMMENT '入场时间戳(毫秒)',
                          `entry_human_time` text COMMENT '入场可读时间',
                          `entry_sz` double DEFAULT NULL COMMENT '入场规模',
                          `entry_tid` bigint DEFAULT NULL COMMENT '入场交易ID',
                          `entry_oid` bigint DEFAULT NULL COMMENT '入场订单ID',
                          `entry_crossed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '入场是否交叉',
                          `entry_liquidation` json DEFAULT NULL COMMENT '入场清算信息',
                          `entry_commission_dollars` double DEFAULT NULL COMMENT '入场手续费(美元)',
                          `entry_closed_pnl` double DEFAULT NULL COMMENT '入场已平仓盈亏',
                          `exit_price` double DEFAULT NULL COMMENT '出场价格',
                          `exit_ts_ms` bigint DEFAULT NULL COMMENT '出场时间戳(毫秒)',
                          `exit_human_time` text COMMENT '出场可读时间',
                          `exit_sz` double DEFAULT NULL COMMENT '出场规模',
                          `exit_tid` bigint DEFAULT NULL COMMENT '出场交易ID',
                          `exit_oid` bigint DEFAULT NULL COMMENT '出场订单ID',
                          `exit_crossed` tinyint(1) DEFAULT NULL COMMENT '出场是否交叉',
                          `exit_liquidation` json DEFAULT NULL COMMENT '出场清算信息',
                          `exit_commission_dollars` double DEFAULT NULL COMMENT '出场手续费(美元)',
                          `exit_closed_pnl` double DEFAULT NULL COMMENT '出场已平仓盈亏',
                          `exit_plan` json DEFAULT NULL COMMENT '出场计划',
                          `realized_gross_pnl` double DEFAULT NULL COMMENT '毛已实现盈亏',
                          `realized_net_pnl` double DEFAULT NULL COMMENT '净已实现盈亏',
                          `total_commission_dollars` double DEFAULT NULL COMMENT '总手续费(美元)',
                          `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
                          `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
                          PRIMARY KEY (`id`),
                          KEY `idx_trades_model_entry_ts_desc` (`model_id`,`entry_ts_ms` DESC) COMMENT '交易历史查询索引',
                          KEY `idx_trades_exit_oid` (`exit_oid`) COMMENT '出场订单ID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='完整交易记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trades`
--

LOCK TABLES `trades` WRITE;
/*!40000 ALTER TABLE `trades` DISABLE KEYS */;
/*!40000 ALTER TABLE `trades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'nof0-backed-end'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-09 16:45:27
