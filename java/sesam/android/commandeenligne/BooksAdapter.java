package sesam.android.commandeenligne;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BooksAdapter extends ArrayAdapter<Books> {

    public BooksAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v;

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = layoutInflater.inflate(R.layout.books_cell, null);

        Books currentBook = getItem(position);
        String thisBook = currentBook.getTitre();
        String thisArtist = currentBook.getAuteur();
        String thisCover = currentBook.getCover();
        String thisQuantity = currentBook.getQuantity();

        TextView title = (TextView) v.findViewById(R.id.bookTitle);
        title.setText(thisBook);

        TextView author = (TextView) v.findViewById(R.id.bookAuthor);
        author.setText(thisArtist);

        ImageView cover = (ImageView) v.findViewById(R.id.bookCover);
        String icon = thisCover;
        int resId = getContext().getResources().getIdentifier(icon, "mipmap", getContext().getPackageName());
        cover.setImageResource(resId);

        TextView quantity = (TextView) v.findViewById(R.id.bookQuantity);
        quantity.setText(thisQuantity);


        //int piste = getApplicationContext().getResources().getIdentifier(getString(), "raw", getPackageName());
        //MediaPlayer mediaPlayer = MediaPlayer.create(this,mnemonic);
        return v;
    }
}
