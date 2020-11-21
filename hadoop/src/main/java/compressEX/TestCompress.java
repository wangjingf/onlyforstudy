package compressEX;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @Author: xu.dm
 * @Date: 2019/9/2 20:30
 * @Description:
 */
public class TestCompress {
    public static void main(String args[]) throws Exception{
//        compress("d:/tmp/mytest.log","org.apache.hadoop.io.compress.BZip2Codec");
//        compress("d:/tmp/mytest.log","org.apache.hadoop.io.compress.GzipCodec");
//        compress("d:/tmp/mytest.log","org.apache.hadoop.io.compress.DefaultCodec");

//          decompress("d:/tmp/mytest.log.bz2");
          decompress("d:/tmp/mytest.log.gz");
          decompress("d:/tmp/mytest.log.deflate");

    }

    private static void compress(String filename, String method) throws Exception {
        FileInputStream fis = new FileInputStream(filename);
        Class codeClass = Class.forName(method);

        CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codeClass, new Configuration());

        FileOutputStream fos = new FileOutputStream(new File(filename)+codec.getDefaultExtension());
        CompressionOutputStream cos = codec.createOutputStream(fos);

        IOUtils.copyBytes(fis,cos,2048,false);

        IOUtils.closeStream(cos);
        IOUtils.closeStream(fos);
        IOUtils.closeStream(fis);
    }

    private static void decompress(String filename) throws Exception{
        CompressionCodecFactory factory = new CompressionCodecFactory(new Configuration());
        CompressionCodec codec = factory.getCodec(new Path(filename));
        if(codec==null){
            System.out.println("cannot find codec for file " + filename);
            return;
        }

        CompressionInputStream cis = codec.createInputStream(new FileInputStream(filename));

        FileOutputStream fos = new FileOutputStream(new File(filename)+".decode");

        IOUtils.copyBytes(cis,fos,2048,false);

        IOUtils.closeStream(fos);
        IOUtils.closeStream(cis);


    }

}
