package bph.map;

import java.util.Objects;

public class Pair {
    private Integer first;
    private Integer last;
    
    public Pair(Integer first, Integer last) {
        this.first=Objects.requireNonNull(first);
        this.last=Objects.requireNonNull(last);
    }
    
    public Integer first() {
        return first;
    }
    
    public Integer last() {
        return last;
    }
        
    public void changeFirst(int first) {
        this.first=first;
    }

    public void changeLast(int last) {
        this.last=last;
    }
    
    @Override
    public String toString() {
    	return "Pair["+first+","+last+"]\n";
    }
    
    public Pair copy() {
    	return new Pair(first,last);
    }
}