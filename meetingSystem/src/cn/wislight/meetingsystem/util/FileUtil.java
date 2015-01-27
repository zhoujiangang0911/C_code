package cn.wislight.meetingsystem.util;

import java.util.ArrayList;

import cn.wislight.meetingsystem.R;

public class FileUtil {

	private static ArrayList<String> PPT_SUFFIX = new ArrayList<String>();

	static {
		PPT_SUFFIX.add(".ppt");
		PPT_SUFFIX.add(".pptx");
	}

	private static ArrayList<String> AUDIO_SUFFIX = new ArrayList<String>();
	static {
		AUDIO_SUFFIX.add(".rmi");
		AUDIO_SUFFIX.add(".wav");
		AUDIO_SUFFIX.add(".mp1");
		AUDIO_SUFFIX.add(".mp2");
		AUDIO_SUFFIX.add(".mp3");
		AUDIO_SUFFIX.add(".mid");
		AUDIO_SUFFIX.add(".ra");
		AUDIO_SUFFIX.add(".rm");
		AUDIO_SUFFIX.add(".ram");
	}

	private static ArrayList<String> IMAGE_SUFFIX = new ArrayList<String>();
	static {
		IMAGE_SUFFIX.add(".bmp");
		IMAGE_SUFFIX.add(".gif");
		IMAGE_SUFFIX.add(".jpg");
		IMAGE_SUFFIX.add(".jpeg");
		IMAGE_SUFFIX.add(".psd");
		IMAGE_SUFFIX.add(".png");
		IMAGE_SUFFIX.add(".wbmp");
	}

	private static ArrayList<String> VIDEO_SUFFIX = new ArrayList<String>();
	static {
		VIDEO_SUFFIX.add(".avi");
		VIDEO_SUFFIX.add(".mov");
		VIDEO_SUFFIX.add(".wmv");
		VIDEO_SUFFIX.add(".mp4");
		VIDEO_SUFFIX.add(".mpeg");
		VIDEO_SUFFIX.add(".mpg");
		VIDEO_SUFFIX.add(".dat");
		VIDEO_SUFFIX.add(".rm");
		VIDEO_SUFFIX.add(".qt");
		VIDEO_SUFFIX.add(".flv");
		VIDEO_SUFFIX.add(".ts");
	}

	private static ArrayList<String> EXCEL_SUFFIX = new ArrayList<String>();
	static {
		EXCEL_SUFFIX.add(".xls");
		EXCEL_SUFFIX.add(".xlsx");
	}

	private static ArrayList<String> DOC_SUFFIX = new ArrayList<String>();
	static {
		DOC_SUFFIX.add(".doc");
		DOC_SUFFIX.add(".docx");
		DOC_SUFFIX.add(".dosx");
	}
	private static ArrayList<String> PDF_SUFFIX = new ArrayList<String>();
	static {
		PDF_SUFFIX.add(".pdf");
	}
	private static ArrayList<String> TEXT_SUFFIX = new ArrayList<String>();
	static {
		TEXT_SUFFIX.add(".txt");
		TEXT_SUFFIX.add(".ini");
		TEXT_SUFFIX.add(".db");
		TEXT_SUFFIX.add(".xml");
	}

	public static int getFileTypeImageRes(String path) {
		// TODO Auto-generated method stub
		if (path.lastIndexOf(".") < 0) // Don't have the suffix
			return R.drawable.ic_file_unknown;

		String fileSuffix = path.substring(path.lastIndexOf("."));
		if (PPT_SUFFIX.contains(fileSuffix)) {
			return R.drawable.ic_file_ppt;
		} else if (AUDIO_SUFFIX.contains(fileSuffix)) {
			return R.drawable.ic_file_audio;
		} else if (IMAGE_SUFFIX.contains(fileSuffix)) {
			return R.drawable.ic_file_image;
		} else if (VIDEO_SUFFIX.contains(fileSuffix)) {
			return R.drawable.ic_file_video;
		} else if (EXCEL_SUFFIX.contains(fileSuffix)) {
			return R.drawable.ic_file_excel;
		} else if (DOC_SUFFIX.contains(fileSuffix)) {
			return R.drawable.ic_file_doc;
		} else if (PDF_SUFFIX.contains(fileSuffix)) {
			return R.drawable.ic_file_pdf;
		} else if (TEXT_SUFFIX.contains(fileSuffix)) {
			return R.drawable.ic_file_txt;
		}
		return R.drawable.ic_file_unknown;
	}
}
