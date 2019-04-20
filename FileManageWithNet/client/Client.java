package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread {

    //����һ��Socket����
    Socket socket = null;
    int operator;
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
     
   public synchronized int setOperator(int opt) {
 	   operator = opt;
 	   return operator;
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
                	
                 //1Ϊչʾ��ǰ����Ŀ¼    2Ϊչʾ��ǰĿ¼�������ļ� 3����·�� 4����·���µ�һ���ļ��� 
                 //5������һĿ¼ 6����һ���ļ�  7����һ���ļ���  8ɾ��һ���ļ����ļ��� 9�ڵ�ǰĿ¼�¿���һ���ļ�
                	if ( opt >= 3 && opt <= 9) {
						sourse = scanner.next();  //ֻ��Ҫ����Դ�ļ� һ������
					}
                //10����һ���ļ���Ŀ���ļ� ������Ҫ��������·���� //11����һ���ļ��е�Ŀ���ļ�������Ҫ��������·����
                //12��ǰĿ¼��ѡ��һ���ļ�����һ�������ļ�       13��ǰĿ¼��ѡ��һ�������ļ�����������ļ�
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
    //�������
    public static void main(String[] args) {
        //��Ҫ����������ȷ��IP��ַ�Ͷ˿ں�
        Client clientTest=new Client("127.0.0.1", 1234);
        clientTest.start();
    }
}