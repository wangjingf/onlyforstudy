package OutputFormatEx;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/8/31 22:15
 * @Description: 继承RecordWriter，实现数据输出到不同目录文件
 */
public class FRecordWriter extends RecordWriter<Text, NullWritable> {
    FSDataOutputStream out1 = null;
    FSDataOutputStream out2 = null;

    @Override
    public void write(Text key, NullWritable value) throws IOException, InterruptedException {
        // 判断是否包含“baidu”和"alibaba"字符串,输出到不同文件
        if (key.toString().contains("baidu") || key.toString().contains("alibaba")) {
            out1.write(key.toString().getBytes());
        } else {
            out2.write(key.toString().getBytes());
        }

    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        IOUtils.closeStream(out1);
        IOUtils.closeStream(out2);
    }

    public FRecordWriter(TaskAttemptContext job) {
        FileSystem fs;
        try {
            Path path1 = new Path("output1/a.log");
            Path path2 = new Path("output2/b.log");
            System.out.println(path1.getName());
            System.out.println(path2.getName());
            fs = FileSystem.get(job.getConfiguration());
            out1 = fs.create(path1);
            out2 = fs.create(path2);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
