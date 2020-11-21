package WritableComparableEx;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/8/29 16:17
 * @Version: 1.0
 * @Description: 统计总流量，按总流量倒序输出数据
 * phone_data.txt
 * 1	13736230513	192.196.100.1	www.atguigu.com	2481	24681	200
 **/
public class FlowBeanSorted implements WritableComparable<FlowBeanSorted> {
    //上行
    private long upFlow;
    //下行
    private long downFlow;
    //上下行合计
    private long sumFlow;

    public FlowBeanSorted() {
    }

    public FlowBeanSorted(long upFlow, long downFlow) {
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow = upFlow + downFlow;
    }

    public long getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(long upFlow) {
        this.upFlow = upFlow;
    }

    public long getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(long downFlow) {
        this.downFlow = downFlow;
    }

    public long getSumFlow() {
        return sumFlow;
    }

    public void setSumFlow(long sumFlow) {
        this.sumFlow = sumFlow;
    }

    public void setAll(long upFlow, long downFlow) {
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow = upFlow + upFlow;
    }

    @Override
    public String toString() {
        return upFlow + "\t" + downFlow + "\t" + sumFlow;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(this.upFlow);
        out.writeLong(this.downFlow);
        out.writeLong(this.sumFlow);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.upFlow = in.readLong();
        this.downFlow = in.readLong();
        this.sumFlow = in.readLong();
    }

    @Override
    public int compareTo(FlowBeanSorted o) {
        //按总流量sumFlow倒序排列
        if (this.sumFlow > o.getSumFlow())
            return -1;
        else if (this.sumFlow < o.getSumFlow())
            return 1;
        else
            return 0;
    }
}
