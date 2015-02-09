package pap.tuozon.com.pictureautoplayer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pap.tuozon.com.pictureautoplayer.view.PictureAutoPlayerView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PictureAutoPlayerView papv = (PictureAutoPlayerView)findViewById(R.id.pictureplay);
        List<View> views = new ArrayList<View>();
        int[] resIds = {R.drawable.i1,R.drawable.i2,R.drawable.i3,R.drawable.i4};
        papv.setImageResources(resIds);
        papv.setOnPageClickListener(new PictureAutoPlayerView.OnPageClickListener() {
            @Override
            public void onPageClick(int position) {
                Toast.makeText(getBaseContext(), "#onPageClick position = " + position, Toast.LENGTH_SHORT).show();
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

}
