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
			String schemaname = url.substring(url.lastIndexOf('/') + 1);

			Connection conn = null;
			try {
				if ("mysql".equals(dbtype)) {
					conn = getMySqlConnection(url, user, pass);
				} else if ("pgsql".equals(dbtype)) {
					conn = getPostGreSQLConnection(url, user, pass);
				}
				HttpSession sessioninfo = request.getSession();
				sessioninfo.setAttribute("dbtype", dbtype);
				sessioninfo.setAttribute("url", url);
				sessioninfo.setAttribute("user", user);
				sessioninfo.setAttribute("pass", pass);
				sessioninfo.setAttribute("schema", schemaname);
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
							+ "<title> Database Administrator Portal  </title> \n"
							+ "<link href=\"styles.css\" rel=\"stylesheet\" /></head> \n"
							+ "<body><h1>List of Database Tables</h1> \n");

			DatabaseMetaData meta = conn.getMetaData();
			String table[] = { "TABLE" };
			ResultSet rscolumns = null;
			String columnName;
			out.println("<form action=\"DataExport\" method='post' class='sform'>"
					+ "<input type=\"submit\" value=\"Mask and Export\" class=\"button1\">");
			ResultSet rstables = meta.getTables(schemaname, null, "%", table);
			while (rstables.next()) {

				String tableName = rstables.getString("TABLE_NAME");
				rscolumns = meta.getColumns(null, null, tableName, null);

				out.println("<table border=\"1\">\r\n" + "<thead><tr><td colspan=4><span class='table_toggle' tname='" + tableName +"'>+" + tableName.toUpperCase()
						+ "</span></td></tr></thead><tbody style='display:none;' id='"+ tableName +"'><tr>\r\n" + "<td>NAME</td>\r\n" + "<td>TYPE</td>\r\n"
						+ "<td>SIZE</td>\r\n");
				out.println("<td>MASK?</td></tr>");
				while (rscolumns.next()) {

					columnName = rscolumns.getString("COLUMN_NAME");
					out.println("<tr><td>" + columnName.toUpperCase() + "</td>");
					out.println("<td>" + rscolumns.getString("TYPE_NAME").toUpperCase() + "</td>");
					out.println("<td>" + rscolumns.getString("COLUMN_SIZE").toUpperCase() + "</td>"
							+ "<td><input type='checkbox' name='" + columnName + "'/>" + "</td></tr>");
				}

				out.println("</tbody></table>");
			}

			out.println("<br></form></body>");
			out.println("<script src=\"jQuery-ui.js\" type=\"text/javascript\"></script>");
			out.println("<script src=\"tables.js\" type=\"text/javascript\"></script>");
			out.println("</html>");

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

	public static Connection getPostGreSQLConnection(String url, String user, String pass) throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(url, user, pass);
		return conn;
	}

}