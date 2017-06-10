package com.darryncampbell.datawedge_websockets_bridge;

import android.app.IntentService;
import android.content.Intent;

import java.net.InetSocketAddress;

public class WebSocketIntentService extends IntentService {

    private static final int SERVER_PORT = 12345;
    private static final String SERVER_ADDRESS = "127.0.0.1";
    //  This MUST match the action defined in DataWedge which is sending us Intents (via startService).
    //  Note: This is also separately defined in the manifest
    public static final String datawedge_intent_key_action = "com.symbol.datawedge.barcode.ACTION";
    private static Boolean serverStarted = false;
    private static MySocketServer mServer;


    public WebSocketIntentService() {
        super("WebSocketIntentService");
        //  Try and stay alive as long as we can but this is no guarantee Android will not kill us
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            //  Android may kill the Intent Service, the logic here will awaken the IntentService when it
            //  receives a scan and send it when a client re-connects.
            if (!serverStarted)
            {
                //  Start the WebSocket Server
                mServer = new MySocketServer(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT), this);
                mServer.start();
                serverStarted = true;
            }
            final String action = intent.getAction();
            if (action != null && action.equals(datawedge_intent_key_action));
            {
                mServer.sendScanToBrowser(intent);
            }
        }
    }

}
