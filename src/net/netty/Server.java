package IO.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;

/**
 * @author linjing
 * @date: Created in 2020/8/25
 */
public class Server {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup bossGroup =  new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup,workerGroup)
                .channel(ServerSocketChannel.class)
                .childOption(ChannelOption.SO_BACKLOG, 128)
                .childHandler(new ChannelHandlerAdapter() {
                    @Override
                    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                        super.handlerAdded(ctx);
                        //ctx.pipeline().addLast(new ServerHandler());
                    }
                });

        ChannelFuture channelFuture = serverBootstrap.bind(3500).sync();
        channelFuture.channel().closeFuture().sync();

        System.out.println("sever start...");
    }
}
