package br.developersd3.sindquimica.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

import br.developersd3.sindquimica.daos.CategoryDao;
import br.developersd3.sindquimica.models.Category;;




@Model

public class CategoryController {
	
	@Inject
	private CategoryDao categoryDao;
	private Category category = new Category();
	private List<Category> categoryList = new ArrayList<>();
	
	
	private Integer idToEdit;
	
	public Integer getIdToEdit() {
		return idToEdit;
	}

	public void setIdToEdit(Integer idToEdit) {
		this.idToEdit = idToEdit;
	}
	
	public List<Category> getCategoryList(){
		return this.categoryList;
	}
	
		
	public void setCategory(Category category){
		this.category = category;
	}
	
	public Category getCategory(){
		return this.category;
	}
	
	@PostConstruct
	private void postConstruct() {
		categoryList.addAll(categoryDao.findAll());
	}	
	
	public void loadDetails(){
		this.category = categoryDao.getById(Category.class,idToEdit);
	}
	
	public String save() {
		categoryDao.save(category);
		return "/category/list?faces-redirect=true";
	}

	public String update(Integer id) {
		category.setId(id);
		categoryDao.update(category);
		return "/category/list?faces-redirect=true";
	}

	public String remove(Integer id) {
		Category category = categoryDao.getById(Category.class,idToEdit);
		categoryDao.delete(category);
		return "/category/list?faces-redirect=true";
	}		
	
	

}
