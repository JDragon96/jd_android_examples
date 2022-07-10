package com.example.kotlin_android.ndk

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_android.databinding.ActivityNdkCalculatorBinding

class NdkCalculator: AppCompatActivity() {

    //Properties//////////////////////////////////////////
    private lateinit var ViewBinding: ActivityNdkCalculatorBinding

    data class Point(val x: Double, val y: Double, val z: Double)
    data class CalculateObject(val left:Double, val right:Double, val operator: Char, val flag: Boolean)

    //Binding/////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewBinding = ActivityNdkCalculatorBinding.inflate(layoutInflater)
        setContentView(ViewBinding.root)

        ViewBinding.tvResult.setText("")

        //Number pad Button////////////////////////////////
        ViewBinding.btn0.setOnClickListener{
            AddNumber('0')
        }
        ViewBinding.btn1.setOnClickListener{
            AddNumber('1')
        }
        ViewBinding.btn2.setOnClickListener{
            AddNumber('2')
        }
        ViewBinding.btn3.setOnClickListener{
            AddNumber('3')
        }
        ViewBinding.btn4.setOnClickListener{
            AddNumber('4')
        }
        ViewBinding.btn5.setOnClickListener{
            AddNumber('5')
        }
        ViewBinding.btn6.setOnClickListener{
            AddNumber('6')
        }
        ViewBinding.btn7.setOnClickListener{
            AddNumber('7')
        }
        ViewBinding.btn8.setOnClickListener{
            AddNumber('8')
        }
        ViewBinding.btn9.setOnClickListener{
            AddNumber('9')
        }


        //Operator Button////////////////////////////////
        ViewBinding.btnMinus.setOnClickListener{
            AddOperator('-')
        }
        ViewBinding.btnPlus.setOnClickListener{
            AddOperator('+')
        }
        ViewBinding.btnMul.setOnClickListener{
            AddOperator('*')
        }
        ViewBinding.btnDiv.setOnClickListener{
            AddOperator('/')
        }
        ViewBinding.btnDelete.setOnClickListener{
            DelTextOnResult()
        }
        ViewBinding.btnEqual.setOnClickListener{
            AddOperator('=')
        }
    }

    //Binding/////////////////////////////////////////////
    external fun CommandAdd(inputArray: Array<Point>, length: Int): Double
    external fun CommandOperation(inputObject: CalculateObject): Double

    //Methods/////////////////////////////////////////////
    fun ExecuteCalculator(calObject: CalculateObject): Double{
        return CommandOperation(calObject)
    }
    fun AddOperator(text: Char){
        // 연산자를 추가할 때
        var currentText = ViewBinding.tvResult.text.toString()

        // 3. Equal버튼 클릭 한 경우
        if(OperatorCheck(currentText)){

            if(text == '='){
                var calObject = ParsingObject(currentText)

                if(!calObject.flag) return
                var result = ExecuteCalculator(calObject)
                ViewBinding.tvResult.setText(result.toString())
            }
            else{
                var calObject = ParsingObject(currentText)

                if(!calObject.flag) return
                var result = ExecuteCalculator(calObject)
                ViewBinding.tvResult.setText(result.toString() + text)
            }
        }
        // 2. 없는 경우?
        // 그래도 연산자 추가
        else{
            ViewBinding.tvResult.setText(currentText + text)
        }
    }
    fun AddNumber(text: Char){
        // 연산자를 추가할 때
        var currentText = ViewBinding.tvResult.text.toString()
        ViewBinding.tvResult.setText(currentText + text)
    }
    fun DelTextOnResult(){
        var currentText = ViewBinding.tvResult.text.toString()

        if(currentText.length > 1){
            ViewBinding.tvResult.setText(currentText.removeRange(currentText.length-1, currentText.length))
        }
        else{
            ViewBinding.tvResult.setText(currentText.removeRange(0,currentText.length))
        }
    }
    fun OperatorCheck(inputText: String): Boolean{
        if(inputText.contains('+') or
            inputText.contains('-') or
            inputText.contains('/') or
            inputText.contains('*')){
            return true
        }
        else{
            return false
        }
    }
    fun ParsingObject(inputText:String): CalculateObject{
        var flag = false

        if(inputText.contains('+')){
            var splitText = inputText.split('+')
            if(splitText[0] != "" && splitText[1] != ""){
                flag = true
                return CalculateObject(splitText[0].toDouble(), splitText[1].toDouble(), '+', flag)
            }
            else{
                return CalculateObject(0.0, 0.0, '+', flag)
            }
        }
        else if(inputText.contains('-')){
            var splitText = inputText.split('-')
            if(splitText[0] != "" && splitText[1] != ""){
                flag = true
                return CalculateObject(splitText[0].toDouble(), splitText[1].toDouble(), '-', flag)
            }
            else{
                return CalculateObject(0.0, 0.0, '-', flag)
            }
        }
        else if(inputText.contains('/')){
            var splitText = inputText.split('/')
            if(splitText[0] != "" && splitText[1] != ""){
                flag = true
                return CalculateObject(splitText[0].toDouble(), splitText[1].toDouble(), '/', flag)
            }
            else{
                return CalculateObject(0.0, 0.0, '/', flag)
            }
        }
        else{
            var splitText = inputText.split('*')
            if(splitText[0] != "" && splitText[1] != ""){
                flag = true
                return CalculateObject(splitText[0].toDouble(), splitText[1].toDouble(), '*', flag)
            }
            else{
                return CalculateObject(0.0, 0.0, '*', flag)
            }
        }
    }
}