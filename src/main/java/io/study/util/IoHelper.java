//
// Source code recreated from transfer .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.study.util;

import io.entropy.async.IAsyncCloseable;
import io.entropy.config.AppConfig;
import io.entropy.core.mix.ah;
import io.entropy.exceptions.EntropyException;
import io.entropy.io.serialize.IByteArraySerializer;
import io.entropy.io.serialize.IStreamSerializer;
import io.entropy.io.serialize.impl.JavaSerializer;
import io.entropy.lang.IStepProgressListener;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoHelper {
    static final Logger LOG = LoggerFactory.getLogger(IoHelper.class);
    public static final String ENCODING_UTF8 = "UTF-8";
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    public static final Charset CHARSET_ISO8859 = Charset.forName("ISO-8859-1");
    public static final int DEFAULT_BUF_SIZE = AppConfig.var("io.default_buffer_size").toInt(4096);


    public IoHelper() {
    }



    public static void safeClose(AutoCloseable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (Exception ex) {
                ;
            }
        }

    }

    public static void safeCloseObject(Object obj) {
        if (obj instanceof AutoCloseable) {
            safeClose((AutoCloseable)obj);
        }

    }

    public static void safeCloseAll(Collection<?> closable) {
        if (closable != null) {
            Iterator iter = closable.iterator();

            while(iter.hasNext()) {
                Object bext = iter.next();
                safeCloseObject(bext);
            }
        }

    }

    public static void safeCloseAsync(Object closable) {
        if (closable instanceof IAsyncCloseable) {
            try {
                ((IAsyncCloseable)closable).closeAsync();
            } catch (Exception ex) {
                ;
            }
        } else if (closable instanceof AutoCloseable) {
            safeClose((AutoCloseable)closable);
        }

    }

    public static void write(OutputStream outstream, byte[] bytes, IStepProgressListener listener) throws IOException {
        if (listener != null) {
            listener.begin();
        }

        outstream.write(bytes);
        if (listener != null) {
            listener.onStep((long)bytes.length);
            listener.end();
        }

    }

    public static void copy(InputStream inputStream, OutputStream outStream) throws IOException {
        copy((InputStream)inputStream, (OutputStream)outStream, DEFAULT_BUF_SIZE, (IStepProgressListener)null);
    }

    public static void copy(InputStream in, OutputStream out, int bufSize, IStepProgressListener listener) throws IOException {
        if (listener != null) {
            listener.begin();
        }

        byte[] bytes = new byte[bufSize];

        while(true) {
            int byteValue = in.read(bytes);
            if (byteValue < 0) {
                if (listener != null) {
                    listener.end();
                }

                return;
            }

            out.write(bytes, 0, byteValue);
            if (listener != null) {
                listener.onStep((long)byteValue);
            }
        }
    }

    public static void copy(Reader reader, Writer writer) throws IOException {
        copy((Reader)reader, (Writer)writer, DEFAULT_BUF_SIZE, (IStepProgressListener)null);
    }

    public static void copy(Reader reader, Writer writer, int bufSize, IStepProgressListener listener) throws IOException {
        char[] arr = new char[bufSize];
        if (listener != null) {
            listener.begin();
        }



        while(true) {
            int byteValue = reader.read(arr);
            if (byteValue < 0) {
                if (listener != null) {
                    listener.end();
                }

                return;
            }

            writer.write(arr, 0, byteValue);
            if (listener != null) {
                listener.onStep((long)byteValue);
            }
        }
    }

    public static String readText(Reader reader) throws IOException {
        StringWriter stringWriter = new StringWriter();
        copy((Reader)reader, (Writer)stringWriter, DEFAULT_BUF_SIZE, (IStepProgressListener)null);
        return stringWriter.toString();
    }

    public static byte[] readBytes(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        copy((InputStream)in, (OutputStream)outStream, DEFAULT_BUF_SIZE, (IStepProgressListener)null);
        return outStream.toByteArray();
    }

    public static String readText(InputStream in, String encoding) throws IOException {
        return readText(toReader(in, encoding));
    }

    public static Reader toReader(InputStream in, String encoding) throws IOException {
        if (encoding == null) {
            encoding = "UTF-8";
        }

        return new InputStreamReader(in, encoding);
    }

    public static Writer toWriter(OutputStream in, String encoding) throws IOException {
        if (encoding == null) {
            encoding = "UTF-8";
        }

        return new OutputStreamWriter(in, encoding);
    }

    public static int readTill(InputStream in, byte targetByte, byte[] arr, int offset) throws IOException {
        int length = arr.length;

        do {
            int elm = in.read();
            if (elm < 0) {
                throw new EntropyException("util.err_read_data_unexpected_eof");
            }

            arr[offset] = (byte)elm;
            if (elm == targetByte) {
                return offset;
            }

            ++offset;
        } while(offset < length);

        throw (new EntropyException("util.err_read_data_not_find_end_byte")).param("end", targetByte);
    }

    public static void readFully(InputStream in, byte[] arr, int off, int len) throws IOException {
        while(true) {
            int bytes = in.read(arr, off, len);
            if (bytes < 0) {
                throw new EntropyException("util.err_read_data_unexpected_eof");
            }

            len -= bytes;
            if (len <= 0) {
                return;
            }

            off += bytes;
        }
    }

    public static void readFully(InputStream in, byte[] arr) throws IOException {
        readFully(in, arr, 0, arr.length);
    }

    public static String getEncodingFromBOM(InputStream in) throws IOException {
        in.mark(4);
        byte[] arr = new byte[4];
        readFully(in, arr);
        String encoding = null;
        if (arr[0] == 0 && arr[1] == 0 && arr[2] == -2 && arr[3] == -1 || arr[0] == -1 && arr[1] == -2 && arr[2] == 0 && arr[3] == 0) {
            encoding = "UTF-32";
        } else if (arr[0] == -2 && arr[1] == -1 || arr[0] == -1 && arr[1] == -2) {
            encoding = "UTF-16";
            in.reset();
            in.read(arr, 0, 2);
        } else if (arr[0] == -17 && arr[1] == -69 && arr[2] == -65) {
            encoding = "UTF-8";
            in.reset();
            in.read(arr, 0, 3);
        } else {
            in.reset();
        }

        return encoding;
    }


    public static long transferFullyFrom(OutputStream outputStream, ReadableByteChannel readableByteChannel, long startIndex, long endIndex) throws IOException {
        if (outputStream instanceof FileOutputStream) {
            return transfer(((FileOutputStream)outputStream).getChannel(), readableByteChannel, startIndex, endIndex);
        } else {
            throw new EntropyException("not_impl");
        }
    }

    public static long transferFullyTo(InputStream in, WritableByteChannel channel, long startIndex, long endIndex) throws IOException {
        if (in instanceof FileInputStream) {
            return transfer(((FileInputStream)in).getChannel(), channel, startIndex, endIndex);
        } else {
            if (startIndex > 0L) {
                long skiped = in.skip(startIndex);
                if (skiped != startIndex) {
                    return 0L;
                }
            }

            byte[] bytes = new byte[DEFAULT_BUF_SIZE];
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            long index = endIndex;

            while(true) {
                int readed = in.read(bytes, 0, (int)Math.min((long)bytes.length, endIndex));
                if (readed < 0) {
                    break;
                }

                if (readed > 0) {
                    buffer.limit(readed);
                    buffer.position(0);
                    writeToChannel(channel, buffer);
                    index -= (long)readed;
                    if (index <= 0L) {
                        break;
                    }
                }
            }

            return endIndex - index;
        }
    }

    public static void writeToChannel(WritableByteChannel channel, ByteBuffer buffer) throws IOException {
        long writeBytes;
        do {
            writeBytes = (long)channel.write(buffer);
        } while(writeBytes > 0L || !buffer.hasRemaining());

        throw new EntropyException("io.err_writeToChannel_fail");
    }

    static long transfer(FileChannel fileChannel, WritableByteChannel writeChannel, long startIndex, long endIndex) throws IOException {
        long index;
        long transferBytes;
        for(index = 0L; index < endIndex; index += transferBytes) {
            transferBytes = fileChannel.transferTo(index + startIndex, endIndex - index, writeChannel);
            if (transferBytes <= 0L) {
                break;
            }
        }

        return index;
    }

    private static long transfer(FileChannel fileChannel, ReadableByteChannel byteChannel, long startPos, long endPos) throws IOException {
        long index;
        long transferBytes;
        for(index = 0L; index < endPos; index += transferBytes) {
            transferBytes = fileChannel.transferFrom(byteChannel, index + startPos, endPos - index);
            if (transferBytes <= 0L) {
                break;
            }
        }

        return index;
    }


}
