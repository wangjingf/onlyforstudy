package io.study.helper;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import io.study.exception.StdException;


import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonTool {
    static final ObjectMapper mapper = new ObjectMapper();
    static{
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
    }
    public static String serialize(Object bean) {

        try {

            return mapper.writer().writeValueAsString(bean);

        } catch (Exception e) {
            throw new StdException("json.err_serialize",e);
        }
    }
    public static Object deserialize(String str){
        try {
            if(StringHelper.isEmpty(str)){
                return null;
            }
            str = str.trim();
             if(str.startsWith("{")){
                return mapper.readValue(str,Map.class);
            }
            return mapper.readValue(str,List.class);
        } catch (IOException e) {
            throw new StdException("json.err_deserialize_bean",e);
        }
    }
    public static <T> T parseBean(String str, Class<T> clazz) {
        try {
            T vo = mapper.readValue(str, clazz);
            return vo;
        } catch (Exception e) {
            throw new StdException("json.err_parse_bean",e);
        }
    }

    public static <T> List<T> parseBeanList(String str, Class<T> clazz) {
        try {
            if(StringHelper.isEmpty(str)){
                return null;
            }
             JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            List<T> list = mapper.readValue(str, javaType);
            return list;
        } catch (Exception e) {
            throw new StdException("json.err_parse_bean",e);
        }
    }
    public static void main(String[] args) {
         Map<String,Object> map = new HashMap<>();
         map.put("sid",1);
         map.put("date",new Date());
         List list = new ArrayList();
         list.add(map);
         String val = JsonTool.serialize(map);
        System.out.println(val);
        String collection = JsonTool.serialize(list);
        System.out.println(collection);
        Object deserialize = JsonTool.deserialize(val);
        System.out.println(deserialize);
        List s = (List) JsonTool.deserialize(collection);
        System.out.println(JsonTool.deserialize(collection));
    }
}
