package cn.wislight.meetingsystem.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.FileUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator 附件列表
 */
public class AttachmentListActivity extends BaseActivity {
	private ListView listView;
	private List<Map<String, Object>> list;
	private LoadingDialog loadingdiag;
	@Override
	public void initView() {
		listView = (ListView) findViewById(R.id.lv_attachment);
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		loadingdiag.show();
		
		String url = "MeetingManage/mobile/getAttachmentList.action?no=";
		url += getIntent().getStringExtra(Constants.ID);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(AttachmentListActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new
				// String(response)+"wangting"+response.toString(), 100).show();
				loadingdiag.hide();
				
				if (response.contains("用户未登陆")){
					Toast.makeText(AttachmentListActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				listView.setVisibility(View.VISIBLE);

				AttachmentListAdapter adapter = new AttachmentListAdapter(
						AttachmentListActivity.this);
				list = new ArrayList<Map<String, Object>>();

				try {
					JSONArray jsonArray = new JSONObject(response)
							.getJSONArray("list");

					Map<String, Object> map;

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						map = new HashMap<String, Object>();
						map.put("mtime", jsonObject.getString("mtime"));
						map.put("url", jsonObject.getString("url"));
						map.put("size", jsonObject.getLong("size"));
						list.add(map);
					}

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(AttachmentListActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
				}
				adapter.setData(list);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String url = MeetingSystemClient.getBASE_URL() + "MeetingManage/" + (String) list.get(arg2)
								.get("url");
						
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(url));
						startActivity(intent);
					}
				});

			}
		});
	}

	@Override
	public void setupView() {
		setContentView(R.layout.attachment_main);
	}
}

class AttachmentListAdapter extends BaseAdapter {
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;

	public AttachmentListAdapter(Context context) {
		this.mcontext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.attachment_list_common_item, null);

			holder = new ViewHolder();
			// 根据自定义的Item布局加载布局
			holder.fileType = (ImageView) convertView
					.findViewById(R.id.img_filetype);
			holder.fileName = (TextView) convertView
					.findViewById(R.id.tv_fileName);
			holder.mtime = (TextView) convertView.findViewById(R.id.tv_mtime);
			holder.size = (TextView) convertView.findViewById(R.id.tv_size);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// ((String)data.get(position).get("summary"));
		holder.mtime.setText((String)data.get(position).get("mtime"));
		
		long size = (Long)data.get(position).get("size");
		String sizeSuffix = "Byte";
		if (size > 1024){
			size /= 1024;
			sizeSuffix = "KB";
		} 
		if (size > 1024){
			size /= 1024;
			sizeSuffix = "MB";
		}
		holder.size.setText(size + sizeSuffix);
		
		String url = (String)data.get(position).get("url");
		holder.fileName.setText(url.substring(url.lastIndexOf("/") + 1));
		holder.fileType.setImageResource(FileUtil.getFileTypeImageRes(url));
		return convertView;
	}

	public void setData(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		data = list;
	}

	class ViewHolder {
		public ImageView fileType;
		public TextView fileName;
		public TextView mtime;
		public TextView size;
	};
	
};
