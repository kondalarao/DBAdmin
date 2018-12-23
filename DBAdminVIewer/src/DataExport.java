import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataExport extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample", "root", "admin");

			Statement statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from student");
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet spreadsheet = workbook.createSheet("data");

			XSSFRow row = spreadsheet.createRow(0);
			XSSFCell cell;
			cell = row.createCell(0);
			cell.setCellValue("ID");
			cell = row.createCell(1);
			cell.setCellValue("NAME");
			cell = row.createCell(2);
			cell.setCellValue("SKILL");

			int i = 1;

			while (resultSet.next()) {
				row = spreadsheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue(resultSet.getString("ID"));
				cell = row.createCell(1);
				cell.setCellValue(resultSet.getString("NAME"));
				cell = row.createCell(2);
				cell.setCellValue(resultSet.getString("SKILL"));
				cell = row.createCell(3);

				i++;
			}

//			FileOutputStream out = new FileOutputStream(new File("E:\\exceldatabase1.xlsx"));
//			workbook.write(out);
//			out.close();
//			out.flush();
			ByteArrayOutputStream fileOutput = new ByteArrayOutputStream();
			workbook.write(fileOutput);
			
			ServletOutputStream outputStream = response.getOutputStream();
			byte[] file = fileOutput.toByteArray();
			
			response.setContentLength(file.length);
			response.setHeader("Content-Disposition", "attachment;filename='exceldatabase1.xlsx'");
			response.setHeader("charset", "iso-8859-1");
			response.setContentType("application/octet-stream");
			outputStream.write(file, 0, file.length);
			response.setStatus(HttpServletResponse.SC_OK);
	
			outputStream.flush();
			outputStream.close();
			response.flushBuffer();
			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}