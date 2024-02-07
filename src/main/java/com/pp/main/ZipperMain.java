package com.pp.main;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.pp.util.DoPrint;
import com.pp.util.Prop;

import io.github.cdimascio.dotenv.Dotenv;


/**
 * @author umair.ali
 * @version 1.0
 * 
 * Main class Will Contain And Perform Business Logic (ZipperMain.class) 
 */
public class ZipperMain {

	/**
	 * @see README.md File For Configuration
	 */
	private static final Dotenv dotenv = Dotenv.configure().filename(Prop.getProperty("env.path")).load();

	private static final String REPO_PATH = dotenv.get("REPO_PATH");
	private static final String USERNAME = dotenv.get("USERNAME");
	private static final String PASSWORD = dotenv.get("PASSWORD");

	public static void main(String[] args) {
		try {
			gitPull();

			File repoFolder = new File(REPO_PATH + "/pens");
			File[] subfolders = repoFolder.listFiles(File::isDirectory);

			if (subfolders != null) {
				for (File subfolder : subfolders) {
					zipSubfolder(subfolder);
				}

				commitAndPush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void gitPull() throws GitAPIException {
		try (Git git = Git.open(new File(REPO_PATH))) {
			CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(USERNAME, PASSWORD);
			PullCommand pull = git.pull().setCredentialsProvider(credentialsProvider);
			pull.call();
			DoPrint.logInfo("Git pull successful.");
		} catch (IOException e) {
			DoPrint.logException("PULL EXCEPTION", e);
		}
	}

	private static void zipSubfolder(File subfolder) {
		try {
			String zipFileName = subfolder.getName() + ".zip";

			File zipFile = new File(REPO_PATH + "/zipper-files", zipFileName);

			// Check if the zip file already exists
			if (zipFile.exists()) {
				DoPrint.logInfo("Zip Exists Deleting: " + zipFileName);
				if (!zipFile.delete()) {
					DoPrint.logInfo("Failed to delete existing zip file: " + zipFile.getName());
					return;
				}
			}

			try (FileOutputStream fos = new FileOutputStream(zipFile); ZipOutputStream zos = new ZipOutputStream(fos)) {
				File[] files = subfolder.listFiles();
				if (files != null) {
					for (File file : files) {
						addToZip(file, zos, "");
					}
				}
			}

			DoPrint.logInfo("Zipped: " + subfolder.getName());
		} catch (IOException e) {
			DoPrint.logException("ZIPPING EXCEPTION FOLDER:@@" + subfolder.getName() + "@@", e);
		}
	}

	private static void addToZip(File file, ZipOutputStream zos, String parentFolder) throws IOException {
		if (file.isDirectory()) {
			// Recursively add files in the subdirectory
			String entryName = parentFolder + file.getName() + "/";
			zos.putNextEntry(new ZipEntry(entryName));

			File[] files = file.listFiles();
			if (files != null) {
				for (File subFile : files) {
					addToZip(subFile, zos, entryName);
				}
			}
		} else {
			// Add a file to the zip
			String entryName = parentFolder + file.getName();
			zos.putNextEntry(new ZipEntry(entryName));

			try (FileInputStream fis = new FileInputStream(file)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = fis.read(buffer)) != -1) {
					zos.write(buffer, 0, bytesRead);
				}
			}
		}
	}

	private static void commitAndPush() {
		try (Git git = Git.open(new File(REPO_PATH))) {
			git.add().addFilepattern(".").call();
			git.commit().setMessage("Updated Zip Files By Cron Schedular").call();
			CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(USERNAME, PASSWORD);
			git.push().setCredentialsProvider(credentialsProvider).call();
			DoPrint.logInfo("Git commit and push completed.");
		} catch (IOException | GitAPIException e) {
			DoPrint.logException("COMMIT AND PUSH EXCEPTION", e);
		}
	}
}
