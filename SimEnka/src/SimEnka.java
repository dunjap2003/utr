import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class SimEnka {
	
	public static void main(String[] args) {
			String[] ulazniNizovi = new String[10000]; //ulazni nizovi odvojeni znakom |
			String[] skupStanja = new String[100]; //skup stanja odvojenih zarezom
			String[] skupSimbolaAbecede = new String[100]; //skup simbola abecede odvojenih zarezom
			String[] skupPrihvatljivihStanja = new String[100]; //skup prihvatljivih stanja odvojenih zarezom
			String pocetnoStanje = null;
			Set<String> funkcijePrijelaza = new TreeSet<>(); //6. redak nadalje -> funkcije prijelaza
			
			Scanner sc = new Scanner(System.in);
			int counter = 1;
			while(sc.hasNextLine()){
				String redak = sc.nextLine();
				if(counter <= 5) {
					if(counter == 1) {
						ulazniNizovi = redak.split("\\|");
						counter++;
				    }
					else if(counter == 2) {
						skupStanja = redak.split(",");
						counter++;
					}
					else if(counter == 3) {
						skupSimbolaAbecede = redak.split(",");
						counter++;
					}
					else if(counter == 4) {
						skupPrihvatljivihStanja = redak.split(",");
						counter++;
					}
					else if(counter == 5){
						pocetnoStanje = redak;
						counter++;
					}
				}
					
					else {
						funkcijePrijelaza.add(redak);
					}
				}
			
			TreeMap<String, Map<String, Set<String>>> mapaStanja = new TreeMap<>();
		
		    for(String fun: funkcijePrijelaza) {
				Map<String, Set<String>> mapa = new TreeMap<String, Set<String>>();
				Set<String> listaIducihStanja = new TreeSet<String>();
				String trenutnoStanje = fun.substring(0, fun.indexOf(","));
				String simbolAbecede = fun.substring(fun.indexOf(",") + 1, fun.indexOf("-"));
				String skupIducihStanja = fun.substring(fun.indexOf(">") + 1);
				String iducaStanjaRazdvojeno[] = skupIducihStanja.split(",");
					for(int j = 0; j < iducaStanjaRazdvojeno.length; j++) {
						listaIducihStanja.add(iducaStanjaRazdvojeno[j]);
				}
			  
				if(mapaStanja.get(trenutnoStanje) == null) {
					mapa.put(simbolAbecede, listaIducihStanja);
					mapaStanja.put(trenutnoStanje, mapa);
				}
				else {
					mapaStanja.get(trenutnoStanje).put(simbolAbecede, listaIducihStanja);
				}
		    }
		    
			StringBuilder konacniIspis = new StringBuilder();
			for(String niz: ulazniNizovi) {
				String ulazniNiz[] = niz.split(",");
				Set<String> setZaIspis = new TreeSet<>();
				Set<String> setStanja = new TreeSet<>();
				setStanja.add(pocetnoStanje);
				Set<String> epsOk = new TreeSet<>();
				for(String ss: setStanja) {
					dohvatiEpsilonPrijelaze(ss, mapaStanja, epsOk);
				}
				setStanja.addAll(epsOk);
				for(String set: setStanja) {
					konacniIspis.append(set + ",");
				}
				
				if(konacniIspis.charAt(konacniIspis.length() - 1) == ',') {
					konacniIspis.deleteCharAt(konacniIspis.lastIndexOf(","));
				}
				
				konacniIspis.append("|");
				
				for(String znak: ulazniNiz) {
				    Set<String> setZnak = new TreeSet<>();
					for(String set: setStanja) {
					    ispisujStanje(setZnak, set, znak, mapaStanja);
					    setZaIspis.addAll(setZnak);
				    }
				    setStanja.addAll(setZnak);
				   
				    epsOk.clear();
					for(String set: setZnak) {
						dohvatiEpsilonPrijelaze(set, mapaStanja, epsOk);
				        setZaIspis.addAll(epsOk);	
					}
					setStanja.addAll(epsOk);
					    
					if(setZaIspis.isEmpty()) {
						konacniIspis.append("#");
					}
							
					for(String ispis: setZaIspis) {
						if(setZaIspis.size() == 1 && ispis.equals("#")) {
							konacniIspis.append(ispis);
						}
						
						else if(setZaIspis.size() != 1 && ispis.equals("#")){
							continue;
						}
						
						else {
							konacniIspis.append(ispis);
						}
						
						konacniIspis.append(",");
					}
					
					setStanja.clear();
					setStanja.addAll(setZaIspis);
					setZaIspis.clear();
				
					if(konacniIspis.charAt(konacniIspis.length() - 1) == ',') {
						konacniIspis.deleteCharAt(konacniIspis.lastIndexOf(","));
					}

					konacniIspis.append("|");
				}
				
				konacniIspis.delete(konacniIspis.length() - 1, konacniIspis.length());
				konacniIspis.append("\n");
			}
			
				System.out.println(konacniIspis);
		}
		
		private static void ispisujStanje(Set<String> set, String stanje, String znak, TreeMap<String, Map<String, Set<String>>> mapaStanja) {
			if(mapaStanja.get(stanje) == null) {
				return;
			}
			
			Set<String> iducaStanja = mapaStanja.get(stanje).get(znak);
			
			if(iducaStanja == null) {
				return;
			}

		    for(String s: iducaStanja) {
		    	set.add(s);
		    }
		}
		
		private static void dohvatiEpsilonPrijelaze(String stanje, Map<String, Map<String, Set<String>>> mapaStanja, Set<String> retSet){
			if(mapaStanja.get(stanje) == null) {
				return;
			}
			
			else {
				Set<String> pomSet = new TreeSet<>();
				pomSet = mapaStanja.get(stanje).get("$");
				if(pomSet == null) {
					return;
				}
				
				for(String s: pomSet) {
					if(!s.equals(stanje) && !retSet.contains(s)) {
						retSet.add(s);
						dohvatiEpsilonPrijelaze(s, mapaStanja, retSet);
					}
				}
			}
		}
}
