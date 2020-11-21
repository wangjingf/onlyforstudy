package WritableComparableEx;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/8/29 16:42
 * @Version: 1.0
 * @Description: TODO
 **/
public class FlowSortedReducer extends Reducer<FlowBeanSorted, Text,Text,FlowBeanSorted> {
    @Override
    protected void reduce(FlowBeanSorted key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for (Text value : values) {
            context.write(value,key);
        }
    }
}
