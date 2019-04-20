package ѧ������ϵͳ;

public class Teacher implements Comparable<Teacher>{
	static int IDbegin = 100;
	private int ID;
	private Sex sex;
	private String name;
	public Class t_cls;
	
	Teacher(Class t_ce){
		this.ID = ++IDbegin;
		this.sex = Randomfun.getRandomSex();
		this.name = Randomfun.getRandomName();
		this.t_cls = t_ce;
	}
	
	public int T_ID() {
		return this.ID;
	}
	public Sex T_sex() {
		return sex;
	}
	public String T_name(){
		return name;
	}
	public Class T_cls(){
		return t_cls;
	}

	
	
	 @Override   //�涨���򷽷����ڼ����б��Խ�ʦID��������
	 public int compareTo(Teacher tea){
		return this.ID - tea.ID;
	 }
	 @Override
	 public String toString() {
		 return ID +"\t"+name+"\t"+sex+"\n";
	 }
}
