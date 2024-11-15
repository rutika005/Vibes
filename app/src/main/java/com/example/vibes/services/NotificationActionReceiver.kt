//package com.example.vibes.service
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//
//class NotificationActionReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent?) {
//        when (intent?.action) {
//            "ACTION_PLAY_PAUSE" -> {
//                // Toggle play/pause in the music service
//                // You might need to use a local broadcast or another communication mechanism
//            }
//            "ACTION_STOP" -> {
//                // Stop the music and service
//                context.stopService(Intent(context, MusicService::class.java))
//            }
//        }
//    }
//}
