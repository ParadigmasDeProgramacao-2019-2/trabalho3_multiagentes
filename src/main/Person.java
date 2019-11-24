package main;

public class Person {
	
	private Integer line;
	private Integer column;
	
	private Boolean infected;
	private Integer daysInfected;
	private Boolean healed;
	
	private Boolean dead;
	
	public Person(int line, int column) {
		this.line = line;
		this.column = column;
		
		this.infected = false;
		this.dead = false;
		this.healed = false;
		this.daysInfected = 0;
	}
	
	public int getLine() {
		return line;
	}
	public void setLine(Integer line) {
		this.line = line;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(Integer column) {
		this.column = column;
	}
	public boolean isInfected() {
		return infected;
	}
	public void setInfected(Boolean infected) {
		this.infected = infected;
	}
	public int getDaysInfected() {
		return daysInfected;
	}
	public void setDaysInfected(Integer daysInfected) {
		this.daysInfected = daysInfected;
	}
	
	public void incrementDaysInfected() {
		this.daysInfected = this.daysInfected + 1;
	}

	public Boolean isDead() {
		return dead;
	}

	public void setDead(Boolean dead) {
		this.dead = dead;
	}
	
	public Boolean isHealed() {
		return healed;
	}

	public void setHealed(Boolean healed) {
		this.healed = healed;
	}

}
