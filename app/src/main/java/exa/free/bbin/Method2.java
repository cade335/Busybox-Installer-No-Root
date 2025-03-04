package exa.free.bbin;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import exa.free.interfaces.AppSelector;

public class Method2 extends Fragment implements AppSelector {

    Context context;
    List<ApplicationAdapterListItem> applicationAdapterListItems;
    TerminalChooserAdapter terminalChooserAdapter;
    SharedPreferences sharedPreferences;
    ListView listView;
    AlertDialog.Builder alertDialog;
    AlertDialog alert;
    Button button;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    TextView textView3;
    TextView textView5;
    ProgressDialog mProgressDialog;
    String s;
    String s2;
    InterstitialAd mInterstitialAd;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.method2, container, false);

        context = getActivity().getApplicationContext();

        sharedPreferences = context.getSharedPreferences("GlobalPreferences", 0);
        s = sharedPreferences.getString("ChoosenTerminal", "None");

        button = view.findViewById(R.id.button);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        button4 = view.findViewById(R.id.button4);
        button5 = view.findViewById(R.id.button5);
        textView3 = view.findViewById(R.id.textView3);
        textView5 = view.findViewById(R.id.textView5);

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-5748356089815497/5836001022");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Connecting...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);

        if(Build.VERSION.SDK_INT >= 21){
            s2 = Build.SUPPORTED_ABIS[0];
        }else{
            s2 = Build.CPU_ABI;
        }

        if(s.equals("None")){
            textView3.setText("Step 3 : Please Choose a Terminal Emulator App first");
            textView5.setText("Step 5 : Please Choose a Terminal Emulator App first");
            button3.setEnabled(false);
            button5.setEnabled(false);
        }else{
            if(s2.equals("arm64-v8a")){
                textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
            }else if (s2.contains("arm")){
                textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
            }else if(s2.equals("x86")){
                textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
            }else if(s2.equals("x86_64")){
                textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
            }else if(s2.equals("mips")){
                textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
            }else if(s2.equals("mips64")){
                textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Install().execute();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAppsDialog();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInterstitialAd != null && mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else{
                    ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    if(s2.equals("arm64-v8a")){
                        ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                        clipboard.setPrimaryClip(clip);
                    }else if (s2.contains("arm")){
                        ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                        clipboard.setPrimaryClip(clip);
                    }else if(s2.equals("x86")){
                        ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                        clipboard.setPrimaryClip(clip);
                    }else if(s2.equals("x86_64")){
                        ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                        clipboard.setPrimaryClip(clip);
                    }else if(s2.equals("mips")){
                        ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                        clipboard.setPrimaryClip(clip);
                    }else if(s2.equals("mips64")){
                        ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " &&  chmod 755 busybox");
                        clipboard.setPrimaryClip(clip);
                    }
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = sharedPreferences.getString("ChoosenTerminal", "None");
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(s);
                if(isPackageInstalled(s, context.getPackageManager())){
                    startActivity(intent);
                }else{
                    Toast.makeText(context, "Oops, looks like the application has been uninstalled or hidden, please reinstall/enable it, or choose another Terminal Emulator App", Toast.LENGTH_LONG).show();
                }
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                if(s2.equals("arm64-v8a")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s);
                    clipboard.setPrimaryClip(clip);
                }else if (s2.contains("arm")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s);
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("x86")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s);
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("x86_64")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s);
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("mips")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s);
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("mips64")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s);
                    clipboard.setPrimaryClip(clip);
                }
            }
        });
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                if(s2.equals("arm64-v8a")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                    clipboard.setPrimaryClip(clip);
                }else if (s2.contains("arm")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("x86")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("x86_64")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("mips")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("mips64")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " &&  chmod 755 busybox");
                    clipboard.setPrimaryClip(clip);
                }
            }
        });

        return view;
    }
    @Override
    public void selectApp(final String packageName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ChoosenTerminal", packageName);
        editor.apply();
        s = sharedPreferences.getString("ChoosenTerminal", "None");
        if(s2.equals("arm64-v8a")){
            textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
            textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
        }else if (s2.contains("arm")){
            textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
            textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
        }else if(s2.equals("x86")){
            textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
            textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
        }else if(s2.equals("x86_64")){
            textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
            textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
        }else if(s2.equals("mips")){
            textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
            textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
        }else if(s2.equals("mips64")){
            textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && mv " + context.getExternalFilesDir(null) + "/busybox " + "/data/data/" + s + " && chmod 755 busybox");
            textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
        }
        button3.setEnabled(true);
        button5.setEnabled(true);
        alert.dismiss();
    }
    @Override
    public void removeApp(String packageName){
    }
    @Override
    public boolean isSelected(String packageName){
        return false;
    }
    public void showAppsDialog(){
        final ViewGroup nullParent = null;
        alertDialog = new AlertDialog.Builder(getActivity());
        alert = alertDialog.create();
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.notify2, nullParent);
        listView = view.findViewById(R.id.listView);

        alert.setView(view);
        alert.show();
        new InitializeApps().execute();
    }
    private class Install extends AsyncTask<Void, Void, Void> {
        String s;
        final ViewGroup nullParent = null;
        private AlertDialog.Builder builder;
        private AlertDialog alertDialog;
        private ProgressDialog dialog;

        private Install() {
            this.builder = null;
            this.alertDialog = null;
            this.dialog = null;
        }

        protected void onPreExecute() {
            if(Build.VERSION.SDK_INT >= 26){
                this.builder = new AlertDialog.Builder(getActivity());
                this.alertDialog = builder.create();
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View view = layoutInflater.inflate(R.layout.progress_bar, nullParent);
                this.alertDialog.setView(view);
                this.alertDialog.setCancelable(false);
                this.alertDialog.show();
                TextView textView = view.findViewById(R.id.textView);
                textView.setText(R.string.installing);
            }else{
                this.dialog = new ProgressDialog(getActivity());
                this.dialog.setMessage(getString(R.string.installing));
                this.dialog.setIndeterminate(true);
                this.dialog.setCancelable(false);
                this.dialog.show();
            }
        }

        protected Void doInBackground(Void... params) {
            if(Build.VERSION.SDK_INT >= 21){
                s = Build.SUPPORTED_ABIS[0];
            }else{
                s = Build.CPU_ABI;
            }
            if(s.equals("arm64-v8a")){
                CreateFile("busybox", R.raw.busybox_arm64);
            }else if (s.contains("arm")){
                CreateFile("busybox", R.raw.busybox_arm);
            }else if(s.equals("x86")){
                CreateFile("busybox", R.raw.busybox_x86);
            }else if(s.equals("x86_64")){
                CreateFile("busybox", R.raw.busybox_amd64);
            }else if(s.equals("mips")){
                CreateFile("busybox", R.raw.busybox_mips);
            }else if(s.equals("mips64")){
                CreateFile("busybox", R.raw.busybox_mips64);
            }else{
                Toast.makeText(context, "Sorry, your device is not supported !", Toast.LENGTH_LONG).show();
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            if(Method2.this.isVisible()){
                if(Build.VERSION.SDK_INT >= 26){
                    this.alertDialog.dismiss();
                }else{
                    this.dialog.dismiss();
                }
            }
        }
    }
    private class InitializeApps extends AsyncTask<Void, Void, Void> {
        final ViewGroup nullParent = null;
        private AlertDialog.Builder builder;
        private AlertDialog alertDialog;
        private ProgressDialog dialog;
        private boolean shouldShowSystemApps;

        private InitializeApps() {
            this.builder = null;
            this.alertDialog = null;
            this.dialog = null;
        }

        protected void onPreExecute() {
            if(Build.VERSION.SDK_INT >= 26){
                this.builder = new AlertDialog.Builder(getActivity());
                this.alertDialog = builder.create();
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View view = layoutInflater.inflate(R.layout.progress_bar, nullParent);
                this.alertDialog.setView(view);
                this.alertDialog.setCancelable(false);
                this.alertDialog.show();
                TextView textView = view.findViewById(R.id.textView);
                textView.setText("Working");
                shouldShowSystemApps = sharedPreferences.getBoolean("ShouldShowSystemApps", false);
            }else{
                this.dialog = new ProgressDialog(getActivity());
                this.dialog.setMessage("Working");
                this.dialog.setIndeterminate(true);
                this.dialog.setCancelable(false);
                this.dialog.show();
            }
        }
        protected Void doInBackground(Void... params) {
            if(shouldShowSystemApps){
                List<ApplicationAdapterListItem> applicationAdapterListItems = getAllApps();
                terminalChooserAdapter = new TerminalChooserAdapter(context, Method2.this, applicationAdapterListItems);
            }else{
                List<ApplicationAdapterListItem> applicationAdapterListItems = getUserApps();
                terminalChooserAdapter = new TerminalChooserAdapter(context, Method2.this, applicationAdapterListItems);
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            if(Method2.this.isVisible()){
                if(Build.VERSION.SDK_INT >= 26){
                    this.alertDialog.dismiss();
                }else{
                    this.dialog.dismiss();
                }
                listView.setAdapter(terminalChooserAdapter);
            }
        }
    }
    private List<ApplicationAdapterListItem> getUserApps(){
        applicationAdapterListItems = new ArrayList<>();
        List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);
        final PackageItemInfo.DisplayNameComparator comparator = new PackageItemInfo.DisplayNameComparator(context.getPackageManager());
        Collections.sort(packageInfos, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo one, PackageInfo two) {
                return comparator.compare(one.applicationInfo, two.applicationInfo);
            }
        });
        for(int i = 0; i < packageInfos.size(); i++){
            PackageInfo packageInfo = packageInfos.get(i);
            if(!isSystemPackage(packageInfo)){
                if(isApplicationExistOnLauncher(packageInfo.applicationInfo.packageName)){
                    String appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
                    String packageName = packageInfo.applicationInfo.packageName;
                    Drawable icon = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
                    applicationAdapterListItems.add(new ApplicationAdapterListItem(appName, packageName, icon));
                }
            }
        }
        return applicationAdapterListItems;
    }
    private List<ApplicationAdapterListItem> getAllApps(){
        applicationAdapterListItems = new ArrayList<>();
        List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);
        final PackageItemInfo.DisplayNameComparator comparator = new PackageItemInfo.DisplayNameComparator(context.getPackageManager());
        Collections.sort(packageInfos, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo one, PackageInfo two) {
                return comparator.compare(one.applicationInfo, two.applicationInfo);
            }
        });
        for(int i = 0; i < packageInfos.size(); i++){
            PackageInfo packageInfo = packageInfos.get(i);
            if(isApplicationExistOnLauncher(packageInfo.applicationInfo.packageName)){
                String appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
                String packageName = packageInfo.applicationInfo.packageName;
                Drawable icon = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
                applicationAdapterListItems.add(new ApplicationAdapterListItem(appName, packageName, icon));
            }
        }
        return applicationAdapterListItems;
    }
    private void CreateFile(String filename, int resource) {
        try {
            InputStream in = getResources().openRawResource(resource);
            FileOutputStream out = new FileOutputStream(context.getExternalFilesDir(null) + "/" + filename);
            byte[] buff = new byte[1024];
            int read = 0;

            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }
        } catch (IOException e) {
            Log.e("error", "Failed to copy file: " + filename, e);
        }
    }
    private boolean isSystemPackage(PackageInfo packageInfo) {
        return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
    private boolean isApplicationExistOnLauncher(String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            if(packageName.equals(resolveInfo.activityInfo.packageName)){
                return true;
            }
        }
        return false;
    }
    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
