package com.adi.example.first;

public class Home extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        DI di = new DI(R.raw.services, getApplicationContext());

        Picture picture = di.getService("Picture");
        picture.isCameraAvailable();
    }
}
