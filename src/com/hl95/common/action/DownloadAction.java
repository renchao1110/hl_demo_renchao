package com.hl95.common.action;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang.StringUtils;
import org.pj.criterion.core.action.CriterionAction;
import org.pj.criterion.core.support.properties.PropertiesManager;
import org.pj.criterion.core.support.upload.UploadManagerException;
import org.pj.criterion.core.util.StringUtil;

public class DownloadAction extends CriterionAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String downloadKey;

	private String fileName;

	private String selfPath;
	
	private String filePath;
	
	public String toBuilding(){
		return "building";
	}

	private String getSourceDir() {
		String source = null;
		if (StringUtils.isNotBlank(getFilePath())) {
			String webContext = this.getWebRootPath();
			PropertiesManager pm = (PropertiesManager) this
					.getWebServletContext().getAttribute(
							"IntranetProperties.cfg");
			
			String platform = pm.getString("platform");
			String downloadDir = null;
			downloadDir = pm.getString(getDownloadKey() + "." + platform);
			if (StringUtils.isBlank(downloadDir)) {
				throw new UploadManagerException("downloadDir 不能为null!");
			}
			if(platform.equals("windows")){
				if (downloadDir.substring(0, 1).equals("/")) {
					source = webContext + downloadDir.substring(1);
				} else if (!downloadDir.substring(0, 1).equals("/")) {
					source = downloadDir;
				}
			}else if(platform.equals("linux")){
				if (downloadDir.substring(0, 1).equals("/")) {
					source = downloadDir;
				} else if (!downloadDir.substring(0, 1).equals("/")) {
					source = webContext + downloadDir.substring(1);
				}
			}
			if(StringUtils.isNotBlank(getSelfPath())){
				source+="/"+getSelfPath();
				source +=getFilePath();
			}else{
				source += "/" + getFilePath();
			}
			
		}
		return source;
	}

	public String doDownLoad() throws Exception {
		HttpServletResponse response = this.getHttpServletResponse();

		String attch_name = null;

		String sourceDir = null;

		attch_name = StringUtil.toUtf8String(getFileName());
		sourceDir = getSourceDir();

		File file = new File(sourceDir);
		
		if(!file.exists()){
			throw new DownloadException("抱歉!你下载的"+getFileName()+"的文件不存在");
		}
		InputStream in = new FileInputStream(file);
//		String newName = new String(getFileName().getBytes("GBK"),"ISO-8859-1");

		
		response
				.setContentType("Content-type: application/x-msdownload");
		response.addHeader("Content-Disposition", "attachment; filename=\""
				+ attch_name + "\"");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
		
		byte[] buff = new byte[1000];
		OutputStream out = response.getOutputStream();
		int c;
		while ((c = in.read(buff, 0, 1000)) > 0) {
			out.write(buff, 0, c);
			out.flush();
		}
		out.close();
		in.close();
		
		
		return "download";
	}
	
	public void dex(){
		try{
			String _fname = StringUtils.defaultIfEmpty(getParameter("_fname"), "");
			String _dpath = StringUtils.defaultIfEmpty(getParameter("_dpath"), "");
			if(StringUtils.isNotBlank(_fname) && StringUtils.isNotBlank(_dpath)){
				HttpServletResponse response = getHttpServletResponse();
				int _index = _dpath.lastIndexOf(".")+1;
				if (_index > -1) {
					String postfix = _dpath.substring(_index);
					postfix = postfix.toLowerCase();
					_fname += "." + postfix;
				}
				String downLoadName = new String(_fname.getBytes("gbk"), "iso8859-1"); 
				response.setContentType("Content-type: application/x-msdownload");
				response.addHeader("Content-Disposition", "attachment; filename=\""
						+ downLoadName + "\"");
				response.setHeader("Content-Transfer-Encoding", "binary;");
				response.setHeader("Pragma", "no-cache;");
				response.setHeader("Expires", "-1;");
				
				byte[] buff = new byte[1000];
				OutputStream out=null;
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(getHttpServletRequest().getRealPath(File.separator)+_dpath);
					out = response.getOutputStream();
					int c;
					while ((c = fis.read(buff, 0, 1000)) > 0) {
						out.write(buff, 0, c);
						out.flush();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(out != null){
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(fis != null){
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}catch (Exception e) {
		}
	}
	
	/* excel下载	 */
	public void downloadExcel(){
		try{
			String _fname = StringUtils.defaultIfEmpty(getParameter("_fname"), "");
			String _dpath = StringUtils.defaultIfEmpty(getParameter("_dpath"), "");
			if(StringUtils.isNotBlank(_fname) && StringUtils.isNotBlank(_dpath)){
				HttpServletResponse response = getHttpServletResponse();
				int _index = _dpath.lastIndexOf(".")+1;
				if (_index > -1) {
					String postfix = _dpath.substring(_index);
					postfix = postfix.toLowerCase();
					_fname += "." + postfix;
				}
				String downLoadName = new String(_fname.getBytes("gbk"), "iso8859-1"); 
				response.setContentType("Content-type: application/x-msdownload");
				response.addHeader("Content-Disposition", "attachment; filename=\""
						+ downLoadName + "\"");
				response.setHeader("Content-Transfer-Encoding", "binary;");
				response.setHeader("Pragma", "no-cache;");
				response.setHeader("Expires", "-1;");
				
				byte[] buff = new byte[1000];
				OutputStream out=null;
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(_dpath);
					out = response.getOutputStream();
					int c;
					while ((c = fis.read(buff, 0, 1000)) > 0) {
						out.write(buff, 0, c);
						out.flush();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(out != null){
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(fis != null){
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}catch (Exception e) {
		}
	}
	
	
	public String getDownloadKey() {
		return downloadKey;
	}

	public void setDownloadKey(String downloadKey) {
		this.downloadKey = downloadKey;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSelfPath() {
		return selfPath;
	}

	public void setSelfPath(String selfPath) {
		this.selfPath = selfPath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
