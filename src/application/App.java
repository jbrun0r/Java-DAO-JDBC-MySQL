package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class App {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
//		Seller seller = sellerDao.findAll();
		
		System.out.println(sellerDao.findAll());

	}

}
