import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class SimPa {

	public static void main(String[] args) {
		Set<String> ulazniNizovi = null;
		Set<String> skupStanja = null;
		Set<String> ulazniZnakovi = null;
		Set<String> znakoviStoga = null;
		Set<String> skupPrihvatljivihStanja = null;
		String pocetnoStanje = null;
		String pocetniZnakStoga = null;
		Set<String> funkcijePrijelaza = new TreeSet<>();
		
		Scanner sc = new Scanner(System.in);
		int counter = 1;
		while(sc.hasNextLine()){
			String redak = sc.nextLine();
			if(counter <= 7) {
				if(counter == 1) {
					ulazniNizovi = new LinkedHashSet<String>(Arrays.asList(redak.split("\\|")));
					counter++;
			    }
				else if(counter == 2) {
					skupStanja = new TreeSet<String>(Arrays.asList(redak.split(",")));
					counter++;
				}
				else if(counter == 3) {
					ulazniZnakovi = new TreeSet<String>(Arrays.asList(redak.split(",")));
					counter++;
				}
				else if(counter == 4) {
					znakoviStoga = new TreeSet<String>(Arrays.asList(redak.split(",")));
					counter++;
				}
				else if(counter == 5){
					skupPrihvatljivihStanja = new TreeSet<String>(Arrays.asList(redak.split(",")));
					counter++;
				}
				else if(counter == 6) {
					pocetnoStanje = redak;
					counter++;
				}
				else if(counter == 7) {
					pocetniZnakStoga = redak;
					counter++;
				}	
			}
				
			else {
				funkcijePrijelaza.add(redak);
			}
		}
		
		Map<String, String> prijelazi = new LinkedHashMap<>();
		for(String fun: funkcijePrijelaza) {
			String[] polje = fun.split("->");
			String kljuc = polje[0];
			String vrijednost = polje[1];
			prijelazi.put(kljuc, vrijednost);
		}
		
		 StringBuilder konacniIspis = new StringBuilder();
		 for(String niz: ulazniNizovi) {
			 StringBuilder maliIspis = new StringBuilder();
			 String[] ulazniNiz = niz.split(",");
			 Stack<String> stog = new Stack<>();
			 String novoPocetnoStanje = pocetnoStanje;
			 String noviPocetniZnakStoga = pocetniZnakStoga;
			 stog.push(noviPocetniZnakStoga);
			 for(String u: ulazniNiz) {
				 boolean sveProdjeno = false;
				 maliIspis.append(novoPocetnoStanje + "#");
				 for(int i = stog.size() - 1; i >= 0; i--) {
					 maliIspis.append(stog.elementAt(i));
				 }
				 maliIspis.append("|");
				 while(!sveProdjeno) {
					 boolean dalje = false;
					 for(String k: prijelazi.keySet()) {
						 String[] kRast = k.split(",");
						 if(kRast[0].equals(novoPocetnoStanje) && kRast[1].equals("$") && kRast[2].equals(noviPocetniZnakStoga)) {
							 dalje = true;
							 stog.pop();
							 String[] v = prijelazi.get(k).split(",");
							 novoPocetnoStanje = v[0];
							 String[] zaStog = v[1].split("");
							 noviPocetniZnakStoga = zaStog[0];
							 for(int i = zaStog.length - 1; i >= 0; i--) {
									stog.push(zaStog[i]);
								}
							 maliIspis.append(novoPocetnoStanje + "#");
							 for(int i = stog.size() - 1; i >= 0; i--){
								 maliIspis.append(stog.elementAt(i));
							 }
							 maliIspis.append("|");
						 } 
					 }
					 
					 if(!dalje) {
						 sveProdjeno = true;
					 }	 
				 }
				 
				 boolean pronadjeno = false;
				 for(String kljuc: prijelazi.keySet()) {
					 String[] dijeloviKljuca = kljuc.split(",");
					 if(dijeloviKljuca[0].equals(novoPocetnoStanje) && dijeloviKljuca[1].equals(u) && dijeloviKljuca[2].equals(noviPocetniZnakStoga)) {
						 pronadjeno = true;
						 stog.pop();
						 String vrijednost = prijelazi.get(kljuc);
						 String[] dijeloviVrijednosti = vrijednost.split(",");
						 String[] zaStog = dijeloviVrijednosti[1].split("");
						 
						 if(!zaStog[0].equals("$")) {
								for(int i = zaStog.length - 1; i >= 0; i--) {
									stog.push(zaStog[i]);
								}
							}
							
						if(stog.isEmpty()) {
							stog.push("$");
						}
							
						novoPocetnoStanje = dijeloviVrijednosti[0];
						noviPocetniZnakStoga = stog.peek();
						break;
					 }
				 }
				 
				 if(!pronadjeno) {
				    maliIspis.append("fail");
					maliIspis.append("|");
					maliIspis.append("0\n");
					break;
				} 
			 }
			 
			 if(maliIspis.indexOf("fail") == -1) {
				maliIspis.append(novoPocetnoStanje + "#");
				for(int i = stog.size() - 1; i >= 0; i--) {
					maliIspis.append(stog.elementAt(i));
				}
				maliIspis.append("|");
						
				for(String prihvatljivo: skupPrihvatljivihStanja){
					if(novoPocetnoStanje.equals(prihvatljivo)) {
						maliIspis.append("1\n");
					}
								
					else {
						boolean prodjeno2 = false;
						boolean odgovara = false;
						while(!prodjeno2) {
		                    boolean dalje2 = false;
							for(String klj: prijelazi.keySet()) {
								String pocZnakStog = stog.peek();
								dalje2 = false;
								String[] kljRast = klj.split(",");
								if(kljRast[0].equals(novoPocetnoStanje) && kljRast[1].equals("$") && kljRast[2].equals(pocZnakStog)) {
									dalje2 = true;
									stog.pop();
									 String[] v = prijelazi.get(klj).split(",");
									 novoPocetnoStanje = v[0];
									 String[] zaStog = v[1].split("");
									 noviPocetniZnakStoga = zaStog[0];
									 for(int i = zaStog.length - 1; i >= 0; i--) {
											stog.push(zaStog[i]);
										}
									 maliIspis.append(novoPocetnoStanje + "#");
									 for(int i = stog.size() - 1; i >= 0; i--){
										 maliIspis.append(stog.elementAt(i));
									 }
									 maliIspis.append("|");
									 for(String prihvatljivo2: skupPrihvatljivihStanja) {
										 if(novoPocetnoStanje.equals(prihvatljivo2)) {
											 prodjeno2 = true;
											 odgovara = true;
											 maliIspis.append("1\n");
										 }
									 }
								}
							}
							
							if(!dalje2) {
								prodjeno2 = true;
							}
						}
						
						if(!odgovara) {
							maliIspis.append("0\n");
						}
					}
				}
			}
			 konacniIspis.append(maliIspis);
		 }
		System.out.println(konacniIspis);
	}
}