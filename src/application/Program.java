package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Departament;
import model.entities.Seller;

public class Program {
	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();

		System.out.println("===== TEST 1: Seller findById =====");
		Seller seller = sellerDao.findById(3);
		
		System.out.println(seller);
		System.out.println("\n===== TEST 2: Seller findByDepartment =====");
		
		Departament departament = new Departament(1, null);
		
		List<Seller> list = sellerDao.findByDepartment(departament);
		
		for (Seller obj : list) {
			
			System.out.println(obj);
		}
		
		System.out.println("\n===== TEST 3: Seller findAll =====");
		
		
		list = sellerDao.findAll();
		
		for (Seller obj : list) {
			
			System.out.println(obj);
		}
		
		System.out.println("\n===== TEST 4: Seller insert =====");
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.00, departament);
		
		sellerDao.insert(newSeller);
		
		System.out.println("Inserted! new id+" +newSeller.getId());

		System.out.println("\n===== TEST 5: Seller update =====");
		
		seller = sellerDao.findById(1);
		seller.setName("Martha Waine");
		sellerDao.update(seller);
		
		System.out.println("Updade completed");
	}
}
