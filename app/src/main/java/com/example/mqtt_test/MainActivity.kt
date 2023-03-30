package com.example.mqtt_test

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence


class MainActivity : AppCompatActivity() {
    val filter = "SeniorProject/unity/filter" // the MQTT topic to publish to
    val c2s = "SeniorProject/rasa/client" // the MQTT topic to publish to
    val s2c = "SeniorProject/rasa/server" // the MQTT topic to publish to
    //val pairing = "SeniorProject/unity/pairing" // the MQTT topic to publish to
    // val message = "Hello, MQTT!" // the message to publish
    val qos = 1 // the quality of service level
    val broker = "tcp://172.104.91.57:1883" // the MQTT broker URL
    val clientId = "android_sub" // the client ID to use
    val clientIdPub = "android_pub" // the client ID to use
    val persistence = MemoryPersistence() // use a memory-based persistence store
    val client = MqttClient(broker, clientId, persistence) // create an MQTT client
    val clientPub = MqttClient(broker, clientIdPub, persistence) // create an MQTT client
    val connOpts = MqttConnectOptions() // create connection options
    val connOptsPub = MqttConnectOptions() // create connection options

    lateinit var testInput: EditText
    lateinit var rasaAIInput: EditText
    lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testInput = findViewById(R.id.etPublic)
        rasaAIInput = findViewById(R.id.etPublic2)
        textView = findViewById(R.id.textView)
        // set up option
        connOpts.isAutomaticReconnect = true
        connOpts.isCleanSession = false
        client.connect(connOpts) // connect to the MQTT broker
        //client.subscribe("hello/world")
        client.subscribe(s2c, object : IMqttMessageListener {
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                // Handle incoming MQTT message here
                textView.text="${message.toString()}"
                // client.disconnect()
            }
        })
    }
    fun publish(view: View) {
        connOptsPub.isCleanSession = true // set clean session flag
        clientPub.connect(connOptsPub) // connect to the MQTT broker
        val mqttTopic = clientPub.getTopic(filter) // get the topic to publish to
        val mqttMessage = MqttMessage(testInput.text.toString().toByteArray()) // create a new MQTT message
        mqttMessage.qos = qos // set the QoS level
        mqttTopic.publish(mqttMessage) // publish the message
        clientPub.disconnect() // disconnect from the MQTT broker
    }
    fun rasa_ai(view: View) {
        connOptsPub.isCleanSession = true // set clean session flag
        clientPub.connect(connOptsPub) // connect to the MQTT broker
        val mqttTopic = clientPub.getTopic(c2s) // get the topic to publish to
        val mqttMessage = MqttMessage(rasaAIInput.text.toString().toByteArray()) // create a new MQTT message
        mqttMessage.qos = qos // set the QoS level
        mqttTopic.publish(mqttMessage) // publish the message
        clientPub.disconnect() // disconnect from the MQTT broker
    }
    /*fun pairing(view: View) {
        connOpts.isCleanSession = true // set clean session flag
        client.connect(connOpts) // connect to the MQTT broker
        val mqttTopic = client.getTopic(pairing) // get the topic to publish to
        val mqttMessage = MqttMessage("@@@@@@@@".toByteArray()+android.os.Build.MODEL.toString().toByteArray()) // create a new MQTT message
        mqttMessage.qos = qos // set the QoS level
        mqttTopic.publish(mqttMessage) // publish the message
        client.disconnect() // disconnect from the MQTT broker
    }*/
}

