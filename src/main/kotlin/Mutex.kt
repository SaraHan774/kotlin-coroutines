import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.system.measureTimeMillis

fun main() {
    runBlocking {
        val mutex = Mutex()
        var counter = 0
        withContext(Dispatchers.Default){
            //병렬 처리가 가능하다.
            massiveRunMutex{
                mutex.withLock {
                    //lost update 없음
                    counter++
                }
            }
        }
        println("Counter = $counter")
    }
}

suspend fun massiveRunMutex(action : suspend () -> Unit){
    val n = 100
    val k = 1000

    val time = measureTimeMillis {
        coroutineScope{
            repeat(n){
                launch {
                    repeat(k){
                        action()
                    }
                }
            }
        }
    }
    println("Completed ${n * k} actions in $time ms")
}
