package fileManageWithNet;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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
    String DesReceiveFromClient;
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
    	//System.out.println("set command to: " + str);
    	command = str;
   	   return command;
      }
    
    public synchronized String getDes() {
   	   return DesReceiveFromClient;
      }
     public synchronized String setDes(String str) {
     	//System.out.println("set command to " + str);
    	 DesReceiveFromClient = str;
    	   return DesReceiveFromClient;
       }
     
     public void receiveFile(InputStream is)throws IOException {
  	   //System.out.println("join received file !");
  	   ObjectInputStream ois = new ObjectInputStream(is);
  	   //System.out.println("ready fos!");
  	   FileOutputStream fos = new FileOutputStream(DesReceiveFromClient);
  	   //System.out.println("end fos!");
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
                //System.out.println("client saying: "+new String(buf,0,len));
            	if ((new String(buf, 0, len)).equals("15")) { //��ʾ���������ļ�
            		System.out.println("׼�������ļ���..");
            		receiveFile(in);
            		System.out.println("�����ļ���ϣ�..");
            	}
            	else {
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
                        	//System.out.println("operate is "+ operate +"\nsoucce is: "+ source+"\ndestiny is: " + destiny);

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
    							if (temp !=null ) {
    								out.write((temp.toString()).getBytes());
								}
    							else {
    								out.write(("����һ��Ŀ¼��...").getBytes());
    							}
    								
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
    							File temp = new File(source);
    							if (!temp.exists() || temp.isDirectory()) {
    								out.write(("Դ�ļ�������...").getBytes());
								}
    							else {
    								out.write(("14").getBytes());
    								fileManage.ServerToClient(out, source);
    								out.write((" ").getBytes());
    							}	
    						}
    							break;
    						case 15:{  //���������ͻ��˴����ļ�
    							setDes(destiny);
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


