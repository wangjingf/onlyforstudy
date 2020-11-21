package OutputFormatEx;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/8/31 22:06
 * @Description:
 */
public class FilterReducer extends Reducer<Text, NullWritable,Text,NullWritable> {
    private Text newLine = new Text();
    @Override
    protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {

        //循环null值的values是防止key里有重复的数据没有被取出
        //Iterable<NullWritable> values迭代器里存储了key和value（虽然本例中value都是null值）
        //通过循环迭代器，迭代器里的key值也会被不断取出赋值到Text key中（她俩公用内存地址）
        for (NullWritable value : values) {
            newLine.set(key.toString()+"\r\n");
            context.write(newLine,value);
        }
    }
}
