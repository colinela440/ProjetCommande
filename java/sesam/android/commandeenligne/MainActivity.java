package sesam.android.commandeenligne;          //Page liste des commandes

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "Main Activity Commande";

    private List<Commandes> myCommandes;
    private ListView commande_list;
    private CommandesAdapter commandesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        commande_list = (ListView) findViewById(R.id.commandesListe);
        commandesAdapter = new CommandesAdapter(getApplicationContext(),0);
        myCommandes = new ArrayList<>();

        myCommandes.add(new Commandes("1","nhdg00058", "5 articles"));
        myCommandes.add(new Commandes("2","JeanPhilippe47", "8 articles"));
        myCommandes.add(new Commandes("5","Dori98lola", "7 articles"));
        myCommandes.add(new Commandes("7","arkfahdon","1 articles"));
        myCommandes.add(new Commandes("8","Xx_pop_xX", "2 articles"));
        myCommandes.add(new Commandes("10","henri delors", "27 articles"));

        commande_list.setAdapter(commandesAdapter);
        commandesAdapter.addAll(myCommandes);


        /*
        TextView commState = findViewById(R.id.commState);
        String state = getIntent().getStringExtra("keyState");
        String num = getIntent().getStringExtra("keyNum");

        switch(state){
            case "doing":
                commState.setText(R.string.commande_stateDoing);
                commState.setBackgroundColor(Color.parseColor("#FFFFEE58"));
                break;
            case "done":
                commState.setText(R.string.commande_stateDone);
                commState.setBackgroundColor(Color.parseColor("#FF99CC00"));
                break;
        }
         */
    }


    //bouton to page home
    public void btToHome(View view) {
        Intent monIntentToHome = new Intent(this, MainActivityHome.class);
        startActivity(monIntentToHome);
    }

    //bouton to page details
    public void btToDetails(View view) {
        Intent monIntentToDetails = new Intent(this, MainActivity2.class);
        startActivity(monIntentToDetails);
    }

    //bouton to page blth (logo)
    public void btToBlth(View view) {
        Intent monIntentToBlth = new Intent(this, MainActivityBlth.class);
        startActivity(monIntentToBlth);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemClick: you clicked on an order");

        String commandeNumber = myCommandes.get(position).getNumber();
        String commandeClient = myCommandes.get(position).getClient();
        Log.d(TAG, "onItemClick: order number " + commandeNumber + "for client " + commandeClient);

     /*   TextView chosenOrder = (TextView) findViewById(R.id.textView);
        chosenOrder.setText(commandeNumber);
        String thisOrder = chosenOrder.getText().toString();
*/
        String thisOrder = myCommandes.get(position).getNumber();
        Intent orderIntent = new Intent(this, MainActivity2.class);
        orderIntent.putExtra("keyOrder",thisOrder);
        startActivity(orderIntent);
    }
}