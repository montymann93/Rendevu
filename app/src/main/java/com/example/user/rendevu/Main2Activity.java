package com.example.user.rendevu;
/*
    Ricardo Cantu
    This class holds the configurations for the tab view.
    This implements each of the three fragment.
 */
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class Main2Activity extends AppCompatActivity implements MyDialogFragment.UserNameListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    /*
    *
    *referencing firebase for data storage
    * */
    private DatabaseReference myDatabaseReference;
    private String personId;
    private static final int REQUEST_INVITE = 0;  //used for sending invites

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFinishUserDialog(String addedName, String addedNum) {
        /*
        * Contact name and Phone number added in dialog are passed here
        * to be inserted into firebase.
        * */

        try {
            Toast.makeText(this, "ADDED: " + addedName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            getMenuInflater().inflate(R.menu.menu_main2, menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        try {
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onOptionsItemSelected(item);
    }


    /*
    * Josh
    * Handler for sending invitation when send invite
    * button is clicked.
    *
    * */
    public void onInviteClicked (View v){
        try {
            Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                    .setMessage(getString(R.string.invitation_message))
                    .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                    .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                    .setCallToActionText(getString(R.string.invitation_cta))
                    .build();
            startActivityForResult(intent, REQUEST_INVITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            String TAG = "";
            Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

            if (requestCode == REQUEST_INVITE) {
                if (resultCode == RESULT_OK) {
                    // Get the invitation IDs of all sent messages
                    String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                    for (String id : ids) {
                        Log.d(TAG, "onActivityResult: sent invitation " + id);
                    }
                } else {
                    // Sending failed or it was canceled, show failure message to the user
                    // ...
                    Toast.makeText(getApplicationContext(), "Failed Invite!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    * Josh
    *
    * When logout button is pressed,
    * user is sent back to the main screen.
    * */
    public void onLogoutClick(View vu){
        try {
            Intent intent = new Intent(Main2Activity.this, MainActivity.class);
            startActivity(intent);
            finish();  //closes current activity before moving to the next.
            Toast.makeText(getApplicationContext(), "You Are Now Logged Out......Goodbye", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /*
    * Josh
    * Controls for the pop-up dialog fragment,
    * could probably be useful for something else
    *
    * */
    public void onClick(View view) {
        // close existing dialog fragments
        try {
            android.app.FragmentManager manager = getFragmentManager();
            android.app.Fragment frag = manager.findFragmentByTag("fragment_edit_name");
            if (frag != null) {
                manager.beginTransaction().remove(frag).commit();
            }
            switch (view.getId()) {
                case R.id.showCustomFragment:
                    MyDialogFragment editNameDialog = new MyDialogFragment();
                    editNameDialog.show(manager, "fragment_edit_name");
                    break;

                //generic alert fragment
                case R.id.showAlertDialogFragment:
                    MyAlertDialogFragment alertDialogFragment = new MyAlertDialogFragment();
                    alertDialogFragment.show(manager, "fragment_edit_name");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *  A placeholder fragment containing a simple view.  THIS FRAGMENT IS NOT USED.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = null;
            try {
                fragment = new PlaceholderFragment();
                Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = null;
            try {
                rootView = inflater.inflate(R.layout.fragment_main2, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rootView;
        }
    }

    /**
     *  THIS IS THE FRAGMENT THAT CONTAINS THE VIEW FOR THE CONTACT TAB.
     *  Ricardo Cantu
     */
    public static class ContactTabFragment extends Fragment {

        private List<Person> persons;

        public ContactTabFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.contact_tab, container, false);
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);

            initializeData();
            cardAdapter adapter = new cardAdapter(persons);
            recyclerView.setAdapter(adapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            return rootView;
        }

        private void initializeData() {
            persons = new ArrayList<>();
            persons.add(new Person("Andrew Garfield", "34 years old", R.drawable.contact_pic));
            persons.add(new Person("Emma Stone", "29 years old", R.drawable.contact_pic));
            persons.add(new Person("Rhys Ifans", "50 years old", R.drawable.contact_pic));
            persons.add(new Person("Denis Leary", "60 years old", R.drawable.contact_pic));
            persons.add(new Person("Martin Sheen", "77 years old", R.drawable.contact_pic));
            persons.add(new Person("Sally Field", "71 years old", R.drawable.contact_pic));
            persons.add(new Person("Irrfan Khan", "50 years old", R.drawable.contact_pic));
            persons.add(new Person("Campbell Scott", "56 years old", R.drawable.contact_pic));
            persons.add(new Person("Embeth Davidtz", "52 years old", R.drawable.contact_pic));
            persons.add(new Person("Chris Zylka", "29 years old", R.drawable.contact_pic));
            persons.add(new Person("Max Charles", "25 years old", R.drawable.contact_pic));
            persons.add(new Person("C. Thomas Howell", "30 years old", R.drawable.contact_pic));
        }
    }

    /**
     *  THIS IS THE FRAGMENT THAT CONTAINS THE VIEW FOR THE MAIN SCREEN TAB.
     *  Tamim Alekozai
     */
    public static class MainScreenTabFragment extends Fragment{

        public MainScreenTabFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            try {
                super.onCreate(savedInstanceState);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = null;
            try {
                rootView = inflater.inflate(R.layout.mainscreen_tab, container, false);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rootView;
        }

    }

    /**
     *  THIS IS THE FRAGMENT THAT CONTAINS THE VIEW FOR THE SETTINGS TAB.
     *  Alexander Mann
     */
    public static class SettingsTabFragment extends Fragment{

        public SettingsTabFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            try {
                super.onCreate(savedInstanceState);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = null;
            try {
                rootView = inflater.inflate(R.layout.settings_tab, container, false);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rootView;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter{

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            try {
                switch (position){
                    case 0:
                        return new ContactTabFragment();
                    case 1:
                        return new MainScreenTabFragment();
                    case 2:
                        return new SettingsTabFragment();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            try {
                switch (position) {
                    case 0:
                        return "Contacts";
                    case 1:
                        return "Main";
                    case 2:
                        return "Settings";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
