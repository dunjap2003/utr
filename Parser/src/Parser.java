import java.util.Scanner;

public class Parser {
	
	static int broj;
	static String ulazniZnak;
	static boolean prolazA;
	static boolean prolazB;
	static StringBuilder konacniIspis = new StringBuilder();
	static String[] ulazniZnakovi;
	

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String ulazniNiz = sc.nextLine();
	    String ulazniNiz$ = ulazniNiz.concat("$");
		
		ulazniZnakovi = ulazniNiz$.split("");
		
	    broj = 0;
		ulazniZnak = ulazniZnakovi[broj];
		prolazA = prolazB = true;
		
		S();
		
		if(!konacniIspis.substring(konacniIspis.length() - 2, konacniIspis.length()).equals("NE"))
			if(ulazniZnak.equals("$")) {
				konacniIspis.append("\nDA");
			}
			else {
				konacniIspis.append("\nNE");
			}
		
		System.out.println(konacniIspis);
		
		return;
	}
	
	private static void S() {
		konacniIspis.append("S");
		if(ulazniZnak.equals("a")) {
			broj++;
			ulazniZnak = ulazniZnakovi[broj];
			A();
			if(!prolazA) {
				return;
			}
			B();
		}
		
		else if(ulazniZnak.equals("b")) {
			broj++;
			ulazniZnak = ulazniZnakovi[broj];
			B();
			if(!prolazB) {
				return;
			}
			A();
		}
		
		else {
			konacniIspis.append("\nNE");
		}
		
		return;
	}
	
	private static void A() {
		konacniIspis.append("A");
		if(ulazniZnak.equals("a")) {
			broj++;
			ulazniZnak = ulazniZnakovi[broj];
		}
		
		else if(ulazniZnak.equals("b")) {
			broj++;
			ulazniZnak = ulazniZnakovi[broj];
			C();
		}
		
		else {
			prolazA = false;
			konacniIspis.append("\nNE");
		}
		
		return;
	}
	
	private static void B() {
		konacniIspis.append("B");
		if(ulazniZnak.equals("c")) {
			broj++;
			ulazniZnak = ulazniZnakovi[broj];
			if(ulazniZnak.equals("c")) {
				broj++;
				ulazniZnak = ulazniZnakovi[broj];
				S();
				if(ulazniZnak.equals("b")) {
					broj++;
					ulazniZnak = ulazniZnakovi[broj];
					if(ulazniZnak.equals("c")) {
						broj++;
						ulazniZnak = ulazniZnakovi[broj];
						return;
					}
					
					else {
						prolazB = false;
						konacniIspis.append("\nNE");		
						return;
					}
				}
				
				else {
					prolazB = false;
					if(!konacniIspis.substring(konacniIspis.length() - 2, konacniIspis.length()).equals("NE")) {
						konacniIspis.append("\nNE");
					}
					return;
				}
			}
			else {
				prolazB = false;
				konacniIspis.append("\nNE");
				return;
			}
		}
	}
	
	private static void C() {
		konacniIspis.append("C");
		A();
		if(!prolazA) {
			return;
		}
		A();
		return;
	}
}