package sakura.llar.calculatorapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

class CalculatorViewModel : ViewModel() {

    private val _equationText = MutableLiveData("")
    val equationText: LiveData<String> = _equationText

    private val _resultText = MutableLiveData("")
    val resultText: LiveData<String> = _resultText

    fun onButtonClick(btn: String) {

        _equationText.value?.let {
            when (btn) {
                "AC" -> {
                    _equationText.value = ""
                    _resultText.value = ""
                }

                "C" -> {
                    if (it.isNotEmpty()) {
                        _equationText.value =
                            it.substring(0, it.length - 1)
                    }
                }

                "=" -> {
                    _equationText.value = _resultText.value
                }

                else -> {
                    _equationText.value = it + btn
                    try {
                        _resultText.value = calculatorResult(_equationText.value.toString())
                    } catch (_: Exception) {
                        // Error handling.
                    }
                }
            }
        }
    }
}

fun calculatorResult(equation: String): String {

    val modifiedEquation = equation.replace("×", "*").replace("÷", "/")

    val context: Context = Context.enter()
    context.optimizationLevel = -1

    val scriptable: Scriptable = context.initStandardObjects()
    var finalResult =
        context.evaluateString(scriptable, modifiedEquation, "Javascript", 1, null).toString()

    if (finalResult.endsWith(".0")) {
        finalResult = finalResult.replace(".0", "")
    }

    finalResult = finalResult.replace('.', ',')

    return finalResult
}