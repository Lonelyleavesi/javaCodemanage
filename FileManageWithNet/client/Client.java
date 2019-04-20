package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread {

    //定义一个Socket对象
    Socket socket = null;
    int operator;
    public Client(String host, int port) {
    	operator = 0;
        try {
            //需要服务器的IP地址和端口号，才能获得正确的Socket对象
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized int getOperator() {
 	   return operator;
    }
     
   public synchronized int setOperator(int opt) {
 	   operator = opt;
 	   return operator;
   }
    @Override
    public void run() {
        //客户端一连接就可以写数据个服务器了
        new sendMessThread().start();
        super.run();
        try {
            // 读Sock里面的数据
            InputStream s = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = s.read(buf)) != -1) {
                System.out.println(new String(buf, 0, len));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //往Socket里面写数据，需要新开一个线程
    class sendMessThread extends Thread{
        @Override
        public void run() {
            super.run();
            //写操作
            Scanner scanner=null;
            OutputStream os= null;
            try {
                scanner=new Scanner(System.in);
                os= socket.getOutputStream(); 
                String sourse = "x";
                String destiny = "x";
                do {
                	int opt=scanner.nextInt();
                	setOperator(opt);
                	
                 //1为展示当前所在目录    2为展示当前目录下所有文件 3设置路径 4进入路径下的一个文件夹 
                 //5返回上一目录 6创建一个文件  7创建一个文件夹  8删除一个文件或文件夹 9在当前目录下拷贝一个文件
                	if ( opt >= 3 && opt <= 9) {
						sourse = scanner.next();  //只需要输入源文件 一个参数
					}
                //10拷贝一个文件到目的文件 （都需要输入完整路径） //11拷贝一个文件夹到目的文件（都需要输入完整路径）
                //12当前目录下选择一个文件生成一个加密文件       13当前目录下选择一个加密文件输出到解密文件
					if (opt >= 10 && opt <= 12) {
						sourse = scanner.next();
						destiny = scanner.next();
					}
						
                    os.write((opt + "|" + sourse +"|" +destiny).getBytes());
                    os.flush();
                } while (true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            scanner.close();
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //函数入口
    public static void main(String[] args) {
        //需要服务器的正确的IP地址和端口号
        Client clientTest=new Client("127.0.0.1", 1234);
        clientTest.start();
    }
}