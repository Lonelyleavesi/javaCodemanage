package client;

import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.chrono.ThaiBuddhistEra;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Component;

import javax.sound.midi.Receiver;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import javax.swing.JTextPane;
import java.awt.ScrollPane;
import javax.swing.event.CaretListener;
import javax.swing.text.DefaultCaret;

import client.Client.sendMessThread;

import javax.swing.event.CaretEvent;
import java.awt.Scrollbar;
import java.awt.Color;
import java.awt.TextArea;

public class ClientUI extends JFrame {

	//面向可视化界面的Client
	public class Client extends Thread {

	    //定义一个Socket对象
	    Socket socket = null;
	    int operator;
	    String sourse = "x";
	    String destiny = "x";
	    sendMessThread sMessThread;
	    public Client(String host, int port) {
	    	operator = 999;
	    	sMessThread = new sendMessThread();
	        try {
	            //需要服务器的IP地址和端口号，才能获得正确的Socket对象
	            socket = new Socket(host, port);
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	            status.append("Connecting Failed...\n");
				reciveData.setText(status.toString());
	        } catch (IOException e) {
	            e.printStackTrace();
	            status.append("Connecting Failed...\n");
				reciveData.setText(status.toString());
	        }

	    }
	    
	     
	    public synchronized int getOperator() {
	 	   return operator;
	    }
	     public synchronized int setOperator(int opt) {
	 	   operator = opt;
	 	   return operator;
	   }
	    public synchronized String getSource() {
	  	   return sourse;
	     }
	    public synchronized String getDestiny() {
	   	   return destiny;
	      }
	   
	   
	   public synchronized void setOPT(int opt, String sre, String des) {
	   	   setOperator(opt);
	   		//1为展示当前所在目录（相对路径）    2为展示当前目录下所有文件（相对路径） 3设置路径（绝对路径） 4进入路径下的一个文件夹 （相对路径）
	   		//5返回上一目录（相对路径） 6创建一个文件（相对路径）  7创建一个文件夹（相对路径）  8删除一个文件或文件夹（绝对路径） 9在当前目录下拷贝一个文件 （相对路径）
	   		if ( opt >= 3 && opt <= 9) {
				sourse = sre;  //只需要输入源文件 一个参数
			}
	   		//10拷贝一个文件到目的文件 （都需要输入（绝对路径）） //11拷贝一个文件夹到目的文件（都需要输入（绝对路径））
	   		//12当前目录下选择一个文件生成一个加密文件  （都需要输入（相对路径））     13当前目录下选择一个加密文件输出到解密文件（都需要输入（相对路径））
			if (opt >= 10 && opt <= 13) {
				sourse = sre;
				destiny = des;
			}
				
			if (opt  == 14) {  //服务器传文件到客户端
				sourse = sre;
				destiny = des;
			}
			if (opt  == 15) {  //客户端传文件到服务器
				sourse = sre;
				destiny = des;
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
	    	sMessThread.start();
	        status.append("开始接受数据 !"+"\n");
			reciveData.setText(status.toString());
	        super.run();
	        try {
	            // 读Sock里面的数据
	            InputStream is = socket.getInputStream();
	            byte[] buf = new byte[1024];
	            int len = 0; 
	            while ((len = is.read(buf)) != -1) {
	                System.out.println(new String(buf, 0, len)); 
	                status.append(new String(buf, 0, len)+"\n");
					reciveData.setText(status.toString());
	                if ((new String(buf, 0, len)).equals("14")) { //表示即将接受文件
						System.out.println("begin received file !");
						status.append("begin received file !"+"\n");
						reciveData.setText(status.toString());
						receiveFile(is);
						System.out.println("end received file !");
						status.append("end received file !"+"\n");
						reciveData.setText(status.toString());
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
	        status.append("文件发送完毕！"+"\n");
			reciveData.setText(status.toString());
			return tfile;	
	    }
	    
	    //往Socket里面写数据，需要新开一个线程
	    class sendMessThread extends Thread{
	        @Override
	        public void run() {
	            super.run();
	            //写操作
	            OutputStream os= null;
	            status.append("开始发送 !"+"\n");
				reciveData.setText(status.toString());
	            try {  
	                os= socket.getOutputStream(); 
	                do {
	                	int opt =getOperator();
	                	String tsourse = getSource();
	                    String  tdestiny = getDestiny();
	                    
						if (opt != 999) {
							System.out.println("send : " + (opt + "|" + tsourse +"|" + tdestiny));
		                    os.write((opt + "|" + tsourse +"|" + tdestiny).getBytes());
		                    os.flush();
		                    setOperator(999);
						}
						if (opt == 15) {
							os.write("15".getBytes());
							os.flush();
							sendFile(os,tsourse);
						}
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
	}
	
	
	private JTextField Source;
	private JTextField Desnity;
	private JTextField IPString;
	private JTextField portString;
	private StringBuffer status;
	TextArea reciveData;
	Client cTest;
	
	public ClientUI() {
		// TODO Auto-generated constructor stub
		
		setSize(681,552);
		setLocation(400,400);
		setTitle("Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel labelSource = new JLabel("Source");
		labelSource.setBounds(30, 74, 93, 18);
		getContentPane().add(labelSource);
		
		Source = new JTextField();
		Source.setBounds(113, 72, 243, 18);
		getContentPane().add(Source);
		Source.setColumns(10);
		
		JLabel labelDesnity = new JLabel("Desnity");
		labelDesnity.setBounds(30, 101, 78, 16);
		getContentPane().add(labelDesnity);
		
		Desnity = new JTextField();
		Desnity.setColumns(10);
		Desnity.setBounds(113, 100, 243, 18);
		getContentPane().add(Desnity);
		
		IPString = new JTextField();
		IPString.setText("127.0.0.1");
		IPString.setBounds(72, 23, 140, 18);
		getContentPane().add(IPString);
		IPString.setColumns(10);
		
		portString = new JTextField();
		portString.setText("1234");
		portString.setColumns(10);
		portString.setBounds(263, 23, 93, 18);
		getContentPane().add(portString);
		
		JLabel lblIp = new JLabel("IP\uFF1A");
		lblIp.setBounds(30, 24, 32, 18);
		getContentPane().add(lblIp);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(222, 23, 32, 18);
		getContentPane().add(lblPort);
		
		JLabel lblNewLabel = new JLabel("\u65E0\u987B\u8F93\u5165");
		lblNewLabel.setBounds(383, 49, 54, 15);
		getContentPane().add(lblNewLabel);
		
		JLabel lblsource = new JLabel("\u9700\u8981\u8F93\u5165Source");
		lblsource.setBounds(383, 135, 117, 18);
		getContentPane().add(lblsource);
		
		JLabel lblsourcedesnity = new JLabel("\u9700\u8981\u8F93\u5165Source\u4E0EDesnity");
		lblsourcedesnity.setBounds(383, 275, 187, 18);
		getContentPane().add(lblsourcedesnity);
		
		reciveData = new TextArea();
		reciveData.setBounds(30, 161, 347, 311);
		getContentPane().add(reciveData);
		
		status = new StringBuffer("");
		
		//与服务端建立连接
		JButton connect_button = new JButton("\u8FDE\u63A5");
		connect_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				String IP = IPString.getText();
				int port = Integer.parseInt(portString.getText());
				status.append("Begin Connecting...\n");
				reciveData.setText(status.toString());
		            //需要服务器的IP地址和端口号，才能获得正确的Socket对象
					cTest = new Client(IP, port);
					cTest.start();
				if (cTest.socket.isConnected()) {
					status.append("Connecting successful...\n");
					reciveData.setText(status.toString());
				}else {
					status.append("Connecting Failed...\n");
					reciveData.setText(status.toString());
				}
			}
		});
		connect_button.setBounds(382, 21, 93, 23);
		getContentPane().add(connect_button);
		
		
		//显示当前目录
		JButton ShowCurr_button = new JButton("\u53D6\u5F97\u5F53\u524D\u76EE\u5F55");
		ShowCurr_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				status.append("当前路径为："+"\n");
				reciveData.setText(status.toString());
				cTest.setOPT(1, "x", "x");
			}
		});

		ShowCurr_button.setBounds(383, 72, 121, 18);
		getContentPane().add(ShowCurr_button);
		
		JLabel lblReceive = new JLabel("\u72B6\u6001\u680F");
		lblReceive.setBounds(30, 137, 54, 15);
		getContentPane().add(lblReceive);
		
		//显示当期目录所有文件
		JButton disPlayFile_button = new JButton("\u5F53\u524D\u76EE\u5F55\u6240\u6709\u6587\u4EF6");
		disPlayFile_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.append("当期目录所有文件为："+"\n");
				reciveData.setText(status.toString());
				cTest.setOPT(2, "x", "x");
			}
		});
		disPlayFile_button.setBounds(514, 73, 141, 20);
		getContentPane().add(disPlayFile_button);
		
