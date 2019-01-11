package com.example.druid.example.entity;

import com.example.druid.db.annotation.Cloumn;
import com.example.druid.db.entity.Entity;
import com.example.druid.db.annotation.Table;
import lombok.Data;

/**
 * 玩家实体
 * @author linlazy
 */
@Data
@Table("player")
public class PlayerEntity extends Entity {

    /**
     * 玩家
     */
    @Cloumn(pk = true)
    private long playerId;

    /**
     * 用户名
     */
    @Cloumn
    private String username;

    /**
     * 密码
     */
    @Cloumn
    private String password;

    /**
     * 职业
     */
    @Cloumn
    private int profession;

    /**
     * 血量
     */
    @Cloumn
    private int hp;

    /**
     * 蓝
     */
    @Cloumn
    private int mp;

    /**
     * 金币
     */
    private int gold;
}
