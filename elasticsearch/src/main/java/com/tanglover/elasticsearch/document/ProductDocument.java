package com.tanglover.elasticsearch.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.io.Serializable;

/**
 * @author TangXu
 * @create 2019-05-28 15:49
 * @description:
 */
@Document(indexName = "orders", type = "product")
@Mapping(mappingPath = "productIndex.json") // 解决IK分词不能使用问题
public class ProductDocument implements Serializable {

    @Id
    private String id;
    @Field(analyzer = "ik_max_word",searchAnalyzer = "ik_max_word", type = FieldType.text)
    private String productName;
    @Field(analyzer = "ik_max_word",searchAnalyzer = "ik_max_word", type = FieldType.text)
    private String productDesc;

    private long createTime;

    private long updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}