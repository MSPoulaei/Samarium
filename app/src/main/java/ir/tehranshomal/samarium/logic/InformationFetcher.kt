package ir.tehranshomal.samarium.logic
import kotlinx.coroutines.*

//fun infoFetcher(){
//    val scope = CoroutineScope(Job() + Dispatchers.Default)
//    val job = scope.launch {
//        try {
//            performLongRunningTask()
//        } catch (e: CancellationException) {
//            println("Coroutine was cancelled")
//        }
//    }
//    Thread.sleep(8000)
//    job.cancel()
//}
//
//suspend fun performLongRunningTask() {
//    while (true){
//        delay(5000)
//        println("Data updated")
//    }
//}