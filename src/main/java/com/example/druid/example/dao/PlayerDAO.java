package com.example.druid.example.dao;


import com.example.druid.db.dao.EntityDao;
import com.example.druid.example.entity.PlayerEntity;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class PlayerDAO extends EntityDao<PlayerEntity> {

    /**
     * 通过玩家标识获取玩家实体
     * @param actorId 玩家ID
     * @return 返回玩家实体
     */
    public  PlayerEntity getPlayerEntity(long actorId) {
        return  getEntityByPK(actorId);
    }


    /**
     * 通过玩家用户名获取玩家实体
     * @param username 用户名
     * @return 返回玩家实体
     */
    public  PlayerEntity getPlayerEntityByUsername(String username) {
        return  null;
    }

    @Override
    protected Class<PlayerEntity> forClass() {
        return PlayerEntity.class;
    }
}
