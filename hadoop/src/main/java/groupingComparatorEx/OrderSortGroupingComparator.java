package groupingComparatorEx;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * @Author: xu.dm
 * @Date: 2019/8/30 16:15
 * @Version: 1.0
 * @Description: reducer数据分组，在数据从map阶段送到reducer后，在归并执行前，重新进行分组
 * 通过这种方式，重新调整数据进入reducer的key值
 *
 * 本例中，map送过来的key是OrderBean,也是按Orderbean排序（id升序，价格降序），
 * 数据送到reduce后，如果不分组，那么相同id不同价格的数据被认为是不同的key，
 * 经过自定义分组（继承WritableComparator），只使用id作为分组的条件，
 * reduce在归并前key的数据只按id判断，价格被忽略，
 * 那么：{1，300}和{1,200}这种数据就会被认为是相同的key，即key=1，其他忽略，所以最终输出的时候，价格只保留最高。
 **/
public class OrderSortGroupingComparator extends WritableComparator {


    protected OrderSortGroupingComparator() {
        super(OrderBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBean aBean = (OrderBean)a;
        OrderBean bBean = (OrderBean)b;
        if(aBean.getOrder_id()>bBean.getOrder_id()){
            return 1;
        }else if(aBean.getOrder_id()<bBean.getOrder_id()){
            return -1;
        }else {
            return 0;
        }
    }
}
