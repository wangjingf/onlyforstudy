package wordCount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class WordCountMapper extends Mapper<Object, Text, Text, IntWritable> {
    //自定义计数器
    private enum Counters {LINES,WORDS}

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        context.getCounter(Counters.LINES).increment(1);
        String line = value.toString();
        String[] words = line.split("\\s+");
        for (String word : words) {
            context.getCounter(Counters.WORDS).increment(1);
            System.out.println(word);
            context.write(new Text(word), new IntWritable(1));
        }
    }
}
