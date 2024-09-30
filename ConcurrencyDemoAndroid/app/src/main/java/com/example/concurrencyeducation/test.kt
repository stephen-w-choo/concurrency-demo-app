import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() {
    var counter = 0

    runBlocking {
        val jobs = List(1000) {
            launch(Dispatchers.Default) {
                for (j in 0 until 1000) {
                    counter++
                }
            }
        }
        jobs.forEach { it.join() }
    }

    println("Counter: $counter")
}