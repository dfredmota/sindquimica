package br.developersd3.sindquimica.datatable;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import br.developersd3.sindquimica.models.Sindicato;
 
public class LazySorter implements Comparator<Sindicato> {
 
    private String sortField;
     
    private SortOrder sortOrder;
     
    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
 
    public int compare(Sindicato car1, Sindicato car2) {
        try {
            Object value1 = Sindicato.class.getField(this.sortField).get(car1);
            Object value2 = Sindicato.class.getField(this.sortField).get(car2);
 
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
