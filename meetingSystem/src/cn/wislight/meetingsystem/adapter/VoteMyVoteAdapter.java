package cn.wislight.meetingsystem.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.wislight.meetingsystem.R;




public class VoteMyVoteAdapter extends BaseAdapter{
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;
	private int [] voteResultText = {
			R.string.vote_result_quote,
			R.string.vote_result_agree,
			R.string.vote_result_unagree,
	};
	private int [] voteResultImg = {
			R.drawable.vote_btn_quote,
			R.drawable.vote_btn_agree,
			R.drawable.vote_btn_unagree
	};
	public VoteMyVoteAdapter(Context context) {
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
		ViewHolder holder = null;  
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.vote_my_vote_list_item, null);

		    holder = new ViewHolder();  
            //根据自定义的Item布局加载布局   
            holder.summary = (TextView)convertView.findViewById(R.id.tv_summary);  
            holder.keywords = (TextView)convertView.findViewById(R.id.tv_keywords);  
            holder.time = (TextView)convertView.findViewById(R.id.tv_time);
            holder.tvstate = (TextView)convertView.findViewById(R.id.tv_vote_state);
            holder.imgstate = (ImageView)convertView.findViewById(R.id.img_vote_state );
            
            convertView.setTag(holder);  
			
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		
		 holder.summary.setText((String)data.get(position).get("summary"));  
         holder.keywords.setText((String)data.get(position).get("keywords"));  
         holder.time.setText((String)data.get(position).get("starttime") + " -- "
        		 		+  (String)data.get(position).get("endtime"));  
         
         int voteResult = Integer.parseInt((String)data.get(position).get("voteresult"));
         if (voteResult >= 0 && voteResult < 3){
        	 holder.tvstate.setText(voteResultText[voteResult]);
        	 holder.imgstate.setImageResource(voteResultImg[voteResult]);
         }
		
		return convertView;
	}
	
	public void setData(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		data = list;
	}
	
	class ViewHolder  
	{  
	    public TextView summary;  
	    public TextView keywords;  
	    public TextView time;  
	    public TextView tvstate;
	    public ImageView imgstate;
	};
} ;