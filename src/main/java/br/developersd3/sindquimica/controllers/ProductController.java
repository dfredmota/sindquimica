package br.developersd3.sindquimica.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

import br.developersd3.sindquimica.daos.CategoryDao;
import br.developersd3.sindquimica.daos.ProductDao;
import br.developersd3.sindquimica.models.Category;
import br.developersd3.sindquimica.models.Product;	


@Model

public class ProductController {
	
	@Inject
	private ProductDao productDao;
	private Product product = new Product();
	private List<Product> productList = new ArrayList<>();
	
	@Inject
	private CategoryDao categoryDao;
	private List<Category> categoryList = new ArrayList<>();
	
	private Integer idToEdit;
	
	public Integer getIdToEdit() {
		return idToEdit;
	}

	public void setIdToEdit(Integer idToEdit) {
		this.idToEdit = idToEdit;
	}
	
	public List<Product> getProductList(){
		return this.productList;
	}
	
	public List<Category> getCategoryList(){
		return this.categoryList;
	}	
		
	public void setProduct(Product product){
		this.product = product;
	}
	
	public Product getProduct(){
		return this.product;
	}
	
	@PostConstruct
	private void postConstruct() {
		productList.addAll(productDao.findAll());
		categoryList.addAll(categoryDao.findAll());
		product.setCategory(new Category());
	}	
	
	public void loadDetails(){
		this.product = productDao.getById(Product.class,idToEdit);
	}
	
	public String save() {
		productDao.save(product);
		return "/product/list?faces-redirect=true";
	}

	public String update(Integer id) {
		product.setId(id);
		productDao.update(product);
		return "/product/list?faces-redirect=true";
	}

	public String remove(Integer id) {
		Product product = productDao.getById(Product.class,idToEdit);
		productDao.delete(product);
		return "/product/list?faces-redirect=true";
	}		

	

}
