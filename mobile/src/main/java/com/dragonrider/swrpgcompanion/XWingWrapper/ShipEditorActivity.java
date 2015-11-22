package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.Classes.Database;
import com.dragonrider.swrpgcompanion.Classes.Weapon;
import com.dragonrider.swrpgcompanion.Classes.XmlImport;
import com.dragonrider.swrpgcompanion.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ShipEditorActivity extends Activity {

    public static Vehicle InnerShip;

    private EditText txtName;
    private TextView txtCategory;
    private EditText txtArmor;
    private EditText txtSilhouette;
    private EditText txtManoeuvrabilite;
    private EditText txtHull;
    private EditText txtStrain;
    private EditText txtAft;
    private EditText txtFore;
    private EditText txtPort;
    private EditText txtStarboard;
    private EditText txtSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_editor);



        txtName = (EditText) findViewById(R.id.txtName);
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InnerShip.setName(s.toString());
            }
        });

        txtCategory = (EditText) findViewById(R.id.txtCategory);
        txtCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InnerShip.getCategories().clear();
                for (String str : s.toString().split("[;]")){
                    InnerShip.getCategories().add(str);
                }
            }
        });



        txtArmor = (EditText) findViewById(R.id.txtArmor);
        txtArmor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InnerShip.setArmor(Integer.valueOf(s.toString()));
            }
        });

        txtSilhouette = (EditText) findViewById(R.id.txtSilhouette);
        txtSilhouette.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InnerShip.setSilhouette(Integer.valueOf(s.toString()));
            }
        });

        txtManoeuvrabilite = (EditText) findViewById(R.id.txtManever);
        txtManoeuvrabilite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InnerShip.setHandling(Integer.valueOf(s.toString()));
            }
        });

        txtSpeed = (EditText) findViewById(R.id.txtSpeed);
        txtSpeed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InnerShip.setSpeed(Integer.valueOf(s.toString()));
            }
        });

        txtHull = (EditText) findViewById(R.id.txtHull);
        txtHull.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InnerShip.setHullTrauma(Integer.valueOf(s.toString()));
            }
        });


        txtStrain = (EditText) findViewById(R.id.txtStrain);
        txtStrain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InnerShip.setSystemStrain(Integer.valueOf(s.toString()));
            }
        });


        txtAft = (EditText) findViewById(R.id.txtPoupe);
        txtAft.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InnerShip.setHandling(Integer.valueOf(s.toString()));
            }
        });


        txtFore = (EditText) findViewById(R.id.txtProue);
        txtFore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InnerShip.setHandling(Integer.valueOf(s.toString()));
            }
        });

        txtPort = (EditText) findViewById(R.id.txtBabord);
        txtPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InnerShip.setHandling(Integer.valueOf(s.toString()));
            }
        });
        txtStarboard = (EditText) findViewById(R.id.txtTribord);
        txtStarboard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InnerShip.setHandling(Integer.valueOf(s.toString()));
            }
        });
        txtManoeuvrabilite = (EditText) findViewById(R.id.txtManever);
        txtManoeuvrabilite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InnerShip.setHandling(Integer.valueOf(s.toString()));
            }
        });


        Hashtable<String, Integer> skillList = XmlImport.getDataSkills();
        Hashtable<String, Integer> ItemQualityList = XmlImport.getDataItemQualities();
        final Hashtable<String, Weapon> weaponList = XmlImport.getDataWeapons(skillList, ItemQualityList);

        findViewById(R.id.btnAddWeapon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ShipEditorActivity.this);
                final WeaponListAdapter adapter = new WeaponListAdapter(weaponList);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Weapon weapon = adapter.getWeaponAt(which);
                        VehicleWeapon wp = new VehicleWeapon().setBaseWeapon(weapon);
                        InnerShip.getWeapons().add(wp);
                        UpdateWeaponList();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setTitle("Ajouter une arme...");
                builder.show();
            }
        });


        if (InnerShip != null)
            OpenShip(InnerShip);
        else
            OpenShip(new Vehicle());
    }



    private class WeaponListAdapter extends BaseAdapter {



        private  List<String> ShownData;
        private List<Weapon> RealWeapons;
        /**
         * Constructor
         *
         * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
         *                 Maps contain the data for each row, and should include all the entries specified in
         *                 "from"
         */
        public WeaponListAdapter(Hashtable<String, Weapon> data) {


            ShownData = new ArrayList<>();
            RealWeapons = new ArrayList<>();
            for (Weapon wp : data.values()) {
                ShownData.add(wp.toString());
                RealWeapons.add(wp);
            }

        }

        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return ShownData.size();
        }

        /**
         * Get the data item associated with the specified position in the data set.
         *
         * @param position Position of the item whose data we want within the adapter's
         *                 data set.
         * @return The data at the specified position.
         */
        @Override
        public Object getItem(int position) {
            return ShownData.get(position);
        }


        public Weapon getWeaponAt(int position) {
            return RealWeapons.get(position);
        }
        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * Get a View that displays the data at the specified position in the data set. You can either
         * create a View manually or inflate it from an XML layout file. When the View is inflated, the
         * parent View (GridView, ListView...) will apply default layout parameters unless you use
         * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
         * to specify a root view and to prevent attachment to the root.
         *
         * @param position    The position of the item within the adapter's data set of the item whose view
         *                    we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *                    is non-null and of an appropriate type before using. If it is not possible to convert
         *                    this view to display the correct data, this method can create a new view.
         *                    Heterogeneous lists can specify their number of view types, so that this View is
         *                    always of the right type (see {@link #getViewTypeCount()} and
         *                    {@link #getItemViewType(int)}).
         * @param parent      The parent that this view will eventually be attached to
         * @return A View corresponding to the data at the specified position.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(parent.getContext());
            textView.setText(ShownData.get(position));
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 4, 4, 4);
            textView.setLayoutParams(params);
            return textView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ship_editor, menu);
        return true;
    }


    private void OpenShip(Vehicle v) {
        InnerShip = v;


        txtName.setText(InnerShip.getName());
        if (!InnerShip.getCategories().isEmpty()) {
            String str = "";
            for (String s : InnerShip.getCategories())
                str += s + ";";
            str = str.substring(0, str.length() - 1);

            txtCategory.setText(str);
        }
        txtAft.setText(String.valueOf(InnerShip.getDefAft()));
        txtArmor.setText(String.valueOf(InnerShip.getArmor()));
        txtFore.setText(String.valueOf(InnerShip.getDefFore()));
        txtPort.setText(String.valueOf(InnerShip.getDefPort()));
        txtStarboard.setText(String.valueOf(InnerShip.getDefStarboard()));
        txtHull.setText(String.valueOf(InnerShip.getHullTrauma()));
        txtStrain.setText(String.valueOf(InnerShip.getSystemStrain()));
        txtSilhouette.setText(String.valueOf(InnerShip.getSilhouette()));
        txtManoeuvrabilite.setText(String.valueOf(InnerShip.getHandling()));
        txtSpeed.setText(String.valueOf(InnerShip.getSpeed()));


        UpdateWeaponList();



    }




    private void UpdateWeaponList() {
        LinearLayout WeaponList = (LinearLayout) findViewById(R.id.WeaponList);

        WeaponList.removeAllViews();



        for (final VehicleWeapon wp : InnerShip.getWeapons()) {

            View baseView = LayoutInflater.from(this).inflate(R.layout.listitem_vehiculeweapon, WeaponList, false);


            ((TextView)baseView.findViewById(R.id.txtName)).setText(wp.getName());
            ((TextView)baseView.findViewById(R.id.txtName)).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    wp.setName(s.toString());
                }
            });




            WeaponList.addView(baseView);

        }
    }


    public void mnuOpenFromDatabaseClick(MenuItem item) {
        VehicleFighterAddPopup.Show(this, new VehicleFighterAddPopup.onSelectVehicleListener() {
            @Override
            public void onSelectVehicle(Vehicle vehicle) {
                OpenShip(vehicle);

            }
        }, false);
    }

    public void mnuSaveClick(MenuItem item) {
        Database db = new Database(this);
        db.SaveVehicle(InnerShip);
    }

    public void mnuNewShip(MenuItem item) {
        OpenShip(new Vehicle());
    }
}
