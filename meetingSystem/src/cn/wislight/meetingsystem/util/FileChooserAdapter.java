package cn.wislight.meetingsystem.util;

import java.io.File;
import java.util.ArrayList;

import cn.wislight.meetingsystem.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class FileChooserAdapter extends BaseAdapter {

	private ArrayList<FileInfo> mFileLists;
	private LayoutInflater mLayoutInflater = null;

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
	

	
	
	public FileChooserAdapter(Context context, ArrayList<FileInfo> fileLists) {
		super();
		mFileLists = fileLists;
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFileLists.size();
	}

	@Override
	public FileInfo getItem(int position) {
		// TODO Auto-generated method stub
		return mFileLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			view = mLayoutInflater.inflate(R.layout.filechooser_gridview_item,
					null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		FileInfo fileInfo = getItem(position);
        //TODO 
		
		holder.tvFileName.setText(fileInfo.getFileName());
		
		if(fileInfo.isDirectory()){      //文件夹
			holder.imgFileIcon.setImageResource(R.drawable.ic_file_folder);
			holder.tvFileName.setTextColor(Color.GRAY);
		}
		else if(fileInfo.isPPTFile()){   //PPT文件
			holder.imgFileIcon.setImageResource(R.drawable.ic_file_ppt);
			holder.tvFileName.setTextColor(Color.RED);
		}
		else if(fileInfo.isTxtFile()){   //文本类文件
			holder.imgFileIcon.setImageResource(R.drawable.ic_file_txt);
			holder.tvFileName.setTextColor(Color.BLACK);
		}
		else if(fileInfo.isDocFile()){   //文本类文件
			holder.imgFileIcon.setImageResource(R.drawable.ic_file_doc);
			holder.tvFileName.setTextColor(Color.BLACK);
		}
		else if(fileInfo.isExcelFile()){   //文本类文件
			holder.imgFileIcon.setImageResource(R.drawable.ic_file_excel);
			holder.tvFileName.setTextColor(Color.BLACK);
		}
		else if(fileInfo.isPdfFile()){   //文本类文件
			holder.imgFileIcon.setImageResource(R.drawable.ic_file_pdf);
			holder.tvFileName.setTextColor(Color.MAGENTA);
		}
		else if(fileInfo.isImageFile()){   //文本类文件
			holder.imgFileIcon.setImageResource(R.drawable.ic_file_image);
			holder.tvFileName.setTextColor(Color.BLACK);
		}
		else if(fileInfo.isVideoFile()){   //文本类文件
			holder.imgFileIcon.setImageResource(R.drawable.ic_file_video);
			holder.tvFileName.setTextColor(Color.CYAN);
		}
		else if(fileInfo.isAudioFile()){   //文本类文件
			holder.imgFileIcon.setImageResource(R.drawable.ic_file_audio);
			holder.tvFileName.setTextColor(Color.CYAN);
		}
		else {                           //未知文件
			holder.imgFileIcon.setImageResource(R.drawable.ic_file_unknown);
			holder.tvFileName.setTextColor(Color.GRAY);
		}
		return view;
	}

	static class ViewHolder {
		ImageView imgFileIcon;
		TextView tvFileName;

		public ViewHolder(View view) {
			imgFileIcon = (ImageView) view.findViewById(R.id.imgFileIcon);
			tvFileName = (TextView) view.findViewById(R.id.tvFileName);
		}
	}

	
	enum FileType {
		FILE , DIRECTORY;
	}

	// =========================
	// Model
	// =========================
	static class FileInfo {
		private FileType fileType;
		private String fileName;
		private String filePath;

		public FileInfo(String filePath, String fileName, boolean isDirectory) {
			this.filePath = filePath;
			this.fileName = fileName;
			fileType = isDirectory ? FileType.DIRECTORY : FileType.FILE;
		}

		public boolean isPdfFile() {
			if(fileName.lastIndexOf(".") < 0)  //Don't have the suffix 
				return false ;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if(!isDirectory() && PDF_SUFFIX.contains(fileSuffix))
				return true ;
			else
				return false ;
		}

		public boolean isAudioFile() {
			if(fileName.lastIndexOf(".") < 0)  //Don't have the suffix 
				return false ;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if(!isDirectory() && AUDIO_SUFFIX.contains(fileSuffix))
				return true ;
			else
				return false ;
		}

		public boolean isVideoFile() {
			if(fileName.lastIndexOf(".") < 0)  //Don't have the suffix 
				return false ;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if(!isDirectory() && VIDEO_SUFFIX.contains(fileSuffix))
				return true ;
			else
				return false ;
		}

		public boolean isImageFile() {
			if(fileName.lastIndexOf(".") < 0)  //Don't have the suffix 
				return false ;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if(!isDirectory() && IMAGE_SUFFIX.contains(fileSuffix))
				return true ;
			else
				return false ;
		}

		public boolean isExcelFile() {
			if(fileName.lastIndexOf(".") < 0)  //Don't have the suffix 
				return false ;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if(!isDirectory() && EXCEL_SUFFIX.contains(fileSuffix))
				return true ;
			else
				return false ;
		}

		public boolean isDocFile() {
			if(fileName.lastIndexOf(".") < 0)  //Don't have the suffix 
				return false ;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if(!isDirectory() && DOC_SUFFIX.contains(fileSuffix))
				return true ;
			else
				return false ;
		}

		public boolean isTxtFile() {
			if(fileName.lastIndexOf(".") < 0)  //Don't have the suffix 
				return false ;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if(!isDirectory() && TEXT_SUFFIX.contains(fileSuffix))
				return true ;
			else
				return false ;
		}

		public boolean isPPTFile(){
			if(fileName.lastIndexOf(".") < 0)  //Don't have the suffix 
				return false ;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if(!isDirectory() && PPT_SUFFIX.contains(fileSuffix))
				return true ;
			else
				return false ;
		}
  
		public boolean isDirectory(){
			if(fileType == FileType.DIRECTORY)
				return true ;
			else
				return false ;
		}
		
		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		@Override
		public String toString() {
			return "FileInfo [fileType=" + fileType + ", fileName=" + fileName
					+ ", filePath=" + filePath + "]";
		}
	}

}
