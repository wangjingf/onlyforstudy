package io.wjf.study.hello;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.TaskContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.util.LongAccumulator;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RddHelloWorld {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();

        conf.setAppName("WordCounter")//
                .setMaster("local");

        String fileName = "d:/a.txt";

        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> lines = sc.textFile(fileName, 3);
        LongAccumulator accumulator = sc.sc().longAccumulator();
        sc.sc().broadcast(1,null);
        accumulator.add(123L);
        accumulator.add(100);;
        int i = 123;
        lines.map(new Function<String, Object>() {
            @Override
            public Object call(String v1) throws Exception {
                return null;
            }
        });
        JavaRDD<String> words = lines
                .flatMap(new FlatMapFunction<String, String>() {
                    private static final long serialVersionUID = 1L;

                    // 以前的版本好像是Iterable而不是Iterator
                    @Override
                    public Iterator<String> call(String line) throws Exception {
                        List<String> ret = new LinkedList<>();
                        for (String s : line.split(" ")) {
                            if(!StringUtils.isEmpty(s)){
                                ret.add(s);
                            }
                        }

                        accumulator.add(123);
                        System.out.println(i);
                        return ret.iterator();
                    }
                });

                //words.reduce()

                words.foreach(new VoidFunction<String>() {
                    @Override
                    public void call(String s) throws Exception {
                        System.out.println(s);
                    }
                });
                accumulator.value();
        sc.close();
    }
}
