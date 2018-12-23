import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class DatabaseReader extends HttpServlet {

	@SuppressWarnings("resource")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String url = request.getParameter("connectionstring");
			String user = request.getParameter("user");
			String pass = request.getParameter("pass");
			String dbtype = request.getParameter("dbtype");

			Connection conn = null;
			try {
				if ("mysql".equals(dbtype)) {
					conn = getMySqlConnection(url, user, pass);
					HttpSession sessioninfo = request.getSession();
					sessioninfo.setAttribute("dbtype", dbtype);
					sessioninfo.setAttribute("url", url);
					sessioninfo.setAttribute("user", user);
					sessioninfo.setAttribute("pass", pass);
					System.out.println("Got Connection");
				} else {

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				response.getOutputStream().println("Unable to connect to the database" + e.getMessage());
				e.printStackTrace();
			}

			PrintWriter out = response.getWriter();
			out.println(
					"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\"http://www.w3.org/TR/html4/loose.dtd\">\n"
							+ "<html> \n" + "<head> \n"
							+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"> \n"
							+ "<title> Database Administrator Portal  </title> \n" + "</head> \n" + "<body><h3>List of Database Tables</h3> \n");

			DatabaseMetaData meta = conn.getMetaData();
			String table[] = { "TABLE" };
			ResultSet rstables = null;
			ResultSet rscolumns = null;
			String columnName;
			String schemaname = url.substring(url.lastIndexOf('/')+1);
			rstables = meta.getTables(schemaname, null, "%", table);
			while (rstables.next()) {

				String tableName = rstables.getString("TABLE_NAME");
				rscolumns = meta.getColumns(null, null, tableName, null);
				out.println("<br>TABLE NAME : " + tableName.toUpperCase());
				out.println("<form action=\"DataExport\" method='post'>");
				out.println("<table border=\"1\">\r\n" + "<tr>\r\n" + "<td>COLUMN_NAME</td>\r\n"
							+ "<td>TYPE_NAME</td>\r\n" + "<td>COLUMN_NAME</td>\r\n");
				out.println("<td>MASK?</td></tr>");
				while (rscolumns.next()) {

					columnName = rscolumns.getString("COLUMN_NAME");
					out.println("<tr><td>" + columnName.toUpperCase() + "</td>");
					out.println("<td>" + rscolumns.getString("TYPE_NAME").toUpperCase()+ "</td>");
					out.println("<td>" + rscolumns.getString("COLUMN_SIZE").toUpperCase() + "</td>"
							+ "<td><input type='checkbox' name='"+columnName+"'/>"
							+ "</td></tr>");
				}

				out.println("</table>");
				out.println("<input type='hidden' name='tablename' value='"+tableName+"'/><input type=\"submit\" value=\"Mask and Export\"></form>");

			}
			
			out.println("</body></html>");

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Connection getMySqlConnection(String url, String user, String pass) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		// String url = "jdbc:mysql://localhost:3306/sample";
		// String username = "root";
		// String password = "admin";

		Connection conn = DriverManager.getConnection(url, user, pass);
		return conn;
	}

	public static Connection getOracleConnection() throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:caspian";
		String username = "mp";
		String password = "mp2";

		Class.forName(driver); // load Oracle driver
		Connection conn = DriverManager.getConnection(url, username, password);
		return conn;
	}

}