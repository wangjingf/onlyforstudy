package topN;

import WritableComparableEx.FlowBeanSorted;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * @Author: xu.dm
 * @Date: 2019/9/4 10:42
 * @Version: 1.0
 * @Description: TODO
 **/
public class TopNReducer extends Reducer<FlowBeanSorted, Text,Text,FlowBeanSorted> {
    // 定义一个TreeMap作为存储数据的容器（天然按key排序）
    TreeMap<FlowBeanSorted, Text> flowMap = new TreeMap<>();

    @Override
    protected void reduce(FlowBeanSorted key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for (Text value : values) {

            FlowBeanSorted bean = new FlowBeanSorted();
            bean.setAll(key.getUpFlow(),key.getDownFlow());

            // 1 向treeMap集合中添加数据
            flowMap.put(bean, new Text(value));

            // 2 限制TreeMap数据量，超过10条就删除掉流量最小的一条数据
            if (flowMap.size() > 10) {
                // flowMap.remove(flowMap.firstKey());
                flowMap.remove(flowMap.lastKey());
            }
        }

    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
//        遍历集合，输出数据
        Iterator<FlowBeanSorted> it = flowMap.keySet().iterator();

        while (it.hasNext()) {

            FlowBeanSorted v = it.next();

            context.write(new Text(flowMap.get(v)), v);
        }

    }
}
