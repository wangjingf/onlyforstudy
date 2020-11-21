package flowSum;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/8/27 14:37
 * @Version: 1.0
 * @Description: TODO
 **/
public class FlowCountMapper extends Mapper<LongWritable, Text,Text,FlowBean> {
    //自定义计数器
    private enum Counters {LINES}

    FlowBean flowBean = new FlowBean();
    Text phoneKey = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        context.getCounter(Counters.LINES).increment(1);
        String[] fields = line.split("\\s+");
        String phoneNumber = fields[1];
        //因为样本数据中有不完整字段是用空格或者"\t"分隔，而本例中的分隔符也恰好是"\t",样本只有头尾是标准的
        //所以数据分割后的取法就是按头尾来取，避过中间的字段。
        long upFlow = Long.parseLong(fields[fields.length-3]);
        long downFlow = Long.parseLong(fields[fields.length-2]);

        phoneKey.set(phoneNumber);
        flowBean.setAll(upFlow,downFlow);

        context.write(phoneKey,flowBean);
    }
}
