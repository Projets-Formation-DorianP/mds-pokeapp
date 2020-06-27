package fr.mds.pokeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.mds.pokeapp.adapter.PokedexAdapter;
import fr.mds.pokeapp.model.Pokedex;
import fr.mds.pokeapp.model.PokedexResult;
import fr.mds.pokeapp.model.Pokemon;
import fr.mds.pokeapp.network.GetPokedexDataService;
import fr.mds.pokeapp.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokedexActivity extends AppCompatActivity {

    private RecyclerView rv_pokedex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);

        rv_pokedex = (RecyclerView) findViewById(R.id.rv_pokedex);

        GetPokedexDataService service = RetrofitInstance.getRetrofitInstance().create(GetPokedexDataService.class);
        Call<Pokedex> call = service.getPokedex();
        call.enqueue(new Callback<Pokedex>() {
            @Override
            public void onResponse(Call<Pokedex> call, Response<Pokedex> response) {
                generateDataList(response.body().getResults());
            }

            @Override
            public void onFailure(Call<Pokedex> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(PokedexActivity.this, "Something went wrong... Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to generate List of data using RecyclerView with custom adapter
    private void generateDataList(List<PokedexResult> pokedexResult) {
        Context context = PokedexActivity.this;
        PokedexAdapter adapter = new PokedexAdapter(context, pokedexResult);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv_pokedex.setLayoutManager(layoutManager);
        rv_pokedex.setAdapter(adapter);
    }

    // Show Pokémon View
    public void showPokemon(View view) {
        Intent intent = new Intent(this, PokemonActivity.class);
        startActivity(intent);
    }

}
