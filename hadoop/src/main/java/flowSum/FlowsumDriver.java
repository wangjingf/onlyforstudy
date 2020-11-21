package flowSum;

import CustomPartitioner.ProvincePartitioner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @Author: xu.dm
 * @Date: 2019/8/27 15:08
 * @Version: 1.0
 * @Description: 统计手机号上行、下行流量合计，并统计上下行总流量
 **/
public class FlowsumDriver {
    public static void main(String args[]) throws Exception{


        args = new String[]{"input/phone*.txt","output/"};

        long startTime = System.currentTimeMillis();
        Configuration conf = new Configuration();
//        conf.set("fs.defaultFS", "file:///");

        Job job = Job.getInstance(conf,"FlowSum");
        job.setJarByClass(FlowsumDriver.class);
        job.setMapperClass(FlowCountMapper.class);
        job.setReducerClass(FlowCountReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //使用CombineTextInputFormat对小文件进行组合，128M划分一个片
        job.setInputFormatClass(CombineTextInputFormat.class);
        CombineTextInputFormat.setMaxInputSplitSize(job,1024*1024*128);

        //自定义分区，输出5个文件
        job.setPartitionerClass(ProvincePartitioner.class);
        job.setNumReduceTasks(5);


        FileInputFormat.addInputPath(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        Path outPath = new Path(args[1]);
        FileSystem fs = FileSystem.get(conf);
        if(fs.exists(outPath)){
            fs.delete(outPath,true);
        }

        boolean result = job.waitForCompletion(true);

        long endTime = System.currentTimeMillis();
        long timeSpan = endTime - startTime;
        System.out.println("运行耗时："+timeSpan+"毫秒。");

        System.exit( result ? 0 : 1);
    }

}
