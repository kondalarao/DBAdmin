<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Database Admin Login</title>
</head>
<body>
	<form method="post" action="DatabaseReader">
		<center>
			<br>Choose Database : <select name="dbtype">
				<option value="mysql">MySQL</option>
				<option value="pgsql">Postgres SQL</option>
			</select> <br> <br>Connection URL : <input type="text"
				name="connectionstring" size="20px" required> <br> <br>User
			Name : <input type="text" name="user" size="20px" required> <br>
			<br>Password : <input type="password" name="pass" size="20px"
				required> <br><br><input type="submit" value="Connect">
		</center>
	</form>
</body>
</html>