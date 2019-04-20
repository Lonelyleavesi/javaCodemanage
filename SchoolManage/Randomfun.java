package 学生管理系统;

import java.util.Random;

public class Randomfun {
	//随机函数的类，方便调用
	Randomfun(){
		
	}
	static String getRandomName(){  //随机生成字符串，用于生成名字
		 String StrFirst = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻";
	     String str="梦琪忆柳之桃慕青问兰尔岚元香初夏沛菡傲珊曼文乐菱子赫祺祾朝彦圣鹏新哲鼎明楷林景瑞琪洋捷杰寒柏敬易荣轩君昊熠彤鸿煊苑杰贵伦煜洋郎霖圣烨瀚天孝云厉俞宇柏轩睿睿霖泓贻烁政鑫煜倩皓宸钰天昆勤嘉洋";
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
	
	static Sex getRandomSex() { //随机生成性别
		 int pick = new Random().nextInt(Sex.values().length);
		 return Sex.values()[pick];
	 }
	
	static int getRandomScore(int num,int flot){
		Random rand = new Random();
		return rand.nextInt(flot) + num;
	}
}
