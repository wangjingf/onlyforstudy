package groupingComparatorEx;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/8/30 16:21
 * @Version: 1.0
 * @Description: 如果不执行Iterable<NullWritable> values迭代，直接取key
 * 那么key根据分组只保留从map->shuffle->reduce流程里第一个排序值，如果key是一个bean对象（即复合键），key就是排序输出的第一个对象。
 *
 * 如果执行Iterable<NullWritable> values迭代，那么迭代器滚动数据过程中，会依次对OrderBean key赋值，
 * 原理是Iterable<NullWritable> values里也存了key值，滚动中key被取出，而迭代器里key和reduce里key公用内存地址（复用）
 * 所以迭代器滚动过程，对key和value都进行了赋值
 * 可以用 OrderBean mykey = new OrderBean(key.getOrder_id(),key.getPrice());来测试，mykey是不会跟着迭代器滚动的。
 * 通过这个特性，可以实现排序数据取TopN
 **/
public class OrderReducer extends Reducer<OrderBean, NullWritable,OrderBean,NullWritable> {
    @Override
    protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
//        OrderBean mykey = new OrderBean(key.getOrder_id(),key.getPrice());
        //实现topN数据输出
        int topN = 3;
        for (NullWritable value : values) {
            System.out.println(key.hashCode());
            context.write(key,NullWritable.get());
            topN--;
            if(topN<=0)break;
        }


    }
}
