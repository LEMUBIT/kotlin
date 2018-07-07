// WITH_RUNTIME
// WITH_COROUTINES
// COMMON_COROUTINES_TEST
import helpers.*
import COROUTINES_PACKAGE.*
import COROUTINES_PACKAGE.intrinsics.*

class Controller {
    suspend fun suspendHere(): Int = suspendCoroutineOrReturn { x ->
        1
    }
    suspend fun suspendThere(): String = suspendCoroutineOrReturn { x ->
        "?"
    }
}

fun builder(c: suspend Controller.() -> Unit) {
    c.startCoroutine(Controller(), EmptyContinuation)
}

fun box(): String {
    var result = ""

    builder {
        result += "-"
        var _i = 0
        while (_i <= 10000) {
            val i = _i++
            if (i % 2 == 0) {
                result += suspendHere().toString()
            }
            else if (i == 3) {
                result += suspendThere()
            }
        }
        result += "+"
    }

    var mustBe = "-"
    var _i  = 0
    while (_i <= 10000) {
        val i = _i++
        if (i % 2 == 0) {
            mustBe += "1"
        }
        else if (i == 3) {
            mustBe += "?"
        }
    }
    mustBe += "+"

    if (result != mustBe) return "fail: $result/$mustBe"

    return "OK"
}
