package com.hl95.common.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.pj.criterion.core.support.properties.PropertiesManager;
import org.pj.criterion.core.util.DateFormatUtil;

import com.hl95.sys.entity.SysEmployee;


public class WriteMsg extends Thread {

	private List<SysEmployee> employees;

	private String content;

	private ServletContext context;

	public WriteMsg() {

	}

	public WriteMsg(List<SysEmployee> employees, String content,
			ServletContext context) {
		this.employees = employees;
		this.content = content;
		this.context = context;
	}

	public boolean WriteMsgFile(String empId, String content) {
		boolean writeOkYn = true;
		// ActionContext contexts = ActionContext.getContext();
		// context = (ServletContext) contexts
		// .get(ServletActionContext.SERVLET_CONTEXT);
		// PropertiesManager pm = (PropertiesManager)
		// context.getAttribute("IntranetProperties.cfg");
		// String uploadDir=null;
		// String platform=pm.getString("platform");
		// uploadDir = pm.getString("intranet.bgn."+platform);
		String dateStr = DateFormatUtil.format(new Date(), "yyyyMMddHHmmssms");
		String writeDateStr = DateFormatUtil.format(new Date(),
				"yyyy-MM-dd HH:mm:ss");
		String fileName = dateStr + ".sisenmsg";
		String path = "c:\\Program Files\\bgn\\server\\Server_Plug_in\\Abet_File\\ServerMsgList\\"
				+ empId + fileName;
		File f = null;
		BufferedWriter output = null;
		try {
			f = new File(path);
			output = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(f), "UTF-8"));
			output.write("False");
			output.newLine();
			output.write("");
			output.newLine();
			output.write(empId);
			output.newLine();
			output.write("False");
			output.newLine();
			output.write(content + "  " + writeDateStr);
		} catch (Exception e) {
			writeOkYn = false;
		}
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return writeOkYn;
	}

	public void writeMsg() {

		PropertiesManager pm = (PropertiesManager) context
				.getAttribute("IntranetProperties.cfg");
		String uploadDir = null;
		String platform = pm.getString("platform");
		uploadDir = pm.getString("intranet.bgn." + platform);
		String dateStr = DateFormatUtil.format(new Date(), "yyyyMMddHHmmssms");
		String fileName = dateStr + ".sisenmsg";
		// String path =
		// "D:\\bgn\\Server_Plug_in\\Abet_File\\ServerMsgList\\"+fileName;
		for (SysEmployee employee : employees) {
			String empId = employee.getEmpId();
			String path = uploadDir + empId + fileName;
			try {
				File f = new File(path);
				BufferedWriter output = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
				output.write("True");
				output.newLine();
				output.write("admin");
				output.newLine();
				output.write(empId);
				output.newLine();
				output.write("False");
				output.newLine();
				output.write(content);
				output.close();
				Thread.sleep(2000);
			} catch (Exception e) {

			}

		}

	}

	@SuppressWarnings("finally")
	public void run() {
		// TODO Auto-generated method stub

		try {
			writeMsg();

		} catch (java.lang.Throwable e) {
			// e.printStackTrace();
			// System.out.println("______________"+userName+"_______________");
			// if(this.down!=null){
			// System.out.println(down.getSubject());
			// }
		}

	}
}
