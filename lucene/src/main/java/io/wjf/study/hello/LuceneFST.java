package io.wjf.study.hello;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.IntsRefBuilder;
import org.apache.lucene.util.fst.Builder;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.PositiveIntOutputs;
import org.apache.lucene.util.fst.Util;

import java.io.IOException;

public class LuceneFST {
    public static void main(String[] args) throws IOException {
        String inputValues[] = {"ade", "cae","cat"};
        long outputValues[] = {5, 10, 15};

        PositiveIntOutputs outputs = PositiveIntOutputs.getSingleton();
        Builder<Long> builder = new Builder<Long>(FST.INPUT_TYPE.BYTE1, outputs);
        IntsRefBuilder scratchInts = new IntsRefBuilder();
        for (int i = 0; i < inputValues.length; i++) {
            BytesRef scratchBytes = new BytesRef(inputValues[i]);
            IntsRef intsRef = Util.toIntsRef(scratchBytes, scratchInts);//将字符串转换为字节数组
            builder.add(intsRef, outputValues[i]);
        }
        FST<Long> fst = builder.finish();
        System.out.println(fst.toString());
        Long value = Util.get(fst, new BytesRef("cat"));
        System.out.println(value);
    }
}
