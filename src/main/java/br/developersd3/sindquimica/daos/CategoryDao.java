package br.developersd3.sindquimica.daos;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.developersd3.sindquimica.models.Category;

@Repository
@Transactional
public class CategoryDao extends GenericDao<Category, Integer> {


  

}
