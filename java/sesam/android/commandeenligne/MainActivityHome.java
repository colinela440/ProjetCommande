package sesam.android.commandeenligne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivityHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        Button btToCommandes = (Button)findViewById(R.id.btToCommandes);
        Intent monIntentToCommandes = new Intent(this, MainActivity.class);

        btToCommandes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(monIntentToCommandes);
            }
        });

        Button btToBlth = (Button)findViewById(R.id.btToBlth);
        Intent monIntentToBlth = new Intent(this, MainActivityBlth.class);

        btToBlth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(monIntentToBlth);
            }
        });
    }
}