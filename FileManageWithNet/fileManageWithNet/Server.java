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
  	 //1.读取的数组长度
         int lenght = ois.readInt();
         //2.读取次数
         long times = ois.readLong();
         //3.读取最后一次字节长度
         int lastBytes = ois.readInt();
         byte[] bytes = new byte[lenght];
         System.out.println("ready read!...");
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
    public void run(){

        super.run();
        try{
            System.out.println("wait client connect...");
            socket = server.accept();
            sendMessThread SM = new sendMessThread();
            SM.start();//连接并返回socket后，再启用发送消息线程
            System.out.println(socket.getInetAddress().getHostAddress()+"SUCCESS TO CONNECT...");
            InputStream in = socket.getInputStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len=in.read(buf))!=-1){
                //System.out.println("client saying: "+new String(buf,0,len));
            	if ((new String(buf, 0, len)).equals("15")) { //表示即将接受文件
            		System.out.println("准备接受文件！..");
            		receiveFile(in);
            		System.out.println("接受文件完毕！..");
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

                        	 //1为展示当前所在目录    2为展示当前目录下所有文件 3设置路径 4进入路径下的一个文件夹 
                            //5返回上一目录 6创建一个文件  7创建一个文件夹  8删除一个文件或文件夹 9在当前目录下拷贝一个文件
                        	switch (operate) {
    						case 1:{  //1为展示当前所在目录
    							out.write((fileManage.showCurrPath()).getBytes());
    		                    out.flush();
    						}
    						break;
    						case 2:{  // 2为展示当前目录下所有文件
    							ArrayList<String> temp = fileManage.disPlayFile();
    							if (temp == null) {
    								out.write(("错误路径!....").getBytes());
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
    						case 3:{  //3设置路径
    							File temp = fileManage.setPath(source);
    							if (temp == null) {
    								out.write(("设置路径失败！可能是"+source+"不存在....").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 4:{  //4进入路径下的一个文件夹 
    							File temp = fileManage.joinPath(source);
    							if (temp == null) {
    								out.write(("进入文件夹失败！可能是"+source+"不存在....").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 5:{  //5返回上一目录
    							File temp = fileManage.quitOneLevel();		
    							if (temp !=null ) {
    								out.write((temp.toString()).getBytes());
								}
    							else {
    								out.write(("无上一级目录！...").getBytes());
    							}
    								
    						}
    							break;
    						case 6:{  //6创建一个文件
    							File temp = fileManage.makeFile(source);
    							if (temp == null) {
    								out.write(("创建文件失败！可能是"+source+"已经存在....").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 7:{  //7创建一个文件夹
    							File temp = fileManage.makeDir(source);
    							if (temp == null) {
    								out.write(("创建文件夹失败！可能是"+source+"已经存在....").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 8:{  // 8删除一个文件或文件夹 
    							File temp = fileManage.deleteFile(source);
    							if (temp == null) {
    								out.write(("源文件不存在...").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 9:{  // 9在当前目录下拷贝一个文件
    							File temp = fileManage.copyCurrLoc(source);
    							if (temp == null) {
    								out.write(("源文件不存在...").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 10:{  //10拷贝一个文件到目的文件 （都需要输入完整路径） 
    							File temp = fileManage.copyFile(source,destiny);
    							if (temp == null) {
    								out.write(("源文件不存在...").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 11:{  //11拷贝一个文件夹到目的文件（都需要输入完整路径）
    	
    							File temp = fileManage.copyDirectionary(source, destiny);
    							if (temp == null) {
    								out.write(("源文件不存在...").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 12:{  //12当前目录下选择一个文件生成一个加密文件     
    							File temp = fileManage.EncFile(source, destiny);
    							if (temp == null) {
    								out.write(("源文件不存在...").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 13:{  //  13当前目录下选择一个加密文件输出到解密文件
    							File temp = fileManage.DecFile(source, destiny);
    							if (temp == null) {
    								out.write(("源文件不存在...").getBytes());
    							}
    							else
    							{
    								out.write((temp.toString()).getBytes());
    							}
    						}
    							break;
    						case 14:{  //服务器到客户端传输文件
    							File temp = new File(source);
    							if (!temp.exists() || temp.isDirectory()) {
    								out.write(("源文件不存在...").getBytes());
								}
    							else {
    								out.write(("14").getBytes());
    								fileManage.ServerToClient(out, source);
    								out.write((" ").getBytes());
    							}	
    						}
    							break;
    						case 15:{  //服务器到客户端传输文件
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

    //函数入口
    public static void main(String[] args) {
        Server server = new Server(1234);
        server.start();
    }
}


