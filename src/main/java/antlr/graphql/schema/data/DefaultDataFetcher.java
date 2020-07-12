package antlr.graphql.schema.data;

import antlr.graphql.schema.entity.FieldInfo;
import io.study.reflect.BeanTool;
import org.springframework.cglib.core.ReflectUtils;

import java.util.Map;

public class DefaultDataFetcher implements IDataFetcher {


    @Override
    public Object fetchData(Object parentValue, FieldInfo selectionField, Map<String, Object> args, FetcherContext context) {
        return null;
    }
}
