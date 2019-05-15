package com.tanglover.sso.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @author TangXu
 * @create 2019-05-15 16:18
 * @description:
 */
@Repository
public class BaseDao {

    private static final Logger logger = LoggerFactory.getLogger(BaseDao.class);

    //定义JDBC
    @Resource(name = "_np")
    public NamedParameterJdbcTemplate _np;

    SimpleDateFormat sdfYm = new SimpleDateFormat("yyyyMM");
    SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyyMMdd");

    public NamedParameterJdbcTemplate jdbcTemplate() {
        return _np;
    }

    /**
     * Execute a query given static SQL, mapping each row to a Java object
     *
     * @param beanClass
     * @param sql
     * @param <T>
     * @return
     */
    public <T> T query(Class<T> beanClass, String sql) {
        try {
            List<T> list = this._np.getJdbcOperations().query(sql, new BeanPropertyRowMapper<>(beanClass));
            if (null != list && 0 < list.size()) {
                return list.get(0);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("BaseDao query", e);
        }
        return null;
    }

    /**
     * Execute a query given static SQL, mapping each row to a list
     *
     * @param sql
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> queryForList(String sql, Map<String, ?> paramMap) {
        try {
            return this._np.queryForList(sql, paramMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("BaseDao queryForList", e);
        }
        return null;
    }

    /**
     * Execute a query given static SQL, mapping each row to a map
     *
     * @param sql
     * @param paramMap
     * @return
     */
    public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap) {
        try {
            return this._np.queryForMap(sql, paramMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("BaseDao queryForMap", e);
        }
        return null;
    }

}