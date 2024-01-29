import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import org.jetbrains.skiko.MainUIDispatcher
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Timer {

    var formattedTime by mutableStateOf("00:00:000")
    private var coroutineScope = CoroutineScope(MainUIDispatcher)
    private var isActive = false
    private var timeInMillis = 0L
    private var lastTimestamp = 0L

    fun start(){
        if(isActive){
            return
        }

        coroutineScope.launch {
            lastTimestamp = System.currentTimeMillis()
            this@Timer.isActive = true
            while(this@Timer.isActive){
                delay(10L)
                timeInMillis += System.currentTimeMillis() - lastTimestamp
                lastTimestamp = System.currentTimeMillis()
                formattedTime = formatTime(timeInMillis)
            }
        }
    }

    fun pause(){
        isActive = false
    }

    fun reset(){
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(MainUIDispatcher)
        timeInMillis = 0L
        lastTimestamp = 0L
        formattedTime = "00:00:000"
        isActive = false
    }

    private fun formatTime(time: Long): String{
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("mm:ss:SSS")
        return localDateTime.format(formatter)
    }
}