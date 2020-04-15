package io.study.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoHelper {
    static final Logger logger = LoggerFactory.getLogger(IoHelper.class);
    public static void safeClose(AutoCloseable closeable){
        try{
            if(closeable != null){
                closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
