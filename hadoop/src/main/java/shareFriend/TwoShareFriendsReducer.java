package shareFriend;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/9/4 17:08
 * @Version: 1.0
 * @Description: 第二次输出结果
 * A-B	E C
 * A-C	D F
 * A-D	E F
 * A-E	D B C
 * A-F	O B C D E
 * A-G	F E C D
 * A-H	E C D O
 * A-I	O
 * A-J	O B
 * A-K	D C
 * A-L	F E D
 * A-M	E F
 * B-C	A
 * B-D	A E
 * B-E	C
 * B-F	E A C
 * B-G	C E A
 * B-H	A E C
 **/
public class TwoShareFriendsReducer extends Reducer<Text,Text,Text,Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        StringBuffer sb = new StringBuffer();

        for (Text friend : values) {
            sb.append(friend).append(" ");
        }

        context.write(key, new Text(sb.toString()));

    }
}
