-- 万众会唐贷款还款交易, 做虚拟提现操作. 既结算时调用账户调账接口减去结算金额
update SETTLE_PROFIT_RULE set SETTLE_OBJECT='INTER_USER_ID', SETTLE_MAIN='4411' where PROFIT_DETAIL_ID in (3020,3021,3022,3023) and SUB_ID = 1 and STATUS_ID = 1
-- 旅游分期 使用40144 民生渠道 将交易代付 并有头寸操作
UPDATE `settle`.`SETTLE_PROFIT_RULE` SET `SETTLE_MAIN` = '40144' WHERE (`PROFIT_DETAIL_ID` = '3009') AND (`SUB_ID` = '2');