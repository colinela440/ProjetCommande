package sesam.android.commandeenligne;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CommandesAdapter extends ArrayAdapter<Commandes> {

    public CommandesAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View v;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = layoutInflater.inflate(R.layout.commandes_cell, null);

        Commandes currentCommande = getItem(position);
        String thisCommande = currentCommande.getNumber();
        String thisClient = currentCommande.getClient();
        String thisQuantity = currentCommande.getQuantity();

        TextView commandeNb = (TextView) v.findViewById(R.id.commNumber);
        commandeNb.setText((thisCommande));

        TextView client = (TextView) v.findViewById(R.id.commClient);
        client.setText((thisClient));

        TextView quantity = (TextView) v.findViewById(R.id.commQuantity);
        quantity.setText((thisQuantity));

        return v;
    }
}
