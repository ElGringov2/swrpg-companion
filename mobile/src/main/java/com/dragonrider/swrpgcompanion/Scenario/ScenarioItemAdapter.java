package com.dragonrider.swrpgcompanion.Scenario;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.R;

import java.util.Date;
import java.util.List;

public class ScenarioItemAdapter extends RecyclerView.Adapter {


    public String ScenarioName;




    private List<ScenarioItem> Items;

    private Context context;
    private Date mDate;


    public ScenarioItemAdapter(List<ScenarioItem> items, final Context context) {
        Items = items;
        this.context = context;

        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                if (mDate != null) {

                    for (ScenarioItem item : Items)
                        if (!item.getNotification()) {
                            long lNow = new Date().getTime();
                            long lLimit = mDate.getTime() + (item.getDuration() * 1000 * 60 * 60);


                            if (lLimit <= lNow)
                            {
                                item.setNotification(true);


                                //Wear notification

                                int notificationId = Items.indexOf(item) + 1000;


                                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                                        .setBackground(BitmapFactory.decodeResource(context.getResources(), R.drawable.scenarioeditormenu));

                                Intent resultIntent = new Intent(context, activityScenarioViewer.class);
                                PendingIntent pendingItent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                String contentText = String.format("Attention, un élément de scénario arrive à échéance:\n%s (sur %s)", item.getName(), item.getLocation());

                                NotificationCompat.Builder notificationBuilder =
                                        new NotificationCompat.Builder(context)
                                                .setSmallIcon(R.drawable.ic_launcher)
                                                .setContentTitle("SWEotE")
                                                .extend(wearableExtender)
                                                .setVibrate(new long[]{100, 100, 100})
                                                .setContentIntent(pendingItent)
                                                .setAutoCancel(true)
                                                .setContentText(contentText);


// Get an instance of the NotificationManager service
                                NotificationManagerCompat notificationManager =
                                        NotificationManagerCompat.from(context);

// Build the notification and issues it with notification manager.
                                notificationManager.notify(notificationId, notificationBuilder.build());
                                //end wear notification


                            }

                        }






                }
                handler.postDelayed( this, 60 * 1000 );
            }
        }, 60 * 1000 );
    }

    public void removeAt(int position) {
        Items.remove(position);
        super.notifyItemRemoved(position);
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public Date getDate() {
        return mDate;
    }






    public static class TextScenarioViewHolder extends RecyclerView.ViewHolder {

        public TextScenarioViewHolder(View itemView) {
            super(itemView);

            textView1 = (TextView) itemView.findViewById(R.id.textView);
            textView2 = (TextView) itemView.findViewById(R.id.textView2);

        }

        public TextView textView1;
        public TextView textView2;


    }


    public static class FightScenarioViewHolder extends RecyclerView.ViewHolder {
        public FightScenarioViewHolder(View itemView) {
            super(itemView);

            imageButton = (ImageButton) itemView.findViewById(R.id.ImageButton);
            textView = (TextView) itemView.findViewById(R.id.textView);

        }

        public ImageButton imageButton;
        public TextView textView;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View v;

        switch (viewType) {

            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scenarioitem_text, parent, false);
                return new TextScenarioViewHolder(v);
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scenarioitem_fight, parent, false);
                return new FightScenarioViewHolder(v);

        }


        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ScenarioItem item = Items.get(position);


        Date myDate;
        if (mDate == null)
            myDate = new Date();
        else
            myDate = new Date(mDate.getTime());


        item.UpdateViewHolder(holder, context, myDate);



    }

    @Override
    public int getItemViewType(int position) {

        ScenarioItem item = Items.get(position);

        if (item.getClass() == TextScenarioItem.class)
            return 1;
        if (item.getClass() == FightScenarioItem.class)
            return 2;

        return super.getItemViewType(position);


    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

}
