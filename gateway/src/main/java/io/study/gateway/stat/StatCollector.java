package io.study.gateway.stat;


import com.jd.vd.common.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class StatCollector {
    static final Logger logger = LoggerFactory.getLogger(StatCollector.class);
    Queue<RequestStat> stats = new ConcurrentLinkedDeque();
    public void onStat(RequestStat stat){
        //stats.add(stat);
        String startTime = StringHelper.formatDate(new Date(stat.getStartTime()),"yyyy-MM-dd HH:mm:ss");
        logger.info("stat.log_request:uri={},start={},cost={}",stat.uri,startTime,stat.cost());
    }
}
