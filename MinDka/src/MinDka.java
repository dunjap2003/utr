import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MinDka {

	public static void main(String[] args) {
		Set<String> skupStanja = null;
		Set<String> provjera = null;
		Set<String> skupSimbolaAbecede = null;
		Set<String> skupPrihvatljivihStanja = null;
		String pocetnoStanje = null;
		Set<String> funkcijePrijelaza = new TreeSet<>();

		Scanner sc = new Scanner(System.in);
		int counter = 1;
		while(sc.hasNextLine()){
			String redak = sc.nextLine();
			if(counter <= 4) {
				if(counter == 1) {
					skupStanja = new TreeSet<>(Arrays.asList(redak.split(",")));
					provjera = new TreeSet<>(Arrays.asList(redak.split(",")));
					counter++;
				}
				else if(counter == 2) {
					skupSimbolaAbecede = new TreeSet<>(Arrays.asList(redak.split(",")));
					counter++;
				}
				else if(counter == 3) {
					skupPrihvatljivihStanja = new TreeSet<>(Arrays.asList(redak.split(",")));
					counter++;
				}
				else if(counter == 4){
					pocetnoStanje = redak;
					counter++;
				}
			}
			
			else {
				funkcijePrijelaza.add(redak);
			}
		}
		
		TreeMap<String, Map<String, String>> mapaStanja = new TreeMap<>();
	
	    for(String fun: funkcijePrijelaza) {
			Map<String, String> mapa = new TreeMap<String, String>();
			String trenutnoStanje = fun.substring(0, fun.indexOf(","));
			String simbolAbecede = fun.substring(fun.indexOf(",") + 1, fun.indexOf("-"));
			String iduceStanje = fun.substring(fun.indexOf(">") + 1);
			
			if(mapaStanja.get(trenutnoStanje) == null) {
				mapa.put(simbolAbecede, iduceStanje);
				mapaStanja.put(trenutnoStanje, mapa);
			}
			else {
				mapaStanja.get(trenutnoStanje).put(simbolAbecede, iduceStanje);
			}
	    }
	    
	    Map<String, Boolean> obidjeno = new TreeMap<String, Boolean>();
	    for(String s: skupStanja) {
	    	obidjeno.put(s, false);
	    }
	    
	    pronadiNedohvatljive(pocetnoStanje, obidjeno, mapaStanja);
	    
	    for(String o: provjera) {
	    	if(obidjeno.get(o) == false) {
	    		skupStanja.remove(o);
	    		skupPrihvatljivihStanja.remove(o);
	    		mapaStanja.remove(o);
	    	}
	    }
	    
	    Map<String, Set<String>> istovjetna = pronadiNeistovjetne(mapaStanja, skupPrihvatljivihStanja, skupStanja, skupSimbolaAbecede);
	    
	    for(String i: istovjetna.keySet()) {
	    	if(!istovjetna.get(i).isEmpty()) {
	    		skupPrihvatljivihStanja.removeAll(istovjetna.get(i));
	    		skupStanja.removeAll(istovjetna.get(i));
	    		for(String s: istovjetna.get(i)) {
	    			for(String sk: skupStanja) {
	    				for(String ab: skupSimbolaAbecede) {
	    					if(mapaStanja.get(sk).get(ab).equals(s)){
	    						mapaStanja.get(sk).remove(ab);
	    						mapaStanja.get(sk).put(ab, i);
	    					}
	    				}
	    			}
	    			mapaStanja.remove(s);
	    			if(s.equals(pocetnoStanje)) {
	    				pocetnoStanje = i;
	    			}
	    		}
	    	}
	    }
	    
	    StringBuilder konacniIspis = new StringBuilder();
	    for(String stanje: skupStanja) {
	    	konacniIspis.append(stanje + ",");
	    }
	    if(konacniIspis.charAt(konacniIspis.length() - 1) == ',')  {
		    konacniIspis.deleteCharAt(konacniIspis.length() - 1);
	    }
	    konacniIspis.append("\n");
	    
	    for(String abeceda: skupSimbolaAbecede) {
	    	konacniIspis.append(abeceda + ",");
	    }
	    if(konacniIspis.charAt(konacniIspis.length() - 1) == ',')  {
		    konacniIspis.deleteCharAt(konacniIspis.length() - 1);
	    }
	    konacniIspis.append("\n");
	    
	    for(String prihvatljivo: skupPrihvatljivihStanja) {
	    	konacniIspis.append(prihvatljivo + ",");
	    }
	    if(konacniIspis.charAt(konacniIspis.length() - 1) == ',')  {
		    konacniIspis.deleteCharAt(konacniIspis.length() - 1);
	    }
	    konacniIspis.append("\n");
	    
	    konacniIspis.append(pocetnoStanje + "\n");
	    
	    for(String ms: mapaStanja.keySet()) {
	    	for(String m: skupSimbolaAbecede) {
	    	konacniIspis.append(ms + "," + m + "->" + mapaStanja.get(ms).get(m) + "\n");
	    	}
	    }
	    if(konacniIspis.charAt(konacniIspis.length() - 1) == ',')  {
		    konacniIspis.deleteCharAt(konacniIspis.length() - 1);
	    }
	    
	    System.out.println(konacniIspis);
	}
	
	private static void pronadiNedohvatljive(String pocetnoStanje, Map<String, Boolean> obidjeno, Map<String, Map<String, String>> mapaStanja) {
		obidjeno.put(pocetnoStanje, true);
		Map<String, String> mapa = mapaStanja.get(pocetnoStanje);
	    for(String s: mapa.values()) {
	    	if(obidjeno.get(s) == false) {
	    		pronadiNedohvatljive(s, obidjeno, mapaStanja);
			}
		}
	}
	
	private static Map<String, Set<String>> pronadiNeistovjetne(Map<String, Map<String, String>> mapaStanja, Set<String> skupPrihvatljivihStanja, Set<String> skupStanja, Set<String> skupSimbolaAbecede) {
		Set<String> prihvatljivaStanja = new TreeSet<>();
		Set<String> neprihvatljivaStanja = new TreeSet<>();
		for(String s: skupPrihvatljivihStanja) {
			prihvatljivaStanja.add(s);
		}
		
		for(String st: skupStanja) {
			if(!prihvatljivaStanja.contains(st)) {
				neprihvatljivaStanja.add(st);
			}
		}
		
		Map<String, Map<String, Boolean>> iks = new TreeMap<>();
		for(String str: skupStanja) {
			Map<String, Boolean> prijelazi = new TreeMap<>();
			for(String s: skupStanja) {
					prijelazi.put(s, true);
			}
			
			iks.put(str, prijelazi);
		}
	
		for(String i: skupStanja) {
			if(prihvatljivaStanja.contains(i)) {
				for(String n: neprihvatljivaStanja) {
						iks.get(i).put(n, false);
				}
			}
			
			else {
				for(String p: prihvatljivaStanja) {
					iks.get(i).put(p, false);
				}
			}
		}
		
		Map<String, List<String>> listaZapamcenih = new TreeMap<>();
		for(String s: skupStanja) {
			for(String s2: skupStanja) {
				for(String a: skupSimbolaAbecede) {
					String lijevi = String.format("%s,%s", mapaStanja.get(s).get(a), mapaStanja.get(s2).get(a));
					String desni = String.format("%s,%s", s, s2);
					if(listaZapamcenih.get(lijevi) == null) {
						List<String> lista = new ArrayList<>();
						lista.add(desni);
						listaZapamcenih.put(lijevi, lista);
					}
								
					else {
						listaZapamcenih.get(lijevi).add(desni);
					}
						
					if(iks.get(mapaStanja.get(s).get(a)).get(mapaStanja.get(s2).get(a)) == false) {
						iks.get(s).put(s2, false);
						if(listaZapamcenih.get(lijevi) != null) {
							for(String g: listaZapamcenih.get(lijevi)) {
								String[] elementi = g.split(",");
								iks.get(elementi[0]).put(elementi[1], false);
								iks.get(elementi[1]).put(elementi[0], false);
							}
						}
					}
				}
			}
		}
		
		Map<String, Set<String>> istovjetna = new TreeMap<>();
		for(String string: iks.keySet()) {
			for(String s: skupStanja) {
				if(iks.get(string).get(s) == true) {
					if(istovjetna.get(string) == null) {
						Set<String> stanja = new TreeSet<>();
						stanja.add(s);
						istovjetna.put(string, stanja);
					}
					else {
						istovjetna.get(string).add(s);
					}
				}
			}
		}
		
		for(String i: istovjetna.keySet()) {
			istovjetna.get(i).remove(i);
			for(String k: istovjetna.get(i)) {
				if(k.compareTo(i) < 0) {
					istovjetna.get(i).remove(k);
					break;
				}
			}
		}
		
		return istovjetna;
	}
}