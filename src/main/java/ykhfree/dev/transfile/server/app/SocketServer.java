package ykhfree.dev.transfile.server.app;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * @Project       : 원격저장소 통신용 서버단 socket 모듈
 * @프로그램 설명   : Netty를 이용하여 특정 서버에서 요청하는 파일을 전송하기 위한 서버단 socket 모듈
 * @파일명         : SocketServer.java
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
public class SocketServer {

    /**
     * 로그관리자 인스턴스
     */
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    /**
     * main method
     * @param args
     */
    public static void main(String[] args) throws IOException {

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

            // EventLoop : 연결의 수명주기 중 발생하는 이벤트를 처리하는 핵심 추상화를 정의
            EventLoopGroup parentGroup = new NioEventLoopGroup(1);
            EventLoopGroup childGroup = new NioEventLoopGroup();    // 쓰레드 개수를 정할 수 있음. 현재는 값이 없으므로 CPU 코어 수로 세팅됨.

            try {

                ServerBootstrap sb = new ServerBootstrap(); // 부트 스트랩(서버를 구성하는 시동코드) 객체 생성
                sb.group(parentGroup, childGroup)   // 그룹 지정
                    .channel(NioServerSocketChannel.class)  // 채널 초기화
                    .handler(new LoggingHandler(LogLevel.ERROR)) // 로그 레빌 지정
                    .childHandler(new InitializerPipeline(prop))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true); // 자식 채널의 초기화

                ChannelFuture cf = sb.bind(socketPort).sync();  // 인커밍 커넥션을 액세스하기 위해 바인드하고 시작합니다.

                cf.channel().closeFuture().sync();  // 서버 소켓이 닫힐때까지 대기합니다.

            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            } finally {
                parentGroup.shutdownGracefully();
                childGroup.shutdownGracefully();
            }

        } catch(IOException e) {
            logger.error(e.getMessage());
        }
    }

}