package study.designer.chain.demo3;

import java.util.ArrayList;
import java.util.List;

public class Chain {
    public List<Filter> filters = new ArrayList<>();
    public void addFilter(Filter filter){
        filters.add(filter);
    }
    public Invoker buildChainInvoker(Invoker lastInvoker){
        for (int i = filters.size() - 1; i >= 0 ; i--) {
            Filter filter = filters.get(i);
            Invoker nextInvoker = lastInvoker;
            lastInvoker = new Invoker() {
                @Override
                public void invoke(Invocation invocation) {
                    filter.invoke(nextInvoker,invocation);
                }
            };
        }
        return lastInvoker;
    }
    public static void main(String[] args){
         Chain chain = new Chain();
         Filter filter1 = new Filter() {
             @Override
             public void invoke(Invoker invoker, Invocation invocation) {
                 System.out.println("filter 1 invokerd");
                 invoker.invoke(invocation);
             }
         };
        Filter filter2 = new Filter() {
            @Override
            public void invoke(Invoker invoker, Invocation invocation) {
                System.out.println("filter 2 invokerd");
                invoker.invoke(invocation);
            }
        };
        chain.addFilter(filter1);
        chain.addFilter(filter2);
       Invoker lastInvoker =  chain.buildChainInvoker(new Invoker() {
            @Override
            public void invoke(Invocation invocation) {
                System.out.println("last filter invoked");
            }
        });
       lastInvoker.invoke(new Invocation());
    }
}
