package com.pp.main;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
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

public class ZipperMain {
	private static final String REPO_PATH = Dotenv.configure().load().get("REPO_PATH");
    private static final String USERNAME = Dotenv.configure().load().get("USERNAME");
    private static final String PASSWORD = Dotenv.configure().load().get("PASSWORD");

	public static void main(String[] args) {
		try {
			gitPull();

			// Step 2: Zip subfolders
			File repoFolder = new File(REPO_PATH);
			File[] subfolders = repoFolder.listFiles(File::isDirectory);

			if (subfolders != null) {
				for (File subfolder : subfolders) {
					zipSubfolder(subfolder);
				}

				// Step 3: Commit and push changes
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
			e.printStackTrace();
		}
	}

	private static void zipSubfolder(File subfolder) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String zipFileName = subfolder.getName() + "_" + sdf.format(new Date()) + ".zip";

			File zipFile = new File(subfolder.getParent(), zipFileName);

			// Check if the zip file already exists
			if (zipFile.exists()) {
				// Delete the existing zip file
				if (!zipFile.delete()) {
					DoPrint.logInfo("Failed to delete existing zip file: " + zipFile.getName());
					return;
				}
			}

			// Create a ZipOutputStream for the subfolder
			try (FileOutputStream fos = new FileOutputStream(zipFile); ZipOutputStream zos = new ZipOutputStream(fos)) {

				// Add each file in the subfolder to the zip file
				File[] files = subfolder.listFiles();
				if (files != null) {
					for (File file : files) {
						addToZip(file, zos, "");
					}
				}
			}

			DoPrint.logInfo("Zipped: " + subfolder.getName());
		} catch (IOException e) {
			e.printStackTrace();
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
			// Add changes
			git.add().addFilepattern(".").call();

			// Commit changes
			git.commit().setMessage("Updated subfolders").call();

			// Push changes
			git.push().call();

			DoPrint.logInfo("Git commit and push completed.");
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		}
	}
}
