-- ���ڻ��ƴ�����, ���������ֲ���. �Ƚ���ʱ�����˻����˽ӿڼ�ȥ������
update SETTLE_PROFIT_RULE set SETTLE_OBJECT='INTER_USER_ID', SETTLE_MAIN='4411' where PROFIT_DETAIL_ID in (3020,3021,3022,3023) and SUB_ID = 1 and STATUS_ID = 1
-- ���η��� ʹ��40144 �������� �����״��� ����ͷ�����
UPDATE `settle`.`SETTLE_PROFIT_RULE` SET `SETTLE_MAIN` = '40144' WHERE (`PROFIT_DETAIL_ID` = '3009') AND (`SUB_ID` = '2');