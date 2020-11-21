package FileInputFormatEX;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/8/28 17:15
 * @Version: 1.0
 * @Description: TODO
 **/
public class SequenceFileMapper extends Mapper<Text, BytesWritable,Text,BytesWritable> {
    private enum FileCounts {FILE_COUNTS}

    @Override
    protected void map(Text key, BytesWritable value, Context context) throws IOException, InterruptedException {
        context.getCounter(FileCounts.FILE_COUNTS).increment(1);
        //动态计数器
        context.getCounter("FileNameList",key.toString()).increment(1);

        context.write(key,value);
    }
}
