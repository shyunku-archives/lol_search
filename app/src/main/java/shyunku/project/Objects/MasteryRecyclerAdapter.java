package shyunku.project.Objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import shyunku.project.R;

public class MasteryRecyclerAdapter extends RecyclerView.Adapter<MasteryRecyclerAdapter.ViewHolder> {
    public ArrayList<MasteryInfo> mastery;
    public Context context;

    public MasteryRecyclerAdapter(ArrayList<MasteryInfo> infos){this.mastery = infos;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.mastery_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.championNameView.setText(mastery.get(position).getChampion().getName());
        holder.masteryPointView.setText(mastery.get(position).getChampionPoint()+" 점");
        holder.masteryLevelView.setText("숙련도 "+mastery.get(position).getChampionLevel()+" 단계");
        //holder.championIconView.setImageBitmap();
    }

    @Override
    public int getItemCount() {
        return mastery.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView championNameView;
        public ImageView championIconView;
        public TextView masteryLevelView;
        public TextView masteryPointView;

        public View entireView;

        public ViewHolder(@NonNull View view) {
            super(view);
            entireView = view;

            championNameView = (TextView)view.findViewById(R.id.mastery_champion_name);
            championIconView = (ImageView)view.findViewById(R.id.mastery_champion_icon);
            masteryLevelView = (TextView)view.findViewById(R.id.mastery_stage);
            masteryPointView = (TextView)view.findViewById(R.id.masetery_points);
        }
    }
}
