package client;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread {

    //定义一个Socket对象
    Socket socket = null;
    int operator;
    String sourse = "x";
    String destiny = "x";
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
     
    public synchronized String getSource() {
  	   return sourse;
     }
    public synchronized String getDestiny() {
   	   return destiny;
      }
   public synchronized int setOperator(int opt) {
 	   operator = opt;
 	   return operator;
   }
   
   public synchronized void setOPT() {
	   Scanner scanner=null;
	   scanner=new Scanner(System.in);
	   int opt=scanner.nextInt();
   	   setOperator(opt);
   	
   		//1为展示当前所在目录（相对路径）    2为展示当前目录下所有文件（相对路径） 3设置路径（绝对路径） 4进入路径下的一个文件夹 （相对路径）
   		//5返回上一目录（相对路径） 6创建一个文件（相对路径）  7创建一个文件夹（相对路径）  8删除一个文件或文件夹（绝对路径） 9在当前目录下拷贝一个文件 （相对路径）
   		if ( opt >= 3 && opt <= 9) {
			sourse = scanner.next();  //只需要输入源文件 一个参数
		}
   		//10拷贝一个文件到目的文件 （都需要输入（绝对路径）） //11拷贝一个文件夹到目的文件（都需要输入（绝对路径））
   		//12当前目录下选择一个文件生成一个加密文件  （都需要输入（相对路径））     13当前目录下选择一个加密文件输出到解密文件（都需要输入（相对路径））
		if (opt >= 10 && opt <= 13) {
			sourse = scanner.next();
			destiny = scanner.next();
		}
			
		if (opt  == 14) {  //服务器传文件到客户端
			sourse = scanner.next();
			destiny = scanner.next();
		}
		if (opt  == 15) {  //客户端传文件到服务器
			sourse = scanner.next();
			destiny = scanner.next();
		}
   }
   public void receiveFile(InputStream is)throws IOException {
	   ///System.out.println("join received file !");
	   ObjectInputStream ois = new ObjectInputStream(is);
	   //System.out.println("ready fos!");
	   FileOutputStream fos = new FileOutputStream(destiny);
	   //System.out.println("end fos!");
	 //1.读取的数组长度
       int lenght = ois.readInt();
       //2.读取次数
       long times = ois.readLong();
       //3.读取最后一次字节长度
       int lastBytes = ois.readInt();
       byte[] bytes = new byte[lenght];
       //System.out.println("ready read!...");
       while(times > 1){
           ois.readFully(bytes);
           fos.write(bytes);
           fos.flush();
           //System.out.println("剩余"+times+"次");
           times -- ;
       }
       //System.out.println("处理最后...");
       bytes = new byte[lastBytes];
       ois.readFully(bytes);
       fos.write(bytes);
       fos.flush();
       //System.out.println("文件接收完毕");	
   }
   
    @Override
    public void run() {
        //客户端一连接就可以写数据个服务器了
        new sendMessThread().start();
        super.run();
        try {
            // 读Sock里面的数据
            InputStream is = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = 0; 
            while ((len = is.read(buf)) != -1) {
                System.out.println(new String(buf, 0, len)); 
                if ((new String(buf, 0, len)).equals("14")) { //表示即将接受文件
					System.out.println("begin received file !");
					receiveFile(is);
					System.out.println("end received file !");
				}
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File sendFile(OutputStream out, String tFile) throws IOException {
    	int val = 2048;
		File tfile = new File(tFile);
		if (!tfile.exists()) {
			return null;
		}
		ObjectOutputStream oos = new ObjectOutputStream(out);
		FileInputStream fis = new FileInputStream(tFile);
		long fileSize = tfile.length();
        byte[] bytes = new byte[val];
        System.out.println("开始发送文件……！");
      //算出即将发送字节数组的字数
        long times = fileSize/val+1;
        //算出最后一组字节数组的有效字节数
        int lastBytes = (int)fileSize%2048;
        //1.发送字节数组长度
        oos.writeInt(val);
        //2.发送次数
        oos.writeLong(times);
        oos.flush();
        //3.最后一次字节个数
        oos.writeInt(lastBytes);
        oos.flush();
      //读取字节数组长度的字节，返回读取字节数中的数据个数
        int value = fis.read(bytes);
        while(value != -1){
            //偏移字节数读取
            oos.write(bytes,0,value);
            oos.flush();
            value = fis.read(bytes);
        }
        System.out.println("文件发送完毕！");
		return tfile;	
    }
    
    //往Socket里面写数据，需要新开一个线程
    class sendMessThread extends Thread{
        @Override
        public void run() {
            super.run();
            //写操作
            OutputStream os= null;
            try {
                
                os= socket.getOutputStream(); 
               
                do {
                	setOPT();
                	int opt =getOperator();
                	String tsourse = getSource();
                    String  tdestiny = getDestiny();
                    
					if (opt != 999) {
	                    os.write((opt + "|" + tsourse +"|" + tdestiny).getBytes());
	                    os.flush();
					}
					if (opt == 15) {
						os.write("15".getBytes());
						os.flush();
						sendFile(os,tsourse);
					}
                    opt = 999;
                    //setOperator(opt);
                    os.flush();
                } while (true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //函数入口
//    public static void main(String[] args) {
//        //需要服务器的正确的IP地址和端口号
//        Client clientTest=new Client("127.0.0.1", 1234);
//        clientTest.start();
//    }
}