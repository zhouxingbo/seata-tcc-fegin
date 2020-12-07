package com.alibaba.cloud.examples;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 余额账户 DAO 实现
 */
@Component
public class AccountDAO {

    @Resource(name = "nameUserJdbcTemplate")
    private NamedParameterJdbcTemplate jdbc;

    public Account selectById(Long userId) {
        String sql="SELECT * FROM account WHERE `user_id`=:user_id";
        Map<String,Object> params=new HashMap<>();
        params.put("user_id",userId);
        return jdbc.queryForObject(sql, params, new BeanPropertyRowMapper<>(Account.class));
    }

    public int updateFrozen(Long userId, BigDecimal subtract, BigDecimal add) {
        Map<String, Object> params = new HashMap<>();
        String sql = "UPDATE account SET `residue`=:residue,`frozen`=:frozen WHERE `user_id`=:userId ";
        params.put("userId", userId);
        params.put("residue", subtract);
        params.put("frozen", add);
        return jdbc.update(sql, params);
    }

    public int updateFrozenToUsed(long userId, BigDecimal money) {
        Map<String,Object> params=new HashMap<>();
        String sql="UPDATE account SET `frozen`=`frozen`-:frozen, `used`=`used`+:used WHERE `user_id`=:userId ";
        params.put("userId",userId);
        params.put("frozen",money);
        params.put("used",money);
        return jdbc.update(sql,params);

    }

    public int updateFrozenToResidue(long userId, BigDecimal money) {
        Map<String,Object> params=new HashMap<>();
        String sql="UPDATE account SET `frozen`=`frozen`-:money, `residue`=`residue`+:money WHERE `user_id`=:userId ";
        params.put("userId",userId);
        params.put("frozen",money);
        params.put("residue",money);
        return jdbc.update(sql,params);

    }
}
