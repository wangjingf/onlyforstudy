package KeyValueTextInputFormatEX;

import flowSum.FlowCountMapper;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/8/28 11:06
 * @Version: 1.0
 * @Description: TODO
 **/
public class KVTextMapper extends Mapper<Text,Text,Text, LongWritable> {
    private enum Counters {LINES}

    LongWritable v = new LongWritable(1);

    @Override
    protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
        context.getCounter(Counters.LINES).increment(1);
        context.write(key,v);
    }
}
