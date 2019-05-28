package ykhfree.dev.transfile.server.app;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.Properties;


public class InitializerPipeline extends ChannelInitializer<SocketChannel> {

	private Properties prop;

	InitializerPipeline(Properties prop) {
   		this.prop = prop;
	}

	@Override
	protected void initChannel(SocketChannel ch) {
		ChannelPipeline pipeline = ch.pipeline();
		ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
		ch.pipeline().addLast(new ObjectEncoder());
		pipeline.addLast("handler", new SocketServerHandler(this.prop));
	}
}
