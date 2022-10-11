package com.mike.mqttdigitalsignrsa.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuaUtils {
    static Globals globals = JsePlatform.standardGlobals();
    public static void compile(File file) {
        try {
            globals.load(new FileReader(file), "script").call();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static LuaValue invoke(String func, Object... parameters) {
        if (parameters != null && parameters.length > 0) {
            LuaValue[] values = new LuaValue[parameters.length];
            for (int i = 0; i < parameters.length; i++)
                values[i] = CoerceJavaToLua.coerce(parameters[i]);
            return globals.get(func).call(LuaValue.listOf(values));
        } else
            return globals.get(func).call();
    }
}
