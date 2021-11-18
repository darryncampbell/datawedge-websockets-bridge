package com.darryncampbell.datawedge_websockets_bridge;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import java.net.InetSocketAddress;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

public class MySocketServer extends WebSocketServer {

    private WebSocket mSocket;
    private static final String TAG = "Datawedge WS Bridge";
    private Intent bufferedScan = null;
    private Context context;
    private static final String datawedge_intent_key_source = "com.symbol.datawedge.source";
    private static final String datawedge_intent_key_label_type = "com.symbol.datawedge.label_type";
    private static final String datawedge_intent_key_data = "com.symbol.datawedge.data_string";

    public MySocketServer(InetSocketAddress address, Context context) {
        super(address);
        this.context = context;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        mSocket = conn;
        Log.i(TAG, "onOpen");
        if (bufferedScan != null && bufferedScan.getAction().equals(WebSocketIntentService.datawedge_intent_key_action))
        {
            sendScanToBrowser(bufferedScan);
            bufferedScan = null;
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.i(TAG, "onClose");
        mSocket = null;
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.i(TAG, "message: " + message);
        try {
            //  Proof of concept to control DataWedge via it's API, allowing it to be called from a socket
            JSONObject jsonObject = new JSONObject(message);
            String action = jsonObject.getString("action");
            String key = jsonObject.getString("extra_key");
            String value = jsonObject.getString("extra_value");
            if (action != null && key != null && value != null)
            {
                Intent dwIntent = new Intent();
                dwIntent.setAction(action);
                dwIntent.putExtra(key, value);
                context.sendBroadcast(dwIntent);
            }
        } catch (JSONException e) {
            Log.w(TAG, "Unable to parse JSON message from page");
            e.printStackTrace();
        }
        //mSocket.send("Hello from Android: " + DateFormat.getDateTimeInstance().format(new Date()));
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.e(TAG, "error: " + ex.getMessage());
    }

    public void sendScanToBrowser(Intent intent) {
        if (mSocket != null)
        {
            //  A barcode has been scanned
            String decodedSource = intent.getStringExtra(datawedge_intent_key_source);
            String decodedData = intent.getStringExtra(datawedge_intent_key_data);
            String decodedLabelType = intent.getStringExtra(datawedge_intent_key_label_type);
            String message = "Barcode (" + decodedData + ") [" + decodedLabelType + "]";
            Log.i(TAG, "Message to send: " + message);

            mSocket.send(message);
        }
        else {
            //  If the client has not yet connected to us but we have received a scan then buffer it
            //  and send it when we next connect
            setBufferedScan(intent);
            Log.w(TAG, "error - socket is null, buffering scan");
        }
    }

    public void setBufferedScan(Intent bufferedScan) {
        this.bufferedScan = bufferedScan;
    }
}