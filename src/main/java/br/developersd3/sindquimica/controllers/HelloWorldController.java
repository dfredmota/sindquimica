package br.developersd3.sindquimica.controllers;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.service.SindicatoService;

@Model
public class HelloWorldController
{

   private String name;
   private String label;
   
   @Inject
   @Named("sindicatoService")
   private SindicatoService sindicatoService;

   public void action()
   {
	  sindicatoService.all();
      System.out.println("button clicked!");
      label = "Hello " + name;
   }

   public String getLabel()
   {
      return label;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }
}