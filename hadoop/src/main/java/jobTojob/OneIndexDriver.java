package jobTojob;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @Author: xu.dm
 * @Date: 2019/9/3 17:00
 * @Version: 1.0
 * @Description: 两个job串联，job1->job2
 * 按文件分别求词频
 * 第一个mapreduce实现分别求词频，词与文件名组合成key
 * 第二个mapreduce实现格式调整，将上一步的key拆分，然后与值组合
 **/
public class OneIndexDriver {
    public static void main(String args[]) throws Exception{
        // 输入输出路径需要根据自己电脑上实际的输入输出路径设置
        args = new String[] { "input/wc*.txt", "job1_output/" };

        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        job.setJarByClass(OneIndexDriver.class);

        job.setMapperClass(OneIndexMapper.class);
        job.setReducerClass(OneIndexReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);

    }
}
