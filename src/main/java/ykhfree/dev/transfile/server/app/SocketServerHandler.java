package ykhfree.dev.transfile.server.app;

import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ykhfree.dev.transfile.socketmodel.EchoFile;
import ykhfree.dev.transfile.socketmodel.EchoTimeStamp;
import ykhfree.dev.transfile.socketmodel.ResultMsg;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Project       : 원격저장소 통신용 서버단 socket 모듈
 * @프로그램 설명   : Netty를 이용하여 특정 서버에서 요청하는 파일을 전송하기 위한 서버단 socket 모듈 핸들러
 * @파일명         : SocketServerHandler.java
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
public class SocketServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 로그관리자 인스턴스
     */
    private static final Logger logger = LoggerFactory.getLogger(SocketServerHandler.class);

    private RandomAccessFile randomAccessFile;
    private Properties prop;

    SocketServerHandler(Properties prop) {
        this.prop = prop;
    }

    // 채널이 접속되자마자 실행할 코드를 정의
    @Override
    public void channelActive(ChannelHandlerContext ctx) {}

    // 채널을 읽을 때 동작할 코드를 정의
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        //파일 전송
        if(msg instanceof EchoFile) {

            EchoFile echoFile = (EchoFile) msg;

            //첫번째 조각을 보낼 경우
            int dataLength = Integer.parseInt(this.prop.getProperty("buffer"));
            if(echoFile.getCountPackage() == 0) {
                try {
                    File file = new File(echoFile.getSrcFilePath());

                    if (file.exists()) {    //파일이 존재한다면

                        logger.info(echoFile.getSrcFilePath() + " transfer start!!!");

                        randomAccessFile = new RandomAccessFile(file, "r");
                        randomAccessFile.seek(0);
                        
                        //파일용량이 dataLength보다 작은지 확인..작다면 전송바이트를 실제파일용량과 같게한다.
                        if(randomAccessFile.length() < dataLength) {
                        	dataLength = (int) randomAccessFile.length();
                        }

                        int sumCountpackage;
                        if ((randomAccessFile.length() % dataLength) == 0) {
                            sumCountpackage = (int) (randomAccessFile.length() / dataLength);
                        } else {
                            sumCountpackage = (int) (randomAccessFile.length() / dataLength) + 1;
                        }
                        byte[] bytes = new byte[dataLength];

                        if (randomAccessFile.read(bytes) != -1) {
                            EchoFile msgFile = new EchoFile();
                            msgFile.setSumCountPackage(sumCountpackage);
                            msgFile.setCountPackage(1);
                            msgFile.setBytes(bytes);
                            msgFile.setSrcFilePath(file.getName());
                            ctx.writeAndFlush(msgFile);
                        }

                    } else {    //파일이 존재하지 않다면
                        ResultMsg resultMsg = new ResultMsg();
                        resultMsg.setResultCode("FAIL");
                        resultMsg.setDetailMsg("파일이 없습니다.");

                        ctx.writeAndFlush(resultMsg);
                    }
                } catch (IOException i) {
                    i.printStackTrace();
                }

            //파일 조각을 계속 보낸다.
            } else {

                try {
                    EchoFile msgEchoFile = (EchoFile) msg;
                    int countPackage = msgEchoFile.getCountPackage();

                    // int의 최대수는 2147483647 이기 때문에 하단 if문 추가함.
                    // 아래 if문이 없으면 4GB 이상의 파일을 전송할 때 오류 발생함.
                    if (countPackage < 8193) {
                        randomAccessFile.seek(countPackage * dataLength - dataLength);
                    } else {
                        randomAccessFile.seek((long) countPackage * dataLength - dataLength);
                    }

                    int byteLength;

                    long remainderFileCount = randomAccessFile.length()
                            - randomAccessFile.getFilePointer();

                    if (remainderFileCount < dataLength) {
                        byteLength = (int) remainderFileCount;

                    } else {
                        byteLength = dataLength;
                    }
                    byte[] bytes = new byte[byteLength];
                    if (randomAccessFile.read(bytes) != -1 && remainderFileCount > 0) {
                        msgEchoFile.setCountPackage(countPackage);
                        msgEchoFile.setBytes(bytes);
                        ctx.writeAndFlush(msgEchoFile);
                    } else {

                        logger.info(msgEchoFile.getSrcFilePath() + " transfer End!!!");

                        randomAccessFile.close();
                        ctx.close();
                    }
                } catch (IOException i) {
                    i.printStackTrace();
                    randomAccessFile.close();
                }
            }

        // 타임스탬프 보내기
        // 원격지에 있는 파일이 파일을 요청하는 서버의 파일보다 최신인지 확인하기 위한 로직
        } else if(msg instanceof EchoTimeStamp) {

            EchoTimeStamp echoTimeStamp = (EchoTimeStamp) msg;
            File file = new File(echoTimeStamp.getFileNm());

            if(file.exists()) {

                //파일인지 체크
                if(file.isFile()) { //파일일 경우
                    echoTimeStamp.setFileAt(true);
                    echoTimeStamp.setTimeStamp(file.lastModified());

                } else if(file.isDirectory()){    //폴더이면
                    echoTimeStamp.setFileAt(false);

                    //폴더의 파일리스트 가져오기
                    File[] fileList = file.listFiles();

                    if(fileList != null && fileList.length > 0) {
                        List<String> list = new ArrayList<>();

                        for (File imageFile : fileList) {
                            list.add(imageFile.getName());
                        }

                        echoTimeStamp.setFileList(list);

                        File firstFile = new File(echoTimeStamp.getFileNm() + File.separator + echoTimeStamp.getFileList().get(0));
                        echoTimeStamp.setTimeStamp(firstFile.lastModified());
                    }
                }

                ctx.writeAndFlush(echoTimeStamp);
            } else {
                ResultMsg resultMsg = new ResultMsg();
                resultMsg.setResultCode("FAIL");
                resultMsg.setDetailMsg("파일이 없습니다.");

                ctx.writeAndFlush(resultMsg);
            }
        }

    }

    // 채널 읽는 것을 완료했을 때 동작할 코드를 정의 합니다.
    /*public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }*/

    // 예외가 발생할 때 동작할 코드를 정의 합니다.
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        //cause.printStackTrace(); // 쌓여있는 트레이스를 출력합니다.
        logger.error(cause.getMessage());
        randomAccessFile.close();

        ctx.close(); // 컨텍스트를 종료시킵니다.
    }

}