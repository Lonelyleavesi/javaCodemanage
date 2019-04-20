package 学生管理系统;

public class Class {
	Classenum name;
	int ID;
	
	Class(Classenum n){
		this.name = n;
		ID = name.hashCode();
	}
	Class(){
		this.name = null;
		ID = name.hashCode();
	}
}
