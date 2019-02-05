package ba.unsa.etf.rpr;

import java.util.*;
import java.util.function.Function;

public class Fabrika {
    private Set<Masina>masine = new TreeSet<>();

    public Masina dodajKupljenuMasinu(String kupljena, int i) {
        Masina m = new Masina(kupljena,i);
        masine.remove(m);
        masine.add(m);
        return m;
    }

    public Masina dodajDomacuMasinu(String domaca, int i) {
        Masina m = new DomacaMasina(domaca,i);
        masine.remove(m);
        masine.add(m);
        return m;
    }

    public void dodajMaterijal(String nazivMasine, String nazivMaterijala, int cijena) {
        for ( Masina m : masine ) {
            if ( m.getNaziv().equals(nazivMasine) ) {
                m.registrujMaterijal(nazivMaterijala, cijena);
            }
        }
    }

    public Map<Masina, String> najviseProizvoda() {
        Map<Masina, String> map = new HashMap<>();
        for (Masina m : masine){
            if(!m.getUpaljen())
                continue;
            String current = m.dajMaterijalKojiMoguNajviseProizvesti();
            if(current != null ) map.putIfAbsent(m,current);
        }
        return map;
    }

    public Map<Masina, Integer> cijenaZaMaterijal(String m1) {
        Map<Masina, Integer> res = new HashMap<>();
        for ( Masina m : masine ) {
            if ( !m.getUpaljen() ) {
                continue;
            }

            int cijena = -1;
            try {
                cijena = m.cijena(m1);
            } catch ( IllegalArgumentException ex ) {}
            res.put(m, cijena);
        }
        return res;
    }

    public Set<Masina> dajMasine(Function<Masina, Boolean> filter) {
        Set<Masina> set = new TreeSet<>();
        for ( Masina m : masine ) {
            if ( filter == null || filter.apply(m) ) {
                set.add(m);
            }
        }
        return set;
    }
    @Override
    public String toString() {
        String res = "";
        int i = 1;
        for ( Masina m : masine ) {
            res += i + ". " + m.toString() + "\n";
            ++ i;
        }
        return res;
    }
}
