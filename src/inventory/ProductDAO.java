package inventory;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductDAO {
	private final String url;
	private final String username;
	private final String password;
	
	public ProductDAO(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public Product getProduct(int id) throws SQLException {
		final String sql = "SELECT * FROM products WHERE product_id = ?";
		
		Product product = null;
		Connection conn = getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		
		if (rs.next()) {
			String name = rs.getString("name");
			String description = rs.getString("description");
			double price = rs.getDouble("price");
			int quantity = rs.getInt("quantity");
			
			product = new Product(id, name, description, price, quantity);
		}
		
		rs.close();
		pstmt.close();
		conn.close();
		
		return product;
	}
	
	public List<Product> getProducts() throws SQLException {
		System.out.println(url);
		final String sql = "SELECT * FROM products ORDER BY product_id ASC";
		
		List<Product> products = new ArrayList<Product>();
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		while (rs.next()) {
			int id = rs.getInt("product_id");
			String name = rs.getString("name");
			String description = rs.getString("description");
			double price = rs.getDouble("price");
			int quantity = rs.getInt("quantity");
			
			products.add(new Product(id, name, description, price, quantity));
		}
		
		rs.close();
		stmt.close();
		conn.close();
		
		return products;
	}
	
	public boolean insertProduct(String name, String description, double price, int quantity) throws SQLException {       
		final String sql = "INSERT INTO products (name, description, price, quantity) " +
			"VALUES (?, ?, ?, ?)";
		
        Connection conn = getConnection();        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        pstmt.setString(1, name);
        pstmt.setString(2, description);
        pstmt.setDouble(3, price);
        pstmt.setInt(4, quantity);
        int affected = pstmt.executeUpdate();
        
        pstmt.close();
        conn.close();
        
        return affected == 1;
    }
	
    public boolean updateProduct(Product product) throws SQLException {
    	final String sql = "UPDATE products SET name = ?, description= ?, price = ?, quantity = ? " +
    		"WHERE product_id = ?";
    			
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
                
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getDescription());
        pstmt.setDouble(3, product.getPrice());
        pstmt.setInt(4, product.getQuantity());
        pstmt.setInt(5, product.getId());
        int affected = pstmt.executeUpdate();
        
        pstmt.close();
        conn.close();
        
        return affected == 1;
    }
	
    public boolean deleteProduct(Product product) throws SQLException {
    	final String sql = "DELETE FROM products WHERE product_id = ?";
    	
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        pstmt.setInt(1, product.getId());
        int affected = pstmt.executeUpdate();
        
        pstmt.close();
        conn.close();
        
        return affected == 1;
    }
    
    public List<Product> searchProduct(String category, String query) throws SQLException {
    	String sql = "";
    	Connection conn = getConnection();
    	PreparedStatement pstmt = null;
    	
    	if (category.equals("price")) {
    		double amount = 0;
    		try {
    			amount = Double.parseDouble(query);
    		} catch (NumberFormatException e) {
    			
    		}
    		sql = "SELECT * FROM products WHERE price = ?";
    		pstmt = conn.prepareStatement(sql);
    		pstmt.setDouble(1,  amount);
    		
    	} else if (category.equals("description")) {
    		sql = "SELECT * FROM products WHERE description = ?";
    		pstmt = conn.prepareStatement(sql);
    		pstmt.setString(1, query);
    		
    	} else if (category.equals("name")) {
    		sql = "SELECT * FROM products WHERE name = ?";
    		pstmt = conn.prepareStatement(sql);
    		pstmt.setString(1, query);
    	}
    	
    	List<Product> products = new ArrayList<Product>();
    	ResultSet rs = pstmt.executeQuery();
		
		while (rs.next()) {
			int id = rs.getInt("product_id");
			String name = rs.getString("name");
			String description = rs.getString("description");
			double price = rs.getDouble("price");
			int quantity = rs.getInt("quantity");
			
			products.add(new Product(id, name, description, price, quantity));
		}
		
		rs.close();
		pstmt.close();
		conn.close();
		
		return products;
    	
    }
    
	private Connection getConnection() throws SQLException {
		final String driver = "com.mysql.cj.jdbc.Driver";
		
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return DriverManager.getConnection(url, username, password);
	}
}