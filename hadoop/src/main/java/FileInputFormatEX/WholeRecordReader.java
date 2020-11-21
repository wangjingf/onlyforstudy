package FileInputFormatEX;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * @Author: xu.dm
 * @Date: 2019/8/28 16:54
 * @Version: 1.0
 * @Description: TODO
 **/
public class WholeRecordReader extends RecordReader<Text, BytesWritable> {

    private FileSplit split;
    private Configuration conf;
    private Text k = new Text();
    private BytesWritable v = new BytesWritable();
    private boolean processed = false;

    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        this.split = (FileSplit) split;
        this.conf = context.getConfiguration();
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (!processed) {
            byte[] contents = new byte[((int) split.getLength())];
            Path path = split.getPath();
            FileSystem fs = path.getFileSystem(conf);
            try (FSDataInputStream in = fs.open(path)) {
                IOUtils.readFully(in, contents, 0, contents.length);
                k.set(path.toString());
                v.set(contents, 0, contents.length);
            }
            processed = true;
            return true;
        }
        return false;
    }

    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
        return k;
    }

    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return v;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return processed ? 1.0f:0.0f;
    }

    @Override
    public void close() throws IOException {

    }
}
