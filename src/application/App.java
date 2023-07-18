package application;

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
		System.out.println("Find by Department:");
		for (Seller sellerByDep : sellers) {
			System.out.println(sellerByDep);
		}
	}

}
