package com.example.felix_its.actionbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.felix_its.actionbar.adapters.MoviesAdapter;
import com.example.felix_its.actionbar.model.Movie;
import com.example.felix_its.actionbar.model.MoviesResponse;
import com.example.felix_its.actionbar.network.ApiClient;
import com.example.felix_its.actionbar.network.ApiEndpointInterface;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ListView moviesList;
    private ArrayAdapter<Movie> moviesAdapter;
    private List<Movie> movies;


    private static final String TAG = MainActivity.class.getSimpleName();
    private String API_KEY = "b7cd3340a794e5a2f35e3abb820b497f";
    private int currentPage = 1;
    private int previousPage = 0;
    private int totalPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        moviesList = (ListView) findViewById(R.id.lvMoviesList);
        movies = new ArrayList<>();
        getNowPlayingMovies(currentPage);
        setupLisViewListener();
        }

    public void getNowPlayingMovies(int page) {
        if(API_KEY.isEmpty()) {
            Toast.makeText(this, "No API KEY", Toast.LENGTH_LONG).show();
            return;
        }

        ApiEndpointInterface apiService = ApiClient.getClient().create(ApiEndpointInterface.class);

        Call<MoviesResponse> call = apiService.getNowPayingMovies(API_KEY, page);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                movies = response.body().getResults();
                totalPages = response.body().getTotalPages();
                Log.d(TAG, "request now playing movies");

                moviesAdapter = new MoviesAdapter(getApplicationContext(), movies);
                moviesList.setAdapter(moviesAdapter);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    private void setupLisViewListener() {
        moviesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MovieDetails.class);

                intent.putExtra("movie", moviesAdapter.getItem(position));

                startActivity(intent);
            }
        });

        moviesList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent=new Intent(MainActivity.this,PersoanlinfoActivity.class);
        startActivity(intent);
        if (id == R.id.action_settings) {
            return true; }
            return super.onOptionsItemSelected(item); }
}
