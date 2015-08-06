package com.yahoo.googleimagesearch.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.yahoo.googleimagesearch.R;
import com.yahoo.googleimagesearch.adapters.ColorSpinnerAdapter;
import com.yahoo.googleimagesearch.models.ImageSearchSettings;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends DialogFragment {
    private EditText etSiteFilter;
    private Spinner spImageSize;
    private Spinner spImageType;
    private Spinner spImageColor;
    private ImageSearchSettings settings;
    private Button btnSave;
    private Button btnCancel;
    // endregion

    // region Listeners
    View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveSettings();
            getDialog().dismiss();
        }
    };
    View.OnClickListener cancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getDialog().dismiss();
        }
    };
    // endregion

    public SettingsFragment() {}

    public static SettingsFragment newInstance(ImageSearchSettings settings) {
        SettingsFragment frag = new SettingsFragment();
        Bundle args = new Bundle();
        args.putParcelable("settings", settings);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container);
        bindUIElements(view);
        setUpListeners();
        settings = getArguments().getParcelable("settings");

        // ToDo: Unable to make it work as expected (showing each spinner in a different color..)
        // decorateColorSpinner(view);

        // set dialog title
        getDialog().setTitle(R.string.settings_dialog_title);
        // set previous settings
        etSiteFilter.setText(settings.getSite());
        spImageSize.setSelection(settings.getSize());
        spImageType.setSelection(settings.getType());
        spImageColor.setSelection(settings.getColor());

        return view;
    }

    private void decorateColorSpinner(View view) {
        ColorSpinnerAdapter adapter = new ColorSpinnerAdapter(view.getContext());
        spImageColor.setAdapter(adapter);
    }

    private void bindUIElements(View view) {
        etSiteFilter = (EditText) view.findViewById(R.id.etSiteFilter);
        spImageSize = (Spinner) view.findViewById(R.id.spImageSize);
        spImageType = (Spinner) view.findViewById(R.id.spImageType);
        spImageColor = (Spinner) view.findViewById(R.id.spImageColor);
        btnSave = (Button) view.findViewById(R.id.btnSettingsSave);
        btnCancel = (Button) view.findViewById(R.id.btnSettingsCancel);
    }

    private void setUpListeners() {
        btnSave.setOnClickListener(saveClickListener);
        btnCancel.setOnClickListener(cancelClickListener);
    }

    public void saveSettings() {
        settings.setSite(etSiteFilter.getText().toString());
        settings.setSize(spImageSize.getSelectedItemPosition());
        settings.setType(spImageType.getSelectedItemPosition());
        settings.setColor(spImageColor.getSelectedItemPosition());
    }
}
