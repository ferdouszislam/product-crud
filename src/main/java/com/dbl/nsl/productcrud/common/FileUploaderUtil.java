package com.dbl.nsl.productcrud.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

public abstract class FileUploaderUtil {
	
	private static final String RESOURCE_DIR = "src/main/resources/static/";
	private static final int V1_FILE_HEIGHT = 300;
	private static final int V1_FILE_WIDTH = 300;
	private static final int V2_FILE_HEIGHT = 50;
	private static final int V2_FILE_WIDTH = 50;
	
	/**
	 * save image file to server and get {@code Photo} object
	 * @param photoLabel any string
	 * @param itemType type of the item the image belongs to
	 * @param itemId id of the item the image belongs to
	 * @param photoFile
	 * @param saveV1 if true, resize and save v1 format of the photoFile
	 * @param saveV1 if true, resize and save v2 format of the photoFile
	 * @return {@code Photo} object for the saved {@code photoFile}
	 * {@code null} if {@code photoFile} is null or empty
	 */
	public static Photo savePhoto(String photoLabel, ItemType itemType, Long itemId, 
			MultipartFile photoFile, boolean saveV1, boolean saveV2) 
					throws IndexOutOfBoundsException, IOException {
		if (Objects.isNull(photoFile) || photoFile.isEmpty()) return null;
		String ppOriginalFileName = saveFile(FileType.IMAGE, itemType, itemId, photoFile);
		String ppV1FileName = saveV1 ? saveResizedImage(ppOriginalFileName, itemType, V1_FILE_WIDTH, V1_FILE_HEIGHT, "v1") : "";
		String ppV2FileName = saveV2 ? saveResizedImage(ppOriginalFileName, itemType, V2_FILE_WIDTH, V2_FILE_HEIGHT, "v2") : "";
		return new Photo(photoLabel, ppOriginalFileName, ppV1FileName, ppV2FileName);
	}
	
	/**
	 * size of a file in KB
	 */
	public static Double getFileSize(MultipartFile file) {
		return (double) file.getSize() / 1024.0D;
	}
	
	/**
	 * resize existing saved file in server to given {@code width} and {@code height}
	 * @param originalFileName file name of the existing image inside "/static/imgs/{@code itemType.getValue()}
	 * or, file path from "/static/imgs/" directory 
	 * @param itemType of the item the image belongs to
	 * @param width in pixel
	 * @param height in pixel
	 * @param vLabel suffix for the resized file name
	 * @return saved file name with path from "/static" directory
	 */
	private static String saveResizedImage(String originalFileName, ItemType itemType, int width, int height, String vLabel)
			throws IOException, IndexOutOfBoundsException {
		try {
			originalFileName = getActualFileNameFromPath(originalFileName);
			String p = System.getProperty("user.dir") + "/" + RESOURCE_DIR + "/" + FileType.IMAGE.getValue() + "/" + itemType.getValue();
			String originalFilePath = p + "/" + originalFileName;
			String resizedFileName = getResizedFileNameWithVersionLabel(originalFileName, vLabel);
			String resizedImageFilePath = p + "/" + resizedFileName;
			Thumbnails.of(originalFilePath).size(width, height).toFile(resizedImageFilePath);
			return FileType.IMAGE.getValue() + "/" + itemType.getValue() + "/" + resizedFileName;
		} catch (IOException | IndexOutOfBoundsException e) {
			throw e;
		}
	}

	private static String getActualFileNameFromPath(String originalFileName) {
		String[] sp = originalFileName.split("/");
		return sp[sp.length-1];
	}

	private static String getResizedFileNameWithVersionLabel(String originalFileName, String vLabel) {
		String[] sp = originalFileName.split("\\.");
		return sp[0] + "_" + vLabel + "." + sp[1];
	}
	
	public static String saveFile(FileType fileType, ItemType itemType, Long itemId, MultipartFile file) 
			throws IOException, IndexOutOfBoundsException {
		String filePathStr = RESOURCE_DIR + "/" + fileType.getValue() + "/" + itemType.getValue() + "/";
		String fileNameWithoutExtension = itemType.getValue() + "_" + itemId.toString() + "_" + System.nanoTime();
		return fileType.getValue() + "/" + itemType.getValue() + "/" + writeFile(filePathStr, fileNameWithoutExtension, file);
	}
	
	private static String writeFile(String filePathStr, String fileNameWithoutExtension, MultipartFile file) 
			throws IOException, IndexOutOfBoundsException {
		try {
			String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			String fileName = fileNameWithoutExtension + fileExtension;
			Path filePath = Paths.get(filePathStr);
			if (!Files.exists(filePath)) Files.createDirectories(filePath);
			Files.copy(file.getInputStream(), filePath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
			return fileName;
		} catch (IOException | IndexOutOfBoundsException e) {
			throw e;
		}
	}
	
	public enum FileType {
		IMAGE ("imgs");
		private final String v;
		private FileType(String v) {
			this.v = v;
		}
		public String getValue() {
			return v;
		}
	}
	
	public enum ItemType {
		PRODUCT_PHOTO("product_photo");
		private final String v;
		private ItemType(String v) {
			this.v = v;
		}
		public String getValue() {
			return v;
		}
	}
}
