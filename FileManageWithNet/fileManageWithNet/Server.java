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
            SM.start();//连接并返回socket后，再启用发送消息线程
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
    								out.write((temp.toString()).getBytes());
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

    						}
    							break;
    						case 15:{  //服务器到客户端传输文件

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


