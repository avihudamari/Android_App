package com.pjct.ddb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import com.pjct.ddb.Entities.Parcel;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pjct.ddb.Entities.Enums.*;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinnerType;
    Spinner spinnerBreakable;
    Spinner spinnerPackageWeight;
    TextView textViewLocation;
    TextInputEditText TextInputEditTextPhoneNumber;
    // Acquire a reference to the system Location Manager
    LocationManager locationManager;
    Location parcelLocation;

    // Define a listener that responds to location updates
    LocationListener locationListener;
    //init DB
    static DatabaseReference ParcelsRef;
    static List<Parcel> parcelList;
    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://dblogisticare.firebaseio.com/");
        ParcelsRef = database.getReference("parcels");
      // ParcelsRef.setValue("Hello, World!");
        parcelList = new ArrayList<>();
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getLocation();
    }

    @Override
    public void onClick(View v) {
        // check if the number is valid
        if(TextInputEditTextPhoneNumber.length() <10){

            Toast.makeText(getApplicationContext(),"Invalid phone number",Toast.LENGTH_SHORT).show();
            return;
        }
        Button sendButton = (Button)findViewById(R.id.sendButton);
        sendButton.setClickable(false);
        Parcel parcel = getParcelFromView();
        String key = ParcelsRef.push().getKey();
        parcel.setKey(key);
        ParcelsRef.child(key).setValue(parcel);

        ///  clear the feilds
        clearFileds();

        // send to firebase
        sendButton.setClickable(true);

        // msg to user that parcel added successfuly to the system
        Toast.makeText(getApplicationContext(), "The parcel added successfully.", Toast.LENGTH_SHORT).show();;
    }


    private  void clearFileds(){
        TextInputEditTextPhoneNumber.setText("");
        spinnerType.setSelection(0);
        spinnerBreakable.setSelection(0);
        spinnerPackageWeight.setSelection(0);
    }


    private void initView() {
        // spinnerType for type
        spinnerType = (Spinner) findViewById(R.id.type_spinner);
        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this,R.array.type_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        //spinnerType for breakable
         spinnerBreakable = (Spinner) findViewById(R.id.breakable_spinner);
         ArrayAdapter<CharSequence> adapter_breakable;
         adapter_breakable = ArrayAdapter.createFromResource(this,R.array.breakable_list, android.R.layout.simple_spinner_item);
         adapter_breakable.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinnerBreakable.setAdapter(adapter_breakable);

        //spinnerType for package weight
        spinnerPackageWeight = (Spinner) findViewById(R.id.package_weight_spinner);
        ArrayAdapter<CharSequence> adapter_package_weight;
        adapter_package_weight = ArrayAdapter.createFromResource(this,R.array.Package_weight_list, android.R.layout.simple_spinner_item);
        adapter_package_weight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPackageWeight.setAdapter(adapter_package_weight);
        parcelLocation = new Location("a");
        //textViewDeliverymanPhone for location
        textViewLocation = (TextView)findViewById(R.id.locationTextView);

        //TextInputEditText for phone number
        TextInputEditTextPhoneNumber = (TextInputEditText)findViewById(R.id.TextInputEditTextPhoneNumber);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //    Toast.makeText(getBaseContext(), location.toString(), Toast.LENGTH_LONG).show();
                parcelLocation.setLatitude(location.getLatitude());
                parcelLocation.setLongitude(location.getLongitude());
                textViewLocation.setText(getPlace(location));////location.toString());

                // Remove the listener you previously added
                //  locationManager.removeUpdates(locationListener);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }


    private void getLocation() {

        //     Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);

        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
         //   stopUpdateButton.setEnabled(true);
          //  getLocationButton.setEnabled(false);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }


    public String getPlace(Location location) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses.size() > 0) {
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                return cityName;
            }

            return "no place: \n (" + location.getLongitude() + " , " + location.getLatitude() + ")";
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }
        return "IOException ...";
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the location", Toast.LENGTH_SHORT).show();
            }
        }

    }



    private Parcel getParcelFromView() {
        PackType thisParcelType = PackType.values()[(int) spinnerType.getSelectedItemId()];
        boolean thisParcelBreakable;
        if (spinnerBreakable.getSelectedItemId() ==0){
            thisParcelBreakable = true;
        }else{
            thisParcelBreakable = false;
        }

        //packageWeight
        PackageWeight thisParcelPackageWeight =  PackageWeight.values()[(int) spinnerPackageWeight.getSelectedItemId()];

        //phoneNumber (receiver)
        String thisParcelPhoneNumber = TextInputEditTextPhoneNumber.getText().toString();

        //dateSend
        Date dateSend = new Date(); //return the current date

        //status
        PackStatus status = PackStatus.SENT;

        //No need date recived and Phone reciver.. its set in future.

        Parcel parcel = new Parcel(null,thisParcelType,thisParcelBreakable,thisParcelPackageWeight,
                           null,thisParcelPhoneNumber,dateSend,status);
        parcel.setLongitude(parcelLocation.getLongitude());
        parcel.setLatitude(parcelLocation.getLatitude());
        parcel.setDateReceived(new Date());
        return parcel;

    }

    //convert date to string
    public String convertDateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);
    }



    // create the menu on the bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }




    // func for menu in the bar

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.Parcels:
               // Toast.makeText(this, "ddd", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ParcelsHistory.class));
                break;
            case R.id.qr_code_scan:
                Intent intent = new Intent(MainActivity.this, QrCodeActivity.class);
                //intent.putExtra("oldValue", "valueYouWantToChange");
                startActivityForResult(intent, 0); //I always put 0 for someIntVal
            break;
                default:break;
        }
        return true;
    }



    // in case , that user scan qr code , add parcel from qr results
    protected void addParcelFromQr(String string){

        String[] arrOfStr = string.split(",", 4);
        if (arrOfStr.length != 4){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            return;
        }

        int index1 = 0;
        int index2 =0;
        int index3 = 0;
        switch (arrOfStr[0]){

            case "0":index1 = 0;
            break;
            case "1":index1 =1;
            break;
            case  "2":index1 =2;
            break;
            default: Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            return;

        }

        switch (arrOfStr[1]){
            case "0":index2 =0;
            break;
            case "1":index2 =1;
            break;
           default:
               Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
               return;
        }
        switch (arrOfStr[2]){
            case "0":index3 =0;
                break;
            case "1":index3 =1;
                break;
            case "2":index3 =2;
            break;
            case "3":index3 = 3;
            default:
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                return;
        }
        if(arrOfStr[3].length() != 10){
             Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            return;
        }

        spinnerType.setSelection(index1);
        spinnerBreakable.setSelection(index2);
        spinnerPackageWeight.setSelection(index3);
        TextInputEditTextPhoneNumber.setText(arrOfStr[3]);
        onClick(null);
    }


    // get results from qr activity scan
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // in try  ,because if no t, in the case user push on "back" button , its throw Exception
        try{
            super.onActivityResult(requestCode, resultCode, data);
            String editTextValue = data.getStringExtra("Qr");
            addParcelFromQr(editTextValue);
        }catch (Exception e){

        }



    }

}
