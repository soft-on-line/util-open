package org.open.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

public class DownFTPFile {

	private String           hostname;
	private String           username;
	private String           password;
	private String           localBaseDir;
	private String           remoteBaseDir;

	private static FTPClient ftpClient = new FTPClient();

	public static void main(String[] args) {
		System.out.println("This is main of : " + DownFTPFile.class);

		//		private String host     = "115.238.101.43";
		//		private int    port     = 21;
		//		private String username = "download";
		//		private String password = "down10ad";

		String hostname = "115.238.101.43";
		String username = "download";
		String password = "down10ad";
		String localBaseDir = "d:/tempftp/";
		String remoteBaseDir = "download";
		//		String remoteBaseDir = "";

		DownFTPFile downFTPClient = new DownFTPFile();
		downFTPClient.setHostname(hostname);
		downFTPClient.setUsername(username);
		downFTPClient.setPassword(password);
		downFTPClient.setLocalBaseDir(localBaseDir);
		downFTPClient.setRemoteBaseDir(remoteBaseDir);
		downFTPClient.startDown();
	}

	private void startDown() {
		try {
			ftpClient.connect(hostname);
			ftpClient.login(username, password);
			//			FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
			//			ftpClient.configure(conf);

			ftpClient.configure(new FTPClientConfig(ftpClient.getSystemType()));

			//			ftpClient.setControlEncoding("GBK");
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			FTPFile[] files = null;
			//			boolean changedir = ftpClient.changeWorkingDirectory(remoteBaseDir);
			//			if (changedir) {

			ftpClient.enterLocalPassiveMode();

			files = ftpClient.listFiles();
			for (int i = 0; i < files.length; i++) {
				//					downloadFile(files[i], localBaseDir, remoteBaseDir);
				System.out.println(files[i]);
			}
			//			}

			//			ftpClient.listFiles();

			ftpClient.enterLocalPassiveMode();

			files = ftpClient.listDirectories();
			for (int i = 0; i < files.length; i++) {
				downloadFile(files[i], localBaseDir, remoteBaseDir);
				System.out.println(files[i]);
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	private void downloadFile(FTPFile ftpFile, String relativeLocalPath, String relativeRemotePath) {
		if (ftpFile.isFile()) {//down file
			if (ftpFile.getName().indexOf("?") == -1) {
				OutputStream outputStream = null;
				try {
					outputStream = new FileOutputStream(relativeLocalPath + ftpFile.getName());
					ftpClient.retrieveFile(ftpFile.getName(), outputStream);
					outputStream.flush();
					outputStream.close();
				}
				catch (Exception e) {
					System.err.println(e);
				}
				finally {
					try {
						if (outputStream != null)
							outputStream.close();
					}
					catch (IOException e) {
						System.out.println("ShowasaFile");
					}
				}
			}
		} else { //deal dirctory
			String newlocalRelatePath = relativeLocalPath + ftpFile.getName();
			String newRemote = new String(relativeRemotePath + ftpFile.getName().toString());

			File fl = new File(newlocalRelatePath);
			if (!fl.exists()) {
				fl.mkdirs();
			}
			try {
				newlocalRelatePath = newlocalRelatePath + '/';
				newRemote = newRemote + "/";
				String currentWorkDir = ftpFile.getName().toString();
				//enter relative workdirectory
				boolean changedir = ftpClient.changeWorkingDirectory(currentWorkDir);
				if (changedir) {
					FTPFile[] files = null;
					files = ftpClient.listFiles();
					for (int i = 0; i < files.length; i++) {
						downloadFile(files[i], newlocalRelatePath, newRemote);
					}
				}
				//return parent directory
				if (changedir)
					ftpClient.changeToParentDirectory();
			}
			catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLocalBaseDir() {
		return localBaseDir;
	}

	public void setLocalBaseDir(String localBaseDir) {
		this.localBaseDir = localBaseDir;
	}

	public String getRemoteBaseDir() {
		return remoteBaseDir;
	}

	public void setRemoteBaseDir(String remoteBaseDir) {
		this.remoteBaseDir = remoteBaseDir;
	}
}
