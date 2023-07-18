package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class App {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao();

		Seller seller = sellerDao.findById(3);
		System.out.println("Find by id: " + seller);

		Department department = new Department(3, null);
		List<Seller> sellers = sellerDao.findByDepartment(department);
		System.out.println("\r\nFind by Department:");
		for (Seller sellerByDep : sellers) {
			System.out.println(sellerByDep);
		}		
		
		sellers = sellerDao.findAll();
		System.out.println("\r\nFind all:");
		for (Seller sellerByDep : sellers) {
			System.out.println(sellerByDep);
		}
		
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("\r\nNew seller, id: " + newSeller.getId());
		
		seller = sellerDao.findById(1);
		seller.setName("João");
		sellerDao.update(seller);
		System.out.println("\r\nUpdate seller, id: " + seller);
		
		Integer id = 2;
		System.out.println("\r\nDelete seller, id: "+id);
		sellerDao.deleteById(id);
	}

}
