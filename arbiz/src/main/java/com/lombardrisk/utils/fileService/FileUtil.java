package com.lombardrisk.utils.fileService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Leo Tu on 7/27/15.
 */
public class FileUtil extends FileUtils
{
	public static int BUFFER_SIZE = 2048;

	public static void ZipFiles(ArrayList<String> fileNames, String zipfile)
	{
		byte[] buf = new byte[1024];
		try
		{
			FileOutputStream fos = new FileOutputStream(zipfile);
			ZipOutputStream out = new ZipOutputStream(fos);
			for (String fileNAME : fileNames)
			{
				FileInputStream in = new FileInputStream(new File(fileNAME));
				out.putNextEntry(new ZipEntry(fileNAME));
				int len;
				while ((len = in.read(buf)) > 0)
				{
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
			fos.close();
		}
		catch (IOException e)
		{
			// e.printStackTrace();
		}
	}

	private static List<String> unTar(InputStream inputStream, String destDir) throws Exception
	{
		List<String> fileNames = new ArrayList<String>();
		TarArchiveInputStream tarIn = new TarArchiveInputStream(inputStream, BUFFER_SIZE);
		TarArchiveEntry entry = null;
		try
		{
			while ((entry = tarIn.getNextTarEntry()) != null)
			{
				fileNames.add(entry.getName());
				if (entry.isDirectory())
				{
					createDirectory(destDir, entry.getName());
				}
				else
				{
					File tmpFile = new File(destDir + File.separator + entry.getName());
					createDirectory(tmpFile.getParent() + File.separator, null);
					OutputStream out = null;
					try
					{
						out = new FileOutputStream(tmpFile);
						int length = 0;
						byte[] b = new byte[2048];
						while ((length = tarIn.read(b)) != -1)
						{
							out.write(b, 0, length);
						}
					}
					finally
					{
						IOUtils.closeQuietly(out);
					}
				}
			}
		}
		catch (Exception e)
		{
			// e.printStackTrace();
			throw e;
		}
		finally
		{
			IOUtils.closeQuietly(tarIn);
		}

		return fileNames;
	}

	public static List<String> unTar(String tarFile, String destDir) throws Exception
	{
		File file = new File(tarFile);
		return unTar(file, destDir);
	}

	public static List<String> unTar(File tarFile, String destDir) throws Exception
	{
		if (StringUtils.isBlank(destDir))
		{
			destDir = tarFile.getParent();
		}
		destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
		return unTar(new FileInputStream(tarFile), destDir);
	}

	public static List<String> unTarBZip2(File tarFile, String destDir) throws Exception
	{
		if (StringUtils.isBlank(destDir))
		{
			destDir = tarFile.getParent();
		}
		destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
		return unTar(new BZip2CompressorInputStream(new FileInputStream(tarFile)), destDir);
	}

	public static List<String> unTarBZip2(String file, String destDir) throws Exception
	{
		File tarFile = new File(file);
		return unTarBZip2(tarFile, destDir);
	}

	public static List<String> unBZip2(String bzip2File, String destDir) throws IOException
	{
		File file = new File(bzip2File);
		return unBZip2(file, destDir);
	}

	public static List<String> unBZip2(File srcFile, String destDir) throws IOException
	{
		if (StringUtils.isBlank(destDir))
		{
			destDir = srcFile.getParent();
		}
		destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
		List<String> fileNames = new ArrayList<String>();
		InputStream is = null;
		OutputStream os = null;
		try
		{
			File destFile = new File(destDir, FilenameUtils.getBaseName(srcFile.toString()));
			fileNames.add(FilenameUtils.getBaseName(srcFile.toString()));
			is = new BZip2CompressorInputStream(new BufferedInputStream(new FileInputStream(srcFile), BUFFER_SIZE));
			os = new BufferedOutputStream(new FileOutputStream(destFile), BUFFER_SIZE);
			IOUtils.copy(is, os);
		}
		finally
		{
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(is);
		}
		return fileNames;
	}

	public static List<String> unGZ(String gzFile, String destDir) throws IOException
	{
		File file = new File(gzFile);
		return unGZ(file, destDir);
	}

	public static List<String> unGZ(File srcFile, String destDir) throws IOException
	{
		if (StringUtils.isBlank(destDir))
		{
			destDir = srcFile.getParent();
		}
		destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
		List<String> fileNames = new ArrayList<String>();
		InputStream is = null;
		OutputStream os = null;
		try
		{
			File destFile = new File(destDir, FilenameUtils.getBaseName(srcFile.toString()));
			fileNames.add(FilenameUtils.getBaseName(srcFile.toString()));
			is = new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(srcFile), BUFFER_SIZE));
			os = new BufferedOutputStream(new FileOutputStream(destFile), BUFFER_SIZE);
			IOUtils.copy(is, os);
		}
		finally
		{
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(is);
		}
		return fileNames;
	}

