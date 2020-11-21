package jobTojob;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/9/3 16:52
 * @Version: 1.0
 * @Description: TODO
 **/
public class OneIndexMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    String filename;
    Text k = new Text();
    IntWritable v = new IntWritable();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        FileSplit split = (FileSplit) context.getInputSplit();
        filename = split.getPath().getName();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1 获取1行
        String line = value.toString();

        // 2 切割
        String[] fields = line.split(" ");

        for (String word : fields) {

            // 3 拼接
            k.set(word + "--" + filename);
            v.set(1);

            // 4 写出
            context.write(k, v);

        }
    }
}
