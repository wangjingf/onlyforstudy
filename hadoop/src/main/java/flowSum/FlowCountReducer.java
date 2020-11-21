package flowSum;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/8/27 14:54
 * @Version: 1.0
 * @Description: TODO
 **/
public class FlowCountReducer extends Reducer<Text, FlowBean, Text, FlowBean> {
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        long sum_upFlow = 0;
        long sum_downFlow = 0;

        for (FlowBean bean : values){
            sum_upFlow+=bean.getUpFlow();
            sum_downFlow+=bean.getDownFlow();
        }

        FlowBean flowBean = new FlowBean(sum_upFlow,sum_downFlow);
        context.write(key,flowBean);
    }
}
