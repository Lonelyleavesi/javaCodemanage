package fileManageWithNet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManage {
	
	private static final  int numOfEncAndDec = 0x99; //加密解密秘钥
	private	StringBuffer filePath;
	private File file;
	private File fileSeclected;

	public FileManage() {
		filePath = new StringBuffer("D:\\编程\\java\\FileManage\\test");
		file = new File(filePath.toString());
		fileSeclected = null;
	}
	
	//展示当前路径
	public String showCurrPath() {
		System.out.println("当前路径为："+ filePath.toString());
		return this.filePath.toString();
	}
	
	//显示当前选择的文件
	public File showCurrSelectFile() {
		System.out.println("当前选择的文件为:"+fileSeclected.getPath());
		return fileSeclected;
	}
	
	//设置路径
	public File setPath(String path) {    
		String tempPath = path;
		File tempFile = new File(tempPath);
		if (!tempFile.exists()) {
			System.out.println("设置路径失败！可能是"+path+"不存在....");
			return null;
		}
		else {
			filePath = new StringBuffer(path);
			file = new File(filePath.toString());
			System.out.println("路径设置成功："+filePath.toString());
		}
		return tempFile;
	}
	
	//进入下个文件夹 判断是否存在如果没有则进入失败
	public File joinPath(String ex) { 
		String tempPath = filePath.toString()+"\\"+ex;
		//System.out.println(tempPath);  打印出进入的路径
		File tempFile = new File(tempPath);
		if (!tempFile.exists() || !tempFile.isDirectory()) {
			System.out.println("进入路径失败！可能因为不存在"+ex+"或者"+ex+"不为文件夹...");
			return null;
		}
		else {
			filePath.append("\\"+ex);
			file = new File(filePath.toString());
			System.out.println("进入路径成功"+filePath.toString());
		}
		return tempFile;
	}
	
	//选择当前目录下的一个文件
	public File selectFile(String ex) { 
		String tempPath = filePath.toString()+"\\"+ex;
		//System.out.println(tempPath);  打印出进入的路径
		File tempFile = new File(tempPath);
		if (!tempFile.exists() || tempFile.isDirectory()) {
			//System.out.println("选择文件失败！可能因为不存在"+ex+"或者"+ex+"为文件夹...");
			return null;
		}
		else {
			fileSeclected = tempFile;
			System.out.println("选择文件成功："+fileSeclected.toString());
		}
		return tempFile;
	}
	
	//退出一层目录
 	public File quitOneLevel() {
		filePath = new StringBuffer(file.getParent()); 
		file = new File(filePath.toString());
		System.out.println("退出一层目录...");
		return file;
	}
	
	//建立一个文件夹
	public File makeDir(String dirName) {
		String tempPath = filePath.toString()+ "\\"+dirName;
		file = new File(tempPath);
		if(!file.exists()){//如果文件夹不存在
			System.out.println("文件夹"+dirName+"创建成功！");
			file.mkdir();//创建文件夹
		}
		else {
			file = null;
			System.out.println("文件夹"+dirName+"已经存在,不需要再创建!");
			//System.out.println("当前路径为："+ filePath.toString());
		}
		return file;
	}
	
	//建立一个文件
	public File makeFile(String fileName) {
		String tempPath = filePath.toString()+ "\\"+fileName;
		File tFile = new File(tempPath);
		if (!tFile.exists()) {
			try {
				tFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(fileName + "创建成功！");
		}
		else {
			tFile = null;
			System.out.println(fileName + "已经存在，不需要创建！");
		}
		return tFile;
	}
	
	//删除文件或者文件夹
	public File deleteFile(String fileName) {
		String tempPath = filePath.toString()+"\\"+fileName;
		File deteleTemp = new File(tempPath);
		File parent = deteleTemp.getParentFile();
		if (!deteleTemp.exists()) {
			return null;
		}
		if (deteleTemp.exists() && !deteleTemp.isDirectory()) {
			deteleTemp.delete();
		}
		else if (deteleTemp.exists() && deteleTemp.isDirectory()) {
			delFolder(deteleTemp.toString());
		} 
		return parent;
	}
	//删除一个文件夹
	private void delFolder(String folderPath) {
	     try {
	        delAllFile(folderPath); //删除完里面所有内容
	        String filePath = folderPath;
	        filePath = filePath.toString();
	        java.io.File myFilePath = new java.io.File(filePath);
	        myFilePath.delete(); //删除空文件夹
	     } catch (Exception e) {
	       e.printStackTrace(); 
	     }
	}
	//删除一个目录下的所有文件
	private  boolean delAllFile(String path) {
	       boolean flag = false;
	       File file = new File(path);
	       if (!file.exists()) {
	         return flag;
	       }
	       if (!file.isDirectory()) {
	         return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	          if (temp.isDirectory()) {
	             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
	             delFolder(path + "/" + tempList[i]);//再删除空文件夹
	             flag = true;
	          }
	       }
	       return flag;
	     }

	//加密文件的方法，输入一个当前路径已经存在的文件名，以及输出的文件名，得到一个加密的文件
	public File EncFile(String fileName,String outName) {
		String tempPathSorce = filePath.toString()+ "\\"+fileName;
		String tempPathOut = filePath.toString()+"\\" + outName;
		File  temp = null;
		File  fileForReturn = new File(tempPathSorce);
		//System.out.println("当前路径为："+ filePath.toString());
		File tFileSorce = new File(tempPathSorce);
		File tFileOut = new File(tempPathOut);
		if (!tFileSorce.exists()) {
			System.out.println("错误： "+ fileName + "不存在!");
			return null;
		}
		if (!tFileOut.exists()) {
			System.out.println("创建加密文件....");
			try {
				tFileOut.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			temp = EncFileHelp(tFileSorce, tFileOut);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
		//删除源文件，看是否需要
		//tFileSorce.delete();
	}
	//文件加密的子方法
	private  File EncFileHelp(File srcFile, File encFile) throws Exception {
		int dataOfFile = 0; //文件字节内容
		if(!srcFile.exists()){
			System.out.println("source file not exixt");
			return null;
		}
		
		if(!encFile.exists()){
			System.out.println("encrypt file created");
			encFile.createNewFile();
		}
		InputStream fis  = new FileInputStream(srcFile);
		OutputStream fos = new FileOutputStream(encFile);
		
		while ((dataOfFile = fis.read()) > -1) {
			fos.write(dataOfFile^numOfEncAndDec);
		}
		
		fis.close();
		fos.flush();
		fos.close();
		return encFile;
	}	
	
	//文件解密
	public File DecFile(String Encfile,String outName) {
		String tempPathSorce = filePath.toString()+ "\\"+Encfile;
		String tempPathOut = filePath.toString()+"\\" + outName;
		File  temp = null;
		//System.out.println("当前路径为："+ filePath.toString());
		File tFileSorce = new File(tempPathSorce);
		File tFileOut = new File(tempPathOut);
		if (!tFileSorce.exists()) {
			System.out.println("错误： "+ Encfile + "不存在!");
			return null;
		}
		if (!tFileOut.exists()) {
			System.out.println("创建解密文件....");
			try {
				tFileOut.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			temp = DecFilehelp(tFileSorce, tFileOut);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
	//文件解密子方法
	private File DecFilehelp(File encFile, File decFile) throws Exception {
		int dataOfFile = 0; //文件字节内容
		if(!encFile.exists()){
			System.out.println("encrypt file not exixt");
			return null;
		}
	 
		if(!decFile.exists()){
			System.out.println("decrypt file created");
			decFile.createNewFile();
		}
	 
		InputStream fis  = new FileInputStream(encFile);
		OutputStream fos = new FileOutputStream(decFile);
	 
		while ((dataOfFile = fis.read()) > -1) {
			fos.write(dataOfFile^numOfEncAndDec);
		}
		
		fis.close();
		fos.flush();
		fos.close();
		return decFile;
	 }
	//罗列当前目录下所有文件夹
	public ArrayList<String> disPlayFile() {   
		System.out.println("当前目录下所有文件夹：");
		file = new File(filePath.toString());
		ArrayList<String> files = new ArrayList<String>();
		if (filePath.length() == 0 || !file.exists()) {
			System.out.println("错误路径!");
			return null;
		}
		else {
			for(File temp:file.listFiles()){
				if (temp.isDirectory()) {
					files.add(temp.toString());
					System.out.println(temp.toString());
				}
			}
			System.out.println("当前目录下所有文件：");
			for(File temp:file.listFiles()){
				if (!temp.isDirectory()) {
					files.add(temp.toString());
					System.out.println(temp.toString());
				}
			}

		}
		return files;
	}
	
	//拷贝文件夹
	public File copyDirectionary(String sourceDir, String targetDir) 
		throws IOException {  
        // 新建目标目录   
        File temp = (new File(targetDir));  
        temp.mkdirs();
        // 获取源文件夹当前下的文件或目录   
        if (!(new File(sourceDir)).exists()) {
			return null;
		}
        File[] file = (new File(sourceDir)).listFiles();  
        for (int i = 0; i < file.length; i++) {  
            if (file[i].isFile()) {  
                // 源文件   
                File sourceFile=file[i];  
                // 目标文件   
               File targetFile=new File(new File(targetDir).getAbsolutePath()  
            				   +File.separator+file[i].getName());  
               copyFile(sourceFile.toString(),targetFile.toString());  
            }  
            if (file[i].isDirectory()) {  
                // 准备复制的源文件夹   
                String dir1=sourceDir + "/" + file[i].getName();  
                // 准备复制的目标文件夹   
                String dir2=targetDir + "/"+ file[i].getName();  
                copyDirectionary(dir1, dir2);  
            }  
        } 
        return temp;
    }  
	//为当前目录下的一个文件创建拷贝
	public File copyCurrLoc(String FileName) {
		File Source = selectFile(FileName);
		if (Source == null) {
			System.out.println("源文件不存在！");
			return null;
		}
		File Destiny = selectFile(FileName);
		StringBuffer tempName = new StringBuffer();
		
		int i = 1;    //为创建一个文件的多份拷贝设置的标识符
		String[] sourceStrArray = FileName.split("\\.");

		while (Destiny != null) {
			tempName.append(sourceStrArray[0] + "("+i+""+")"+"."+sourceStrArray[1]);
			i++;
			Destiny = selectFile(tempName.toString());
			if (Destiny == null) {
				makeFile(tempName.toString());
				Destiny = selectFile(tempName.toString());
				break;
			}
			tempName = new StringBuffer();
		}
		try {
			copyFile(Source.toString(), Destiny.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Destiny;
	}
	//拷贝一个文件 的子方法
	public File copyFile(String from,String to) throws IOException{
		if (!(new File(from)).exists()) {
			return null;
		}
		BufferedReader in=new BufferedReader(new FileReader(from));
		BufferedWriter out=new BufferedWriter(new FileWriter(to));
		String line=null;
		int linenumber=0;
		while((line=in.readLine())!=null){
			out.write(line+"\n");
			linenumber++;
		}
		in.close();
		out.close();
		System.out.println("line number "+linenumber);
		return new File(to);
	}
	
	public File ServerToClient(OutputStream out, String tFile) throws IOException {
		int val = 2048;
		File tfile = new File(tFile);
		if (!tfile.exists()) {
			return null;
		}
		ObjectOutputStream oos = new ObjectOutputStream(out);
		FileInputStream fis = new FileInputStream(tFile);
		long fileSize = tfile.length();
        byte[] bytes = new byte[val];
        System.out.println("开始读发送文件……！");
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
}
