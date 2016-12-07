package br.developersd3.sindquimica.datatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.developersd3.sindquimica.models.Sindicato;

 
/**
 * Dummy implementation of LazyDataModel that uses a list to mimic a real datasource like a database.
 */
public class LazySindicatoDataModel extends LazyDataModel<Sindicato> {
     	
    /**
	 * 
	 */
	private static final long serialVersionUID = -5003334716585763692L;
	private List<Sindicato> datasource;
     
    public LazySindicatoDataModel(List<Sindicato> datasource) {
        this.datasource = datasource;
    }
     
    @Override
    public Sindicato getRowData(String rowKey) {
        for(Sindicato car : datasource) {
            if(car.getId().toString().equals(rowKey))
                return car;
        }
 
        return null;
    }
 
    @Override
    public Object getRowKey(Sindicato car) {
        return car.getId();
    }
 
    @Override
    public List<Sindicato> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
        List<Sindicato> data = new ArrayList<Sindicato>();
 
        //filter
        for(Sindicato car : datasource) {
            boolean match = true;
 
            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(car.getClass().getField(filterProperty).get(car));
 
                        if(filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                            match = true;
                    }
                    else {
                            match = false;
                            break;
                        }
                    } catch(Exception e) {
                        match = false;
                    }
                }
            }
 
            if(match) {
                data.add(car);
            }
        }
 
        //sort
        if(sortField != null) {
            Collections.sort(data, new LazySorter(sortField, sortOrder));
        }
 
        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);
 
        //paginate
        if(dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            }
            catch(IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        }
        else {
            return data;
        }
    }
}
