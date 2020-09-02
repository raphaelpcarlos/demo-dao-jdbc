package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
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
import model.entities.Departament;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;

	}

	@Override
	public void insert(Seller obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO seller"
		+"(Name, Email, BirthDate, BaseSalary, DepartmentId)"
		+"VALUES\r\n" 
		+"(?, ?, ?, ?, ?)",
		Statement.RETURN_GENERATED_KEYS);
		
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartament().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected >0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1); 
					obj.setId(id);
					
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("nenhuma linha afetada!");
			}
		}	
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
		
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartament().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
			
		}	
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void delete(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			
			st.setInt(1, id);
			
			int rows = st.executeUpdate();
			
			if(rows == 0) {
				throw new DbException("ID inexistente");
			}
			
		}catch (SQLException e) {

		throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement("select *from seller sel\r\n" + "inner join department dep\r\n"
					+ "on sel.DepartmentId = dep.Id\r\n" + "where sel.Id = ? ");

			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				Departament dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);
				return obj;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Seller instantiateSeller(ResultSet rs, Departament dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartament(dep);

		return obj;
	}

	private Departament instantiateDepartment(ResultSet rs) throws SQLException {

		Departament dep = new Departament();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("Name"));

		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");

			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Departament> map = new HashMap<>();
			
			while (rs.next()) {
				
				Departament dep = map.get(rs.getInt("DepartmentId"));

				if(dep == null) {
					
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				 
				Seller obj = instantiateSeller(rs, dep);
			
				list.add(obj);
				
			}
			return list;

		} catch (SQLException e) {
			
			throw new DbException(e.getMessage());
		} finally {
			
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public List<Seller> findByDepartment(Departament departament) {
		
		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");

			st.setInt(1, departament.getId());
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Departament> map = new HashMap<>();
			
			while (rs.next()) {
				
				Departament dep = map.get(rs.getInt("DepartmentId"));

				if(dep == null) {
					
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				 
				Seller obj = instantiateSeller(rs, dep);
			
				list.add(obj);
				
			}
			return list;

		} catch (SQLException e) {
			
			throw new DbException(e.getMessage());
		} finally {
			
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

}
