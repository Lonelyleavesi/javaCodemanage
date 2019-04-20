package 学生管理系统;


public class Scores {
	private Class cls;
	//默认平时分 = 15， mid=15 ex = 20 fin = 30 total = 100
	private int dayScore;
	private int midScore;
	private int exScore;
	private int finScore;
	private int totalScore;
	public int [ ]Scores =null;
	
	Scores(){	}
	Scores(Classenum clsname){
		cls = new Class(clsname);
		dayScore = 0;
		midScore = 0;
		exScore  = 0;
		finScore = 0;
		totalScore = 0;
		Scores = new int[5];
	}
	public int getScoreByType(Scoretype type) {
		switch (type) {
		case dayScore:
			return dayScore;
		case midScore:
			return midScore;
		case exScore:
			return exScore;
		case finScore:
			return finScore;
		case totalScore:
			return totalScore;
		default:
			System.out.println("CIN is Error!");
			break;
		}
		return -1;
	}
	
	public static int getSubByType(Scoretype type) { //为分布准备的分数片
		switch (type) {
		case dayScore:
			return 15;
		case midScore:
			return 15;
		case exScore:
			return 20;
		case finScore:
			return 50;
		case totalScore:
			return 100;
		default:
			System.out.println("CIN is Error!");
			break;
		}
		return -1;
	}
	public Class getCls() {
		return cls;
	}
	public int dayScore() {
		return dayScore;
	}
	public int midScore() {
		return midScore;
	}
	public int exSocore() {
		return exScore;
	}
	public int finScore() {
		return finScore;
	}
	
	public int totalScore() {
		return totalScore;
	}
	//重置分数
	public void setAllScore() {
		this.setDayScore();
		this.setMidScore();
		this.setExScore();
		this.setFinScore();
		this.totalScore = dayScore + midScore + exScore + finScore ;
		Scores[0] = dayScore;
		Scores[1] = midScore;
		Scores[2] = exScore;
		Scores[3] = finScore;
		Scores[4] = totalScore;
		
	}
	
	public void setDayScore() {
		this.dayScore = Randomfun.getRandomScore(10,5);
		this.totalScore = dayScore + midScore + exScore + finScore ;
	}
	public void setMidScore() {
		this.midScore = Randomfun.getRandomScore(8,7);
		this.totalScore = dayScore + midScore + exScore + finScore ;
	}
	public void setExScore() {
		this.exScore = Randomfun.getRandomScore(10,10);
		this.totalScore = dayScore + midScore + exScore + finScore ;
	}
	public void setFinScore() {
		this.finScore = Randomfun.getRandomScore(30,20);
		this.totalScore = dayScore + midScore + exScore + finScore ;
	}
	
	
	@Override 
	public String toString() {
		return " | "+cls.name +":平时成绩:"+dayScore+" 中期成绩:"+midScore+" 实验成绩:"+exScore+" 期末成绩:"+ finScore+" 总成绩："+totalScore;
	}
}
