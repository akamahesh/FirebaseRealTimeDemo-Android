package com.akamahesh.realtimedem;

import android.content.ClipData;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private TextView tvStatus;
    private ImageView ivPlayer1;
    private ImageView ivPlayer2;
    private ImageView ivPlayer3;
    private ImageView ivPlayer4;
    private ImageView ivPlayer5;
    private ImageView ivPlayer6;
    private ImageView ivPlayer7;
    private ImageView ivPlayer8;
    private View rootView;
    private String TAG = getClass().getSimpleName();
    private DatabaseReference mDatabaseReference;


    private Player selectedPlayer;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player5;
    private Player player6;
    private Player player7;
    private Player player8;


    private float XCords;
    private float YCords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatus = findViewById(R.id.tvStatus);
        ivPlayer1 = findViewById(R.id.ivPlayer1);
        ivPlayer2 = findViewById(R.id.ivPlayer2);
        ivPlayer3 = findViewById(R.id.ivPlayer3);
        ivPlayer4 = findViewById(R.id.ivPlayer4);
        ivPlayer5 = findViewById(R.id.ivPlayer5);
        ivPlayer6 = findViewById(R.id.ivPlayer6);
        ivPlayer7 = findViewById(R.id.ivPlayer7);
        ivPlayer8 = findViewById(R.id.ivPlayer8);
        rootView = findViewById(R.id.rootView);
        tvStatus.setText(String.valueOf("0"));
        player1 = new Player(1,ivPlayer1.getX(),ivPlayer1.getY());
        player2 = new Player(2,ivPlayer2.getX(),ivPlayer2.getY());
        player3 = new Player(3,ivPlayer3.getX(),ivPlayer3.getY());
        player4 = new Player(4,ivPlayer4.getX(),ivPlayer4.getY());
        player5 = new Player(5,ivPlayer5.getX(),ivPlayer5.getY());
        player6 = new Player(6,ivPlayer6.getX(),ivPlayer6.getY());
        player7 = new Player(7,ivPlayer7.getX(),ivPlayer7.getY());
        player8 = new Player(8,ivPlayer8.getX(),ivPlayer8.getY());


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference("Players");

        mDatabaseReference.child(String.valueOf(player1.getId())).setValue(player1);
        mDatabaseReference.child(String.valueOf(player2.getId())).setValue(player2);
        mDatabaseReference.child(String.valueOf(player3.getId())).setValue(player3);
        mDatabaseReference.child(String.valueOf(player4.getId())).setValue(player4);
        mDatabaseReference.child(String.valueOf(player5.getId())).setValue(player5);
        mDatabaseReference.child(String.valueOf(player6.getId())).setValue(player6);
        mDatabaseReference.child(String.valueOf(player7.getId())).setValue(player7);
        mDatabaseReference.child(String.valueOf(player8.getId())).setValue(player8);



        ivPlayer1.setOnTouchListener(this);
        ivPlayer2.setOnTouchListener(this);
        ivPlayer3.setOnTouchListener(this);
        ivPlayer4.setOnTouchListener(this);
        ivPlayer5.setOnTouchListener(this);
        ivPlayer6.setOnTouchListener(this);
        ivPlayer7.setOnTouchListener(this);
        ivPlayer8.setOnTouchListener(this);


        rootView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                if(event.getAction() == DragEvent.ACTION_DROP){

                    mDatabaseReference.child(String.valueOf(selectedPlayer.getId())).child("x").setValue(XCords);
                    mDatabaseReference.child(String.valueOf(selectedPlayer.getId())).child("y").setValue(YCords);
                    return true;
                }

                float x = event.getX();
                float y = event.getY();
                if(x!= 0.0){
                    XCords = x;
                    YCords = y;
                }
                selectedPlayer.setY(y);
                selectedPlayer.setX(x);
                String cords = x + "," + y;
                mDatabaseReference.child(String.valueOf(selectedPlayer.getId())).child("x").setValue(selectedPlayer.getX());
                mDatabaseReference.child(String.valueOf(selectedPlayer.getId())).child("y").setValue(selectedPlayer.getY());
                tvStatus.setText(cords);
                return true;
            }
        });


        //Read from the database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    for (DataSnapshot dShot : dataSnapshot.getChildren()) {
                        Player player = dShot.getValue(Player.class);

                        if(player.getId() == selectedPlayer.getId()){
                            Log.v(TAG,player.toString());
                            if(player.getX() == 0.0 || player.getY() == 0.0)
                                return;
                            setPlayerPosition(player);
                        }
                    }
