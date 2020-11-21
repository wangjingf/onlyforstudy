package jobTojob;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/9/3 17:07
 * @Version: 1.0
 * @Description: TODO
 **/
public class TwoIndexMapper extends Mapper<LongWritable, Text,Text,Text> {

    Text k = new Text();
    Text v = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1 获取1行数据
        String line = value.toString();

        // 2用“--”切割
        String[] fields = line.split("--");

        k.set(fields[0]);
        v.set(fields[1]);

        // 3 输出数据
        context.write(k, v);

    }
}
