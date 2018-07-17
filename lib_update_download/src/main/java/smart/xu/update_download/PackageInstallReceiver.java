package smart.xu.update_download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class PackageInstallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("PackageInstallReceiver-"+intent.getDataString());
		String pk = intent.getDataString().substring(8);
		System.out.println("PackageInstallReceiver-"+pk);
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		String config_pk = sp.getString(pk, null);
		if(pk !=null && pk.equals(config_pk)){
			sp.edit().putBoolean("token", true).commit();
		}
	}//

}
