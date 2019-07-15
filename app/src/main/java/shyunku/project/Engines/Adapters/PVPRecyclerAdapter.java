package shyunku.project.Engines.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import shyunku.project.Objects.PVPInfo;
import shyunku.project.R;

public class PVPRecyclerAdapter  extends RecyclerView.Adapter<PVPRecyclerAdapter.ViewHolder>{
    public ArrayList<PVPInfo> pvpList;
    public Context context;

    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public PVPRecyclerAdapter(ArrayList<PVPInfo> pvpList) {
        this.pvpList = pvpList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pvp_item, parent, false);
        PVPRecyclerAdapter.ViewHolder holder = new PVPRecyclerAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PVPInfo info = pvpList.get(position);

        if(info.getBitmap() != null)
            holder.championIcon.setImageBitmap(info.getBitmap());
        holder.winOrLoseView.setText(info.isWIn()?"승리":"패배");
        holder.kdaView.setText(info.getKillCount()+"/"+info.getDieCount()+"/"+info.getAssistCount());
        String formatted = String.format("%.2f", (double)(info.getKillCount()+info.getAssistCount())/(double)info.getDieCount());
        holder.calculatedKdaView.setText(formatted);
        holder.gainedGoldView.setText(info.getGainedGold()+" 골드");
        holder.gainedCsView.setText("CS "+info.getCsCount());
        Date datest = new Date(info.getStartFlag());
        Date dateen = new Date(info.getDuration()*1000+info.getStartFlag());
        holder.startFlagView.setText(sdf.format(datest)+" 시작");
        holder.endFlagView.setText(sdf.format(dateen)+" 종료");
        holder.layout.setBackgroundColor(info.isWIn()? ResourcesCompat.getColor(
                context.getResources(), R.color.winBG, null):ResourcesCompat.getColor(
                        context.getResources(), R.color.loseBG, null));
    }

    @Override
    public int getItemCount() {
        return pvpList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        public ImageView championIcon;
        public TextView winOrLoseView;
        public TextView kdaView;
        public TextView calculatedKdaView;
        public TextView gainedGoldView;
        public TextView gainedCsView;
        public TextView startFlagView;
        public TextView endFlagView;

        public ConstraintLayout layout;

        public ViewHolder(@NonNull View view) {
            super(view);

            championIcon = (ImageView)view.findViewById(R.id.championIcon);
            winOrLoseView = (TextView)view.findViewById(R.id.win_or_lose_view);
            kdaView = (TextView)view.findViewById(R.id.KDA_view);
            calculatedKdaView = (TextView)view.findViewById(R.id.kda_calculated_view);
            gainedGoldView = (TextView)view.findViewById(R.id.gained_gold_view);
            gainedCsView = (TextView)view.findViewById(R.id.gained_cs_view);
            startFlagView = (TextView)view.findViewById(R.id.game_startflag_view);
            endFlagView = (TextView)view.findViewById(R.id.game_endflag_view);

            layout = (ConstraintLayout)view.findViewById(R.id.pvp_constraint);
        }
    }
}