	public static List<String> unTarGZ(File tarFile, String destDir) throws Exception
	{
		if (StringUtils.isBlank(destDir))
		{
			destDir = tarFile.getParent();
		}
		destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
		return unTar(new GzipCompressorInputStream(new FileInputStream(tarFile)), destDir);
	}

	public static List<String> unTarGZ(String file, String destDir) throws Exception
	{
		File tarFile = new File(file);
		return unTarGZ(tarFile, destDir);
	}

	public static void createDirectory(String outputDir, String subDir)
	{
		File file = new File(outputDir);
		if (!(subDir == null || subDir.trim().equals("")))
		{
			file = new File(outputDir + File.separator + subDir);
		}
		if (!file.exists())
		{
			file.mkdirs();
		}
	}

	public static List<String> unZip(String zipfilePath, String destDir) throws Exception
	{
		File zipFile = new File(zipfilePath);
		if (destDir == null || destDir.equals(""))
		{
			destDir = zipFile.getParent();
		}
		destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
		ZipArchiveInputStream is = null;
		List<String> fileNames = new ArrayList<String>();

		try
		{
			is = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipfilePath), BUFFER_SIZE));
			ZipArchiveEntry entry = null;
			while ((entry = is.getNextZipEntry()) != null)
			{
				fileNames.add(entry.getName());
				if (entry.isDirectory())
				{
					File directory = new File(destDir, entry.getName());
					directory.mkdirs();
				}
				else
				{
					OutputStream os = null;
					try
					{
						os = new BufferedOutputStream(new FileOutputStream(new File(destDir, entry.getName())), BUFFER_SIZE);
						IOUtils.copy(is, os);
					}
					finally
					{
						IOUtils.closeQuietly(os);
					}
				}
			}
		}
		catch (Exception e)
		{
			// e.printStackTrace();
			throw e;
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}

		return fileNames;
	}

	public static List<String> unWar(String warPath, String destDir)
	{
		List<String> fileNames = new ArrayList<String>();
		File warFile = new File(warPath);
		try
		{
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(warFile));
			ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, bufferedInputStream);

			JarArchiveEntry entry = null;
			while ((entry = (JarArchiveEntry) in.getNextEntry()) != null)
			{
				fileNames.add(entry.getName());
				if (entry.isDirectory())
				{
					new File(destDir, entry.getName()).mkdir();
				}
				else
				{
					OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(destDir, entry.getName())), BUFFER_SIZE);
					IOUtils.copy(in, out);
					out.close();
				}
			}
			in.close();
		}
		catch (Exception e)
		{
			// e.printStackTrace();
		}

		return fileNames;
	}

	public static List<String> unCompress(String compressFile, String destDir) throws Exception
	{
		String upperName = compressFile.toUpperCase();
		List<String> ret = null;
		if (upperName.endsWith(".ZIP"))
		{
			ret = unZip(compressFile, destDir);
		}
		else if (upperName.endsWith(".TAR"))
		{
			ret = unTar(compressFile, destDir);
		}
		else if (upperName.endsWith(".TAR.BZ2"))
		{
			ret = unTarBZip2(compressFile, destDir);
		}
		else if (upperName.endsWith(".BZ2"))
		{
			ret = unBZip2(compressFile, destDir);
		}
		else if (upperName.endsWith(".TAR.GZ"))
		{
			ret = unTarGZ(compressFile, destDir);
		}
		else if (upperName.endsWith(".GZ"))
		{
			ret = unGZ(compressFile, destDir);
		}
		else if (upperName.endsWith(".WAR"))
		{
			ret = unWar(compressFile, destDir);
		}
		return ret;
	}

}
