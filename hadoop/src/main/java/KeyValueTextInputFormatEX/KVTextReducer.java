package KeyValueTextInputFormatEX;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/8/28 11:11
 * @Version: 1.0
 * @Description: TODO
 **/
public class KVTextReducer extends Reducer<Text,LongWritable,Text,LongWritable> {
    LongWritable v = new LongWritable();
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        long sum = 0;
        for (LongWritable value : values) {
            sum+=value.get();
        }

        v.set(sum);
        context.write(key,v);
    }
}
