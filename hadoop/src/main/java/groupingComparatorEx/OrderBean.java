package groupingComparatorEx;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/8/30 16:00
 * @Version: 1.0
 * @Description: 订单，实现订单id升序，价格降序
 * 0000001	Pdt_01	222.8
 * 0000002	Pdt_05	722.4
 **/
public class OrderBean implements WritableComparable<OrderBean> {

    private int order_id;
    private double price;

    public OrderBean() {
    }

    public OrderBean(int order_id, double price) {
        this.order_id = order_id;
        this.price = price;
    }

    @Override
    public int compareTo(OrderBean o) {
        //订单id升序，价格降序
        if(this.getOrder_id()>o.getOrder_id()){
            return 1;
        }else if (this.getOrder_id()<o.getOrder_id()){
            return -1;
        }
        else{
            if(this.getPrice()>o.getPrice()){
                return -1;
            }else if(this.getPrice()<o.getPrice()){
                return 1;
            }else{
                return 0;
            }
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(order_id);
        out.writeDouble(price);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.order_id = in.readInt();
        this.price = in.readDouble();
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "order_id=" + order_id +
                ", price=" + price;
    }
}
