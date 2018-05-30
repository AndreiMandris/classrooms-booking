package com.etti.classroomsbooking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class EventsAdapter {
//    ArrayList<EventDetails> list;
//    private TextView textView;
//
//    public EventsAdapter(@NonNull Context context, int resource, @NonNull List<EventDetails> objects) {
//        super(context, resource, objects);
//        this.list = new ArrayList<EventDetails>();
//        this.list.addAll(objects);
//    }
//    ViewHolder v = new ViewHolder();
//    public void setcheckbox() {
//
//        Log.d("viewholser" + v, "checkbox" + v.selected);
//
//        v.selected.setVisibility(View.VISIBLE);
//    }
//
//    public class ViewHolder {
//        public TextView intervalString;
//        public TextView userEmail;
//        public CheckBox selected;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
//
//        ViewHolder holder = null;
//        if (convertView == null) {
//
//            LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            convertView = vi.inflate(R.layout.menu_frame_child, null);
//            holder = new ViewHolder();
//
//            holder.intervalString = (TextView) convertView.findViewById(R.id.child_setting_header);
//            holder.selected = (CheckBox) convertView.findViewById(R.id.settings_check);
//
//            //  holder.selected.setVisibility(View.INVISIBLE);
//
//            convertView.setTag(holder);
//
//            //holder.selected.setVisibility(View.INVISIBLE);
//
//            holder.selected.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    CheckBox cb = (CheckBox) v;
//                    SectionsModel _state = (SectionsModel) cb.getTag();
//                    _state.setSelected(cb.isChecked());
//                }
//            });
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        SectionsModel section = list.get(position);
//
//        holder.setting.setText(section.getSection());
//        holder.selected.setChecked(section.isSelected());
//
//        holder.selected.setTag(section);
//
//        return convertView;
//    }
//
//    @Override
//    public int getCount()
//    {
//        return super.getCount();
//    }
//
//    @Override
//    public int getPosition(SectionsModel item) {
//
//        return super.getPosition(item);
//    }

}
