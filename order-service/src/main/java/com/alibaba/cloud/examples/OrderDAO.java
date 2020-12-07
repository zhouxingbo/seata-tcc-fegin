package com.alibaba.cloud.examples;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 余额账户 DAO 实现
 */
@Component
public class OrderDAO {

    @Resource(name = "nameUserJdbcTemplate")
    private NamedParameterJdbcTemplate jdbc;


    public int create(Order order) {
        Map<String,Object> params=new HashMap<>();
        String sql="INSERT INTO `order` (`id`,`user_id`,`product_id`,`count`,`money`,`status`)\n" +
                "        VALUES(:id:, :userId:, :productId:, :count:, :money:,1)";

        params.put("id",order.getId());
        params.put("userId",order.getUserId());
        params.put("productId",order.getProductId());
        params.put("count",order.getCount());
        params.put("money",order.getMoney());
        return jdbc.update(sql,params);
    }

    public int updateStatus(long orderId, int status) {
        Map<String,Object> params=new HashMap<>();
        String sql="UPDATE `order` SET `status`=:status WHERE `id`=:orderId:";
        params.put("status",orderId);
        params.put("orderId",orderId);
        return jdbc.update(sql,params);
    }

    public int deleteById(long orderId) {
        Map<String,Object> params=new HashMap<>();
        String sql="DELETE FROM `order` WHERE `id`=:orderId ";
        params.put("orderId",orderId);
        return jdbc.update(sql,params);

    }
}
