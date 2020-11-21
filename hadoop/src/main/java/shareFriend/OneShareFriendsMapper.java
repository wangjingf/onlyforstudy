package shareFriend;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/9/4 16:32
 * @Version: 1.0
 * @Description: TODO
 **/
public class OneShareFriendsMapper extends Mapper<LongWritable, Text,Text,Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1 获取一行 A:B,C,D,F,E,O
        String line = value.toString();

        // 2 切割
        String[] fields = line.split(":");

        // 3 获取person和好友
        String person = fields[0];
        String[] friends = fields[1].split(",");

        // 4写出去
        for(String friend: friends){

            // 输出 <好友，人>
            context.write(new Text(friend), new Text(person));
        }
    }
}
