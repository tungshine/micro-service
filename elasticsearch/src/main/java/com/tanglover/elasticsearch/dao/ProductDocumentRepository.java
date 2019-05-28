package com.tanglover.elasticsearch.dao;

import com.tanglover.elasticsearch.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @author TangXu
 * @create 2019-05-28 15:47
 * @description:
 */
@Component
public interface ProductDocumentRepository extends ElasticsearchRepository<ProductDocument, String> {

}