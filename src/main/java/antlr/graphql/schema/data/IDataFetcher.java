package antlr.graphql.schema.data;

import antlr.graphql.schema.entity.FieldInfo;

import java.util.Map;

/**
 * 一个fetcher需要几个参数？
 * 1. parentValue肯定需要
 * 2. fldName也需要,field的类型？不一定需要，因为校验及别的操作是取值后的操作
 * 3. args也需要
 * 4. 其他参数放到context里
 * 当指定了参数后接口应该增加哪些参数?
 */
public interface IDataFetcher {
    public Object fetchData(Object parentValue, FieldInfo selectionField, Map<String,Object> args, FetcherContext context);
}
