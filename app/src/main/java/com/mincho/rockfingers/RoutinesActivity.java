package com.mincho.rockfingers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mincho.rockfingers.been.RoutineBeen;
import com.mincho.rockfingers.been.RoutinesBeen;
import com.mincho.rockfingers.database.MainDBNew;
import com.mincho.rockfingers.database.RoutineDB;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class RoutinesActivity extends AppCompatActivity {

    public static ArrayList<RoutineBeen> allRoutine;
    public static ArrayList<RoutinesBeen> allRoutines;
    //база данни
    private static MainDBNew mainDB;
    private RoutineDB rDB;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);

        // инициализиране на база данни
        mainDB = new MainDBNew(getApplicationContext());
        mainDB.open();

        rDB = new RoutineDB(getApplicationContext());
        rDB.open();

        allRoutine = mainDB.selectAllRoutine();
        allRoutines = rDB.getAllRoutines();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        ImageButton leftButton = (ImageButton) findViewById(R.id.left_button);
        ImageButton rightButton = (ImageButton) findViewById(R.id.right_button);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.arrowScroll(View.FOCUS_LEFT);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.arrowScroll(View.FOCUS_RIGHT);
            }
        });


        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        */

    }

    @Override
    protected void onDestroy() {
        mainDB.close();
        rDB.close();
        super.onDestroy();
    }


    /**
     * A placeholder fragment containing a simple view.
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
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            boolean isRoutine = false;
            View rootView = inflater.inflate(R.layout.fragment_routines, container, false);
          //  RoutineBeen crb = null;
            RoutinesBeen cr = allRoutines.get(getArguments().getInt(ARG_SECTION_NUMBER) - 1);
            for (int i = 0; i < allRoutine.size(); i++) {
                if (allRoutine.get(i).getNameRoutine().equalsIgnoreCase(cr.getNameRoutine())) {
                    isRoutine = true;
                   // crb = allRoutine.get(i);
                }
            }
            //name routine
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(cr.getNameRoutine());

            TextView routineDescrTV = (TextView) rootView.findViewById(R.id.short_descr);
            routineDescrTV.setText(cr.getDecrRoutine());
            //board picture
            ImageView campPic = (ImageView) rootView.findViewById(R.id.routine_bord_pic);

            Button b1 = (Button) rootView.findViewById(R.id.routine_button_1);
            Button b2 = (Button) rootView.findViewById(R.id.routine_button_2);

            // Log.e("bb",bb.getPictureBoard());
            if (cr.getPicRoutine().equalsIgnoreCase("none")) {
                //  Log.e("bb","gone");
                campPic.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) b1.getLayoutParams();
                //Log.v("mar", String.valueOf(params.topMargin));
                params.topMargin = params.topMargin * 3;
                //Log.v("mar", String.valueOf(params.topMargin));
                b1.setLayoutParams(params);

            } else {
                //  Log.e("bb","else");
                //инициализация на снимка на борда
                campPic.setVisibility(View.VISIBLE);
                campPic.setImageResource(cr.getIdPictureRoutine(getActivity()));
            }


            if (isRoutine) {
                b1.setText(R.string.button_reset);
                b1.setVisibility(View.VISIBLE);
                b2.setText(R.string.button_edit);
                b2.setVisibility(View.VISIBLE);
               // final int idR = crb.getIdRoutine();
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent test = new Intent(getContext(), EditRoutineActivity.class);
                       // test.putExtra("routine", idR);
                      //  startActivityForResult(test, 1);
                        alertLog();
                    }
                });
            } else {
                b1.setText(R.string.button_add);
                b1.setVisibility(View.VISIBLE);
            }

            final String nicNameR = cr.getNicNameRoutine();
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().setResult(RESULT_OK);
                    try {
                        mainDB.getClass().getDeclaredMethod("add" + nicNameR).invoke(mainDB);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    //mainDB.set3DSimulator();
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(intent);

                }
            });

            return rootView;
        }
        private void alertLog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);

            builder.setTitle(getContext().getString(R.string.title_alert_buy));
            builder.setMessage(getContext().getString(R.string.message_alert_buy));
            builder.setCancelable(false);
            //startPause();

            builder.setPositiveButton(getContext().getString(R.string.buy_button), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Code that is executed when clicking YES
                    dialog.dismiss();
                    //Here Add Firebase LogEvent
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "buy_pro");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "new user");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
                    RockFingerApplication.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.mincho.rockfingerspro"));
                    startActivity(intent);

                }

            });

            builder.setNegativeButton(getContext().getString(R.string.cancel_button), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Code that is executed when clicking NO
                    dialog.dismiss();
                }

            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.

            return allRoutines.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}