package com.pjct.ddb;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pjct.ddb.Entities.Enums.PackStatus;
import com.pjct.ddb.Entities.Parcel;

import java.util.Calendar;
import java.util.List;

import static android.view.View.VISIBLE;



public class StudentsRecycleViewAdapter extends RecyclerView.Adapter<StudentsRecycleViewAdapter.StudentViewHolder> {


    private Context baseContext;
    List<Parcel> parcels;

    public StudentsRecycleViewAdapter(Context baseContext, List<Parcel> parcels) {
        this.parcels = parcels;
        this.baseContext = baseContext;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(baseContext.getApplicationContext()).inflate(R.layout.parcel_item,
                parent,
                false);

        return new StudentViewHolder(v);
    }

    @Override
    public void onViewRecycled(@NonNull StudentViewHolder holder) {
        super.onViewRecycled(holder);
        holder.linnerLayoutParcel.setVisibility(View.GONE);
        holder.linearLayoutDeliverymanPhone.setVisibility(View.GONE);
        holder.linearLayoutDate.setVisibility(View.GONE);

    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {
        long time= System.currentTimeMillis();
        Parcel student = parcels.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(student.getDateSend());
        String date = helperDateAndHour(calendar.get(calendar.DAY_OF_MONTH))  + "/" + helperDateAndHour(calendar.get(Calendar.MONTH) +1 ) + "/" + calendar.get(Calendar.YEAR);
        String hour = helperDateAndHour(calendar.get(Calendar.HOUR))  + ":" + helperDateAndHour(calendar.get(Calendar.MINUTE));
        holder.dateSend.setText(date);
        holder.packType.setText(Parcel.PackTypeTosString(student.getPackType()));
        holder.packageWeight.setText(Parcel.packageWeightTosString(student.getPackageWeight()));
        holder.hourSend.setText(hour);
        holder.stateParcel.setText(Parcel.packStatusTosString(student.getPackStatus()));
        holder.phoner.setText(student.getReceiver_phone());
        holder.textViewDeliverymanPhone.setText(student.getDeliveryman_phone());
        PackStatus status = holder.packStatus;

        if(student.getDateReceived() != null){

            calendar = Calendar.getInstance();
            calendar.setTime(student.getDateReceived());
            date = helperDateAndHour(calendar.get(calendar.DAY_OF_MONTH))  + "/" + helperDateAndHour(calendar.get(Calendar.MONTH) +1 ) + "/" + calendar.get(Calendar.YEAR);
            hour = helperDateAndHour(calendar.get(Calendar.HOUR))  + ":" + helperDateAndHour(calendar.get(Calendar.MINUTE));
            holder.textViewDateReceived.setText(date);
            holder.textViewHourReceived.setText(hour);
        }



        if (student.isBreakable() == true) {

            holder.textViewBreakableParcel.setText("Yes.");
        } else {
            holder.textViewBreakableParcel.setText("No.");
        }
        if (student.getPackStatus() == PackStatus.SENT) {
            holder.imageViewStateParcel.setImageResource(R.drawable.sent_image);
        }
        if (student.getPackStatus() == PackStatus.OFFER_FOR_SHIPPING) {
            holder.imageViewStateParcel.setImageResource(R.drawable.offer_for_shipping);
        }
        if (student.getPackStatus() == PackStatus.IN_THE_WHY) {
            holder.imageViewStateParcel.setImageResource(R.drawable.in_the_why);
            holder.linearLayoutDeliverymanPhone.setVisibility(VISIBLE);
        }
        if (student.getPackStatus() == PackStatus.RECEIVED) {
            holder.imageViewStateParcel.setImageResource(R.drawable.received);
            holder.linearLayoutDeliverymanPhone.setVisibility(VISIBLE);
            holder.linearLayoutDate.setVisibility(VISIBLE);

        }

    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder{

        TextView dateSend;
        TextView packType;
        TextView hourSend;
        TextView breakable;
        TextView packageWeight;
        ImageView imageViewStateParcel;
        TextView stateParcel;
        Location location;
        String receiver_phone;
        PackStatus packStatus;
        String deliveryman_phone;
        TextView textViewDateReceived;
        TextView textViewHourReceived;
        TextView textViewBreakableParcel;
        final TextView phoner;
        final LinearLayout linearLayoutDate;
        final LinearLayout linnerLayoutParcel;
        final LinearLayout linearLayoutDeliverymanPhone;
        TextView textViewDeliverymanPhone;

        StudentViewHolder(final View itemView) {
            super(itemView);
             textViewDateReceived = itemView.findViewById(R.id.dateReceived);
            textViewHourReceived = itemView.findViewById(R.id.hourReceived);
            linearLayoutDate = itemView.findViewById(R.id.linnerDateReceived);
            dateSend = itemView.findViewById(R.id.ParcelDateSend);
            packType = itemView.findViewById(R.id.ParcelType);
            packageWeight = itemView.findViewById(R.id.ParcelWeight);
            hourSend = itemView.findViewById(R.id.ParcelDateSendTime);
            imageViewStateParcel = itemView.findViewById(R.id.imageStateParcel);
            stateParcel = itemView.findViewById(R.id.StateParcel);
            textViewBreakableParcel = itemView.findViewById(R.id.breakable_parcel);
            linnerLayoutParcel = itemView.findViewById(R.id.LinnerParcel);
            phoner = itemView.findViewById(R.id.phoner);
            textViewDeliverymanPhone = itemView.findViewById(R.id.phoned);
            linearLayoutDeliverymanPhone = itemView.findViewById(R.id.dphone);
            itemView.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                       linnerLayoutParcel.setVisibility(View.GONE);
                }
            });

            itemView.findViewById(R.id.shareButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    String text = "Parcel info:";
                    text =  text + "\n \n";
                    text =  text + "\n";
                    Parcel parcel = parcels.get(getAdapterPosition());
                    text = text + "Parcel type: " +Parcel.PackTypeTosString(parcel.getPackType()) + ".";
                    text =  text + "\n";
                    text =  text + "Parcel status: "+ Parcel.packStatusTosString(parcel.getPackStatus())+ ".";
                    text =  text + "\n";
                    text =  text + "Parcel Weight: " + Parcel.packageWeightTosString(parcel.getPackageWeight());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(parcel.getDateSend());
                    String date = helperDateAndHour(calendar.get(calendar.DAY_OF_MONTH))  + "/" + helperDateAndHour(calendar.get(Calendar.MONTH) +1 ) + "/" + calendar.get(Calendar.YEAR);
                    String hour = helperDateAndHour(calendar.get(Calendar.HOUR))  + ":" + helperDateAndHour(calendar.get(Calendar.MINUTE));
                    text =  text + "\n";
                    text =  text + "Shipping date: " + date + "      " + hour;
                    text =  text + "\n";
                    text =  text + "Receiver phone: " + parcel.getReceiver_phone();
                    text =  text + "\n";
                    if (parcel.getPackStatus() == PackStatus.IN_THE_WHY ||parcel.getPackStatus() == PackStatus.RECEIVED) {
                        text =  text + "Deliveryman phone: " + parcel.getDeliveryman_phone();
                        text =  text + "\n";

                    }
                    if(parcel.getPackStatus() == PackStatus.RECEIVED && parcel.getDateReceived() != null){


                        calendar.setTime(parcel.getDateReceived());
                        date = helperDateAndHour(calendar.get(calendar.DAY_OF_MONTH))  + "/" + helperDateAndHour(calendar.get(Calendar.MONTH) +1 ) + "/" + calendar.get(Calendar.YEAR);
                        hour = helperDateAndHour(calendar.get(Calendar.HOUR))  + ":" + helperDateAndHour(calendar.get(Calendar.MINUTE));

                        text =  text + "Date Received: " + date + "      " + hour;
                        text =  text + "\n";
                    }


                    sendIntent.putExtra(Intent.EXTRA_TEXT, text);

                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    baseContext.startActivity(shareIntent);
                }
            });
            itemView.findViewById(R.id.callrphone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phoner.getText().toString()));
                    baseContext.startActivity(intent);
                }
            });

            itemView.findViewById(R.id.calldphone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + textViewDeliverymanPhone.getText().toString()));
                    baseContext.startActivity(intent);






    }
});
itemView.findViewById(R.id.copyrphone).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        setClipboard(baseContext, phoner.getText().toString());
        Toast.makeText(baseContext.getApplicationContext(),"Receiver phone copy to \"Clip Board\"",Toast.LENGTH_SHORT).show();
    }
});

            itemView.findViewById(R.id.copydphone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setClipboard(baseContext, textViewDeliverymanPhone.getText().toString());
                    Toast.makeText(baseContext.getApplicationContext(),"Deliveryman phone copy to \"Clip Board\"",Toast.LENGTH_SHORT).show();
                }
            });



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    LinearLayout linearLayout;
                    if(v.findViewById(R.id.LinnerParcel).getVisibility() == VISIBLE)
                    {


                    }else{
                        linearLayout =  (LinearLayout)v.findViewById(R.id.LinnerParcel);
                        linearLayout.setVisibility(VISIBLE);

                    }

                }
            });


            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

                @Override
                public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.setHeaderTitle("Select Action");

                    MenuItem delete = menu.add(Menu.NONE, 1, 1, "Close");

                    delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                    v.findViewById(R.id.LinnerParcel).setVisibility(View.GONE);

                            return true;
                        }
                    });
                }
            });
        }
    }







    /****************Functions for help******************/

    public String helperDateAndHour(int d){

        if(d < 10){
            return "0" + d;
        }else {
            return  d + "";
        }
    }

    private void setClipboard(Context context, String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

}