		//设置路径
		JButton SetPath_button = new JButton("\u8BBE\u7F6E\u8DEF\u5F84");
		SetPath_button.setForeground(Color.RED);
		SetPath_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.append("设置路径为："+Source.getText()+"....\n");
				reciveData.setText(status.toString());
				cTest.setOPT(3,Source.getText(), "x");
				status.append("当前路径为："+"\n");
				reciveData.setText(status.toString());
				cTest.setOPT(1, "x", "x");
			}
		});
		SetPath_button.setBounds(383, 161, 117, 20);
		getContentPane().add(SetPath_button);
		
		//进入文件夹
		JButton joinPath_button = new JButton("\u8FDB\u5165\u4E00\u4E2A\u6587\u4EF6\u5939");
		joinPath_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.append("进入文件夹："+Source.getText()+"....\n");
				reciveData.setText(status.toString());
				cTest.setOPT(4,Source.getText(), "x");
			}
		});
		joinPath_button.setBounds(507, 161, 141, 21);
		getContentPane().add(joinPath_button);
		
		//返回上一级目录
		JButton quitOneLevel_button = new JButton("\u8FD4\u56DE\u4E0A\u4E00\u76EE\u5F55");
		quitOneLevel_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.append("返回上一级目录..："+"\n");
				reciveData.setText(status.toString());
				cTest.setOPT(5, "x", "x");
				status.append("当前路径为："+"\n");
				reciveData.setText(status.toString());
				cTest.setOPT(1, "x", "x");
			}
		});
		quitOneLevel_button.setBounds(383, 100, 121, 18);
		getContentPane().add(quitOneLevel_button);
		
		//创建文件
		JButton makeFile_button = new JButton("\u521B\u5EFA\u4E00\u4E2A\u6587\u4EF6");
		makeFile_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.append("创建文件："+Source.getText()+"....\n");
				reciveData.setText(status.toString());
				cTest.setOPT(6,Source.getText(), "x");
			}
		});
		makeFile_button.setBounds(383, 191, 117, 18);
		getContentPane().add(makeFile_button);
		
		//创建文件夹
		JButton makeDis_button = new JButton("\u521B\u5EFA\u4E00\u4E2A\u6587\u4EF6\u5939");
		makeDis_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.append("创建文件夹："+Source.getText()+"....\n");
				reciveData.setText(status.toString());
				cTest.setOPT(7,Source.getText(), "x");
			}
		});
		makeDis_button.setBounds(507, 192, 140, 17);
		getContentPane().add(makeDis_button);
		
		//删除一个文件或文件夹
		JButton delete_button = new JButton("\u5220\u9664\u4E00\u4E2A\u6587\u4EF6\u6216\u6587\u4EF6\u5939");
		delete_button.setForeground(Color.BLACK);
		delete_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.append("删除文件或文件夹："+Source.getText()+"....\n");
				reciveData.setText(status.toString());
				cTest.setOPT(8,Source.getText(), "x");
			}
		});
		delete_button.setBounds(383, 219, 187, 18);
		getContentPane().add(delete_button);
		
		//创建一个文件的复制
		JButton copyCurrFile_button = new JButton("\u521B\u5EFA\u5F53\u524D\u76EE\u5F55\u4E00\u4E2A\u6587\u4EF6\u7684\u62F7\u8D1D");
		copyCurrFile_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.append("创建文件 "+Source.getText()+" 的拷贝....\n");
				reciveData.setText(status.toString());
				cTest.setOPT(9,Source.getText(), "x");
			}
		});
		copyCurrFile_button.setBounds(383, 247, 236, 18);
		getContentPane().add(copyCurrFile_button);
		
		//拷贝文件
		JButton copyFileToDes_button = new JButton("\u62F7\u8D1D\u5F53\u524D\u76EE\u5F55\u4E00\u4E2A\u6587\u4EF6\u81F3\u53E6\u4E00\u8DEF\u5F84");
		copyFileToDes_button.setForeground(Color.RED);
		copyFileToDes_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.append("拷贝文件 "+Source.getText()+"\n 至 "+Desnity.getText()+" ...\n");
				reciveData.setText(status.toString());
				cTest.setOPT(10,Source.getText(), Desnity.getText());
			}
		});
		copyFileToDes_button.setBounds(383, 303, 236, 18);
		getContentPane().add(copyFileToDes_button);
		
		
		//拷贝文件夹
		JButton copyDirToDes_button = new JButton("\u62F7\u8D1D\u5F53\u524D\u76EE\u5F55\u4E00\u4E2A\u6587\u4EF6\u5939\u81F3\u53E6\u4E00\u8DEF\u5F84");
		copyDirToDes_button.setForeground(Color.RED);
		copyDirToDes_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.append("拷贝文件夹 "+Source.getText()+"\n 至 "+Desnity.getText()+" ...\n");
				reciveData.setText(status.toString());
				cTest.setOPT(11,Source.getText(), Desnity.getText());
			}
		});
		copyDirToDes_button.setBounds(383, 331, 236, 18);
		getContentPane().add(copyDirToDes_button);
		
		//加密一个文件
		JButton EncFile_button = new JButton("\u4E3A\u4E00\u4E2A\u6587\u4EF6\u751F\u6210\u52A0\u5BC6\u6587\u4EF6(\u5F53\u524D\u76EE\u5F55)");
		EncFile_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.append("加密文件 "+Source.getText()+"\n 至 "+Desnity.getText()+" ...\n");
				reciveData.setText(status.toString());
				cTest.setOPT(12,Source.getText(), Desnity.getText());
			}
		});
		EncFile_button.setBounds(383, 359, 236, 18);
		getContentPane().add(EncFile_button);
		
		//解密一个文件
		JButton DecFile_button = new JButton("\u4E3A\u4E00\u4E2A\u6587\u4EF6\u751F\u6210\u89E3\u5BC6\u6587\u4EF6(\u5F53\u524D\u76EE\u5F55)");
		DecFile_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.append("解密文件 "+Source.getText()+"\n 至 "+Desnity.getText()+" ...\n");
				reciveData.setText(status.toString());
				cTest.setOPT(13,Source.getText(), Desnity.getText());
			}
		});
		DecFile_button.setBounds(383, 387, 236, 18);
		getContentPane().add(DecFile_button);
		
		//从服务器接受文件
		JButton recFileToClient_button = new JButton("\u4ECE\u670D\u52A1\u5668\u62F7\u8D1D\u6587\u4EF6\u81F3\u672C\u5730");
		recFileToClient_button.setForeground(Color.RED);
		recFileToClient_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.append("接受服务器文件 "+Source.getText()+"\n 至本地  "+Desnity.getText()+" ...\n");
				reciveData.setText(status.toString());
				cTest.setOPT(14,Source.getText(), Desnity.getText());
			}
		});
		recFileToClient_button.setBounds(383, 416, 236, 18);
		getContentPane().add(recFileToClient_button);
		
		//发送文件至服务器
		JButton sendFileToSer_button = new JButton("\u4ECE\u672C\u5730\u62F7\u8D1D\u6587\u4EF6\u81F3\u670D\u52A1\u5668");
		sendFileToSer_button.setForeground(Color.RED);
		sendFileToSer_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.append("发送本地文件 "+Source.getText()+"\n 至服务器   "+Desnity.getText()+" ...\n");
				reciveData.setText(status.toString());
				cTest.setOPT(15,Source.getText(), Desnity.getText());
			}
		});
		sendFileToSer_button.setBounds(383, 444, 236, 18);
		getContentPane().add(sendFileToSer_button);
		
		JLabel lblNewLabel_1 = new JLabel("\u7EA2\u8272\u5B57\u4F53\u6807\u6CE8\u7684\u6309\u94AE\u9700\u8981\u8F93\u5165\u7EDD\u5BF9\u8DEF\u5F84\uFF0C\u5373\u5B8C\u6574\u7684\u6587\u4EF6\u8DEF\u5F84");
		lblNewLabel_1.setForeground(Color.RED);
		lblNewLabel_1.setBounds(315, 116, 340, 23);
		getContentPane().add(lblNewLabel_1);
		
}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientUI frame = new ClientUI();
		frame.setVisible(true);
	}
}
