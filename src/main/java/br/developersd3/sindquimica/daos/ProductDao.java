package br.developersd3.sindquimica.daos;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import br.developersd3.sindquimica.models.Product;

@Repository
@Transactional
public class ProductDao extends GenericDao<Product, Integer> {


   

}
