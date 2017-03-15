package uk.co.mandilee.gamescorer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by manda on 11/03/2017.
 */

class ResultAdapter extends ArrayAdapter<Result> {

    public ResultAdapter(Context context, ArrayList<Result> results) {
        super(context, 0, results);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Result currentResult = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID miwok_text_view.
        TextView playerOne = (TextView) listItemView.findViewById(R.id.player_one);
        // Get the Miwok translation from the currentResult object and set this text on
        // the Miwok TextView.
        playerOne.setText(String.valueOf(currentResult.getPlayerOne()));


        // Find the TextView in the list_item.xml layout with the ID miwok_text_view.
        TextView playerTwo = (TextView) listItemView.findViewById(R.id.player_two);
        // Get the Miwok translation from the currentResult object and set this text on
        // the Miwok TextView.
        playerTwo.setText(String.valueOf(currentResult.getPlayerTwo()));

        // Find the ImageView in the list_item.xml layout with the ID image.
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);
        // Check if an image is provided for this word or not
        if (currentResult.hasImage()) {
            // If an image is available, display the provided image based on the resource ID
            imageView.setImageResource(currentResult.getImageResourceId());
            // Make sure the view is visible
            imageView.setVisibility(View.VISIBLE);
        } else {
            // Otherwise hide the ImageView (set visibility to GONE)
            imageView.setVisibility(View.GONE);
        }

        // Return the whole list item layout (containing 2 TextViews) so that it can be shown in
        // the ListView.
        return listItemView;
    }
}
