<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Inventory Management</title>
		
		<style type="text/css">
			<%@ include file="css/styles.css" %>
		</style>
	</head>
	<body>
		<div>
			<h1>Inventory Management</h1>
			
			<div class="header">
				<a href="${pageContext.request.contextPath}/" class="header-button">VIEW ALL</a>
				<a href="${pageContext.request.contextPath}/add" class="header-button">ADD A PRODUCT</a>
			</div>
		</div>
		<div class="search-functions">
                <form action="search">
                    <label class="search-bar-label">Search Bar:<input type="text" name="search_bar" class="search-bar"/>
                    <input type="submit" value="Search" name="sumbit" class="button"/></label> 
                    <label>
                        <label><input class="radio" type="radio" name="search_attribute" value="name">&nbsp; Name &nbsp; &nbsp; </label>
                        <label><input class="radio" type="radio" name="search_attribute" value="description">&nbsp; Description &nbsp; &nbsp; </label>
                        <label><input class="radio" type="radio" name="search_attribute" value="price">&nbsp; Price &nbsp; &nbsp; </label>
      
                    </label>
                </form>
            </div>
		<div>
			<table border="1">
				<tr>
					<th>Name</th>
					<th>Description</th>
					<th>Price</th>
					<th>Quantity</th>
					<th>Actions</th>
				</tr>
				<c:forEach var="product" items="${products}">
					<tr>
						<td><c:out value="${product.name}" /></td>
						<td><c:out value="${product.description}" /></td>
						<td><c:out value="${product.price}" /></td>
						<td><c:out value="${product.quantity}" /></td>
						<td>
							<div>
								<a href="${pageContext.request.contextPath}/update?action=purchase&id=${product.id}" class="button">PURCHASE</a>
							
								<a href="${pageContext.request.contextPath}/edit?id=${product.id}" class="button">EDIT</a>
							</div>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</body>
</html>