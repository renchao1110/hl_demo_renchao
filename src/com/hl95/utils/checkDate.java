package com.hl95.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;

public class checkDate {
	public static String getCellValue(Cell cell) {  
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ret;
        if (cell.toString().indexOf("/") > 0) {
        	ret = cell.toString().replace("/", "-");
        }
        switch (cell.getCellType()) {  
        case Cell.CELL_TYPE_BLANK:  
            ret = "";  
            break;  
        case Cell.CELL_TYPE_BOOLEAN:  
            ret = String.valueOf(cell.getBooleanCellValue());  
            break;  
        case Cell.CELL_TYPE_ERROR:  
            ret = null;  
            break;  
        case Cell.CELL_TYPE_FORMULA:  
            Workbook wb = cell.getSheet().getWorkbook();  
            CreationHelper crateHelper = wb.getCreationHelper();  
            FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();  
            ret = getCellValue(evaluator.evaluateInCell(cell));  
            break;  
        case Cell.CELL_TYPE_NUMERIC:  
            if (DateUtil.isCellDateFormatted(cell)) {   
                Date theDate = cell.getDateCellValue();  
                ret = simpleDateFormat.format(theDate);  
            } else {   
                ret = NumberToTextConverter.toText(cell.getNumericCellValue());  
            }  
            break;  
        case Cell.CELL_TYPE_STRING:  
            ret = cell.getRichStringCellValue().getString();  
            break;  
        default:  
            ret = "";  
        }  
          
        return ret; //有必要自行trim  
    }  
	
	public static String getDateSimp(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-d");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy年MM月dd号");
		SimpleDateFormat sdf6 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf7 = new SimpleDateFormat("yyyy.MM.dd");
		
		String leftStr ="";
		String rightStr="";
		String lastDate="";
		try {
			if(date.length()>4){
				char fie = date.charAt(4);
				if("-".equals(fie+"")){
					leftStr= sdf.format(sdf1.parse(date));
				}else if("/".equals(fie+"")){
					leftStr= sdf.format(sdf3.parse(date));
				}else if("年".equals(fie+"")){
					if("号".equals(date.charAt(10)+"")){						
						leftStr=sdf.format(sdf5.parse(date));
					}else{
						leftStr=sdf.format(sdf6.parse(date));						
					}
				}else if(".".equals(fie+"")){
					leftStr=sdf.format(sdf7.parse(date));
				}
			}else{
				return date;
			}
			if(date.indexOf("-",6)>0){
				lastDate = date.substring(date.indexOf("-",6)+1);
			}else if(date.indexOf("/",6)>0){
				lastDate = date.substring(date.indexOf("/",6)+1);
			}else if(date.indexOf(".",6)>0){
				lastDate = date.substring(date.indexOf(".",6)+1);
			}else if(date.indexOf("至",6)>0){
				lastDate = date.substring(date.indexOf("至",6)+1);
			}else{
				return date;
			}
			if(lastDate.length()>4){
				char fil = lastDate.charAt(4);
				if("-".equals(fil+"")){
					rightStr= sdf.format(sdf1.parse(lastDate));
				}else if("/".equals(fil+"")){
					rightStr= sdf.format(sdf3.parse(lastDate));
				}else if("年".equals(fil+"")){
					if("号".equals(lastDate.charAt(lastDate.length()-1)+"")){						
						rightStr=sdf.format(sdf5.parse(lastDate));
					}else{						
						rightStr=sdf.format(sdf6.parse(lastDate));
					}
				}else if(".".equals(fil+"")){
					rightStr=sdf.format(sdf7.parse(lastDate));
				}else{
					return date;
				}
			}else{
				return date;
			}
			
			return leftStr+"-"+rightStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
}
