package 学生管理系统;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;


public class Simulation {
	ArrayList<Classrooms> clasManage;
	ArrayList<Student> stuManage;
	static int minPersonNumInClassroom = 20; //一个班最小的人数
	static int peopleNum = 100;
	static Scanner sc = new Scanner(System.in); 
	
	//初始化
	Simulation(){
		int numOfClass = Classenum.values().length;
		clasManage = new ArrayList<Classrooms>();
		stuManage = new ArrayList<Student>();
		for(int i = 0; i < numOfClass; i ++) {
			for(int j = 0 ; j < 2 ; j++) {
				clasManage.add(new Classrooms(Classenum.values()[i]));
			}
		}
		initStudent();
	}
	//初始化学生
	private void initStudent() {
		this.clearStudent();
		Student.rebuildID();
		for(int n = 0 ; n < peopleNum; n++)
		{
			stuManage.add(new Student());
		}
	}
	//删除教学班中的学生
	private void clearStuOfClasMange() {
		for(Classrooms cls : this.clasManage) {
			cls.c_stus.clear();
		}
	}
	//清除学生
	private void clearStudent() {
		stuManage.clear();
	}
	//输出列表中所有学生
	private void printEveryStudentScore(ArrayList<Student> stuM){
		for(Student stu : stuM) {
			stu.printScore();
		}
	}
	
	//输出每个学生的成绩
	private void printEveryStudentScore(ArrayList<Student> stuM,Classenum clsname){
		for(Student stu : stuM) {
			stu.printScore(clsname);
		}
	}
	//所有选课
	private void AllStudentSeclect() {
		this.clearStuOfClasMange();
		boolean flag = false;
		while (true) {
			for(Student stu : this.stuManage) {
				SeclectClass(stu);
			}
			for(Classrooms cls : this.clasManage) {
				if(cls.c_stus.size() < minPersonNumInClassroom)
				{
					flag = true;
					break;
				}
			}
			if(flag == false)
			{return ;}
			else {this.clearStuOfClasMange();}
		}
		
	}
	//一名学生进行选课
	private void SeclectClass(Student stu) {
		for(int i = 0; i < Classenum.values().length; i ++) { //ClasManage中0 1是一个科目 2 3 是一个科目 3 4是一个科目
			Random random=new Random();
			int  offset = random.nextInt(2);
			this.clasManage.get(i*2+offset).c_stus.add(stu);
		}
	}
	//按照name寻找一个学生信息
	private void find() {
		System.out.println("\n\n输入一个要查找学生的信息： ");
		String  name = sc.nextLine();
		int result = findScoreByname(name);
		if (result == 0) {
			System.out.println("未查询到此人。");
		}
		
	}
	private int findScoreByname(String name) {
		 for(Student stu :stuManage) {
			 if(stu.name().equals(name)) {
				 stu.printScore();
				return 1; 
			 }
		 }
		 return 0;
	}
	//-------------------------------------------------------
	//取得成绩
	private void getScore() {
		for (Student stu : stuManage) {
			stu.setScore();
		}
	}
	
	//按照总成绩排序输出学生
	private void printBySortByTotalScore() {
		ArrayList<Student> temp = this.stuManage;
		Collections.sort(temp,new Comparator<Student> (){ //自定义排序方法

			@Override
			public int compare(Student o1, Student o2) {
				return o2.totalScore() - o1.totalScore();
			}
	
		});
		for(int i = 0; i < temp.size();i++) {
			temp.get(i).setRank(i+1);
		}
		this.printEveryStudentScore(temp);
		temp.clear();
		temp = null;
	}
	
	//按照单科成绩排名
	private void printBySortByOneTotalScore() { 
		System.out.print("请输入需要排序的科目，包括：");
		for(Classenum name : Classenum.values()) {
				System.out.print(name.toString()+",");
			}
		System.out.print("\n");
		boolean flag = true;
		Classenum target = null;
		while (flag) {
			System.out.print("\n");
			String clsname = sc.nextLine();	
			for(Classenum name : Classenum.values()) {
				if (name.toString().equals(clsname)) {
					flag = false;
					target = name;
					break;
				}
			}
			if(flag) {
				System.out.print("输入不正确请重新输入，包括：");
				for(Classenum name : Classenum.values()) {
					System.out.print(name.toString()+",");
				}
				System.out.print("\n");
			}
		}
		this.oneClaseeSort(target);
	}
	//单科成绩排名
	private void oneClaseeSort(Classenum target){
		ArrayList<Student> temp = this.stuManage;
		Collections.sort(temp, new Comparator<Student>(){ //自定义排序方法
			@Override
			public int compare(Student o1, Student o2) {
				return o2.scos.get(target).totalScore() - o1.scos.get(target).totalScore();
			}
		});
		for(int i = 0; i < temp.size();i++) {
			temp.get(i).setRank(i+1);
		}
		this.printEveryStudentScore(temp,target);
		temp.clear();
		temp = null;
	}
	
