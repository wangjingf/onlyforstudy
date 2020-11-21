package CustomPartitioner;

import flowSum.FlowBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @Author: xu.dm
 * @Date: 2019/8/29 14:10
 * @Version: 1.0
 * @Description: TODO
 **/
public class ProvincePartitioner extends Partitioner<Text, FlowBean> {
    @Override
    public int getPartition(Text text, FlowBean flowBean, int numPartitions) {
        int nPartition = 4;
        String preNum = text.toString().substring(0, 3);
        switch (preNum) {
            case "136":
                nPartition = 0;
                break;
            case "137":
                nPartition = 1;
                break;
            case "138":
                nPartition = 2;
                break;
            case "139":
                nPartition = 3;
                break;
            default:
                nPartition = 4;
        }
        return nPartition;
    }
}
