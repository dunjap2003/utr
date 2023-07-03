import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

public class proba {

	public static void main(String[] args) {
		Set<String> ulazniNizovi = new TreeSet<String>(Arrays.asList("a".split("\\|")));
		Set<String> skupStanja = new TreeSet<String>(Arrays.asList("q0,q1,q2".split(",")));
		Set<String> ulazniZnakovi = new TreeSet<String>(Arrays.asList("a".split(",")));
		Set<String> znakoviStoga = new TreeSet<String>(Arrays.asList("K".split(",")));
		Set<String> skupPrihvatljivihStanja = new TreeSet<String>(Arrays.asList("q1".split(",")));
		String pocetnoStanje = "q0";
		String pocetniZnakStoga = "K";
		String[] funkcijePrijelaza = ("q0,$,K->q1,$\r\n"
				+ "q2,$,K->q2,K"
				+ "q1,a,K->q1,K").split("\r\n");
		

		Map<String, String> prijelazi = new TreeMap<>();
		for(String fun: funkcijePrijelaza) {
			String[] polje = fun.split("->");
			String kljuc = polje[0];
			String vrijednost = polje[1];
			prijelazi.put(kljuc, vrijednost);
		}
		
        StringBuilder konacniIspis = new StringBuilder();
    	for(String niz: ulazniNizovi) {
			String[] ulazniNiz = niz.split(",");
			String novoPocetnoStanje = pocetnoStanje;
		    Stack<String> stog = new Stack<>();
		    String noviPocetniZnakStoga = pocetniZnakStoga;
		    stog.push(pocetniZnakStoga);
			for(String u: ulazniNiz) {
				konacniIspis.append(novoPocetnoStanje + "#");
				for(int i = stog.size() - 1; i >= 0; i--) {
					konacniIspis.append(stog.elementAt(i));
				}
				konacniIspis.append("|");
				String pomocno = novoPocetnoStanje(novoPocetnoStanje, prijelazi, noviPocetniZnakStoga, konacniIspis, stog);
				if(!pomocno.equals(novoPocetnoStanje)) {
					novoPocetnoStanje = pomocno;
					noviPocetniZnakStoga = stog.peek();
					konacniIspis.append("|");
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
					konacniIspis.append("fail|0");
					break;
				}
			}
			
            if(!konacniIspis.toString().contains("fail|0")) {
				konacniIspis.append(novoPocetnoStanje + "#");
				for(int i = stog.size() - 1; i >= 0; i--) {
					konacniIspis.append(stog.elementAt(i));
				}
				konacniIspis.append("|");
					
				for(String prihvatljivo: skupPrihvatljivihStanja){
					if(novoPocetnoStanje.equals(prihvatljivo)) {
						konacniIspis.append("1\n");
					}
							
					else {
						String vrh = stog.peek();
						String pomocno = novoPocetnoStanje(novoPocetnoStanje, prijelazi, vrh, konacniIspis, stog);
						if(pomocno.equals(prihvatljivo)) {
						    for(String k: prijelazi.keySet()) {
						    	String klj[] = k.split(",");
						    	if(klj[0].equals(novoPocetnoStanje)) {
						    		String[] vri = prijelazi.get(k).split(",");
						    		if(vri[0].equals(pomocno)) {
						    			konacniIspis.append(pomocno + "#");
						    			stog.pop();
						    			
						    			String[] zaStog = vri[1].split("");
						    			
						    			
						    			if(!zaStog[0].equals("$")) {
											for(int i = zaStog.length - 1; i >= 0; i--) {
												stog.push(zaStog[i]);
											}
										}
										
										if(stog.isEmpty()) {
											stog.push("$");
										}
										
										for(int i = stog.size() - 1 ; i >= 0; i--) {
											konacniIspis.append(stog.elementAt(i));
										}	
						    		}
						    	}
						    }
							konacniIspis.append("|1\n");
						}
						else {
							konacniIspis.append("0\n");
						}
					}
				}
			}
		}
		System.out.println(konacniIspis);
	}
	
	private static String novoPocetnoStanje(String novoPocetnoStanje, Map<String, String> prijelazi, String pocetniZnakStoga, StringBuilder konacniIspis, Stack<String> stog) {
		for(String kljuc: prijelazi.keySet()) {
			String[] dijeloviKljuca = kljuc.split(",");
			if(dijeloviKljuca[0].equals(novoPocetnoStanje) && dijeloviKljuca[1].equals("$") && dijeloviKljuca[2].equals(pocetniZnakStoga)) {
				String vrijednost = prijelazi.get(kljuc);
				String[] dijeloviVrijednosti = vrijednost.split(",");
				dodajUKonacniIspis(konacniIspis, prijelazi, novoPocetnoStanje, dijeloviVrijednosti[0], stog);
				novoPocetnoStanje = dijeloviVrijednosti[0];
                return novoPocetnoStanje(novoPocetnoStanje, prijelazi, pocetniZnakStoga, konacniIspis, stog);
			}
		}
		
		return novoPocetnoStanje;
	}

	private static void dodajUKonacniIspis(StringBuilder konacniIspis, Map<String, String> prijelazi, String staroStanje, String novoStanje, Stack<String> stog) {
		konacniIspis.append(novoStanje + "#");
		for(String k: prijelazi.keySet()) {
			String klj[] = k.split(",");
			if(klj[0].equals(staroStanje)) {
				String[] vri = prijelazi.get(k).split(",");
	            stog.pop();
	            String[] zaStog = vri[1].split(",");
	            
	            if(!zaStog[0].equals("$")) {
					for(int i = zaStog.length - 1; i >= 0; i--) {
						stog.push(zaStog[i]);
					}
				}
	            
	        	if(stog.isEmpty()) {
					stog.push("$");
				}
	        	
	    		for(int i = stog.size() - 1 ; i >= 0; i--) {
					konacniIspis.append(stog.elementAt(i));
				}	  
			}
		}	
	}
}