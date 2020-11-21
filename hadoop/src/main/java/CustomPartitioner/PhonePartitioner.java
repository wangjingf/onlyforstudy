package CustomPartitioner;

import WritableComparableEx.FlowBeanSorted;
import flowSum.FlowBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @Author: xu.dm
 * @Date: 2019/8/29 14:10
 * @Version: 1.0
 * @Description: TODO
 **/
public class PhonePartitioner extends Partitioner<FlowBeanSorted, Text> {
    @Override
    public int getPartition( FlowBeanSorted bean,Text text, int numPartitions) {
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
