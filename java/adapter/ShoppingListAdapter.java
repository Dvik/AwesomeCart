
package com.transferret.whizzbuy.adapters;


import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.transferret.whizzbuy.R;
import com.transferret.whizzbuy.model.Tasks;

import java.util.ArrayList;

/**
 * Created by Divya on 12/11/2015.
 */

public class ShoppingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_TITLE_ITEM = 1;
    private static final int TYPE_TITLE_ADD = 2;
    private static final int TYPE_SEPERATOR = 3;
    private static final int TYPE_TICKED = 4;
    private static final int TYPE_TICKED_LIST = 5;



    private ArrayList<Tasks> titleList;
    private ArrayList<Tasks> tickedList;
    private ArrayList<Tasks> titleTextList;


    Context context;

    Title titleH;
    TitleItem titleHolder;
    TickedItem tickedHolder;

    public ShoppingListAdapter(Context context, ArrayList<Tasks> titleList, ArrayList<Tasks> tickedList, ArrayList<Tasks> titleTextList) {
        this.context = context;
        this.titleList = titleList;
        this.tickedList = tickedList;
        this.titleTextList = titleTextList;
    }

    public class Title extends RecyclerView.ViewHolder {
        EditText tv;
        public Title(View itemView) {
            super(itemView);
            tv = (EditText) itemView.findViewById(R.id.tv_title);

            tv.addTextChangedListener(new TextWatcher() {

                String selectedText;

                @Override
                public void onTextChanged(CharSequence s, int st, int b, int c) {


                }

                @Override
                public void beforeTextChanged(CharSequence s, int st, int c, int a) {


                }

                @Override
                public void afterTextChanged(Editable s) {

                    selectedText = tv.getText().toString();
                    titleTextList.get(0).name = selectedText;

                }
            });

        }
    }

    public class TitleItem extends RecyclerView.ViewHolder{
        ImageButton img;
        CheckBox chb;
        EditText edt;


        public TitleItem(View itemView) {
            super(itemView);
            img = (ImageButton) itemView.findViewById(R.id.delete);
            chb = (CheckBox) itemView.findViewById(R.id.checkBox);
            edt = (EditText) itemView.findViewById(R.id.textView);

        }

    }

    public class AddTitle extends RecyclerView.ViewHolder{

        public AddTitle(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    titleList.add(new Tasks("", false));

                    notifyItemInserted(titleList.size() + 1);
                    notifyItemRangeChanged(titleList.size(), titleList.size() + 1);
                    Log.d("addItem", titleList.toString());
                    Log.d("addItemAdapter", String.valueOf(getItemCount()));

                }
            });

        }
    }
    public class Seperator extends RecyclerView.ViewHolder{

        public Seperator(View itemView) {
            super(itemView);

        }
    }
    public class Ticked extends RecyclerView.ViewHolder{

        public Ticked(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
    public class TickedItem extends RecyclerView.ViewHolder{

        TextView edt;
        CheckBox chb;
        ImageButton img;
        public TickedItem(View itemView) {
            super(itemView);
            edt = (TextView) itemView.findViewById(R.id.textView);
            edt.setPaintFlags(edt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            chb = (CheckBox) itemView.findViewById(R.id.checkBox);
            img = (ImageButton) itemView.findViewById(R.id.delete);

        }
    }

    public void remove(int position) {

        titleList.remove(position - 1);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, titleList.size() - 1);
    }
    public void removeTicked(int position) {

        tickedList.remove(position - 4 - titleList.size());

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, tickedList.size() - 1);
    }
/*
    public void updateList(ArrayList<Tasks> data) {
        userList = data;
        notifyDataSetChanged();
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_TITLE)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.title_row, parent, false);
            return  new Title((LinearLayout)v);
        }
        else if(viewType == TYPE_TITLE_ITEM)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_keep, parent, false);
            return new TitleItem(v);
        } else if(viewType == TYPE_SEPERATOR)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.seperator, parent, false);
            return new Seperator(v);

        }  else if(viewType == TYPE_TICKED)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticked_row, parent, false);
            return new Ticked(v);

        } else if(viewType == TYPE_TICKED_LIST)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticked_item_row, parent, false);
            return new TickedItem(v);
        } else if(viewType == TYPE_TITLE_ADD)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_layout, parent, false);
            return new AddTitle(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private Tasks getTitleItem(int position)
    {
        return titleList.get(position);
    }

    private Tasks getTickedItem(int position)
    {
        return tickedList.get(position);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof  Title)
        {
            titleH = (Title)holder;

            if(!titleTextList.isEmpty())
            titleH.tv.setText(titleTextList.get(0).name);
            else
                titleH.tv.setText("");

        }

        if(holder instanceof TitleItem)
        {
            titleHolder = (TitleItem)holder;

            Tasks t = titleList.get(position - 1);

            titleHolder.edt.requestFocus();
            titleHolder.edt.setText(t.name);
            titleHolder.chb.setChecked(false);


            titleHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(holder.getAdapterPosition());
                }
            });

            if(titleHolder.chb.isChecked()==false) {

                titleHolder.chb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(isChecked) {


                            Log.d("Item added to 2",titleList.get(holder.getAdapterPosition()-1).name);

                                tickedList.add(new Tasks(titleList.get(holder.getAdapterPosition()-1).name, true));


                                notifyItemInserted(tickedList.size() + titleList.size() + 3);
                               // notifyItemRangeChanged(tickedList.size() + tickedList.size() + 2, tickedList.size());

                                remove(holder.getAdapterPosition());

                        }

                    }
                });
            }
            //titleHolder.edt.setText(t.name);

            titleHolder.edt.addTextChangedListener(new TextWatcher() {

                String selectedText;

                @Override
                public void onTextChanged(CharSequence s, int st, int b, int c) {


                }

                @Override
                public void beforeTextChanged(CharSequence s, int st, int c, int a) {


                }

                @Override
                public void afterTextChanged(Editable s) {

                    selectedText = titleHolder.edt.getText().toString();
                    titleList.get(holder.getAdapterPosition()-1).name = selectedText;

                    }
            });
        }

        if(holder instanceof TickedItem)
        {

            Tasks r = tickedList.get(holder.getAdapterPosition()- 4 - titleList.size());
            tickedHolder = (TickedItem)holder;
            Log.d("Rname",r.name);
            tickedHolder.edt.setText(r.name);
            tickedHolder.chb.setChecked(r.isChecked);


            tickedHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeTicked(holder.getAdapterPosition());
                }
            });

            if(tickedHolder.chb.isChecked()==true) {
                tickedHolder.chb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(!isChecked) {

                            Log.d("Item added to 1",tickedList.get(holder.getAdapterPosition()- 4 - titleList.size()).name);
                            titleList.add(new Tasks(tickedList.get(holder.getAdapterPosition()- 4 - titleList.size()).name, false));

                            notifyItemInserted(titleList.size() + 1);

                            removeTicked(holder.getAdapterPosition());

                        }

                    }
                });
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(isPositionTitle(position))
            return TYPE_TITLE;
        if(isPositionTitleList(position))
            return TYPE_TITLE_ITEM;
        if(isPositionAdd(position))
            return TYPE_TITLE_ADD;
        if(isPositionSeperator(position))
            return TYPE_SEPERATOR;
        if(isPositionTicked(position))
            return TYPE_TICKED;
        if(isPositionTickedList(position))
            return TYPE_TICKED_LIST;

        return 0;
    }

    private boolean isPositionTitle(int position)
    {
        return position == 0;
    }

    private boolean isPositionTitleList(int position)
    {
        if(position>=1 && position<=titleList.size()) {
            return true;
        }
        return false;
    }

    private boolean isPositionAdd(int position)
    {
        if(position == titleList.size()+1)
        return true;
        return false;
    }

    private boolean isPositionSeperator(int position)
    {
        if(position == titleList.size()+2)
            return true;
        return false;
    }

    private boolean isPositionTicked(int position)
    {
        if(position == titleList.size()+3)
            return true;
        return false;
    }

    private boolean isPositionTickedList(int position)
    {
        if(position >= titleList.size()+4 && position < (tickedList.size()+titleList.size()+4) ) {
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return tickedList.size()+titleList.size()+4;
    }

}
