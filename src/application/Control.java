package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import inventory.Product;
import inventory.ProductDAO;

@SuppressWarnings("serial")
public class Control extends HttpServlet {
	
	private ProductDAO dao;
	
	public void init( ) {
		final String url = getServletContext().getInitParameter("JDBC-URL");
		final String username = getServletContext().getInitParameter("JDBC-USERNAME");
		final String password = getServletContext().getInitParameter("JDBC-PASSWORD");
		
		dao = new ProductDAO(url, username, password);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String action = request.getServletPath();
		
		try {
			switch (action) {
			case "/add": //intentionally fall through
			case "/edit": showEditForm(request, response); break;
			case "/insert": insertProduct(request, response); break;
			case "/update": updateProduct(request, response); break;
			case "/search": searchProducts(request, response); break;
			default: viewProducts(request, response); break;
			}
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

	private void viewProducts(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		final String action = request.getParameter("action") != null
				? request.getParameter("action")
				: "null";
		List<Product> products = dao.getProducts();
		request.setAttribute("products", products);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("items.jsp");
		dispatcher.forward(request, response);
	}
	
	private void insertProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		double price = Double.parseDouble(request.getParameter("price"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		
		dao.insertProduct(name,  description,  price,  quantity);
		response.sendRedirect(request.getContextPath() + "/");
	}
	
	private void updateProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		final String action = request.getParameter("action") != null
			? request.getParameter("action")
			: request.getParameter("submit").toLowerCase();
		final int id = Integer.parseInt(request.getParameter("id"));
		
		Product product = dao.getProduct(id);
		switch (action) {
			case "purchase": product.purchaseMe(); break;
			case "save":
				String name = request.getParameter("name");
				String description = request.getParameter("description");
				double price = Double.parseDouble(request.getParameter("price"));
				int quantity = Integer.parseInt(request.getParameter("quantity"));
				
				product.setName(name);
				product.setDescription(description);
				product.setPrice(price);
				product.setQuantity(quantity);
				break;
			case "delete": deleteProduct(id, request, response); return;
		}
		dao.updateProduct(product);
		
		response.sendRedirect(request.getContextPath() + "/");
	}
	
	private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		try {
			final int id = Integer.parseInt(request.getParameter("id"));
			
			Product product = dao.getProduct(id);
			request.setAttribute("product", product);
		} catch (NumberFormatException e) {
			
		} finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("productform.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	private void deleteProduct(final int id, HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		dao.deleteProduct(dao.getProduct(id));
		
		response.sendRedirect(request.getContextPath() + "/");
	}
	
	private void searchProducts(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		final String category = request.getParameter("search_attribute") != null
				? request.getParameter("search_attribute")
				: null;
				
		final String query = request.getParameter("search_bar") != null
				? request.getParameter("search_bar").toLowerCase()
				: "";
		
		List<Product> results = dao.searchProduct(category, query);
		
		request.setAttribute("products", results);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("items.jsp");
		dispatcher.forward(request,  response);
	}
}