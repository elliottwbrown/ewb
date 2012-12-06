// Win32GUINative.cpp : Defines the entry point for the DLL application.
//

#include "stdafx.h"
#include "win32_Win32GUI.h"

BOOL APIENTRY DllMain( HANDLE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved
					 )
{
    return TRUE;
}


struct JavaFunctionDetails {
	JNIEnv *env;
	jmethodID mid;
	jobject function;
};


BOOL CALLBACK EnumWindowsForJavaFunction(HWND hwnd, LPARAM lparam) {
	JavaFunctionDetails* details = (JavaFunctionDetails*)lparam;
	JNIEnv *env = details->env;
    jboolean result = env->CallBooleanMethod(details->function, details->mid, hwnd);
	return (result == JNI_TRUE) ? TRUE : FALSE;
}

/*
 * Class:     win32_Win32GUI
 * Method:    EnumWindows
 * Signature: (Lwin32/EnumFunction;)Z
 */
JNIEXPORT jboolean JNICALL Java_win32_Win32GUI_EnumWindows
  (JNIEnv *env, jclass, jobject function)
{
	JavaFunctionDetails details;
	details.env = env;
	details.function = function;
	jclass cls = env->GetObjectClass(function);
    details.mid = env->GetMethodID(cls, "EnumWindowsProcedure", "(I)Z");
    if (details.mid == 0) {
        return FALSE;
    }
	EnumWindows(EnumWindowsForJavaFunction, (LPARAM)&details);
	return TRUE;
}

/*
 * Class:     win32_Win32GUI
 * Method:    EnumChildWindows
 * Signature: (ILwin32/EnumFunction;)V
 */
JNIEXPORT void JNICALL Java_win32_Win32GUI_EnumChildWindows
  (JNIEnv *env, jclass, jint hwnd, jobject function)
{
	JavaFunctionDetails details;
	details.env = env;
	details.function = function;
	jclass cls = env->GetObjectClass(function);
    details.mid = env->GetMethodID(cls, "EnumWindowsProcedure", "(I)Z");
	if (details.mid == 0) {
        return;
    }
	EnumChildWindows((HWND)hwnd, EnumWindowsForJavaFunction, (LPARAM)&details);

}

/*
 * Class:     win32_Win32GUI
 * Method:    GetLastError
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_win32_Win32GUI_GetLastError
  (JNIEnv *env, jclass)
{
	DWORD error = GetLastError();
	return (jlong) error;
}

/*
 * Class:     win32_Win32GUI
 * Method:    GetWindowText
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_win32_Win32GUI_GetWindowText
  (JNIEnv *env, jclass, jint jhwnd)
{
	char windowText[1024];
	GetWindowText((HWND)jhwnd, &windowText[0], 1024);
	return env->NewStringUTF(&windowText[0]);
}

/*
 * Class:     win32_Win32GUI
 * Method:    GetClassName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_win32_Win32GUI_GetClassName
  (JNIEnv *env, jclass, jint jhwnd)
{
	char className[1024];
	GetClassName((HWND)jhwnd, &className[0], 1024);
	return env->NewStringUTF(&className[0]);
}

/*
 * Class:     win32_Win32GUI
 * Method:    GetMenu
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_win32_Win32GUI_GetMenu
  (JNIEnv *, jclass, jint hwnd)
{
	return (jint)GetMenu((HWND)hwnd);
}

/*
 * Class:     win32_Win32GUI
 * Method:    GetMenuItemCount
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_win32_Win32GUI_GetMenuItemCount
  (JNIEnv *, jclass, jint hmenu)
{
	return GetMenuItemCount((HMENU)hmenu);
}

/*
 * Class:     win32_Win32GUI
 * Method:    GetMenuItemID
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_win32_Win32GUI_GetMenuItemID
  (JNIEnv *, jclass, jint hmenu, jint nPos)
{
	return GetMenuItemID((HMENU)hmenu, nPos);
}

/*
 * Class:     win32_Win32GUI
 * Method:    PostMessage
 * Signature: (IIII)V
 */
JNIEXPORT void JNICALL Java_win32_Win32GUI_PostMessage
  (JNIEnv *, jclass, jint hwnd, jint msg, jint wparam, jint lparam)
{
	PostMessage((HWND)hwnd, msg, wparam, lparam);
}

/*
 * Class:     win32_Win32GUI
 * Method:    GetMenuState
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_win32_Win32GUI_GetMenuState
  (JNIEnv *, jclass, jint hmenu, jint uid, jint uflags)
{
	return GetMenuState((HMENU)hmenu, uid, uflags);
}

/*
 * Class:     win32_Win32GUI
 * Method:    GetSubMenu
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_win32_Win32GUI_GetSubMenu
  (JNIEnv *, jclass, jint hmenu, jint npos)
{
	return (jint)GetSubMenu((HMENU)hmenu, npos);
}

/*
 * Class:     win32_Win32GUI
 * Method:    GetMenuString
 * Signature: (III)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_win32_Win32GUI_GetMenuString
  (JNIEnv *env, jclass, jint hmenu, jint itemID, jint flag)
{
	char buf[1024];
	GetMenuString((HMENU)hmenu, itemID, &buf[0], 1024, flag);
	return env->NewStringUTF(&buf[0]);
}

/*
 * Class:     win32_Win32GUI
 * Method:    SendMessage
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_win32_Win32GUI_SendMessage
  (JNIEnv *, jclass, jint hwnd, jint msg, jint wParam, jint lParam)
{
	return SendMessage((HWND)hwnd, msg, wParam, lParam);
}

/*
 * Class:     win32_Win32GUI
 * Method:    SendMessageWithString
 * Signature: (IIILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_win32_Win32GUI_SendMessageWithString
  (JNIEnv *env, jclass, jint hwnd, jint msg, jint wParam, jstring sparam)
{
	const char *str = env->GetStringUTFChars(sparam, 0);
	return SendMessage((HWND)hwnd, msg, wParam, (LPARAM)str);
    env->ReleaseStringUTFChars(sparam, str);
}

/*
 * Class:     win32_Win32GUI
 * Method:    SendMessageReturnString
 * Signature: (III)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_win32_Win32GUI_SendMessageReturnString
  (JNIEnv *env, jclass, jint hwnd, jint msg, jint wParam)
{
	char buf[1024];
	WORD* length = (WORD*)&buf[0];
	*length = 1024;
	int result = SendMessage((HWND)hwnd, msg, wParam, (LPARAM)&buf[0]);
	return env->NewStringUTF(&buf[0]);
}

/*
 * Class:     win32_Win32GUI
 * Method:    GetParent
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_win32_Win32GUI_GetParent
  (JNIEnv *, jclass, jint hwnd)
{
	return (jint)GetParent((HWND)hwnd);
}

/*
 * Class:     win32_Win32GUI
 * Method:    GetWindowLong
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_win32_Win32GUI_GetWindowLong
  (JNIEnv *, jclass, jint hwnd, jint nIndex)
{
	return GetWindowLong((HWND)hwnd, nIndex);
}

