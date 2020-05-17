package compiler.expr.stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CallStack {
    static final Logger logger = LoggerFactory.getLogger(CallStack.class);
   List<ActivatedRecord> list = new ArrayList<>();
   public void pop(){
       ActivatedRecord activatedRecord = list.get(list.size() - 1);
       logger.info("nestLever is "+ list.size()+" currentVar:"+activatedRecord.getMembers().toString());
        list.remove(list.size()-1);
   }
   public ActivatedRecord peek(){
       if(list.isEmpty()){
           return null;
       }
       return list.get(list.size() -1 );
   }
   public void push(ActivatedRecord record){
       list.add(record);
   }
   public int size(){
       return list.size();
   }
}
