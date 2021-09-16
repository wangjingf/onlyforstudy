package io.study.rest.service;

import com.jd.vd.common.exception.BizException;
import io.study.dao.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

@Service
public class DocumentService {
    @Autowired
    ElasticsearchRestTemplate esRestTemplate;
    @Autowired
    HibernateTemplate hibernateTemplate;
    public Serializable saveDoc(Document document){
        Serializable id = hibernateTemplate.save(document);
        document.setId((Integer) id);
        Document esDocument = esRestTemplate.save(document);
        return id;
    }
    public void updateDocument(Document doc){
        if(doc.getId() == null){
            throw new BizException("文档id必须填写");
        }
        hibernateTemplate.update(doc);

    }
    public void deleteDoc(Document doc){
        if(doc.getId() == null){
            throw new BizException("文档id必须填写");
        }
        hibernateTemplate.delete(doc);
        esRestTemplate.delete(doc);
    }
    public SearchHits<Document> search(int pageNo, int pageSize, Map<String,Object> conds){
        Query query = null;

        return esRestTemplate.search(query, Document.class);
    }
}
