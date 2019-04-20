package ѧ������ϵͳ;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;


public class Simulation {
	ArrayList<Classrooms> clasManage;
	ArrayList<Student> stuManage;
	static int minPersonNumInClassroom = 20; //һ������С������
	static int peopleNum = 100;
	static Scanner sc = new Scanner(System.in); 
	
	//��ʼ��
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
	//��ʼ��ѧ��
	private void initStudent() {
		this.clearStudent();
		Student.rebuildID();
		for(int n = 0 ; n < peopleNum; n++)
		{
			stuManage.add(new Student());
		}
	}
	//ɾ����ѧ���е�ѧ��
	private void clearStuOfClasMange() {
		for(Classrooms cls : this.clasManage) {
			cls.c_stus.clear();
		}
	}
	//���ѧ��
	private void clearStudent() {
		stuManage.clear();
	}
	//����б�������ѧ��
	private void printEveryStudentScore(ArrayList<Student> stuM){
		for(Student stu : stuM) {
			stu.printScore();
		}
	}
	
	//���ÿ��ѧ���ĳɼ�
	private void printEveryStudentScore(ArrayList<Student> stuM,Classenum clsname){
		for(Student stu : stuM) {
			stu.printScore(clsname);
		}
	}
	//����ѡ��
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
	//һ��ѧ������ѡ��
	private void SeclectClass(Student stu) {
		for(int i = 0; i < Classenum.values().length; i ++) { //ClasManage��0 1��һ����Ŀ 2 3 ��һ����Ŀ 3 4��һ����Ŀ
			Random random=new Random();
			int  offset = random.nextInt(2);
			this.clasManage.get(i*2+offset).c_stus.add(stu);
		}
	}
	//����nameѰ��һ��ѧ����Ϣ
	private void find() {
		System.out.println("\n\n����һ��Ҫ����ѧ������Ϣ�� ");
		String  name = sc.nextLine();
		int result = findScoreByname(name);
		if (result == 0) {
			System.out.println("δ��ѯ�����ˡ�");
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
	//ȡ�óɼ�
	private void getScore() {
		for (Student stu : stuManage) {
			stu.setScore();
		}
	}
	
	//�����ܳɼ��������ѧ��
	private void printBySortByTotalScore() {
		ArrayList<Student> temp = this.stuManage;
		Collections.sort(temp,new Comparator<Student> (){ //�Զ������򷽷�

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
	
	//���յ��Ƴɼ�����
	private void printBySortByOneTotalScore() { 
		System.out.print("��������Ҫ����Ŀ�Ŀ��������");
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
				System.out.print("���벻��ȷ���������룬������");
				for(Classenum name : Classenum.values()) {
					System.out.print(name.toString()+",");
				}
				System.out.print("\n");
			}
		}
		this.oneClaseeSort(target);
	}
	//���Ƴɼ�����
	private void oneClaseeSort(Classenum target){
		ArrayList<Student> temp = this.stuManage;
		Collections.sort(temp, new Comparator<Student>(){ //�Զ������򷽷�
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
	
	//ͳ�Ƹ��Ʒֲ�
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
		System.out.print("��������Ҫ�鿴�ֲ��Ŀ�Ŀ��������");
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
				System.out.print("���벻��ȷ���������룬������");
				for(Classenum name : Classenum.values()) {
					System.out.print(name.toString()+",");
				}
			}
				System.out.print("\n");
		}
		
		flag = true;
		System.out.print("��������Ҫ�鿴�ֲ��ĳɼ����ͣ�������");
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
				System.out.print("���벻��ȷ���������룬������");
				for(Scoretype type : Scoretype.values()) {
					System.out.print(type.toString()+",");
				}
			}
				System.out.print("\n");
		}
		initGraph(targetClass,targetScore);
	}
	//����ֲ����ӷ���
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
		System.out.println("\n"+clsnname+"��"+type+"\t�ֲ�����:");
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
	
	//�����н���
	private void Show(Simulation sim) {
		boolean quitFlag = true;
		while(quitFlag)
		{
			System.out.println("\n������Ҫִ�е���� ");
			System.out.println("1���������ѧ�������������ѧ������");
			System.out.println("2����ѧ����������ְ�(���·���)");
			System.out.println("3��ѧ����ȡ����(����ȡ�÷���)");
			System.out.println("4����������༶���");
			System.out.println("5������ѧ��������ѯ�ɼ�");
			System.out.println("6���������ѧ��������������ܳɼ����������Ϊ������");
			System.out.println("7���������ѧ����������������Ƴɼ����������Ϊ������");
			System.out.println("8���������ѧ��(������ĩ)�ɼ��ֲ�");
			System.out.println("9���������ѧ��(������ĩ)�ɼ��ֲ�");
			System.out.println("0���˳�");
			char cmd = sc.next().charAt(0);

			switch (cmd) {
			case '1':{//���ѧ�����
				System.out.println("������ɵ�ѧ����Ϣ���£�");
				printEveryStudentScore(sim.stuManage);
				String  puse = sc.nextLine();
				String  puse1 = sc.nextLine();
					}
					break;
			case '2':{ //�ְ�
				sim.AllStudentSeclect();
				String  puse = sc.nextLine();
					}
					break;
			case '3':{ //ȡ�÷���
				sim.getScore();
				String  puse = sc.nextLine();
					}
					break;
			case '4':{//����༶���
				System.out.println("������ɵĽ�ѧ����Ϣ���£�");
				System.out.println(sim.clasManage);
				String  puse = sc.nextLine();
					}
					break;
			case '5':{//������Ϣ��ѯ�ɼ�
				String  puse = sc.nextLine();
						sim.find();
				String  puse1 = sc.nextLine();
					}
					break;
			case '6':{//�����ܳɼ��������
				sim.printBySortByTotalScore();
				String  puse = sc.nextLine();
				String  puse1 = sc.nextLine();
					}
					break;
			case '7':{//�����ܳɼ��������
				String  puse = sc.nextLine();
				sim.printBySortByOneTotalScore();
				String  puse1 = sc.nextLine();
					}
					break;
			case '8':{//����ɼ��ֲ�
				String  puse = sc.nextLine();
				sim.gatherOneGraph();
				String  puse1 = sc.nextLine();
					}
					break;
			case '9':{//����ɼ��ֲ�
				String  puse = sc.nextLine();
				sim.gatherAllGraph();
				String  puse1 = sc.nextLine();
					}
					break;
			case '0': quitFlag = false; break;
			default:
				System.out.println("������һ��0~7��������");
				break;
			}
		}

	}
	
	public static void main(String[] args) { 
		Simulation sim = new Simulation();		
		sim.Show(sim);
		
	}

}
