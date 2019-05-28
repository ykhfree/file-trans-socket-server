package ykhfree.dev.transfile.client.app;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ykhfree.dev.transfile.socketmodel.EchoFile;
import ykhfree.dev.transfile.socketmodel.ResultMsg;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @Project       : 원격저장소 통신용 클라이언트단 socket 모듈
 * @프로그램 설명   : Netty를 이용하여 특정 서버에서 요청하는 파일을 전송하기 위한 클라이언트단 socket 모듈
 * @파일명         : SocketClient.java
 * @작성자         : 양강현
 * @작성일         : 2017. 9. 20.
 * @version       : 0.8
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일         수정자      수정내용
 *  -------------  --------    ---------------------------
 *   2017. 9. 20.  양강현      최초 생성
 *
 * </pre>
 */
public class SocketClient {

    /**
     * 로그관리자 인스턴스
     */
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private static EchoFile setEchoFile(String fileName, String filePath) {
        EchoFile echoFile = new EchoFile();

        echoFile.setFileNm(fileName);
        echoFile.setFile_to(filePath);

        return echoFile;
    }

    /**
     * main method
     * @param args
     */
    public static void main(String[] args) throws IOException {

        EventLoopGroup group = new NioEventLoopGroup();
        Properties prop = new Properties();
        InputStream input;

        try {

            input = new FileInputStream("./config/config.properties");

            // load a properties file
            prop.load(input);
            input.close();

            logger.info("================================================== load a properties file success");

            // 서버 소켓 포트 번호 지정
            int socketPort = Integer.parseInt(prop.getProperty("port"));
            int bufferSize = Integer.parseInt(prop.getProperty("buffer"));

            EchoFile echoFile = setEchoFile("fileName", "filePath");
            ResultMsg resultMsg = new ResultMsg();

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ByteToMessageDecoder() {
                                @Override
                                protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                                    out.add(in.readBytes(in.readableBytes())); }
                            });
                            ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new SocketClientHandler(echoFile, resultMsg, bufferSize));
                        }
                    });


            logger.info("connect to {}:{}", "127.0.0.1", socketPort);

            ChannelFuture future = bootstrap.connect("127.0.0.1", socketPort).sync();

            //소켓이 닫힐떄까지 대기
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            group.shutdownGracefully();
        } finally {
            group.shutdownGracefully();
        }
    }


}