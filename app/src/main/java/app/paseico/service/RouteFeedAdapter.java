package app.paseico.service;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app.paseico.R;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RouteFeedAdapter extends RecyclerView.Adapter<RouteFeedAdapter.RouteFeedViewHolder>  {

    private final List<String> routeNameList;
    private int resource;

    public RouteFeedAdapter(List<String> list, int resource) {
        routeNameList = list;
        this.resource = resource;
    }


    @NotNull
    @Override
    public RouteFeedViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.route_feed_row;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(resource, parent, false);

        RouteFeedViewHolder viewHolder = new RouteFeedViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NotNull RouteFeedAdapter.RouteFeedViewHolder holder, int position) {
        String name = routeNameList.get(position);
        Log.d("NOMBRE", name);
        holder.Bind(name);
    }

    @Override
    public int getItemCount() {
        return routeNameList.size();
    }

    //Crear el view holder
    class RouteFeedViewHolder extends RecyclerView.ViewHolder {
        TextView mTvListaNumerosView;

        public RouteFeedViewHolder(View itemView) {
            super(itemView);

            mTvListaNumerosView = itemView.findViewById(R.id.route_description);
        }
        void Bind(String text) {
            mTvListaNumerosView.setText(text);
        }
    }
}
