package shareFriend;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/9/4 16:35
 * @Version: 1.0
 * @Description: 第一次输出结果
 * A	I,K,C,B,G,F,H,O,D,
 * B	A,F,J,E,
 * C	A,E,B,H,F,G,K,
 * D	G,C,K,A,L,F,E,H,
 * E	G,M,L,H,A,F,B,D,
 * F	L,M,D,C,G,A,
 * G	M,
 * H	O,
 * I	O,C,
 * J	O,
 * K	B,
 * L	D,E,
 * M	E,F,
 * O	A,H,I,J,F,
 *
 **/
public class OneShareFriendsReducer extends Reducer<Text,Text,Text,Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        StringBuffer sb = new StringBuffer();

        //1 拼接
        for(Text person: values){
            sb.append(person).append(",");
        }

        //2 写出
        context.write(key, new Text(sb.toString()));

    }
}
