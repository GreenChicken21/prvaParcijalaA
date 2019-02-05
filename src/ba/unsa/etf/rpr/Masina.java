package ba.unsa.etf.rpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Masina implements Comparable<Masina>{

    private String naziv;

    private boolean upaljen = false;

    private int sati;
    private int serijski;

    private Map<String, Integer> materijali = new HashMap<>();

    public Masina(String naziv, int serijski) {
        setNaziv(naziv);
        upaljen = false;
        setSerijski(serijski);
        sati = 0;
    }

    public void setNaziv(String naziv) {
        if(naziv.length() < 2)
            throw new IllegalArgumentException("Naziv kratak");
        if(!naziv.matches("[a-zA-Z]+"))
            throw new IllegalArgumentException("Naziv se ne sastoji iskljucivo od slova");
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }


    public void setUpaljen(boolean upaljen) {
        this.upaljen = upaljen;
    }

    public void setSati(int sati) {
        if(sati>8 || sati < 0)
            throw new IllegalArgumentException("Neispravno radno vrijeme");
        this.sati = sati;
    }
    public int getSerijski() {
        return serijski;
    }
    public void setSerijski(int serijski) {
        if(serijski<=0 || serijski>=100000)
            throw new IllegalArgumentException("Neispravan serijski broj");
        this.serijski = serijski;
    }

    public Map<String, Integer> getMaterijali() {
        return materijali;
    }

    public void setMaterijali(Map<String, Integer> materijali) {
        this.materijali = materijali;
    }

    public void upali() throws WrongMachineState {
        if(upaljen) throw new WrongMachineState("Masina vec upaljena");
        upaljen = true;
        sati = 8;
    }

    public void ugasi() throws WrongMachineState {
        if (!upaljen) throw new WrongMachineState("Masina vec ugasena");
        upaljen = false;
        sati = 0;
    }


    public int cijena(String materijal)throws IllegalArgumentException{
        if(!materijali.containsKey(materijal))
            throw new IllegalArgumentException();
        return materijali.get(materijal);
    }

    public int proizvedi(String m1) throws IllegalArgumentException{
        int cijena = cijena(m1);
        if(cijena > sati) throw new IllegalArgumentException();
        sati-=cijena;
        return cijena;
    }

    public void resetuj() throws WrongMachineState { ugasi(); upali(); }

    public int preostaloSati() {
        return sati;
    }

    public void registrujMaterijal(String materijal, int cijena){
        if(cijena<1 || cijena>5) throw new IllegalArgumentException("Neispravna cijena");
        materijali.putIfAbsent(materijal,cijena);

    }
    public Set<String> dajMaterijaleMoguceZaProizvesti() {
        Set<String> rez = new TreeSet<>();
        for(Map.Entry<String, Integer> entry : materijali.entrySet() ){
            if(entry.getValue() <= preostaloSati() ){
                rez.add(entry.getKey());
            }
        }
        return rez;
    }


    public Map<String, Integer> dajMogucnostProizvodnje() {
        Map<String, Integer> mapa = new HashMap<>();
        for (Map.Entry<String, Integer> entry : materijali.entrySet()){
            Integer n = preostaloSati()/entry.getValue();
            mapa.putIfAbsent(entry.getKey(), n);
        }
        return mapa;
    }

    public String dajMaterijalKojiMoguNajviseProizvesti() {
        // u sustini samo materijal sa minimalnom cijenom
            Map.Entry<String, Integer> min = null;
            for ( Map.Entry<String, Integer> mats : materijali.entrySet() ) {
                if ( min == null ) {
                    min = mats;
                } else if ( min.getValue() > mats.getValue() ) {
                    min = mats;
                }
            }
            return (min == null) ? null : min.getKey();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Masina masina = (Masina) o;
        return serijski == masina.serijski;
    }
    @Override
    public int compareTo(Masina o) {
        Integer c1 = dajMaterijaleMoguceZaProizvesti().size();
        Integer c2 = o.dajMaterijaleMoguceZaProizvesti().size();

        Integer poredak = c1 - c2;
        if ( poredak != 0 ) {
            return poredak;
        }

        return naziv.compareTo(o.naziv);
    }
    @Override
    public String toString() {
        String status = "";
        if ( upaljen ) {
            status = String.format("upaljena (preostalo %d sati)", preostaloSati());
        } else {
            status = "ugasena";
        }

        String materijali = "";
        boolean first = true;
        for ( Map.Entry<String, Integer> entry : this.materijali.entrySet() ) {
            if ( first ) {
                first = false;
            } else {
                materijali += ", ";
            }

            materijali += entry.getKey() + " (" + entry.getValue() + ")";
        }

        return String.format("Masina %s je %s. Ona moze proizvesti materijale %s.", naziv, status, materijali);
    }

    public boolean getUpaljen() {
        return upaljen;
    }
}
