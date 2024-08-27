//package com.example.womansafetyapp;
//
//import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Build;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Map;
//
//
//public class FirebaseNotification extends FirebaseMessagingService {
//
//
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        if (Build.VERSION.SDK_INT >= 26) {
//            ((NotificationManager) getSystemService(NotificationManager.class))
//                    .createNotificationChannel(new NotificationChannel("pdfreader", "pdfreader", NotificationManager.IMPORTANCE_DEFAULT));
//        }
//        if (remoteMessage.getData().isEmpty()) {
//            Log.d("Exception", "simple: " + remoteMessage.getNotification().getTitle());
//            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
//        } else {
//            Log.d("Exception", "Data: " + remoteMessage.getData().get("data"));
//
//            showNotification(remoteMessage.getData());
//        }
//    }
//
//
//    public Bitmap getBitmapfromUrl(String imageUrl) {
//        try {
//            URL url = new URL(imageUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap bitmap = BitmapFactory.decodeStream(input);
//            return bitmap;
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            return null;
//
//        }
//    }
//
//    Bitmap bitmap;
//
//    public void showNotification(Map<String, String> map) {
//        if (map != null) {
//            String str = ((String) map.get("title")).toString();
//            String str2 = ((String) map.get("body")).toString();
//            String str3 = ((String) map.get("data")).toString();
//
//            String imageUri = str3;
//            bitmap = getBitmapfromUrl(imageUri);
//            NotificationCompat.Builder contentText = new NotificationCompat.Builder(this, "pdfreader")
//                    .setContentTitle(str)
//                    .setSmallIcon(R.drawable.baseline_circle_notifications_24)
//                    .setAutoCancel(true)
//                    .setLargeIcon(bitmap)
//                    .setStyle(new NotificationCompat.BigPictureStyle()
//                            .bigPicture(bitmap))/*Notification with Image*/
//                    .setContentText(str2);
//
//            Intent intent = new Intent(this, homeguardian.class);
//            intent.setData(Uri.parse(str3));
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("title", str);
//            intent.putExtra("body", str2);
//            intent.putExtra("data", str3);
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
//
//
//                contentText.setContentIntent(PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE));
//
//            } else {
//
//                contentText.setContentIntent(PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT));
//
//            }
//
//
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            NotificationManagerCompat.from(this).notify(0, contentText.build());
//
//        }
//    }
//
//    public void showNotification(String str, String str2) {
//        NotificationCompat.Builder contentText = new NotificationCompat.Builder(this, "miczonpdfreader")
//                .setContentTitle(str)
//                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
//                .setAutoCancel(true)
//                .setStyle(new NotificationCompat.BigPictureStyle()
//                        .bigPicture(bitmap))/*Notification with Image*/
//                .setContentText(str2);
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.setData(Uri.parse(str2));
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
//
//
//            contentText.setContentIntent(PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_NO_CREATE));
//
//        } else {
//
//            contentText.setContentIntent(PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT));
//
//        }
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        NotificationManagerCompat.from(this).notify(0, contentText.build());
//    }
//
//}
