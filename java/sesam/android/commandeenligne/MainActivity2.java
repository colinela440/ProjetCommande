package sesam.android.commandeenligne;          //Page gestion d'une commande

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private List<Books> mBooks;
    private ListView book_list;
    private BooksAdapter booksAdapter;

    private TextView chosenOrderNb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //recuperer le numero de la commande selectionnee
        chosenOrderNb = findViewById(R.id.chosenCommNb);
        String thisOrder = getIntent().getStringExtra("keyOrder");
        chosenOrderNb.setText(thisOrder);
        //le mettre dans une variable ici "order"
        String order = chosenOrderNb.getText().toString();

        book_list = (ListView) findViewById(R.id.detailsListe);
        booksAdapter = new BooksAdapter(getApplicationContext(),0);
        mBooks = new ArrayList<>();


        order = "demo";

        //composition de la commande en fonction de "order"
        switch (order){
            case "1":
                mBooks.add(new Books("Boule et Bill, T1","Jean Roba", "bd_boule","1" ));
                mBooks.add(new Books("Les fleurs du mal","Charles Baudelaire","poesie_baudelaire","1"));
                mBooks.add(new Books("Le colonel Chabert","Honoré de Balzac", "roman_balzac","1"));
                mBooks.add(new Books("Le Cid","Pierre Corneille", "theatre_corneille","1"));
                mBooks.add(new Books("Cyrano de Bergerac","Edmond Rostand", "theatre_rostand","1"));
                break;
            case "2":
                mBooks.add(new Books("La gazette du château, T1","Dorison & Delep", "bd_gazette","2"));
                mBooks.add(new Books("Hernani","Victor Hugo", "theatre_hugo","2"));
                mBooks.add(new Books("Dom Juan","Molière", "theatre_moliere","1"));
                mBooks.add(new Books("Cyrano de Bergerac","Edmond Rostand", "theatre_rostand","3"));
                break;
            case "5":
                mBooks.add(new Books("Les fleurs du mal","Charles Baudelaire","poesie_baudelaire","3"));
                mBooks.add(new Books("Cahier d'un retour au pays natal","Aimé Césaire", "poesie_cesaire","4"));
                break;
            case "7":
                mBooks.add(new Books("Boule et Bill, T1","Jean Roba", "bd_boule","1" ));
                mBooks.add(new Books("Les fleurs du mal","Charles Baudelaire","poesie_baudelaire","1"));
                break;
            case "8":
                mBooks.add(new Books("Blacksad","Canales & Guarnido", "bd_blacksad","1"));
                break;
            case "10":
                mBooks.add(new Books("Blacksad","Canales & Guarnido", "bd_blacksad","1"));
                mBooks.add(new Books("La gazette du château, T1","Dorison & Delep", "bd_gazette","2"));
                mBooks.add(new Books("Le colonel Chabert","Honoré de Balzac", "roman_balzac","1"));
                mBooks.add(new Books("Le Cid","Pierre Corneille", "theatre_corneille","23"));
                break;
            case"demo":
                mBooks.add(new Books("Blacksad","Canales & Guarnido", "bd_blacksad","1"));
                mBooks.add(new Books("Boule et Bill, T1","Jean Roba", "bd_boule","1" ));
                mBooks.add(new Books("La gazette du château, T1","Dorison & Delep", "bd_gazette","5"));
                mBooks.add(new Books("Les fleurs du mal","Charles Baudelaire","poesie_baudelaire","3"));
                mBooks.add(new Books("Cahier d'un retour au pays natal","Aimé Césaire", "poesie_cesaire","1"));
                mBooks.add(new Books("Le colonel Chabert","Honoré de Balzac", "roman_balzac","2"));
                mBooks.add(new Books("Le Cid","Pierre Corneille", "theatre_corneille","7    "));
                mBooks.add(new Books("Hernani","Victor Hugo", "theatre_hugo","5"));
                mBooks.add(new Books("Dom Juan","Molière", "theatre_moliere","1"));
                mBooks.add(new Books("Cyrano de Bergerac","Edmond Rostand", "theatre_rostand","9"));
                break;
        }

        /*
        mBooks.add(new Books("Blacksad","Canales & Guarnido", "bd_blacksad","1"));
        mBooks.add(new Books("Boule et Bill, T1","Jean Roba", "bd_boule","1" ));
        mBooks.add(new Books("La gazette du château, T1","Dorison & Delep", "bd_gazette","5"));
        mBooks.add(new Books("Les fleurs du mal","Charles Baudelaire","poesie_baudelaire","3"));
        mBooks.add(new Books("Cahier d'un retour au pays natal","Aimé Césaire", "poesie_cesaire","1"));
        mBooks.add(new Books("Le colonel Chabert","Honoré de Balzac", "roman_balzac","2"));
        mBooks.add(new Books("Le Cid","Pierre Corneille", "theatre_corneille","7    "));
        mBooks.add(new Books("Hernani","Victor Hugo", "theatre_hugo","5"));
        mBooks.add(new Books("Dom Juan","Molière", "theatre_moliere","1"));
        mBooks.add(new Books("Cyrano de Bergerac","Edmond Rostand", "theatre_rostand","9"));
         */

        book_list.setAdapter(booksAdapter);
        booksAdapter.addAll(mBooks);
    }

    //bouton to page home (logo)
    public void btToHome(View view) {
        Intent monIntentToHome = new Intent(this, MainActivityHome.class);
        startActivity(monIntentToHome);
    }

    //bouton to page commandes (back)
    public void btToCommandes(View view) {
        Intent monIntentToCommandes = new Intent(this, MainActivity.class);
        startActivity(monIntentToCommandes);
    }

    //bouton commande incomplete
    public void btCommIncomplete(View view) {
        Log.d("Main Activity Details", "current order : incomplete");

        String thisState = "doing";
        String thisNum = chosenOrderNb.getText().toString();

        Intent stateIntent = new Intent(this, MainActivity.class);
        stateIntent.putExtra("keyState",thisState);
        stateIntent.putExtra("keyNum",thisNum);
        startActivity(stateIntent);
    }

    public void btCommTerminee(View view) {
        Log.d("Main Activity Details", "current order : finished");

        String thisState = "done";
        String thisNum = chosenOrderNb.getText().toString();

        Intent stateIntent = new Intent(this, MainActivity.class);
        stateIntent.putExtra("keyState",thisState);
        stateIntent.putExtra("keyNum",thisNum);
        startActivity(stateIntent);
    }
}
