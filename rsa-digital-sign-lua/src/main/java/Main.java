import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.ast.Chunk;
import org.luaj.vm2.ast.Exp;
import org.luaj.vm2.ast.Visitor;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.parser.LuaParser;
import org.luaj.vm2.parser.ParseException;

import javax.script.Compilable;
import javax.script.CompiledScript;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class Main {
    static Globals globals = JsePlatform.standardGlobals();
    public static void compile(File file) {
        try {
            globals.load(new FileReader(file), "script").call();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]) throws NoSuchAlgorithmException {
        String script = "rsa.lua";
        compile(new File(script));
        // Use the convenience function on Globals to load a chunk.
         LuaValue result = invoke("encrypt_byte", 11.555, 5, 20453369);
         System.out.println(result);
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
