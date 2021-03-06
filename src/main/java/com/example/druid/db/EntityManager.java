package com.example.druid.db;


import com.example.druid.db.annotation.Cloumn;
import com.example.druid.db.annotation.Table;
import com.example.druid.db.dao.BaseJdbc;
import com.example.druid.db.entity.Entity;
import com.example.druid.db.entity.EntityInfo;
import com.example.druid.db.statement.DeleteStatement;
import com.example.druid.db.statement.InsertStatement;
import com.example.druid.db.statement.UpdateStatement;
import com.example.druid.db.utils.PackageScanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author linlazy
 */
@Component
public class EntityManager {

    private Logger logger = LoggerFactory.getLogger(EntityManager.class);

    @Autowired
    private BaseJdbc baseJdbc;

    private String entityPackage="com.example.druid.example.entity";

    @PostConstruct
     void init(){

        //获取所有@table注解的类类型
        Collection<Class<Entity>> classCollection = PackageScanUtils.scanPackage(entityPackage);
        logger.debug("{}",classCollection);

        //每个table构建 entityInfo
        for(Class<Entity> tableEntityClass: classCollection){
            EntityInfo entityInfo = new EntityInfo();
            Table tableAnnotation = tableEntityClass.getAnnotation(Table.class);
            entityInfo.setTableName(tableAnnotation.value());
            entityInfo.setEntityClass(tableEntityClass);

            List<String> pkColumnNameList = entityInfo.getPkColumnNameList();
            List<String> ordinaryColumnNameList = entityInfo.getOrdinaryColumnNameList();
            Map<String, String> fieldNameTypeMap = entityInfo.getFieldNameTypeMap();

            Field[] declaredFields = tableEntityClass.getDeclaredFields();
            for(Field field: declaredFields){
                if(field.getAnnotation(Cloumn.class) != null){

                    //构建字段名类型映射
                    String fieldName = field.getName();
                    String fieldType = field.getType().getSimpleName();
                    fieldNameTypeMap.put(fieldName,fieldType);

                    //构建主键字段名列表
                    Cloumn columnAnnotation = field.getAnnotation(Cloumn.class);
                    if(columnAnnotation.pk()){
                        pkColumnNameList.add(fieldName);
                    }else {
                        //构建普通字段名列表
                        ordinaryColumnNameList.add(fieldName);
                    }

                }
            }


            EntityInfo.ENTITY_INFO_MAP.put(tableEntityClass,entityInfo);
        }

    }





}
