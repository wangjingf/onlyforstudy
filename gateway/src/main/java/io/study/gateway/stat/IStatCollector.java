package io.study.gateway.stat;

import com.jd.vd.common.util.StringHelper;

import java.util.Date;

public interface IStatCollector {
    public void onStat(RequestStat stat);
}
