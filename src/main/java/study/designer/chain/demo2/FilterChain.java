package study.designer.chain.demo2;

import java.util.ArrayList;
import java.util.List;

public class FilterChain {
    List<Filter> filters = new ArrayList<>();
    int currentIndex = -1;

    public void doFilter(Request request) {
        currentIndex++;
        if(currentIndex >= filters.size()){
            return;
        }
        filters.get(currentIndex).filter(this,request);
    }

    public void addFilter(Filter filter){
        filters.add(filter);
    }
    public static void main(String[] args){
        FilterChain filterChain = new FilterChain();
        Filter filter1 = new Filter() {
            @Override
            public void filter(FilterChain chain, Request request) {
                System.out.println("filter1");
                chain.doFilter(request);
            }
        };
        Filter filter2 = new Filter() {
            @Override
            public void filter(FilterChain chain, Request request) {
                System.out.println("filter2");
                chain.doFilter(request);
            }
        };
        filterChain.addFilter(filter1);
        filterChain.addFilter(filter2);
        filterChain.doFilter(new Request());
    }
}
