package client;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread {

    //����һ��Socket����
    Socket socket = null;
    int operator;
    String sourse = "x";
    String destiny = "x";
    public Client(String host, int port) {
    	operator = 0;
        try {
            //��Ҫ��������IP��ַ�Ͷ˿ںţ����ܻ����ȷ��Socket����
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
   	
   		//1Ϊչʾ��ǰ����Ŀ¼    2Ϊչʾ��ǰĿ¼�������ļ� 3����·�� 4����·���µ�һ���ļ��� 
   		//5������һĿ¼ 6����һ���ļ�  7����һ���ļ���  8ɾ��һ���ļ����ļ��� 9�ڵ�ǰĿ¼�¿���һ���ļ�
   		if ( opt >= 3 && opt <= 9) {
			sourse = scanner.next();  //ֻ��Ҫ����Դ�ļ� һ������
		}
   		//10����һ���ļ���Ŀ���ļ� ������Ҫ��������·���� //11����һ���ļ��е�Ŀ���ļ�������Ҫ��������·����
   		//12��ǰĿ¼��ѡ��һ���ļ�����һ�������ļ�       13��ǰĿ¼��ѡ��һ�������ļ�����������ļ�
		if (opt >= 10 && opt <= 13) {
			sourse = scanner.next();
			destiny = scanner.next();
		}
			
		if (opt  == 14) {  //���������ļ����ͻ���
			sourse = scanner.next();
			destiny = scanner.next();
		}
   }
   public void receiveFile(InputStream is)throws IOException {
	   System.out.println("join received file !");
	   ObjectInputStream ois = new ObjectInputStream(is);
	   System.out.println("ready fos!");
	   FileOutputStream fos = new FileOutputStream(destiny);
	   System.out.println("end fos!");
	 //1.��ȡ�����鳤��
       int lenght = ois.readInt();
       //2.��ȡ����
       long times = ois.readLong();
       //3.��ȡ���һ���ֽڳ���
       int lastBytes = ois.readInt();
       byte[] bytes = new byte[lenght];
       System.out.println("ready read!...");
       while(times > 1){
           ois.readFully(bytes);
           fos.write(bytes);
           fos.flush();
           System.out.println("ʣ��"+times+"��");
           times -- ;
       }
       System.out.println("�������...");
       bytes = new byte[lastBytes];
       ois.readFully(bytes);
       fos.write(bytes);
       fos.flush();
       System.out.println("�ļ��������");	
   }
   
    @Override
    public void run() {
        //�ͻ���һ���ӾͿ���д���ݸ���������
        new sendMessThread().start();
        super.run();
        try {
            // ��Sock���������
            InputStream s = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = 0; 
            while ((len = s.read(buf)) != -1) {
                System.out.println(new String(buf, 0, len)); 
                if ((new String(buf, 0, len)).equals("14")) { //��ʾ���������ļ�
					System.out.println("begin received file !");
					receiveFile(s);
					System.out.println("end received file !");
				}
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //��Socket����д���ݣ���Ҫ�¿�һ���߳�
    class sendMessThread extends Thread{
        @Override
        public void run() {
            super.run();
            //д����
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
    //�������
    public static void main(String[] args) {
        //��Ҫ����������ȷ��IP��ַ�Ͷ˿ں�
        Client clientTest=new Client("127.0.0.1", 1234);
        clientTest.start();
    }
}