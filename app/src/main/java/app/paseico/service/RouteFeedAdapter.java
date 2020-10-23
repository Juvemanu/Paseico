package app.paseico.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app.paseico.R;
import org.jetbrains.annotations.NotNull;

public class RouteFeedAdapter extends RecyclerView.Adapter<RouteFeedAdapter.RouteFeedViewHolder>  {

    private int mNumerosItems;

    public RouteFeedAdapter(int numeroDeItems) {

        mNumerosItems = numeroDeItems;

    }


    @NotNull
    @Override
    public RouteFeedViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.route_feed_row;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        RouteFeedViewHolder viewHolder = new RouteFeedViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NotNull RouteFeedAdapter.RouteFeedViewHolder holder, int position) {
        holder.Bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumerosItems;
    }

    //Crear el view holder


    class RouteFeedViewHolder extends RecyclerView.ViewHolder {

        TextView mTvListaNumerosView;

        public RouteFeedViewHolder(View itemView) {
            super(itemView);

            mTvListaNumerosView = itemView.findViewById(R.id.route_list);
        }

        void Bind(int listaIndex) {
            mTvListaNumerosView.setText(String.valueOf(listaIndex));
        }
    }

}
