package ylw.pocketgreta.apppocketgreta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends ListActivity {
    List<String> historyList = new ArrayList<String>();
    String[] historystr;
    ListView listView;
    boolean flag;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataServerReload();

    }
    public void functionAdapter(){

        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, historystr);
        setListAdapter(adapter);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          //      android.R.layout.simple_spinner_item, historyList);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //listView.setAdapter(adapter);

    }
    public void DataServerReload(){

        Singleton singleton = Singleton.getInstance();
        String tokenAuth = singleton.getTokenAuth();
        AndroidNetworking.get("http://457f5a38fd2e.ngrok.io/rest/bonuses/history")
                .addHeaders("Authorization", "Bearer "+tokenAuth)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int j=0;
                        historystr = new String[response.length()];
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject employee = null;
                            try {
                                employee = response.getJSONObject(i);
                                Instant instant = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    instant = Instant.parse(employee.getString("date"));
                                }
                                String Date = instant.toString();
                                int bonuses = employee.getInt("bonuses");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                                Date date = dateFormat.parse(Date);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd.mm.yyyy hh:mm:ss");
                                String folderName = formatter.format(date);
                                historystr[j]=date.toString()+System.getProperty ("line.separator")+"Bonuses: "+String.valueOf(bonuses);
                                flag=true;
                                functionAdapter();
                                j++;

                            } catch (JSONException e) {

                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("PPP","p");
                        flag=false;
                        // handle error
                    }
                });

        //Отправляйем запрос, получаем инфу.

    }
}
