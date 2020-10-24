package app.paseico.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app.paseico.R;
import app.paseico.data.Route;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RouteFeedAdapter extends RecyclerView.Adapter<RouteFeedAdapter.RouteFeedViewHolder>  {
    private ArrayList<Route> routeList = new ArrayList<>();
    private int resource;

    public RouteFeedAdapter(ArrayList<Route> routeList, int resource) {
        this.routeList = routeList;
        this.resource = resource;
    }

    @NotNull
    @Override
    public RouteFeedViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent,false);
        return new RouteFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull RouteFeedAdapter.RouteFeedViewHolder holder, int position) {
        Route route = routeList.get(position);
        holder.textViewRoute.setText(route.getName());
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class RouteFeedViewHolder extends RecyclerView.ViewHolder {

        TextView textViewRoute;
        public View view;

        public RouteFeedViewHolder(View view) {
            super(view);
            this.view = view;
            this.textViewRoute = (TextView) view.findViewById(R.id.route_title);
        }

        void Bind(int listaIndex) {
            textViewRoute.setText(String.valueOf(listaIndex));
        }
    }

}
