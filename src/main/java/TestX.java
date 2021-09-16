import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContent;
import io.netty.util.ReferenceCountUtil;
 

import javax.servlet.Filter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestX {
    public final static int INT_BLOCK_SHIFT = 13;
    public final static int INT_BLOCK_SIZE = 1 << INT_BLOCK_SHIFT;
    Filter filter = null;

    /**
     *   ChannelHandler channelHandler = null;
     *         ChannelHandlerContext ctx = null;
     *         ByteBuf buffer = ctx.alloc().buffer();
     *         buffer.retain();
     *         ReferenceCountUtil.touch(buffer);
     *         ReferenceCountUtil.retain(buffer);
     *         ctx.channel().config().setAutoRead(false);
     *         Driver driver = null;
     *         Connection connection = driver.connect(null,null);
     *         Channel channel = null;
     * @param args
     * @throws SQLException
     * @throws IOException
     */
    public static void main(String[] args) throws SQLException, IOException {
      
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1000,TimeUnit.MILLISECONDS);
        Request.Builder builder = new Request.Builder();
        Request request = builder.url("http://www.baidu.com").get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        Socket socket = null;
        socket.connect(null);
        socket.getOutputStream().write(null);


    }
    static void x(){
        NioServerSocketChannel socketChannel = null;
        ServerBootstrap bootstrap = null;

    }
}
