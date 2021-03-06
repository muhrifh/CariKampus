package id.carikampus.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.carikampus.R;
import id.carikampus.helper.CariKampusConstants;
import id.carikampus.helper.CariKampusMethods;
import id.carikampus.helper.Preferences;
import id.carikampus.model.Kampus;
import id.carikampus.model.KampusFavorit;
import id.carikampus.viewmodel.KampusFavoritViewModel;
import id.carikampus.viewmodel.KampusListViewModel;

public class KampusListFragment extends Fragment {

    private static final String TAG = "KampusListFragment";
    private static final String ARG_USER_ID = "id_user";

    private KampusListViewModel mKampusListViewModel;
    private RecyclerView mKampusRecyclerView;
    private KampusAdapter mAdapter;

    private KampusFavoritViewModel mKampusFavoritViewModel;
    private int mUserId = 0;

    private View mViewLayoutEmpty;
//    private View mViewLayoutEmptyFavorite;

    private List<Kampus> mKampuses;
    private EditText mCariKampusTextInput;

    public interface Callbacks {
        public void onKampusSelected(int idKampus);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach called");
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach called");
        mCallbacks = null;
    }

    private Callbacks mCallbacks = null;

    private void filterQuery(String query) {
        List<Kampus> kampuses = new ArrayList<>();

        for (Kampus kampus : mKampuses) {
            if (kampus.getNama_kampus().toLowerCase().contains(query.toLowerCase())) {
                kampuses.add(kampus);
            }
        }

        mAdapter.filterSearch(kampuses);
    }

    // Instance
    public static KampusListFragment newInstance(int idUser) {
        KampusListFragment fragment = new KampusListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_ID, idUser);
        fragment.setArguments(args);

        return fragment;
    }

    private void updateUI(List<Kampus> kampus) {
        mKampuses = kampus;
        mAdapter = new KampusAdapter(kampus);
        filterQuery("");
//        mKampusRecyclerView.setAdapter(mAdapter);

        Log.d(TAG, TAG + ".updateUI() Success " + kampus);
    }

    private void setFavoritAdapter(List<KampusFavorit> kampusFavorits) {
        mAdapter.setKampusFavorit(kampusFavorits);
        mKampusRecyclerView.setAdapter(mAdapter);
    }

    private void setAdapterRecylerView() {
        mKampusRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mKampusListViewModel = new ViewModelProvider(this).get(KampusListViewModel.class);
        mKampusFavoritViewModel = new ViewModelProvider(this).get(KampusFavoritViewModel.class);
        mAdapter = new KampusAdapter(Collections.<Kampus>emptyList());

        mUserId = (int) getArguments().getSerializable(ARG_USER_ID);
        Log.d(TAG, TAG + ".onCreate() Success");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_kampus, container, false);
        mViewLayoutEmpty = view.findViewById(R.id.empty_data);
//        mViewLayoutEmptyFavorite = view.findViewById(R.id.empty_favorite);
//        mCariKampusTextInput = view.findViewById(R.id.search_kampus);
        mKampusRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mKampusRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mKampusRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation));
        mKampusRecyclerView.setAdapter(mAdapter);
        setHasOptionsMenu(true);

        Log.d(TAG, TAG + ".onCreateView() Success");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setQueryHint("Cari Kampus");
//        searchView.setHint
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterQuery(newText.toString());
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mKampusListViewModel.getListKampus().observe(
                getViewLifecycleOwner(),
                new Observer<List<Kampus>>() {
                    @Override
                    public void onChanged(List<Kampus> kampus) {
                        // Update the cached copy of
                        updateUI(kampus);
                        Log.i(TAG, TAG + ".Got Kampus: " + kampus.size());
                    }
                }
        );
        mKampusFavoritViewModel.getListKampusFavorit(mUserId).observe(
                getViewLifecycleOwner(),
                new Observer<List<KampusFavorit>>() {
                    @Override
                    public void onChanged(List<KampusFavorit> kampusFavorits) {
                        setFavoritAdapter(kampusFavorits);
                    }
                });

        Log.d(TAG, TAG + ".onViewCreated() Success!");
    }

    /**
     * Kampus Adapter
     */
    public class KampusAdapter extends RecyclerView.Adapter<KampusHolder> {

        private List<Kampus> mKampusList;
        private List<KampusFavorit> mKampusFavoritList;

        private int lastPosition = -1;

        public KampusAdapter(List<Kampus> kampus) {
            mKampusList = kampus;
        }

        public void setKampusFavorit(List<KampusFavorit> kampusFavorits) {
            this.mKampusFavoritList = kampusFavorits;
        }

        public void filterSearch(List<Kampus> kampusList) {
            mViewLayoutEmpty.setVisibility((kampusList.size() == 0 ? View.VISIBLE : View.GONE));
            mKampusRecyclerView.setVisibility((kampusList.size() == 0 ? View.GONE : View.VISIBLE));

            this.mKampusList = kampusList;
            notifyDataSetChanged();
        }


        @Override
        public KampusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new KampusHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(KampusHolder holder, int position) {
            Kampus kampus = mKampusList.get(position);
            holder.bind(CariKampusMethods.isUserFavoriteKampus(kampus, mKampusFavoritList));

            setAnimation(holder.itemView, position);
        }

        @Override
        public int getItemCount() {
            return mKampusList.size();
        }

        private void setAnimation(View viewToAnimate, int position)
        {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition)
            {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.item_animation_fall_down);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }
    }

    /**
     * Kampus Holder
     */
    class KampusHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Kampus mKampus;
        private ImageView mLogoImageView;
        private TextView mNamaKampusTextView, mTotalProdiTextView, mAkreditasiTextView;
        private ToggleButton mToggleButton;

        public KampusHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_kampus, parent, false));
            mLogoImageView = (ImageView) itemView.findViewById(R.id.foto_logo_kampus);
            mNamaKampusTextView = (TextView) itemView.findViewById(R.id.text_nama_kampus);
            mTotalProdiTextView = (TextView) itemView.findViewById(R.id.text_total_prodi);
            mAkreditasiTextView = (TextView) itemView.findViewById(R.id.text_akreditasi);
            mToggleButton = (ToggleButton) itemView.findViewById(R.id.button_favorite);

            mNamaKampusTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), mKampus.getId() + " Clicked!", Toast.LENGTH_SHORT).show();
                    mCallbacks.onKampusSelected(mKampus.getId());
                }
            });

            mToggleButton.setEnabled(false);
        }

        public void bind(Kampus kampus) {
            mKampus = kampus;

            String uri = CariKampusConstants.URL_LOGO_KAMPUS + kampus.getFoto_logo();

            Glide.with(mLogoImageView.getContext())
                    .load(uri)
//                    .centerCrop()
                    //.transform(new RoundedCorners(5))
                    .placeholder(R.drawable.undraw_void)
                    .error(R.drawable.undraw_search)
                    .into(mLogoImageView);


            mNamaKampusTextView.setText(mKampus.getNama_kampus());
            mTotalProdiTextView.setText(mKampus.getTotal_prodi() + " Prodi");
            mAkreditasiTextView.setText("Akreditasi " + mKampus.getAkreditasi());
            mToggleButton.setChecked((mKampus.getLiked() == 1));
        }

        @Override
        public void onClick(View v) {
            mCallbacks.onKampusSelected(mKampus.getId());
            Toast.makeText(getActivity(), mKampus.getId() + " Clicked!", Toast.LENGTH_SHORT).show();
        }
    }
}
