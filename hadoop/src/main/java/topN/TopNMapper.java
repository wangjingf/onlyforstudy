package topN;

import WritableComparableEx.FlowBeanSorted;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * @Author: xu.dm
 * @Date: 2019/9/4 10:09
 * @Version: 1.0
 * @Description: TODO
 **/
public class TopNMapper extends Mapper<LongWritable, Text, FlowBeanSorted,Text> {
    // 定义一个TreeMap作为存储数据的容器（天然按key排序）
    private TreeMap<FlowBeanSorted, Text> flowMap = new TreeMap<>();
    private enum Counters {LINES}

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        context.getCounter(Counters.LINES).increment(1);
        String lines = value.toString();
        String[] fields = lines.split("\\s+");
        String phoneNumber = fields[1];
        long upFlow = Long.parseLong(fields[fields.length-3]);
        long downFlow = Long.parseLong(fields[fields.length-2]);

        FlowBeanSorted k = new FlowBeanSorted();
        Text v = new Text();

        k.setAll(upFlow,downFlow);
        v.set(phoneNumber);

        flowMap.put(k,v);

        //限制TreeMap的数据量，超过10条就删除掉流量最小的一条数据
        if (flowMap.size() > 10) {
//		flowMap.remove(flowMap.firstKey());
            flowMap.remove(flowMap.lastKey());
        }

    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        Iterator<FlowBeanSorted> bean = flowMap.keySet().iterator();

        while (bean.hasNext()) {

            FlowBeanSorted k = bean.next();

            context.write(k, flowMap.get(k));
        }

    }
}
