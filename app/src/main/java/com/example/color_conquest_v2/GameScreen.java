package com.example.color_conquest_v2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class GameScreen extends AppCompatActivity {
    int blue = R.drawable.bluedialog;
    int red = R.drawable.reddialog;
    int player1_tcolour=0, player2_tcolour=0, current_player = 1;
    int player1_bcolor = Color.parseColor("#ff5f57");
    int player2_bcolor = Color.parseColor("#2fb6f0");
    int player1_colour = R.drawable.rounded_red;
    int player2_colour = R.drawable.rounded_blue;
    TextView player1_score,player2_score;
    TextView player1Name,player2Name;
    GridView gridView;
    ArrayList<Cells> cellsArrayList;
    private CustomGridAdapter gridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_screen);
        player1_score = findViewById(R.id.player1_score);
        player2_score = findViewById(R.id.player2_score);
        gridView = findViewById(R.id.gridview);
        String player1_name = getIntent().getStringExtra("player1");
        String player2_name = getIntent().getStringExtra("player2");
        player1Name = findViewById(R.id.Player1_display);
        player1Name.setText(player1_name);
        player2Name = findViewById(R.id.Player2_display);
        player2Name.setText(player2_name);
        cellsArrayList = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            cellsArrayList.add(new Cells("0"));
        }
        gridAdapter = new CustomGridAdapter(cellsArrayList,getApplicationContext());
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cells clickedCell = (Cells) parent.getItemAtPosition(position);
                String number = clickedCell.getNum();
                ConstraintLayout background = findViewById(R.id.main);
                if(current_player == 1){
                    if(player1_tcolour == 0){
                        background.setBackgroundColor(player2_bcolor);
                        player1_tcolour = R.drawable.tile_red;
                        clickedCell.setNum("3");
                        gridAdapter.updateColor(position,player1_colour,player1_tcolour);
                        changePlayer(current_player);
                    }
                    else {
                        if (gridAdapter.getColor(position) == player1_colour) {
                            background.setBackgroundColor(player2_bcolor);
                            if (Integer.parseInt(number) < 3) {
                                clickedCell.setNum("" + (Integer.parseInt(number) + 1));
                                gridAdapter.updateColor(position,player1_colour,player1_tcolour);
                                ((CustomGridAdapter) parent.getAdapter()).notifyDataSetChanged();
                            } else if (Integer.parseInt(number) == 3) {
                                Split(parent, position, player1_colour,player1_tcolour);
                            }
                            changePlayer(current_player);
                        } else {
                            //Toast message saying u clicked the wrong cell.
                            DisplayMessage( "You have Clicked Wrong cell");
                        }
                    }
                }else if(current_player == 2){
                    if(player2_tcolour == 0){
                        if(gridAdapter.getColor(position) != player1_colour) {
                            background.setBackgroundColor(player1_bcolor);
                            player2_tcolour = R.drawable.tile_blue;
                            clickedCell.setNum("3");
                            gridAdapter.updateColor(position, player2_colour, player2_tcolour);
                            changePlayer(current_player);
                        }
                    }
                    else {
                        if (gridAdapter.getColor(position) == player2_colour) {
                            background.setBackgroundColor(player1_bcolor);
                            if (Integer.parseInt(number) < 3) {
                                clickedCell.setNum("" + (Integer.parseInt(number) + 1));
                                ((CustomGridAdapter) parent.getAdapter()).notifyDataSetChanged();
                                gridAdapter.updateColor(position,player2_colour,player2_tcolour);
                            } else if (Integer.parseInt(number) == 3) {
                                Split(parent, position, player2_colour,player2_tcolour);
                            }
                            changePlayer(current_player);
                        }else{
                            //Toast message saying u clicked the wrong cell.
                            DisplayMessage( "You have Clicked Wrong cell");
                        }
                    }
                }
                // Check how many cells have the blue background
                int bluescore = 0,redscore = 0;
                for (int i = 0; i < gridView.getChildCount(); i++) {
                    if(gridAdapter.getColor(i) == R.drawable.rounded_blue){
                        Cells blueCell = (Cells) parent.getItemAtPosition(i);
                        bluescore += Integer.parseInt(blueCell.getNum());
                    }
                }
                for (int i = 0; i < gridView.getChildCount(); i++) {
                    if(gridAdapter.getColor(i) == R.drawable.rounded_red){
                        Cells blueCell = (Cells) parent.getItemAtPosition(i);
                        redscore += Integer.parseInt(blueCell.getNum());
                    }
                }
                player1_score.setText("" + redscore);
                player2_score.setText("" + bluescore);
                if(bluescore == 0 && redscore!= 3){
                    showDialog(player1_name,red,blue);
                }else if(redscore == 0){
                    showDialog(player2_name,blue,red);
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void changePlayer(int player_no){
        if(player_no == 1){
            current_player = 2;
        }else if(player_no == 2){
            current_player = 1;
            //player_no = 1;
        }
        //DisplayMessage( "Player "+ current_player+ " turn" );
    }
    private void DisplayMessage(String Message){
        Toast.makeText(this, Message , Toast.LENGTH_SHORT).show();
    }

    private void Split(AdapterView<?> parent,int position, int player_Colour,int player_tColor){
        Cells clickedCell = (Cells) parent.getItemAtPosition(position);
        clickedCell.setNum("" + 0);
        gridAdapter.updateColor(position,R.drawable.rounded_corner,R.drawable.square_corner);
        if(position % 5 != 0){
            gridAdapter.updateColor(position-1,player_Colour,player_tColor);
            Cells leftCell = (Cells) parent.getItemAtPosition(position-1);
            leftCell.setNum(""+ (Integer.parseInt(leftCell.getNum()) + 1));
            if(Integer.parseInt(leftCell.getNum()) == 4){
                Split(parent,position-1, player_Colour,player_tColor);
            }
        }
        if(position > 4){
            gridAdapter.updateColor(position-5,player_Colour,player_tColor);
            Cells aboveCell = (Cells) parent.getItemAtPosition(position-5);
            aboveCell.setNum(""+ (Integer.parseInt(aboveCell.getNum()) + 1));
            if(Integer.parseInt(aboveCell.getNum()) == 4){
                Split(parent,position-5, player_Colour,player_tColor);
            }
        }
        if(position < 20){
            gridAdapter.updateColor(position+5,player_Colour,player_tColor);
            Cells belowCell = (Cells) parent.getItemAtPosition(position+5);
            belowCell.setNum(""+ (Integer.parseInt(belowCell.getNum()) + 1));
            if(Integer.parseInt(belowCell.getNum()) == 4){
                Split(parent,position+5, player_Colour,player_tColor);
            }
        }
        if(position % 5 != 4){
            gridAdapter.updateColor(position+1,player_Colour,player_tColor);
            Cells rightCell = (Cells) parent.getItemAtPosition(position+1);
            rightCell.setNum(""+ (Integer.parseInt(rightCell.getNum()) + 1));
            if(Integer.parseInt(rightCell.getNum()) == 4){
                Split(parent,position+1, player_Colour,player_tColor);
            }
        }
        ((CustomGridAdapter) parent.getAdapter()).notifyDataSetChanged();
    }
    private void showDialog(String playername,int win_color,int lose_color) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate and set the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.winscreen, null);
        builder.setView(dialogView);

        // Get references to the buttons in the custom layout
        TextView playAgainButton = dialogView.findViewById(R.id.playAgain);
        TextView homeButton = dialogView.findViewById(R.id.home);
        EditText winnerName = dialogView.findViewById(R.id.winnername);

        //Setting the Winner Name and colors
        winnerName.setText(playername);
        playAgainButton.setBackgroundResource(win_color);
        homeButton.setBackgroundResource(lose_color);

        // Set click listeners for the buttons
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restart the activity
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to home activity
                Intent intent = new Intent(GameScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Create and show the dialog
        //builder.setTitle("Title");
        AlertDialog dialog = builder.create();
        dialog.show();
        // Set the background of the dialog window to transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // Reduce the width of the dialog
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.75); // Adjust width here
        dialog.getWindow().setAttributes(layoutParams);
    }
}