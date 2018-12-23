<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 <link href="styles.css" rel="stylesheet" />
<title>Database Administrator Login</title>
</head>
<body>
<div class="container">
	<h1>Database Administrator Portal</h1>
	<form method="post" action="DatabaseReader">
	<ul>
	<li>
		<span class="left">Choose Database : </span>
	    <span class="right">
		    <select name="dbtype">
					<option value="mysql">MySQL</option>
					<option value="pgsql">Postgres SQL</option>
			</select>
		</span>
	</li>
	<li>
		<span class="left">Connection URL : </span>
		<span class="right"><input type="text"	name="connectionstring" class="input" required ></span>
	</li>
	<li>
		<span class="left">User	Name : </span>
		<span class="right"><input type="text" name="user" class="input" required > </span>
	</li>
	<li>
		<span class="left">Password : </span>
		<span class="right"><input type="password" name="pass" class="input" required ></span>
	</li></ul>
			<input type="submit" value="Connect" class="button" />
		
		
	</form>
	</div>
	</body>
</html>