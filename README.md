# file transfer socket server (with Netty)
**Netty를 이용한 파일전송 소켓서버**
<br/><br/>

1. 각 폴더 설명 <br/>
   - config : 설정파일<br/>
   - gradle : gradle wrapper<br/>
   - package : 빌드되어 나온 jar를 이용하여 서비스하고자 할때 같이 동봉되어야 할 파일모음 (실행 .sh 등)<br/>
   - src : 소스<br/>
   
2. socket clinet<br/>
   - ykhfree.dev.transfile.client.app.SocketClient의 main method 실행<br/>
   
3. socket server<br/>
   - ykhfree.dev.transfile.server.app.SocketServer의 main method 실행<br/>
   
4. 설정<br/>
   - 서버IP설정 : ./config/config.properties 수정<br/>
   - 포트설정 : ./config/config.properties 수정<br/>
   - 버퍼사이즈 : ./config/config.properties 수정<br/>