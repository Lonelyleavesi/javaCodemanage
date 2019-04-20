package 学生管理系统;
import java.util.HashMap;

public class Student  implements Comparable<Student>{
	 private static int IDbegin = 1000;
	 private int ID;
	 private String name;
	 private Sex sex;
	 HashMap<Classenum,Scores> scos;
	 private int totalScore;
	 private int Rank;
	 Student(){

		 this.name = Randomfun.getRandomName(); //随机产生名字
		 this.sex = Randomfun.getRandomSex(); //随机产生性别
		 this.ID = ++IDbegin;  
		 Rank = IDbegin - 1000;    //排名或者序号
		 this.scos = new HashMap<Classenum,Scores>();  //通过科目查找成绩
		 for (Classenum clsname : Classenum.values()) {
			 this.scos.put(clsname, new Scores(clsname));
		}
		this.totalScore = 0;
	 }
	 //重建ID的静态常量
	 public static void rebuildID() {
		 IDbegin = 1000;
	 }
	 public int ID() {
		 return this.ID;
	 }
	 
	 public String name() {
		 return this.name;
	 }
	 public Sex Sex() {
		 return this.sex;
	 }
	 public Scores scos(Classenum clsname) {
		 return this.scos.get(clsname);
	 }
	 
	 public void setScore() {
		for(Classenum clsname : Classenum.values()) {
			this.scos.get(clsname).setAllScore();
			this.totalScore += this.scos.get(clsname).totalScore();
		}
	}
	 
	 public int Rank() {
		return Rank;
	}
	 public void setRank(int rank) {
		Rank = rank;
	}
	 public int totalScore() {
		 return this.totalScore;
	 }
	 //输出所有成绩
	 public void printScore() {
		 	System.out.println(name+"   \t序号是："+ Rank+ " 成绩为：");
		 for(Classenum clsname : Classenum.values()) {
			 System.out.print(scos.get(clsname)+" ");
		 }
		 System.out.println("总成绩为：" + this.totalScore);
	 }
	 //输出单科成绩 
	 public void printScore(Classenum clsname) {
		 System.out.println(name+"\t的单科排名为\t"+Rank+"\t成绩为： "+ scos.get(clsname)+" ");
	 }
	 
	 
	 
	 @Override   //规定排序方法用于加入列表后对学号进行排序
	 public int compareTo(Student stu){
		return this.ID - stu.ID;
	 }
	 
	 
	 @Override 
	 public String toString() {
		 return ID +" "+name+" "+sex+" ";
	 }
}
