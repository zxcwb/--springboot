package com.zxc.o2o.util;

import com.zxc.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

public class ImageUtil {
	public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
		String realFileName = FileUtil.getRandomFileName();
		//获取文件扩展名
		String extension = getFileExtension(thumbnail.getImageName());
		makeDirPath(targetAddr);
		//数据库保存图片地址
		String relativeAddr = realFileName + extension;
		File dest = new File(FileUtil.getImgBasePath()+targetAddr+ relativeAddr);
		try {
			//of()方法能够接收多种类型的数据  ，没有生成有水印的图片！！！
			Thumbnails.of(thumbnail.getImage()).size(200, 200).outputQuality(0.25f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return targetAddr+ relativeAddr;
	}

	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		String realFileName = FileUtil.getRandomFileName();
		//获取文件扩展名
		String extension = getFileExtension(thumbnail.getImageName());
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(FileUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage()).size(337, 640).outputQuality(0.5f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}

	/*public static List<String> generateNormalImgs(List<CommonsMultipartFile> imgs, String targetAddr) {
		int count = 0;
		List<String> relativeAddrList = new ArrayList<String>();
		if (imgs != null && imgs.size() > 0) {
			makeDirPath(targetAddr);
			for (CommonsMultipartFile img : imgs) {
				String realFileName = FileUtil.getRandomFileName();
				String extension = getFileExtension(img);
				String relativeAddr = targetAddr + realFileName + count + extension;
				File dest = new File(FileUtil.getImgBasePath() + relativeAddr);
				count++;
				try {
					Thumbnails.of(img.getInputStream()).size(600, 300).outputQuality(0.5f).toFile(dest);
				} catch (IOException e) {
					throw new RuntimeException("创建图片失败：" + e.toString());
				}
				relativeAddrList.add(relativeAddr);
			}
		}
		return relativeAddrList;
	}*/

	//更新店铺图片信息 storePath 是文件的路径还是目录的路径
	//是文件的路径则删除该文件
	//是目录路径则删除该目录下的所有文件
	public static void deleteFileOrPath(String storePath){
		File fileOrPath = new File(FileUtil.getImgBasePath()+storePath);
		if (fileOrPath.exists()){
			//判断是目录还是文件
			if (fileOrPath.isDirectory()){
				//遍历文件目录
				File files[] = fileOrPath.listFiles();
				for (int i = 0;i<files.length;i++){
					files[i].delete();
				}
			}
			//是文件就直接删除
			fileOrPath.delete();
		}
	}

	private static void makeDirPath(String fileName) {
		String realFileParentPath = FileUtil.getImgBasePath()+fileName;
		File dirPath = new File(realFileParentPath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
}
