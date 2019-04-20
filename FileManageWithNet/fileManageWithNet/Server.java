package fileManageWithNet;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public  class  Server extends Thread{
    ServerSocket server = null;
    Socket socket = null;
    FileManage fileManage;
    String command;
    public Server(int port) {
    	command = null;
    	fileManage = new FileManage();
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized String getCommand() {
  	   return command;
     }
    public synchronized String setCommand(String str) {
    	System.out.println("set command to" + str);
    	command = str;
   	   return command;
      }
    @Override
    public void run(){

        super.run();
        try{
            System.out.println("wait client connect...");
            socket = server.accept();
            sendMessThread SM = new sendMessThread();
            SM.start();//���Ӳ�����socket�������÷�����Ϣ�߳�
            System.out.println(socket.getInetAddress().getHostAddress()+"SUCCESS TO CONNECT...");
            InputStream in = socket.getInputStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len=in.read(buf))!=-1){
                System.out.println("client saying: "+new String(buf,0,len));
                if (len > 0) {
                	System.out.println(len);
                    setCommand(new String(buf,0,len));
				}
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    class sendMessThread extends Thread{
        @Override
        public void run(){
            super.run();
            OutputStream out = null;
            try{
                if(socket != null){
                    out = socket.getOutputStream();
                    String in = "";
                    String source = "x";
                	String destiny = "x";
                	int operate = 0;
                    do {
                    	String cmd =  getCommand();
                    	if (cmd != null) {
                        	String[] sourceStrArray =cmd.split("\\|");
                        	operate = Integer.parseInt(sourceStrArray[0]);
                        	source = sourceStrArray[1];
                        	destiny = sourceStrArray[2];
                        	System.out.println(operate + source + destiny);
                        	
                        	 //1Ϊչʾ��ǰ����Ŀ¼    2Ϊչʾ��ǰĿ¼�������ļ� 3����·�� 4����·���µ�һ���ļ��� 
                            //5������һĿ¼ 6����һ���ļ�  7����һ���ļ���  8ɾ��һ���ļ����ļ��� 9�ڵ�ǰĿ¼�¿���һ���ļ�
                        	switch (operate) {
    						case 1:{  //1Ϊչʾ��ǰ����Ŀ¼
    							out.write((fileManage.showCurrPath()).getBytes());
    		                    out.flush();
    						}
    						break;
    						case 2:{  // 2Ϊչʾ��ǰĿ¼�������ļ�
    							ArrayList<String> temp = fileManage.disPlayFile();
    							if (temp == null) {
    								out.write(("����·��!....").getBytes());
    			                    out.flush();
    							}
    							else {
    								for (String string : temp) {
    									out.write((string + "\n").getBytes());
    				                    out.flush();
    								}
    							}
    						}
    							break;
    						case 3:{  //3����·��
    							File temp = fileManage.setPath(source);
    							if (temp == null) {
    								out.write(("����·��ʧ�ܣ�������"+source+"������....").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 4:{  //4����·���µ�һ���ļ��� 
    							File temp = fileManage.joinPath(source);
    							if (temp == null) {
    								out.write(("�����ļ���ʧ�ܣ�������"+source+"������....").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 5:{  //5������һĿ¼
    							File temp = fileManage.quitOneLevel();						
    								out.write((temp.toString()).getBytes());
    						}
    							break;
    						case 6:{  //6����һ���ļ�
    							File temp = fileManage.makeFile(source);
    							if (temp == null) {
    								out.write(("�����ļ�ʧ�ܣ�������"+source+"�Ѿ�����....").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 7:{  //7����һ���ļ���
    							File temp = fileManage.makeDir(source);
    							if (temp == null) {
    								out.write(("�����ļ���ʧ�ܣ�������"+source+"�Ѿ�����....").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 8:{  // 8ɾ��һ���ļ����ļ��� 
    							File temp = fileManage.deleteFile(source);
    							if (temp == null) {
    								out.write(("Դ�ļ�������...").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 9:{  // 9�ڵ�ǰĿ¼�¿���һ���ļ�
    							File temp = fileManage.copyCurrLoc(source);
    							if (temp == null) {
    								out.write(("Դ�ļ�������...").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 10:{  //10����һ���ļ���Ŀ���ļ� ������Ҫ��������·���� 
    							File temp = fileManage.copyFile(source,destiny);
    							if (temp == null) {
    								out.write(("Դ�ļ�������...").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 11:{  //11����һ���ļ��е�Ŀ���ļ�������Ҫ��������·����
    	
    							File temp = fileManage.copyDirectionary(source, destiny);
    							if (temp == null) {
    								out.write(("Դ�ļ�������...").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 12:{  //12��ǰĿ¼��ѡ��һ���ļ�����һ�������ļ�     
    							File temp = fileManage.EncFile(source, destiny);
    							if (temp == null) {
    								out.write(("Դ�ļ�������...").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 13:{  //  13��ǰĿ¼��ѡ��һ�������ļ�����������ļ�
    							File temp = fileManage.DecFile(source, destiny);
    							if (temp == null) {
    								out.write(("Դ�ļ�������...").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 14:{  //���������ͻ��˴����ļ�

    						}
    							break;
    						case 15:{  //���������ͻ��˴����ļ�

    						}
    							break;
    						default:
    							break;
    						}
						}
                    	
                    	out.flush();
                    	if (cmd != null) {
                        	setCommand(null);
						}
                    }while (!in.equals("q"));
                    try{
                        out.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    //�������
    public static void main(String[] args) {
        Server server = new Server(1234);
        server.start();
    }
}


