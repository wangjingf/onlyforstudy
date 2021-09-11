package io.study.gateway.stat;

import io.study.gateway.proxy.ProxyProtocol;
import io.study.gateway.registry.IRegistry;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

 /**索引创建呢
 PUT /gateway_req_stat
{
    "settings": {
      "number_of_shards": 2,
      "number_of_replicas": 1
    },
    "mappings": {
        "properties": {
        "uri": {
          "type":"keyword"
        },
        "proxyProtocol": {
          "type":"keyword"
        },
        "cost":{
            "type":"integer"
  },
        "target": {
          "type":"keyword"
        },
        "startTime": {
          "type":"date"
        },
        "endTime": {
          "type":"date"
        },
        "isSuccess": {
          "type":"boolean"
        },
        "cause":{
          "type":"text",
          "analyzer":"english"
        }
        }
    }
}
  */
@Document(indexName = "gateway_req_stat")
@Setting(settingPath = "stat/es_req_stat.json")
public class RequestStat {
    String uri;
    ProxyProtocol proxyProtocol;
    String target;
    long startTime;
    long endTime;
    int cost;
    boolean isSuccess;
    Throwable cause;

    public RequestStat() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ProxyProtocol getProxyProtocol() {
        return proxyProtocol;
    }

    public void setProxyProtocol(ProxyProtocol proxyProtocol) {
        this.proxyProtocol = proxyProtocol;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
     public  int cost(){
        return cost;
     }
     public int getCost() {
         return cost;
     }

     public void setCost(int cost) {
         this.cost = cost;
     }
 }
