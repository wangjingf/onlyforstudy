package WritableComparableEx;

import CustomPartitioner.PhonePartitioner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @Author: xu.dm
 * @Date: 2019/8/29 16:51
 * @Version: 1.0
 * @Description: TODO
 **/
public class FlowSortedDriver {
    public static void main(String args[]) throws Exception{
        args = new String[]{"input/phone*.txt","output/"};

        Configuration conf  = new Configuration();

        Job job = Job.getInstance(conf);

        job.setJarByClass(FlowBeanSorted.class);

        job.setMapperClass(FlowSortedMapper.class);
        job.setReducerClass(FlowSortedReducer.class);

        //mapper输出与reducer输出类型不一样，所以要独立设置
        job.setMapOutputKeyClass(FlowBeanSorted.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBeanSorted.class);

        job.setPartitionerClass(PhonePartitioner.class);
        job.setNumReduceTasks(5);

        FileInputFormat.addInputPath(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));


        Path outPath = new Path(args[1]);
        FileSystem fs = FileSystem.get(conf);
        if(fs.exists(outPath)){
            fs.delete(outPath,true);
        }

        long startTime = System.currentTimeMillis();
        boolean result = job.waitForCompletion(true);

        long endTime = System.currentTimeMillis();
        long timeSpan = endTime - startTime;
        System.out.println("运行耗时："+timeSpan+"毫秒。");

        System.exit( result ? 0 : 1);
    }
}
