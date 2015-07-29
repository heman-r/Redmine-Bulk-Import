package redmine.action;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class ExcelRead {

    @SuppressWarnings("unchecked")
    public void readXls(String f1) throws Exception {
    	
          String filename = f1;
        List sheetData = new ArrayList();

        FileInputStream fis = null;
        try {
            //
            // Create a FileInputStream that will be use to read the excel file.
            //
            fis = new FileInputStream(filename);

            //
            // Create an excel workbook from the file system.
            //
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            //
            // Get the first sheet on the workbook.
            //
            HSSFSheet sheet = workbook.getSheetAt(0);

            //
            // When we have a sheet object in hand we can iterator on each
            // sheet's rows and on each row's cells. We store the data read
            
            // on an ArrayList so that we can printed the content of the excel
            // to the console.
            //
            int d;
            Iterator rows = sheet.rowIterator();
            while (rows.hasNext()) {
                HSSFRow row = (HSSFRow) rows.next();
                Iterator cells = row.cellIterator();

                List data = new ArrayList();
                int count=0;
               
               while(count<=7){
                   
            	   HSSFCell cell = (HSSFCell) row.getCell(count, Row.RETURN_NULL_AND_BLANK);
                    
                    data.add(cell);
                    count=count+1;
                }

                sheetData.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
//Creating and Updating Issues
       CreateAndUpdate cu=new CreateAndUpdate();
        cu.createIssuesFromExcel(sheetData);
    }

    
    
  }//class