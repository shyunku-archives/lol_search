package shyunku.project.Objects;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import shyunku.project.Activities.InfoActivity;
import shyunku.project.Engines.ImageManager;
import shyunku.project.R;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    public ArrayList<SummonerInfo> infos;
    public Context context;

    private ImageManager imageManager = new ImageManager();

    public MyRecyclerAdapter(ArrayList<SummonerInfo> infos){
        this.infos = infos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nicknameView.setText(infos.get(position).getSummoner().getName());
        holder.iconView.setImageBitmap(infos.get(position).getIconImage());
        holder.levelView.setText(infos.get(position).getSummoner().getSummonerLevel()+" 레벨");
        holder.memoView.setText(infos.get(position).getMemo());

        final int pos = position;
        holder.entireView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InfoActivity.class);
                intent.putExtra("PlayerID", infos.get(pos).getSummoner().getAccountId());
                intent.putExtra("PlayerMemo", infos.get(pos).getMemo());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nicknameView;
        public TextView levelView;
        public TextView memoView;
        public ImageView iconView;

        public View entireView;

        public ViewHolder(@NonNull View view) {
            super(view);
            entireView = view;
            nicknameView = (TextView)view.findViewById(R.id.summonerNicknameTextView);
            levelView = (TextView)view.findViewById(R.id.summonerLevelTextView);
            iconView = (ImageView)view.findViewById(R.id.summonerIconView);
            memoView = (TextView)view.findViewById(R.id.summonerMemoView);
        }
    }

    private void toast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void toastLong(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}
