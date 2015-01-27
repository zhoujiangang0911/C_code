package cn.wislight.meetingsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.ui.vote.VoteManagementActivity;

public class VoteManagerAdapter extends BaseAdapter{
		private Context mContext;
		private LayoutInflater mInflater;
		public VoteManagerAdapter(Context context) {
			this.mContext=context;
			mInflater=LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return 5;
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
			if(convertView==null){
				convertView=mInflater.inflate(R.layout.myvote_main_item, null);
			}
			return convertView;
		}
		
	} 