package WritableComparableEx;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/8/29 16:34
 * @Version: 1.0
 * @Description: TODO
 **/
public class FlowSortedMapper extends Mapper<LongWritable, Text,FlowBeanSorted,Text> {
    private enum Counters {LINES}
    FlowBeanSorted k = new FlowBeanSorted();
    Text v = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        context.getCounter(Counters.LINES).increment(1);
        String lines = value.toString();
        String[] fields = lines.split("\\s+");
        String phoneNumber = fields[1];
        long upFlow = Long.parseLong(fields[fields.length-3]);
        long downFlow = Long.parseLong(fields[fields.length-2]);

        k.setAll(upFlow,downFlow);
        v.set(phoneNumber);

        context.write(k,v);
    }
}
