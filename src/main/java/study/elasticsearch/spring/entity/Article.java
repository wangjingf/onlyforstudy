package study.elasticsearch.spring.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.sql.Timestamp;

@Document(indexName="blog1")
@Setting(settingPath = "elasticsearch_config.json")
public class Article {
    //文档主键 唯一标识
    @Id
    @Field(store=true,index=false,type=FieldType.Text)
    String id;
    @Field(index = true,analyzer = "ik_smart",store=true,searchAnalyzer = "ik_smart",type=FieldType.Text)
    String name;
    @Field(index = true,analyzer = "html_analyzer",store=true,searchAnalyzer = "html_analyzer",type=FieldType.Text)
    String content;
    @Field(index = true,analyzer = "ik_smart",store=true,searchAnalyzer = "ik_smart",type=FieldType.Text)
    String brief;
    @Field(index=false,type=FieldType.Integer)
    int click;
    @Field(index=false,type=FieldType.Text)
    String type;
    @Field(type=FieldType.Keyword)
    String category;
    @Field(index=true,type=FieldType.Date)
    Timestamp createTime;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
