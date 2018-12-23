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

@SuppressWarnings("serial")
public class DatabaseReader extends HttpServlet {

	@SuppressWarnings("resource")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
							+ "<title> My first jsp  </title> \n" + "</head> \n" + "<body> \n"
							+ "<table border=\"2\">\r\n" + "<tr>\r\n" + "<td>COLUMN_NAME</td>\r\n"
							+ "<td>TYPE_NAME</td>\r\n" + "<td>COLUMN_NAME</td>\r\n" + "</tr>");

			DatabaseMetaData meta = conn.getMetaData();
			String table[] = { "TABLE" };
			ResultSet rs = null;

			rs = meta.getTables(conn.getSchema(), null, "%", table);

			while (rs.next()) {

				String tableName = rs.getString("TABLE_NAME");
				rs = meta.getColumns(null, null, tableName, null);
				out.println("Table Name : " + tableName.toUpperCase());
				while (rs.next()) {

					out.println("<tr><td>" + rs.getString("COLUMN_NAME") + "</td>");
					out.println("<td>" + rs.getString("TYPE_NAME") + "</td>");
					out.println("<td>" + rs.getString("COLUMN_SIZE") + "</td></tr>");
				}

				out.println("</table>");
				out.println("<form action=\"DataExport\">" + "<input type=\"submit\" value=\"Export\">" + "</body>"
						+ "</html>");

			}

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