package com.example.druid.db.dao;

import com.example.druid.db.entity.Entity;
import com.example.druid.db.entity.EntityInfo;
import com.example.druid.db.entity.KeyValueEntry;
import com.example.druid.db.queue.DbQueueManager;
import com.example.druid.db.statement.InsertStatement;
import com.example.druid.db.statement.SelectStatement;
import com.example.druid.db.statement.UpdateStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author linlazy
 */
@Component
public  class BaseJdbc<T extends Entity>  {

    private static Logger logger = LoggerFactory.getLogger(BaseJdbc.class);

    @Autowired
    protected DbQueueManager<T> dbQueueManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 批量记录插入
     */
    public void doBatchInsert(List<Entity> entityList) {
        for (Entity entity : entityList) {
            doInsert( entity);
        }
    }
    /**
     * 记录插入
     */
    public void doInsert(Entity entity) {

            List<Object> args = new ArrayList<>();

            //获取主键参数值
            List<KeyValueEntry<String, String>> pkKeyValue = entity.getPkKeyValue();
            List<String> pkValueList = pkKeyValue.stream().map(KeyValueEntry::getValue).collect(Collectors.toList());

            //获取普通列值
            List<KeyValueEntry<String, String>> notNullOrdinaryKeyValue = entity.getNotNullOrdinaryKeyValue();
            List<String> notNullOrdinaryValueList = notNullOrdinaryKeyValue.stream().map(KeyValueEntry::getValue).collect(Collectors.toList());
            args.addAll(notNullOrdinaryValueList);

            args.addAll(pkValueList);
            String prepareSQL = InsertStatement.buildPrepareSQL(entity);
            jdbcTemplate.update(prepareSQL,args);
    }


    /**
     * 批量记录更新
     */
    public void doBatchUpdate(List<Entity> entityList) {

        for (Entity entity : entityList) {
            doUpdate(entity);
        }
    }

    /**
     * 记录更新
     */
    public void doUpdate(Entity entity) {
        List<Object> args = new ArrayList<>();

        //获取普通列值
        List<KeyValueEntry<String, String>> notNullOrdinaryKeyValue = entity.getNotNullOrdinaryKeyValue();
        List<String> notNullOrdinaryValueList = notNullOrdinaryKeyValue.stream().map(KeyValueEntry::getValue).collect(Collectors.toList());
        args.addAll(notNullOrdinaryValueList);
        //获取主键参数值
        List<KeyValueEntry<String, String>> pkKeyValue = entity.getPkKeyValue();
        List<String> pkValueList = pkKeyValue.stream().map(KeyValueEntry::getValue).collect(Collectors.toList());
        args.addAll(pkValueList);
        String prepareSQL = UpdateStatement.buildPrepareSQL(entity);
        jdbcTemplate.update(prepareSQL,args);
    }

    /**
     * 批量记录删除
     */
    public void doBatchDelete(List<Entity> entityList) {

        for (Entity entity : entityList) {
            doDelete(entity);
        }
    }

    /**
     * 记录删除
     * @param entity
     */
    public void doDelete(Entity entity) {
        List<Object> args = new ArrayList<>();

        //获取主键参数值
        List<KeyValueEntry<String, String>> pkKeyValue = entity.getPkKeyValue();
        List<String> pkValueList = pkKeyValue.stream().map(KeyValueEntry::getValue).collect(Collectors.toList());
        args.addAll(pkValueList);
        String prepareSQL = UpdateStatement.buildPrepareSQL(entity);
        jdbcTemplate.update(prepareSQL,args);
    }


    public T query(EntityInfo entityInfo,Object... pkArgs) {
        String selectPrepareSQL = SelectStatement.buildPrepareSQL(entityInfo);
        List<T> query = jdbcTemplate.query(selectPrepareSQL, new RowMapper<T>() {
            @Override
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                try {
                    Entity entity = entityInfo.getEntityClass().newInstance();
                    entity.setEntityInfo(entityInfo);
                    //获取具有Cloum注解上的字段的类型，名称 ，将DB读取的值放进字段
                    for (Map.Entry<String, String> fieldNameType : entityInfo.getFieldNameTypeMap().entrySet()) {

                        Field field = entityInfo.getEntityClass().getDeclaredField(fieldNameType.getKey());
                        field.setAccessible(true);
                        switch (fieldNameType.getValue()) {
                            case "int":
                                field.set(entity, rs.getInt(fieldNameType.getKey()));
                                break;
                            case "long":
                                field.set(entity, rs.getLong(fieldNameType.getKey()));
                                break;
                            case "String":
                                field.set(entity, rs.getString(fieldNameType.getKey()));
                                break;
                            default:
                                break;
                        }
                    }

                    entity.afterReadDB();
                    return (T) entity;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }, pkArgs);

        if(query.size() == 0){
            return null;
        }
        return query.get(0);
    }
}
