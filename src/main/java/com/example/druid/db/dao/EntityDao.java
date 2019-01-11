package com.example.druid.db.dao;

import com.example.druid.db.entity.Entity;
import com.example.druid.db.entity.EntityInfo;
import com.example.druid.db.queue.EntityOperatorType;

/**
 * @author linlazy
 */
public abstract class EntityDao<T extends Entity> extends BaseJdbc<T>{

    /**
     * 制定操作的实体类类型
     * @return
     */
    protected abstract Class<T> forClass();

    /**
     * 更新
     * @param entity
     */
    public void updateQueue(T entity){
        entity.setOperatorType(EntityOperatorType.UPDATE);
        entity.beforeWriteDB();
        //放进队列
        dbQueueManager.pushEntity(entity);
    }

    /**
     * 插入
     * @param entity
     */
    public void insertQueue(T entity){
        entity.setOperatorType(EntityOperatorType.INSERT);
        entity.beforeWriteDB();
        //放进队列
        dbQueueManager.pushEntity(entity);
    }

    /**
     * 删除
     * @param entity
     */
    public void deleteQueue(T entity){
        entity.setOperatorType(EntityOperatorType.INSERT);
        entity.beforeWriteDB();
        //放进队列
        dbQueueManager.pushEntity(entity);
    }

    /**
     * 获取实体
     * @return
     */
    public T getEntityByPK(Object...  pkArgs){
        EntityInfo entityInfo = EntityInfo.ENTITY_INFO_MAP.get(forClass());
        return this.query(entityInfo,pkArgs);
    }
}
