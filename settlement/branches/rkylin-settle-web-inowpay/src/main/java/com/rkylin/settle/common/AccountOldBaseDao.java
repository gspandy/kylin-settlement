package com.rkylin.settle.common;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * Created by thonny on 2015-5-27.
 */
public class AccountOldBaseDao extends SqlSessionDaoSupport {


    @Autowired(required = false)
    @Qualifier("sqlSessionFactory5")
    public void setSqlSessionFactory5(SqlSessionFactory sqlSessionFactory) {
    	super.setSqlSessionFactory(sqlSessionFactory);
        setBatchSqlSessionFactory(sqlSessionFactory);
    }

    private static int BATCH_DEAL_NUM = 500;

    protected static SqlSessionFactory batchSqlSessionFactory;

    /**
     * 批量插入记录
     * 默认500条，也可以通过修改BATCH_DEAL_NUM字段改变记录数量
     * @param statement
     * @param list
     * @return
     */
    public int batchInsert(String statement, List<?> list) {
        SqlSession batchSession = batchSqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int i = 0;
        try {
            for(int cnt = list.size(); i < cnt; i++) {
                try {
                    batchSession.insert(statement, list.get(i));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                if((i + 1) % BATCH_DEAL_NUM == 0) { //BATCH_DEAL_NUM为批量提交的条数
                    batchSession.commit();
                }
            }
            batchSession.commit();
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            batchSession.close();
        }
        return i;
    }

    /**
     * 批量更新记录
     * 默认500条，也可以通过修改BATCH_DEAL_NUM字段改变记录数量
     * @param statement
     * @param list
     * @return
     */
    public int batchUpdate(String statement, List<?> list) {
        SqlSession batchSession = batchSqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int i = 0;
        try {
            for(int cnt = list.size(); i < cnt; i++) {
                batchSession.update(statement, list.get(i));
                if((i + 1) % BATCH_DEAL_NUM == 0) {
                    batchSession.commit();
                }
            }
            batchSession.commit();
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            batchSession.close();
        }
        return i;
    }

    /**
     * 批量删除记录
     * 默认500条，也可以通过修改BATCH_DEAL_NUM字段改变记录数量
     * @param statement
     * @param list
     * @return
     */
    public int batchDelete(String statement, List<?> list) {
        SqlSession batchSession = batchSqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int i = 0;
        try {
            for(int cnt = list.size(); i < cnt; i++) {
                batchSession.delete(statement, list.get(i));
                if((i + 1) % BATCH_DEAL_NUM == 0) {
                    batchSession.commit();
                }
            }
            batchSession.commit();
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            batchSession.close();
        }
        return i;
    }

    public void setBatchSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        batchSqlSessionFactory = sqlSessionFactory;
    }

    public static SqlSessionFactory getBatchSqlSessionFactory() {
        return batchSqlSessionFactory;
    }
}
