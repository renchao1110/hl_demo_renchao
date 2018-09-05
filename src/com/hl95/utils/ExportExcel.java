package com.hl95.utils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.pj.criterion.core.action.CriterionAction;

@SuppressWarnings("serial")
public class ExportExcel  extends CriterionAction {
	@SuppressWarnings("unchecked")
	public static int outExcel(Map<String, Object> map) {
		String path = (String)map.get("path");//文件路径
		String sheetName = (String)map.get("sheetName");//sheet名
		String[] titles = (String[]) map.get("titles");//表头名
		List<String[]> bodyList =  (List<String[]>) map.get("bodys");
		WritableWorkbook book=null;
		try {
			File file = new File(path);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			book = Workbook.createWorkbook(new File(path));
			WritableSheet sheet = null;
			sheet = book.createSheet(sheetName,0);//设置表名
			sheet.getSettings().setVerticalFreeze(1);//固定表头
				//合并单元格
				//设置列宽
				for(int i=0;i<titles.length;i++){
					sheet.setColumnView(i, 30);
				}
				//设置表格样式
				WritableFont bold = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD);//设置字体种类和黑体显示,字体为Arial,字号大小为10,采用黑体显示
				WritableCellFormat titleFormate = new WritableCellFormat(NumberFormats.TEXT);//生成一个单元格样式控制对象
				titleFormate.setFont(bold);
		        titleFormate.setAlignment(Alignment.CENTRE);//单元格中的内容水平方向居中
				titleFormate.setVerticalAlignment(VerticalAlignment.CENTRE);//单元格的内容垂直方向居中
				titleFormate.setBorder(Border.ALL, BorderLineStyle.THIN); //设置边框线
				
				WritableFont bold1 = new WritableFont(WritableFont.createFont("宋体"),10,WritableFont.NO_BOLD);
				WritableCellFormat bodyFormate = new WritableCellFormat(NumberFormats.TEXT);
				bodyFormate.setFont(bold1);
				bodyFormate.setAlignment(Alignment.CENTRE);
				bodyFormate.setVerticalAlignment(VerticalAlignment.CENTRE);
				bodyFormate.setBorder(Border.ALL, BorderLineStyle.THIN); 
				bodyFormate.setWrap(true);//设置自动换行
				//生成表格题头
				for(int i=0 ;i<titles.length;i++){
					Label labe = new Label(i, 0, titles[i],titleFormate);
					sheet.addCell(labe);
				}
				int celNum = 1;
				for(int i=0;i<bodyList.size();i++){
					sheet.setRowView(celNum,500,false);
					String[] obj = (String[])bodyList.get(i) ;
					for(int j=0;j<obj.length;j++){
						Label label=new Label(j,celNum,String.valueOf(obj[j]),bodyFormate);
						sheet.addCell(label);
					}
					celNum ++;
				}
				celNum=3;
				book.write();
			return 0;
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try{
				if(book!=null)book.close();
			}catch(Exception e){
				System.out.println("exception when closing Connection in finally");
				e.printStackTrace();
			}
		}
		return 1;
	}
}
