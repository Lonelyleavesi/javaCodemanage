package ѧ������ϵͳ;

import java.util.Random;

public class Randomfun {
	//����������࣬�������
	Randomfun(){
		
	}
	static String getRandomName(){  //��������ַ�����������������
		 String StrFirst = "��Ǯ��������֣��������������������������ʩ�ſײ��ϻ���κ�ս���л����";
	     String str="��������֮��Ľ���������Ԫ��������հ�ɺ���������Ӻ���������ʥ�����ܶ������־�������ݽܺ��ؾ������������ͮ����Է�ܹ�����������ʥ�����Т�������������������˸������ٻ���������ڼ���";
	     Random random=new Random();
		 
	     StringBuffer sb=new StringBuffer();
	     int first = random.nextInt(StrFirst.length());
	     sb.append(StrFirst.charAt(first));
	     
	     int length = random.nextInt(2)+2;
	     for(int i=1;i<length;i++){
	       int number=random.nextInt(str.length());
	       sb.append(str.charAt(number));
	     }
	     return sb.toString();
	 }
	
	static Sex getRandomSex() { //��������Ա�
		 int pick = new Random().nextInt(Sex.values().length);
		 return Sex.values()[pick];
	 }
	
	static int getRandomScore(int num,int flot){
		Random rand = new Random();
		return rand.nextInt(flot) + num;
	}
}
