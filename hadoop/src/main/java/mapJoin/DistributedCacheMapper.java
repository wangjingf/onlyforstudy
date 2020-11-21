package mapJoin;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import reduceJoin.TableBean;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xu.dm
 * @Date: 2019/9/1 14:45
 * @Description:
 */
public class DistributedCacheMapper extends Mapper<LongWritable, Text, TableBean, NullWritable> {
    private Map<String, String> pdMap = new HashMap<>();
    TableBean bean = new TableBean();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        URI[] cacheFiles = context.getCacheFiles();
        String path = cacheFiles[0].getPath();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
        String line;
        while(StringUtils.isNotEmpty(line = reader.readLine())){

            // 2 切割
            String[] fields = line.split("\t");

            // 3 缓存数据到集合
            pdMap.put(fields[0], fields[1]);
        }

        // 4 关流
        reader.close();

    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] fields = line.split("\t");

        bean.setOrder_id(fields[0]);
        bean.setP_id(fields[1]);
        bean.setAmount(Integer.parseInt(fields[2]));
        bean.setPname(pdMap.get(fields[1]));
        bean.setFlag("map join");

        context.write(bean,NullWritable.get());
    }
}
