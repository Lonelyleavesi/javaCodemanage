package ѧ������ϵͳ;

import java.util.ArrayList;

public class Classrooms {
	static int num = 0;
	int clsID;
	ArrayList<Student> c_stus;
	Teacher c_tcher;
	private Class cls;
	
	Classrooms(Classenum clsname){
		this.clsID = ++num;
		this.c_stus = new ArrayList<Student>();
		this.cls = new Class(clsname);
		this.c_tcher = new Teacher(cls);
	}
	
	public void setStuAllScore(Classenum clsname) {
		this.setStuDayScore(clsname);
		this.setStuExScore(clsname);
		this.setStuMidScore(clsname);
		this.setStuFinScore(clsname);
	}
	public Class getCls() {
		return cls;
	}
	
	public void setStuDayScore(Classenum clsname) {  //����ÿ��ѧ����ƽʱ�ɼ�
		for(Student stu :c_stus) {
			stu.scos(clsname).setDayScore();
		}
	}
	public void setStuMidScore(Classenum clsname) {  //����ÿ��ѧ����ƽʱ�ɼ�
		for(Student stu :c_stus) {
			stu.scos(clsname).setMidScore();
		}
	}
	public void setStuExScore(Classenum clsname) {  //����ÿ��ѧ����ƽʱ�ɼ�
		for(Student stu :c_stus) {
			stu.scos(clsname).setExScore();
		}
	}
	public void setStuFinScore(Classenum clsname) {  //����ÿ��ѧ����ƽʱ�ɼ�
		for(Student stu :c_stus) {
			stu.scos(clsname).setFinScore();
		}
	}
	public void printStudent(){
		for(Student stu : c_stus) {
			System.out.println(stu);
		}
	}
	public void printTeacher() {
		System.out.println(c_tcher + "\n");
	}
	
	public Scores findScoreByName(String name) {
		for (Student stu : c_stus) {
			if(name == stu.name())
			{
				stu.printScore(cls.name);
				return stu.scos.get(cls.name);
			}
		}
		return null;
	}
	
	 @Override 
	 public String toString() {
		 return "\n���Ϊ:"+clsID+"\n��ʦΪ:"+c_tcher.T_name() + "\n��ĿΪ:"+cls.name+"\nѧ������Ϊ:"+c_stus.size()+"\n"+c_stus;
	 }
}
