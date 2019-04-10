package mmt.core;

public class Normal implements Category,java.io.Serializable{
	
	public double getDiscount(){
		return 1.00;
	}

	public String toString(){
		return "NORMAL";
	}
}