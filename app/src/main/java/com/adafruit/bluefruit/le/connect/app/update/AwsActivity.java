package com.adafruit.bluefruit.le.connect.app.update;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.adafruit.bluefruit.le.connect.R;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.BMSTESTDATADO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class AwsActivity extends AppCompatActivity {


    private final double TEST_VALUE = 123456;
    private final double TEST_TIME = 654321;

    private EditText mSendEditText;
    private TextView mReadDataTextView;


    private volatile double mSentData;

    private volatile double mReadData;

    private DynamoDBMapper dynamoDBMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uart);

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

        AWSCredentialsProvider credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();

        // Add code to instantiate a AmazonDynamoDBClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(configuration)
                .build();


        mReadDataTextView = findViewById(R.id.awsReadData);
        mSendEditText = findViewById(R.id.sendEditText);
        mSendEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    onClickSend(null);
                    return true;
                }

                return false;
            }
        });
        mSendEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    // Dismiss keyboard when sendEditText loses focus
                    dismissKeyboard(view);
                }
            }
        });
    }

    // region AWS

    public void createItem(double value) {
        final BMSTESTDATADO testItem = new BMSTESTDATADO();

        testItem.setVALUE(value);

        testItem.setTIME((double)System.currentTimeMillis());

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(testItem);
                // Item saved
            }
        }).start();
    }

    public BMSTESTDATADO readItem(final double time, final double value){

        BMSTESTDATADO item = new BMSTESTDATADO();
        item.setTIME(time);
        item.setVALUE(value);
        Load aws_read = new Load();
        aws_read.setItem(item);
        new Thread(aws_read).start();
        item = aws_read.getItem();
        return item;
    }

    public BMSTESTDATADO updateItem(double time, double value){
        final BMSTESTDATADO item = new BMSTESTDATADO();

        item.setTIME(time);
        item.setVALUE(value);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(item);
            }
        }).start();
        return item;
    }

    public void deleteItem(final double time, final double value){
        new Thread(new Runnable() {
            @Override
            public void run() {
                BMSTESTDATADO item = new BMSTESTDATADO();
                item.setTIME(time);
                item.setVALUE(value);
                dynamoDBMapper.delete(item);
            }
        }).start();
    }


    public class Load implements Runnable{
        BMSTESTDATADO item = new BMSTESTDATADO();

        public void setItem(BMSTESTDATADO item) {
            this.item = item;
        }

        @Override
        public void run(){
            item = dynamoDBMapper.load(
                    BMSTESTDATADO.class,
                    item.getTIME(),
                    item.getVALUE());
        }

        public BMSTESTDATADO getItem() {
            return item;
        }
    }



    // end region




    // Region Layout
    public void onClickRefresh(View view){
        mReadData = readItem(TEST_TIME, mSentData).getVALUE();
        updateUI();
    }

    public void onClickSend(View view) {
        String data = mSendEditText.getText().toString();
        mSentData = Integer.parseInt(data);

        mSendEditText.setText("");

        updateItem(TEST_TIME, mSentData);

    }

    public void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateUI() {
        mReadDataTextView.setText(String.format(getString(R.string.aws_readdata_format), mReadData));
    }

}
