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
	
	private static final  int numOfEncAndDec = 0x99; //���ܽ�����Կ
	private	StringBuffer filePath;
	private File file;
	private File fileSeclected;

	public FileManage() {
		filePath = new StringBuffer("D:\\���\\java\\FileManage\\test");
		file = new File(filePath.toString());
		fileSeclected = null;
	}
	
	//չʾ��ǰ·��
	public String showCurrPath() {
		System.out.println("��ǰ·��Ϊ��"+ filePath.toString());
		return this.filePath.toString();
	}
	
	//��ʾ��ǰѡ����ļ�
	public File showCurrSelectFile() {
		System.out.println("��ǰѡ����ļ�Ϊ:"+fileSeclected.getPath());
		return fileSeclected;
	}
	
	//����·��
	public File setPath(String path) {    
		String tempPath = path;
		File tempFile = new File(tempPath);
		if (!tempFile.exists()) {
			System.out.println("����·��ʧ�ܣ�������"+path+"������....");
			return null;
		}
		else {
			filePath = new StringBuffer(path);
			file = new File(filePath.toString());
			System.out.println("·�����óɹ���"+filePath.toString());
		}
		return tempFile;
	}
	
	//�����¸��ļ��� �ж��Ƿ�������û�������ʧ��
	public File joinPath(String ex) { 
		String tempPath = filePath.toString()+"\\"+ex;
		//System.out.println(tempPath);  ��ӡ�������·��
		File tempFile = new File(tempPath);
		if (!tempFile.exists() || !tempFile.isDirectory()) {
			System.out.println("����·��ʧ�ܣ�������Ϊ������"+ex+"����"+ex+"��Ϊ�ļ���...");
			return null;
		}
		else {
			filePath.append("\\"+ex);
			file = new File(filePath.toString());
			System.out.println("����·���ɹ�"+filePath.toString());
		}
		return tempFile;
	}
	
	//ѡ��ǰĿ¼�µ�һ���ļ�
	public File selectFile(String ex) { 
		String tempPath = filePath.toString()+"\\"+ex;
		//System.out.println(tempPath);  ��ӡ�������·��
		File tempFile = new File(tempPath);
		if (!tempFile.exists() || tempFile.isDirectory()) {
			//System.out.println("ѡ���ļ�ʧ�ܣ�������Ϊ������"+ex+"����"+ex+"Ϊ�ļ���...");
			return null;
		}
		else {
			fileSeclected = tempFile;
			System.out.println("ѡ���ļ��ɹ���"+fileSeclected.toString());
		}
		return tempFile;
	}
	
	//�˳�һ��Ŀ¼
 	public File quitOneLevel() {
		filePath = new StringBuffer(file.getParent()); 
		file = new File(filePath.toString());
		System.out.println("�˳�һ��Ŀ¼...");
		return file;
	}
	
	//����һ���ļ���
	public File makeDir(String dirName) {
		String tempPath = filePath.toString()+ "\\"+dirName;
		file = new File(tempPath);
		if(!file.exists()){//����ļ��в�����
			System.out.println("�ļ���"+dirName+"�����ɹ���");
			file.mkdir();//�����ļ���
		}
		else {
			file = null;
			System.out.println("�ļ���"+dirName+"�Ѿ�����,����Ҫ�ٴ���!");
			//System.out.println("��ǰ·��Ϊ��"+ filePath.toString());
		}
		return file;
	}
	
	//����һ���ļ�
	public File makeFile(String fileName) {
		String tempPath = filePath.toString()+ "\\"+fileName;
		File tFile = new File(tempPath);
		if (!tFile.exists()) {
			try {
				tFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(fileName + "�����ɹ���");
		}
		else {
			tFile = null;
			System.out.println(fileName + "�Ѿ����ڣ�����Ҫ������");
		}
		return tFile;
	}
	
	//ɾ���ļ������ļ���
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
	//ɾ��һ���ļ���
	private void delFolder(String folderPath) {
	     try {
	        delAllFile(folderPath); //ɾ����������������
	        String filePath = folderPath;
	        filePath = filePath.toString();
	        java.io.File myFilePath = new java.io.File(filePath);
	        myFilePath.delete(); //ɾ�����ļ���
	     } catch (Exception e) {
	       e.printStackTrace(); 
	     }
	}
	//ɾ��һ��Ŀ¼�µ������ļ�
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
	             delAllFile(path + "/" + tempList[i]);//��ɾ���ļ���������ļ�
	             delFolder(path + "/" + tempList[i]);//��ɾ�����ļ���
	             flag = true;
	          }
	       }
	       return flag;
	     }

	//�����ļ��ķ���������һ����ǰ·���Ѿ����ڵ��ļ������Լ�������ļ������õ�һ�����ܵ��ļ�
	public File EncFile(String fileName,String outName) {
		String tempPathSorce = filePath.toString()+ "\\"+fileName;
		String tempPathOut = filePath.toString()+"\\" + outName;
		File  temp = null;
		File  fileForReturn = new File(tempPathSorce);
		//System.out.println("��ǰ·��Ϊ��"+ filePath.toString());
		File tFileSorce = new File(tempPathSorce);
		File tFileOut = new File(tempPathOut);
		if (!tFileSorce.exists()) {
			System.out.println("���� "+ fileName + "������!");
			return null;
		}
		if (!tFileOut.exists()) {
			System.out.println("���������ļ�....");
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
		//ɾ��Դ�ļ������Ƿ���Ҫ
		//tFileSorce.delete();
	}
	//�ļ����ܵ��ӷ���
	private  File EncFileHelp(File srcFile, File encFile) throws Exception {
		int dataOfFile = 0; //�ļ��ֽ�����
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
	
	//�ļ�����
	public File DecFile(String Encfile,String outName) {
		String tempPathSorce = filePath.toString()+ "\\"+Encfile;
		String tempPathOut = filePath.toString()+"\\" + outName;
		File  temp = null;
		//System.out.println("��ǰ·��Ϊ��"+ filePath.toString());
		File tFileSorce = new File(tempPathSorce);
		File tFileOut = new File(tempPathOut);
		if (!tFileSorce.exists()) {
			System.out.println("���� "+ Encfile + "������!");
			return null;
		}
		if (!tFileOut.exists()) {
			System.out.println("���������ļ�....");
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
	//�ļ������ӷ���
	private File DecFilehelp(File encFile, File decFile) throws Exception {
		int dataOfFile = 0; //�ļ��ֽ�����
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
	//���е�ǰĿ¼�������ļ���
	public ArrayList<String> disPlayFile() {   
		System.out.println("��ǰĿ¼�������ļ��У�");
		file = new File(filePath.toString());
		ArrayList<String> files = new ArrayList<String>();
		if (filePath.length() == 0 || !file.exists()) {
			System.out.println("����·��!");
			return null;
		}
		else {
			for(File temp:file.listFiles()){
				if (temp.isDirectory()) {
					files.add(temp.toString());
					System.out.println(temp.toString());
				}
			}
			System.out.println("��ǰĿ¼�������ļ���");
			for(File temp:file.listFiles()){
				if (!temp.isDirectory()) {
					files.add(temp.toString());
					System.out.println(temp.toString());
				}
			}

		}
		return files;
	}
	
	//�����ļ���
	public File copyDirectionary(String sourceDir, String targetDir) 
		throws IOException {  
        // �½�Ŀ��Ŀ¼   
        File temp = (new File(targetDir));  
        temp.mkdirs();
        // ��ȡԴ�ļ��е�ǰ�µ��ļ���Ŀ¼   
        if (!(new File(sourceDir)).exists()) {
			return null;
		}
        File[] file = (new File(sourceDir)).listFiles();  
        for (int i = 0; i < file.length; i++) {  
            if (file[i].isFile()) {  
                // Դ�ļ�   
                File sourceFile=file[i];  
                // Ŀ���ļ�   
               File targetFile=new File(new File(targetDir).getAbsolutePath()  
            				   +File.separator+file[i].getName());  
               copyFile(sourceFile.toString(),targetFile.toString());  
            }  
            if (file[i].isDirectory()) {  
                // ׼�����Ƶ�Դ�ļ���   
                String dir1=sourceDir + "/" + file[i].getName();  
                // ׼�����Ƶ�Ŀ���ļ���   
                String dir2=targetDir + "/"+ file[i].getName();  
                copyDirectionary(dir1, dir2);  
            }  
        } 
        return temp;
    }  
	//Ϊ��ǰĿ¼�µ�һ���ļ���������
	public File copyCurrLoc(String FileName) {
		File Source = selectFile(FileName);
		if (Source == null) {
			System.out.println("Դ�ļ������ڣ�");
			return null;
		}
		File Destiny = selectFile(FileName);
		StringBuffer tempName = new StringBuffer();
		
		int i = 1;    //Ϊ����һ���ļ��Ķ�ݿ������õı�ʶ��
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
	//����һ���ļ� ���ӷ���
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
        System.out.println("��ʼ�������ļ�������");
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
}
