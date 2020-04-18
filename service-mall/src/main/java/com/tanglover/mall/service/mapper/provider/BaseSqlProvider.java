package com.tanglover.mall.service.mapper.provider;

import com.tanglover.mall.service.mapper.Tool;
import com.tanglover.mall.service.mapper.annotation.Exclude;
import com.tanglover.mall.service.mapper.annotation.PrimaryKey;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TangXu
 * @create 2020-04-05 14:59
 * @description:
 */
public class BaseSqlProvider<T> {
    @Options
    public String add(T bean) {
        SQL sql = new SQL();
        Class clazz = bean.getClass();
        String tableName = clazz.getSimpleName();
        String realTableName = Tool.hump2Underline(tableName).replaceAll("_entity", "").substring(1);
        sql.INSERT_INTO(realTableName);
        List<Field> fields = getFields(clazz);
        for (Field field : fields) {
            field.setAccessible(true);
            String column = field.getName();
            System.out.println("column:" + Tool.hump2Underline(column));
            sql.VALUES(Tool.hump2Underline(column), String.format("#{" + column + ",jdbcType=VARCHAR}"));
        }
        return sql.toString();
    }

    public String delete(T bean) {
        SQL sql = new SQL();
        Class clazz = bean.getClass();
        String tableName = clazz.getSimpleName();
        String realTableName = Tool.hump2Underline(tableName).replaceAll("_entity", "").substring(1);
        sql.DELETE_FROM(realTableName);
        List<Field> primaryKeyField = getPrimaryKeyFields(clazz);
        if (!primaryKeyField.isEmpty()) {
            for (Field pkField : primaryKeyField) {
                pkField.setAccessible(true);
                sql.WHERE(pkField.getName() + "=" + String.format("#{" + pkField.getName() + "}"));
            }
        } else {
            sql.WHERE(" 1= 2");
            throw new RuntimeException("对象中未包含PrimaryKey属性");
        }
        return sql.toString();

    }


    private List getPrimaryKeyFields(Class clazz) {
        List primaryKeyField = new ArrayList<>();
        List<Field> fields = getFields(clazz);
        for (Field field : fields) {
            field.setAccessible(true);
            PrimaryKey key = field.getAnnotation(PrimaryKey.class);
            if (key != null) {
                primaryKeyField.add(field);
            }
        }

        return primaryKeyField;

    }


    private List getFields(Class clazz) {
        List fieldList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Exclude key = field.getAnnotation(Exclude.class);
            if (key == null) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    public String get(T bean) {
        SQL sql = new SQL();
        Class clazz = bean.getClass();
        String tableName = clazz.getSimpleName();
        String realTableName = Tool.hump2Underline(tableName).replaceAll("_entity", "").substring(1);
        sql.SELECT("*").FROM(realTableName);
        List<Field> primaryKeyField = getPrimaryKeyFields(clazz);
        if (!primaryKeyField.isEmpty()) {
            for (Field pkField : primaryKeyField) {
                pkField.setAccessible(true);
                sql.WHERE(pkField.getName() + "=" + String.format("#{" + pkField.getName() + "}"));
            }
        } else {
            sql.WHERE(" 1= 2");
            throw new RuntimeException("对象中未包含PrimaryKey属性");
        }
        System.out.println("getSql:" + sql.toString());
        return sql.toString();
    }


    public String update(T bean) {

        SQL sql = new SQL();
        Class clazz = bean.getClass();
        String tableName = clazz.getSimpleName();
        String realTableName = Tool.hump2Underline(tableName).replaceAll("_entity", "").substring(1);
        sql.UPDATE(realTableName);
        List<Field> fields = getFields(clazz);
        for (Field field : fields) {
            field.setAccessible(true);
            String column = field.getName();
            if (column.equals("id")) {
                continue;
            }
            System.out.println(Tool.hump2Underline(column));
            sql.SET(Tool.hump2Underline(column) + "=" + String.format("#{" + column + ",jdbcType=VARCHAR}"));
        }

        List<Field> primaryKeyField = getPrimaryKeyFields(clazz);
        if (!primaryKeyField.isEmpty()) {
            for (Field pkField : primaryKeyField) {
                pkField.setAccessible(true);
                sql.WHERE(pkField.getName() + "=" + String.format("#{" + pkField.getName() + "}"));
            }
        } else {
            sql.WHERE(" 1= 2");
            throw new RuntimeException("对象中未包含PrimaryKey属性");
        }
        System.out.println("updateSql:" + sql.toString());
        return sql.toString();
    }
}