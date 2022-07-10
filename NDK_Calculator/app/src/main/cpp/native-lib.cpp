#include <jni.h>
#include "Types.h"
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_kotlin_1android_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jdouble JNICALL
Java_com_example_kotlin_1android_ndk_NdkCalculator_CommandAdd(
        JNIEnv* env,
        jobject /* this */,
        jobjectArray inputArray,
        jint length) {

    int size = env->GetArrayLength(inputArray);
    for(int index = 0; index < size; ++index){
        jobject value = env->GetObjectArrayElement(inputArray, index);
        jclass sampleData = env->GetObjectClass(value);
        jfieldID name = env->GetFieldID(sampleData,"x","D");
        double v = env->GetDoubleField(value, name);
        bool a = true;
    }
    return 0.0;
}

extern "C" JNIEXPORT jdouble JNICALL
Java_com_example_kotlin_1android_ndk_NdkCalculator_CommandOperation(
        JNIEnv* env,
        jobject /* this */,
        jobject inputObj) {

//    jobject getObj = env->GetObjectClass(inputObj);
    jclass getClass = env->GetObjectClass(inputObj);
    jfieldID left = env->GetFieldID(getClass, "left", "D");
    jfieldID right = env->GetFieldID(getClass, "right", "D");
    jfieldID oper = env->GetFieldID(getClass, "operator", "C");

    double left_c = env->GetDoubleField(inputObj, left);
    double right_c = env->GetDoubleField(inputObj, right);
    double oper_c = env->GetCharField(inputObj, oper);

    if(oper_c == '+'){
        return left_c + right_c;
    }
    else if(oper_c == '-'){
        return left_c - right_c;
    }
    else if(oper_c == '*'){
        return left_c * right_c;
    }
    else if(oper_c == '/'){
        return left_c / right_c;
    }
    else{
        return 0.0;
    }
}