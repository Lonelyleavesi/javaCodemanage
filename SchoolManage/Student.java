package ѧ������ϵͳ;
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

		 this.name = Randomfun.getRandomName(); //�����������
		 this.sex = Randomfun.getRandomSex(); //��������Ա�
		 this.ID = ++IDbegin;  
		 Rank = IDbegin - 1000;    //�����������
		 this.scos = new HashMap<Classenum,Scores>();  //ͨ����Ŀ���ҳɼ�
		 for (Classenum clsname : Classenum.values()) {
			 this.scos.put(clsname, new Scores(clsname));
		}
		this.totalScore = 0;
	 }
	 //�ؽ�ID�ľ�̬����
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
	 //������гɼ�
	 public void printScore() {
		 	System.out.println(name+"   \t����ǣ�"+ Rank+ " �ɼ�Ϊ��");
		 for(Classenum clsname : Classenum.values()) {
			 System.out.print(scos.get(clsname)+" ");
		 }
		 System.out.println("�ܳɼ�Ϊ��" + this.totalScore);
	 }
	 //������Ƴɼ� 
	 public void printScore(Classenum clsname) {
		 System.out.println(name+"\t�ĵ�������Ϊ\t"+Rank+"\t�ɼ�Ϊ�� "+ scos.get(clsname)+" ");
	 }
	 
	 
	 
	 @Override   //�涨���򷽷����ڼ����б���ѧ�Ž�������
	 public int compareTo(Student stu){
		return this.ID - stu.ID;
	 }
	 
	 
	 @Override 
	 public String toString() {
		 return ID +" "+name+" "+sex+" ";
	 }
}
