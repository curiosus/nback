package me.curiosus.nback.app;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends ActionBarActivity {

    private Button beginButton;
    private Button locationButton;
    private TextView nBackIndicator;
    private int delayValue = 2000;
    private int nBackValue = 1;
    private int numberOfTurns = 20;
    private int turnsTaken;
    private boolean locationSelected = false;
    private int locationErrors;

    private Random random = new Random(System.currentTimeMillis());
    private View[] locations = new View[9];
    private Handler handler = new Handler();
    private List<Integer> locationHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        beginButton = (Button) findViewById(R.id.begin_button);
        locationButton = (Button) findViewById(R.id.location_btn);
        nBackIndicator = (TextView) findViewById(R.id.nback_indicator);
        nBackIndicator.setText(nBackValue + " back");
        setUpLocationViews();
        addListeners();
    }

    private void setUpLocationViews() {
        locations[0] = findViewById(R.id.btn_0);
        locations[1] = findViewById(R.id.btn_1);
        locations[2] = findViewById(R.id.btn_2);
        locations[3] = findViewById(R.id.btn_3);
        locations[4] = findViewById(R.id.btn_4);
        locations[5] = findViewById(R.id.btn_5);
        locations[6] = findViewById(R.id.btn_6);
        locations[7] = findViewById(R.id.btn_7);
        locations[8] = findViewById(R.id.btn_8);
        for (int i = 0; i < locations.length; i++) {
            View location = locations[i];
            location.setBackgroundColor(Color.DKGRAY);
        }
    }

    private void addListeners() {

        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeTurn();
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationSelected = true;
            }
        });
    }

    private void takeTurn() {
        if (turnsTaken <= numberOfTurns) {
            locationSelected = false;
            final int locationIndex = random.nextInt(9);
            locationHistory.add(locationIndex);
            final View location = locations[locationIndex];
            location.setBackgroundColor(Color.BLUE);
            handler.postDelayed(new VIRunnable(location, locationIndex), delayValue);

        } else {
            String errors = locationErrors + " location errors";
            Toast.makeText(MainActivity.this, errors, Toast.LENGTH_LONG ).show();
            if (locationErrors > 2) {
                if (nBackValue > 1) {
                    nBackValue--;
                }
            } else if (locationErrors == 0) {
                nBackValue++;
            } else {
				//we don't care because nBackValue = nBackValue
			}

            nBackIndicator.setText(nBackValue + " back");
            turnsTaken = 0;
            locationErrors = 0;
            locationSelected = false;
            locationHistory.clear();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

	private class VIRunnable implements Runnable {
		private final View location;
		private final int locationIndex;

		public VIRunnable(View location, int locationIndex) {
			this.location = location;
			this.locationIndex = locationIndex;
		}

		@Override
		public void run() {
			location.setBackgroundColor(Color.DKGRAY);
			if (locationHistory.size() > nBackValue) {
				int nBackLocation = locationHistory.get((locationHistory.size() - 1) - nBackValue);
				if (nBackLocation == locationIndex) {
					if (locationSelected) {
						Toast.makeText(MainActivity.this, "Yay", Toast.LENGTH_SHORT).show();
					} else {
						locationErrors++;
						Toast.makeText(MainActivity.this, "Whoops", Toast.LENGTH_SHORT).show();
					}
				} else {
					if (locationSelected) {
						locationErrors++;
						Toast.makeText(MainActivity.this, "Whoops", Toast.LENGTH_SHORT).show();
					}
				}
			}

			turnsTaken++;

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					takeTurn();
				}
			}, delayValue - 1000);
		}
	}
}
