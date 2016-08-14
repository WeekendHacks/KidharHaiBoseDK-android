package weekendhacks.com.kahahaibosedk;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melvin on 8/13/16.
 */
public class ContactAdapter extends ArrayAdapter<User>{

    private ArrayList<User> mUser = new ArrayList<User>();
    private Context mContext;
    private int layoutResourceId;

    private static class ViewHolder {
        TextView name;
        TextView phone;
    }
    public ContactAdapter (Context context, int resource, ArrayList<User> users) {
        super(context,resource, users);
        this.mContext = context;
        this.layoutResourceId = resource;
        this.mUser = users;
    }

    public void setListData(ArrayList<User> mUser){
        this.mUser = mUser;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layoutResourceId, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.userPhone);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(user.getName());
        viewHolder.phone.setText(user.getPhone());
        return convertView;
    }

}
