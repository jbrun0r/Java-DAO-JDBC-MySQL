package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection connection;
	
	public SellerDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Seller seller) {
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement(
					"INSERT INTO seller  "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES (?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, seller.getName());
			statement.setString(2, seller.getEmail());
			statement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			statement.setDouble(4, seller.getBaseSalary());
			statement.setInt(5, seller.getDepartment().getId());
			
			int rowsAffedcted = statement.executeUpdate();
			
			if(rowsAffedcted>0) {
				ResultSet result = statement.getGeneratedKeys();
				if(result.next()) {
					int id = result.getInt(1);
					seller.setId(id);
				}
				DB.closeResultSet(result);
			}
			else {
				throw new DbException("No rows affected!");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
		}
		
	}

	@Override
	public void update(Seller seller) {
PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement(
					"UPDATE seller  "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
			
			statement.setString(1, seller.getName());
			statement.setString(2, seller.getEmail());
			statement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			statement.setDouble(4, seller.getBaseSalary());
			statement.setInt(5, seller.getDepartment().getId());
			statement.setInt(6, seller.getId());
			
			statement.executeUpdate();
	
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("DELETE FROM seller WHERE Id = ?");
			statement.setInt(1, id);
			
			statement.executeUpdate();
		}
		catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
		}
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement statement = null;
		ResultSet result = null;
		
		String baseQuery = "SELECT seller.*, department.Name AS DepName "
				+ "FROM seller INNER JOIN department "
				+ "ON seller.DepartmentId = department.Id "
				+ "WHERE seller.Id = ?";
		
		try {
			statement = connection.prepareStatement(baseQuery);
			statement.setInt(1, id);
			result = statement.executeQuery();
			
			if (result.next()) {
				Department department = instantiateDepartment(result);
				Seller seller = instantiateSeller(result, department);
				return seller;
			}
			
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
			DB.closeResultSet(result);
		}
	}


	@Override
	public List<Seller> findAll() {
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			result = statement.executeQuery();
			
			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (result.next()) {
				
				Department dep = map.get(result.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantiateDepartment(result);
					map.put(result.getInt("DepartmentId"), dep);
				}
				
				Seller seller = instantiateSeller(result, dep);
				sellers.add(seller);
			}
			return sellers;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
			DB.closeResultSet(result);
		}
	}
	
	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			statement.setInt(1, department.getId());
			result = statement.executeQuery();
			
			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (result.next()) {
				
				Department dep = map.get(result.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantiateDepartment(result);
					map.put(result.getInt("DepartmentId"), dep);
				}
				
				Seller seller = instantiateSeller(result, dep);
				sellers.add(seller);
			}
			return sellers;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
			DB.closeResultSet(result);
		}
	}
	
	private Department instantiateDepartment(ResultSet result) throws SQLException {
		Department department = new Department();
		department.setId(result.getInt("DepartmentId"));
		department.setName(result.getString("DepName"));
		return department;
	}
	
	private Seller instantiateSeller(ResultSet result, Department department) throws SQLException {
		Seller seller = new Seller();
		seller.setId(result.getInt("Id"));
		seller.setName(result.getString("Name")); 
		seller.setEmail(result.getString("Email"));
		seller.setBaseSalary(result.getDouble("BaseSalary"));
		seller.setBirthDate(result.getDate("BirthDate"));
		seller.setDepartment(department);
		return seller;
	}

}
