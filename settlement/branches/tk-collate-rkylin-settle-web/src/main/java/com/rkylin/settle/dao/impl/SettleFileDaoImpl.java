/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.settle.dao.SettleFileDao;
import com.rkylin.settle.pojo.SettleFile;
import com.rkylin.settle.pojo.SettleFileQuery;

@Repository("settleFileDao")
public class SettleFileDaoImpl extends BaseDao implements SettleFileDao {
	
	@Override
	public int countByExample(SettleFileQuery example) {
		return super.getSqlSession().selectOne("SettleFileMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleFileQuery example) {
		return super.getSqlSession().delete("SettleFileMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleFileMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleFile record) {
		return super.getSqlSession().insert("SettleFileMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleFile record) {
		return super.getSqlSession().insert("SettleFileMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleFile> selectByExample(SettleFileQuery example) {
		return super.getSqlSession().selectList("SettleFileMapper.selectByExample", example);
	}
	
	@Override
	public SettleFile selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleFileMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleFile record) {
		return super.getSqlSession().update("SettleFileMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleFile record) {
		return super.getSqlSession().update("SettleFileMapper.updateByPrimaryKey", record);
	}

    @Override
    public List<SettleFile> selectByPreExample(SettleFileQuery example) {
        return super.getSqlSession().selectList("SettleFileMapper.selectByPreExample", example);
    }
	
}
