package io.study.gateway.interceptor;


import com.jd.vd.common.exception.BizException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * filters数量刚才是固定的，只能有这么多，动态加的一下配置在filters里再做处理吧，可以再进行 二级封装,非线程安全
 */

public class FilterLoader {
    ArrayList<IFilter> filters  = new ArrayList<>();
     public void addList(IFilter filter){
         assert  filter != null;
         filters.add(filter);
     }
     int findIndexByKey(String name){
         for (int i = 0; i < filters.size(); i++) {
             if(name.equals(filters.get(i).name())){
                 return i;
             }
         }
         return -1;
     }
     public void addBefore(String name,IFilter filter){
         assert  name != null;
         assert  filter != null;
         int index =  findIndexByKey(name);
         if(index == -1){
             throw new BizException("found invalid filter name:"+name);
         }
         filters.add(index,filter);
     }
    public void addAfter(String name,IFilter filter){
        assert  name != null;
        assert  filter != null;
        int index =  findIndexByKey(name);
        if(index == -1){
            throw new BizException("found invalid filter name:"+name);
        }
        filters.add(index+1,filter);
    }
    public List<IFilter> getFilters(){
         return filters;
    }
}
