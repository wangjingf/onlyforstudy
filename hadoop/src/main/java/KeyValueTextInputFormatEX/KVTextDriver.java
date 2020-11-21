package KeyValueTextInputFormatEX;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.*;

import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @Author: xu.dm
 * @Date: 2019/8/28 11:18
 * @Version: 1.0
 * @Description:
 * KeyValueTextInputFormat样例
 * NLineInputFormat样例
 **/
public class KVTextDriver {
    public static void main(String args[]) throws Exception{

        args = new String[]{"input/wc*.txt","output/"};

        Configuration conf = new Configuration();
        // 设置输入格式KeyValueTextInputFormat的分隔符，缺省是"\t"
        conf.set(KeyValueLineRecordReader.KEY_VALUE_SEPERATOR, " ");
        // 1 获取job对象
        Job job = Job.getInstance(conf);

        // 2 设置jar包位置，关联mapper和reducer
        job.setJarByClass(KVTextDriver.class);
        job.setMapperClass(KVTextMapper.class);
        job.setReducerClass(KVTextReducer.class);

        // 3 设置map输出kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        // 4 设置最终输出kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        // 5 设置输入输出数据路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));

        // 设置输入格式KeyValueTextInputFormat
        job.setInputFormatClass(KeyValueTextInputFormat.class);

        // 设置每个切片InputSplit中划分三条记录
//        NLineInputFormat.setNumLinesPerSplit(job, 3);
        // 使用NLineInputFormat处理记录数
//        job.setInputFormatClass(NLineInputFormat.class);


        // 6 设置输出数据路径
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 删除输出目录，如果存在，测试环节使用
        Path outPath = new Path(args[1]);
        FileSystem fs = FileSystem.get(conf);
        if(fs.exists(outPath)){
            fs.delete(outPath,true);
        }

        long startTime = System.currentTimeMillis();
        // 7 提交job
        boolean result = job.waitForCompletion(true);

        long endTime = System.currentTimeMillis();
        long timeSpan = endTime - startTime;
        System.out.println("运行耗时："+timeSpan+"毫秒。");

        System.exit(result?0:1);
    }

}
