package com.tanglover.elasticsearch.service;

import com.tanglover.elasticsearch.document.ProductDocument;

import java.util.List;

/**
 * @author TangXu
 * @create 2019-05-28 15:56
 * @description:
 */
public interface EsSearchService extends BaseSearchService<ProductDocument> {
    /**
     * 保存
     *
     * @auther: zhoudong
     * @date: 2018/12/13 16:02
     */
    void save(ProductDocument... productDocuments);

    /**
     * 删除
     *
     * @param id
     */
    void delete(String id);

    /**
     * 清空索引
     */
    void deleteAll();

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    ProductDocument getById(String id);

    /**
     * 查询全部
     *
     * @return
     */
    List<ProductDocument> getAll();
}