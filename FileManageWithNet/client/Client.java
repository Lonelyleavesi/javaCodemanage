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
   	
   		//1Ϊչʾ��ǰ����Ŀ¼�����·����    2Ϊչʾ��ǰĿ¼�������ļ������·���� 3����·��������·���� 4����·���µ�һ���ļ��� �����·����
   		//5������һĿ¼�����·���� 6����һ���ļ������·����  7����һ���ļ��У����·����  8ɾ��һ���ļ����ļ��У�����·���� 9�ڵ�ǰĿ¼�¿���һ���ļ� �����·����
   		if ( opt >= 3 && opt <= 9) {
			sourse = scanner.next();  //ֻ��Ҫ����Դ�ļ� һ������
		}
   		//10����һ���ļ���Ŀ���ļ� ������Ҫ���루����·������ //11����һ���ļ��е�Ŀ���ļ�������Ҫ���루����·������
   		//12��ǰĿ¼��ѡ��һ���ļ�����һ�������ļ�  ������Ҫ���루���·������     13��ǰĿ¼��ѡ��һ�������ļ�����������ļ�������Ҫ���루���·������
		if (opt >= 10 && opt <= 13) {
			sourse = scanner.next();
			destiny = scanner.next();
		}
			
		if (opt  == 14) {  //���������ļ����ͻ���
			sourse = scanner.next();
			destiny = scanner.next();
		}
		if (opt  == 15) {  //�ͻ��˴��ļ���������
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
	 //1.��ȡ�����鳤��
       int lenght = ois.readInt();
       //2.��ȡ����
       long times = ois.readLong();
       //3.��ȡ���һ���ֽڳ���
       int lastBytes = ois.readInt();
       byte[] bytes = new byte[lenght];
       //System.out.println("ready read!...");
       while(times > 1){
           ois.readFully(bytes);
           fos.write(bytes);
           fos.flush();
           //System.out.println("ʣ��"+times+"��");
           times -- ;
       }
       //System.out.println("�������...");
       bytes = new byte[lastBytes];
       ois.readFully(bytes);
       fos.write(bytes);
       fos.flush();
       //System.out.println("�ļ��������");	
   }
   
    @Override
    public void run() {
        //�ͻ���һ���ӾͿ���д���ݸ���������
        new sendMessThread().start();
        super.run();
        try {
            // ��Sock���������
            InputStream is = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = 0; 
            while ((len = is.read(buf)) != -1) {
                System.out.println(new String(buf, 0, len)); 
                if ((new String(buf, 0, len)).equals("14")) { //��ʾ���������ļ�
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
        System.out.println("��ʼ�����ļ�������");
      //������������ֽ����������
        long times = fileSize/val+1;
        //������һ���ֽ��������Ч�ֽ���
        int lastBytes = (int)fileSize%2048;
        //1.�����ֽ����鳤��
        oos.writeInt(val);
        //2.���ʹ���
        oos.writeLong(times);
        oos.flush();
        //3.���һ���ֽڸ���
        oos.writeInt(lastBytes);
        oos.flush();
      //��ȡ�ֽ����鳤�ȵ��ֽڣ����ض�ȡ�ֽ����е����ݸ���
        int value = fis.read(bytes);
        while(value != -1){
            //ƫ���ֽ�����ȡ
            oos.write(bytes,0,value);
            oos.flush();
            value = fis.read(bytes);
        }
        System.out.println("�ļ�������ϣ�");
		return tfile;	
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
    //�������
//    public static void main(String[] args) {
//        //��Ҫ����������ȷ��IP��ַ�Ͷ˿ں�
//        Client clientTest=new Client("127.0.0.1", 1234);
//        clientTest.start();
//    }
}