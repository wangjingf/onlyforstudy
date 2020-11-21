package OutputFormatEx;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @Author: xu.dm
 * @Date: 2019/8/31 22:23
 * @Description: 自定义输出
 * 实现对样本按行分割，判断是否包含baidu或alibaba字符串，
 * 包含则写入目录1，不包含写入目录2，
 */
public class FilterDriver {

   public static void main(String args[]) throws Exception{
       args = new String[]{"input/","output/"};


       Configuration conf = new Configuration();
       Job job = Job.getInstance(conf);

       job.setJarByClass(FilterDriver.class);
       job.setMapperClass(FilterMapper.class);
       job.setReducerClass(FilterReducer.class);

       job.setMapOutputKeyClass(Text .class);
       job.setMapOutputValueClass(NullWritable .class);

       job.setOutputKeyClass(Text.class);
       job.setOutputValueClass(NullWritable.class);

       // 要将自定义的输出格式组件设置到job中
       job.setOutputFormatClass(FilterOutputFormat.class);

       FileInputFormat.setInputPaths(job, new Path(args[0]));

       // 虽然我们自定义了outputformat，但是因为我们的outputformat继承自fileoutputformat
       // 而fileoutputformat要输出一个_SUCCESS文件，所以，在这还得指定一个输出目录
       FileOutputFormat.setOutputPath(job, new Path(args[1]));

       Path outPath = new Path(args[1]);
       FileSystem fs = FileSystem.get(conf);
       if(fs.exists(outPath)){
           fs.delete(outPath,true);
       }

       boolean result = job.waitForCompletion(true);
       System.exit(result ? 0 : 1);
   }


}