//                    String value = dataSnapshot.getValue(String.class);
//                    tvStatus.setText(value);
//                    String[] cords = value.split(",");
//                    float x = Float.parseFloat(cords[0]);
//                    float y = Float.parseFloat(cords[1]);
//                    if(x == 0.0 || y == 0.0){
//                        return;
//                    }
//                    btnPlusOne.setX(x);
//                    btnPlusOne.setY(y);
//                    Log.d(TAG, "Value is: " + value);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
                tvStatus.setText(databaseError.toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void storeToFirebase(Player player){
        mDatabaseReference.child(String.valueOf(player.getId())).setValue(player);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.ivPlayer1: {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                player1.setX(v.getX());
                player1.setY(v.getY());
                updatePlayer(player1, v);
            }
            break;
            case R.id.ivPlayer2: {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                player2.setX(v.getX());
                player2.setY(v.getY());
                updatePlayer(player2, v);
            }

            break;
            case R.id.ivPlayer3: {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                player3.setX(v.getX());
                player3.setY(v.getY());
                updatePlayer(player3, v);
            }

            break;
            case R.id.ivPlayer4: {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                player4.setX(v.getX());
                player4.setY(v.getY());
                updatePlayer(player4, v);
            }

            break;
            case R.id.ivPlayer5: {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                player5.setX(v.getX());
                player5.setY(v.getY());
                updatePlayer(player5, v);
            }

            break;
            case R.id.ivPlayer6: {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                player6.setX(v.getX());
                player6.setY(v.getY());
                updatePlayer(player6,v);
            }

            break;
            case R.id.ivPlayer7: {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                player7.setX(v.getX());
                player7.setY(v.getY());
                updatePlayer(player7, v);
            }

            break;
            case R.id.ivPlayer8: {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                player8.setX(v.getX());
                player8.setY(v.getY());
                updatePlayer(player8, v);
            }

            break;
        }
        return true;
    }

    private void updatePlayer(Player player, View v) {
        selectedPlayer = player;
    }

    public void setPlayerPosition(Player playerPosition) {
        switch (playerPosition.getId()){
            case 1:
                ivPlayer1.setX(playerPosition.getX());
                ivPlayer1.setY(playerPosition.getY());
                break;
            case 2:
                ivPlayer2.setX(playerPosition.getX());
                ivPlayer2.setY(playerPosition.getY());
                break;
            case 3:
                ivPlayer3.setX(playerPosition.getX());
                ivPlayer3.setY(playerPosition.getY());
                break;
            case 4:
                ivPlayer4.setX(playerPosition.getX());
                ivPlayer4.setY(playerPosition.getY());
                break;
            case 5:
                ivPlayer5.setX(playerPosition.getX());
                ivPlayer5.setY(playerPosition.getY());
                break;
            case 6:
                ivPlayer6.setX(playerPosition.getX());
                ivPlayer6.setY(playerPosition.getY());
                break;
            case 7:
                ivPlayer7.setX(playerPosition.getX());
                ivPlayer7.setY(playerPosition.getY());
                break;
            case 8:
                ivPlayer8.setX(playerPosition.getX());
                ivPlayer8.setY(playerPosition.getY());
                break;


        }
    }
}
