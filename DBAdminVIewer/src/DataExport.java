import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataExport extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			HttpSession sessionInfo = request.getSession();
			
			Connection connect = null;
			try {
				String dbtype = sessionInfo.getAttribute("dbtype").toString();
				String url = sessionInfo.getAttribute("url").toString();
				String user = sessionInfo.getAttribute("user").toString();
				String pass = sessionInfo.getAttribute("pass").toString();
				if ("mysql".equals(dbtype)) {
					connect = getMySqlConnection(url,
							user, pass);

				} else if ("pgsql".equals(dbtype)) {
					connect = getPostGreSQLConnection(url,
							user, pass);

				} 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				response.getOutputStream().println("Unable to connect to the database" + e.getMessage());
				e.printStackTrace();
			}
			
			DatabaseMetaData meta = connect.getMetaData();
			String table[] = { "TABLE" };
			ResultSet rstables = meta.getTables(sessionInfo.getAttribute("schema").toString(), null, "%", table);
			XSSFWorkbook workbook = new XSSFWorkbook();

			while (rstables.next()) {

				String tablename = rstables.getString("TABLE_NAME");
				String sheetname = tablename.length() > 30 ? tablename.substring(0, 29) : tablename;
				XSSFSheet spreadsheet = workbook.createSheet(sheetname);
				XSSFRow row = spreadsheet.createRow(0);
				XSSFCell cell;
				ResultSet rscolumns = connect.getMetaData().getColumns(null, null, tablename, null);
				ArrayList<String> columns = new ArrayList();
				int columnnum = 0;
				String columnname;
				String query = "select ";

				while (rscolumns.next()) {
					cell = row.createCell(columnnum);
					columnname = rscolumns.getString("COLUMN_NAME");
					cell.setCellValue(columnname);
					columns.add(columnname);
					if ("on".equals(request.getParameter(columnname))) {
						query = query + "kondal_mask(" + columnname + ") as " + columnname + ",";
					} else {
						query = query + columnname + ",";
					}
					columnnum++;
				}
				query = query.substring(0, query.length() - 1);
				query = query + " from " + tablename;

				int rownum = 1;
				Statement statement = connect.createStatement();
				ResultSet resultSet = statement.executeQuery(query);

				while (resultSet.next()) {

					int columnum = 0;
					row = spreadsheet.createRow(rownum);
					Iterator<String> columnItr = columns.iterator();
					while (columnItr.hasNext()) {
						cell = row.createCell(columnum);
						cell.setCellValue(resultSet.getString(columnItr.next()));
						columnum++;
					}
					rownum++;
				}

			}

			// FileOutputStream out = new FileOutputStream(new
			// File("E:\\exceldatabase1.xlsx"));
			// workbook.write(out);
			// out.close();
			// out.flush();
			ByteArrayOutputStream fileOutput = new ByteArrayOutputStream();
			workbook.write(fileOutput);

			ServletOutputStream outputStream = response.getOutputStream();
			byte[] file = fileOutput.toByteArray();

			response.setContentLength(file.length);
			response.setHeader("Content-Disposition", "attachment;filename='data.xlsx'");
			response.setHeader("charset", "iso-8859-1");
			response.setContentType("application/octet-stream");
			outputStream.write(file, 0, file.length);
			response.setStatus(HttpServletResponse.SC_OK);

			outputStream.flush();
			outputStream.close();
			response.flushBuffer();

		} catch (Exception e) {
			System.out.println("Unable to export the masked data : " + e.getMessage());
			e.printStackTrace();
		}

	}
	
	public static Connection getMySqlConnection(String url, String user, String pass) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(url, user, pass);
		return conn;
	}
	public static Connection getPostGreSQLConnection(String url, String user, String pass) throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(url, user, pass);
		return conn;
	}
}