	//统计各科分布
	private void gatherAllGraph() {
		for(Classenum clsname : Classenum.values()) {
			for(Scoretype type : Scoretype.values()) {
				initGraph(clsname, type);
			}
		}
	}
	private void gatherOneGraph() {
		boolean flag = true;
		Classenum targetClass = null;
		Scoretype targetScore = null;
		System.out.print("请输入需要查看分布的科目，包括：");
		for(Classenum name : Classenum.values()) {
				System.out.print(name.toString()+",");
			}
		
		while (flag) {
			System.out.print("\n");
			String clsname = sc.nextLine();	
			for(Classenum name : Classenum.values()) {
				if (name.toString().equals(clsname)) {
					flag = false;
					targetClass = name;
					break;
				}
			}
			if(flag) {
				System.out.print("输入不正确请重新输入，包括：");
				for(Classenum name : Classenum.values()) {
					System.out.print(name.toString()+",");
				}
			}
				System.out.print("\n");
		}
		
		flag = true;
		System.out.print("请输入需要查看分布的成绩类型，包括：");
		for(Scoretype type : Scoretype.values()) {
				System.out.print(type.toString()+",");
			}
		while (flag) {
			System.out.print("\n");
			String typeScore = sc.nextLine();	
			for(Scoretype type : Scoretype.values()) {
				if (type.toString().equals(typeScore)) {
					flag = false;
					targetScore = type;
					break;
				}
			}
			if(flag) {
				System.out.print("输入不正确请重新输入，包括：");
				for(Scoretype type : Scoretype.values()) {
					System.out.print(type.toString()+",");
				}
			}
				System.out.print("\n");
		}
		initGraph(targetClass,targetScore);
	}
	//输出分布的子方法
	private void initGraph(Classenum clsnname, Scoretype type) {
		int [] collec;
		collec = new int[5];
		int sub = Scores.getSubByType(type);
		for(Student stu : this.stuManage) {
			int Scorenum = stu.scos.get(clsnname).getScoreByType(type);
			int index = 0;
			if (sub*0 <= Scorenum  && Scorenum < sub*0.6 ) {
				index = 0;
			}
			if (sub*0.6 <= Scorenum  && Scorenum < sub*0.7 ) {
				index = 1;
			}
			if (0.7 <= Scorenum  && Scorenum < sub*0.8 ) {
				index = 2;
			}
			if (sub*0.8 <= Scorenum  && Scorenum < sub*0.9 ) {
				index = 3;
			}
			if (sub*0.9 <= Scorenum  && Scorenum < sub ) {
				index = 4;
			}
			collec[index]++;
		}
		System.out.println("\n"+clsnname+"的"+type+"\t分布如下:");
		System.out.print(sub*0+"~"+sub*0.6+"\t");
		System.out.print(sub*0.6+"~"+sub*0.7+"\t");
		System.out.print(sub*0.7+"~"+sub*0.8+"\t");
		System.out.print(sub*0.8+"~"+sub*0.9+"\t");
		System.out.print(sub*0.9+"~"+sub+"\t");

		System.out.print("\n");
		System.out.print(collec[0]+"\t");
		for(int i = 2; i <= 5 ; i ++) {
			System.out.print("\t"+collec[i-1]+"\t");
		}
	}
	
	//命令行界面
	private void Show(Simulation sim) {
		boolean quitFlag = true;
		while(quitFlag)
		{
			System.out.println("\n输入需要执行的命令： ");
			System.out.println("1、输出所有学生分数情况（按学号排序）");
			System.out.println("2、对学生进行随机分班(重新分配)");
			System.out.println("3、学生获取分数(重新取得分数)");
			System.out.println("4、输出各个班级情况");
			System.out.println("5、根据学生姓名查询成绩");
			System.out.println("6、输出所有学生分数情况（按总成绩排序，序号则为排名）");
			System.out.println("7、输出所有学生分数情况（按单科成绩排序，序号则为排名）");
			System.out.println("8、输出所有学生(单科期末)成绩分布");
			System.out.println("9、输出各科学生(各科期末)成绩分布");
			System.out.println("0、退出");
			char cmd = sc.next().charAt(0);

			switch (cmd) {
			case '1':{//输出学生情况
				System.out.println("随机生成的学生信息如下：");
				printEveryStudentScore(sim.stuManage);
				String  puse = sc.nextLine();
				String  puse1 = sc.nextLine();
					}
					break;
			case '2':{ //分班
				sim.AllStudentSeclect();
				String  puse = sc.nextLine();
					}
					break;
			case '3':{ //取得分数
				sim.getScore();
				String  puse = sc.nextLine();
					}
					break;
			case '4':{//输出班级情况
				System.out.println("随机生成的教学班信息如下：");
				System.out.println(sim.clasManage);
				String  puse = sc.nextLine();
					}
					break;
			case '5':{//根据信息查询成绩
				String  puse = sc.nextLine();
						sim.find();
				String  puse1 = sc.nextLine();
					}
					break;
			case '6':{//根据总成绩排名输出
				sim.printBySortByTotalScore();
				String  puse = sc.nextLine();
				String  puse1 = sc.nextLine();
					}
					break;
			case '7':{//根据总成绩排名输出
				String  puse = sc.nextLine();
				sim.printBySortByOneTotalScore();
				String  puse1 = sc.nextLine();
					}
					break;
			case '8':{//输出成绩分布
				String  puse = sc.nextLine();
				sim.gatherOneGraph();
				String  puse1 = sc.nextLine();
					}
					break;
			case '9':{//输出成绩分布
				String  puse = sc.nextLine();
				sim.gatherAllGraph();
				String  puse1 = sc.nextLine();
					}
					break;
			case '0': quitFlag = false; break;
			default:
				System.out.println("请输入一个0~7的整数。");
				break;
			}
		}

	}
	
	public static void main(String[] args) { 
		Simulation sim = new Simulation();		
		sim.Show(sim);
		
	}

}
