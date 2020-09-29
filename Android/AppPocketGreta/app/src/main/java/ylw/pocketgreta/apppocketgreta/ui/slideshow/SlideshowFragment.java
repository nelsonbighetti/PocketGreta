package ylw.pocketgreta.apppocketgreta.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import ylw.pocketgreta.apppocketgreta.ActivityWrittenOff;
import ylw.pocketgreta.apppocketgreta.HistoryActivity;
import ylw.pocketgreta.apppocketgreta.R;
import ylw.pocketgreta.apppocketgreta.ui.home.ChooseActivity;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        startActivity(new Intent(getActivity(), ChooseActivity.class));
        return null;
    }
}
