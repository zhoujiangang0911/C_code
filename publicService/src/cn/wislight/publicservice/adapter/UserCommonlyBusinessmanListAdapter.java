package cn.wislight.publicservice.adapter;

import java.util.List;
import java.util.Map;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.ui.user.UserCommonlyBusinessmanListActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserCommonlyBusinessmanListAdapter  extends BaseAdapter{
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;
	
	public UserCommonlyBusinessmanListAdapter(Context context) {
		this.mcontext=context;
		mInflater=LayoutInflater.from(context);
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
		final int finalPosition = position;
		ViewHolder holder = null;  
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.activity_user_commonlyperson_list_item, null);

		    holder = new ViewHolder();  
            //根据自定义的Item布局加载布局   

			holder.username=(TextView) convertView.findViewById(R.id.username);
			holder.userposition=(TextView) convertView.findViewById(R.id.userposition);
			holder.immessage = (ImageView) convertView.findViewById(R.id.immessage);
			holder.callphone = (ImageView) convertView.findViewById(R.id.callphone);
			
            convertView.setTag(holder);  
			
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		
		holder.username.setText((String)data.get(position).get("username")); 
		holder.userposition.setText("("+(String)data.get(position).get("userposition")+")");
		holder.immessage.setOnClickListener(new OnClickListener(){	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub					
				((UserCommonlyBusinessmanListActivity) mcontext).clickIMMessage(mcontext,finalPosition);
			}					
		});	
		holder.callphone.setOnClickListener(new OnClickListener(){	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub					
				((UserCommonlyBusinessmanListActivity) mcontext).clickCallPhone(mcontext,finalPosition);
			}					
		});	
		
		return convertView;
	}
	
	public void setData(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		data = list;
	}
	
	class ViewHolder  
	{  
		public TextView username;  
	    public TextView userposition;  
	    public ImageView immessage;
	    public ImageView callphone;
	};
} ;